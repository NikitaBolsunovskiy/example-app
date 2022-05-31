package service

import de.rhenus.scs.customs.common.events.CctApplicationEventPublisher
import de.rhenus.scs.customs.compliance.client.ComplianceCheckerClient
import de.rhenus.scs.customs.compliance.model.ComplianceCheckRequest
import de.rhenus.scs.customs.compliance.model.ComplianceCheckRequestEntity
import de.rhenus.scs.customs.compliance.model.ComplianceMatchLevel
import de.rhenus.scs.customs.order.event.AddressComplianceCheckFailedEvent
import de.rhenus.scs.customs.order.event.AddressComplianceCheckNotPossibleEvent
import de.rhenus.scs.customs.order.event.AddressComplianceCheckSolvedEvent
import de.rhenus.scs.customs.order.event.CustomsOrderStatusChangedEvent
import de.rhenus.scs.customs.order.model.*
import de.rhenus.scs.customs.order.repository.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import kotlin.math.min

@Service
class CustomsOrderComplianceService(
    private val customsOrderAddressRepository: CustomsOrderAddressRepository,
    private val customsOrderInvoiceAddressRepository: CustomsOrderInvoiceAddressRepository,
    private val customsOrderInvoiceRepository: CustomsOrderInvoiceRepository,
    private val customsOrderSegmentRepository: CustomsOrderSegmentRepository,
    private val customsOrderSegmentTransmitService: CustomsOrderSegmentTransmitService,
    private val complianceChecker: ComplianceCheckerClient,
    private val applicationEventPublisher: CctApplicationEventPublisher,
    @Autowired @Qualifier("jpaTransactionTemplate") private val transactionTemplate: TransactionTemplate,
    private val customsOrderStatusService: CustomsOrderStatusService,
    private val customsOrderRepository: CustomsOrderRepository,
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(CustomsOrderComplianceService::class.java)
    }

    fun checkAllNeverCheckedAddresses() {
        log.info("Starting compliance check for all never checked addresses")
        applicationEventPublisher.disable(listOf(AddressComplianceCheckNotPossibleEvent::class.java))

        try {
            checkAllNeverCheckedCustomsOrderAddresses()
            checkAllNeverCheckedCustomsOrderInvoiceAddresses()
        } finally {
            applicationEventPublisher.enable(listOf(AddressComplianceCheckNotPossibleEvent::class.java))
        }

        log.info("Compliance check for all never checked addresses finished")
    }

    private fun checkAllNeverCheckedCustomsOrderAddresses() {
        for (address in customsOrderAddressRepository.findNotCheckedAddresses()) {
            log.info("Checking Customs Order Address ID: {}", address.id)

            executeAddressComplianceCheck(address.customsOrder, address)
        }
    }

    private fun checkAllNeverCheckedCustomsOrderInvoiceAddresses() {
        for (address in customsOrderInvoiceAddressRepository.findNotCheckedAddresses()) {
            log.info("Checking Customs Order Invoice Address ID: {}", address.id)

            executeAddressComplianceCheck(
                customsOrderInvoiceRepository.findByIdOrNull(address.customsOrderInvoice?.id)?.customsOrder,
                address
            )
        }
    }

    private fun executeAddressComplianceCheck(order: CustomsOrder?, address: ComplianceCheckableAddress) {
        transactionTemplate.execute {
            order?.id?.also { checkAddressCompliance(it, address) }
            address.id
        }
    }

    fun checkAddressCompliance(orderId: Long, address: ComplianceCheckableAddress): Boolean {
        val request = buildComplianceCheckRequest("Address-" + address.id, listOf(address))

        return doComplianceCheck(orderId, request, listOf(address)).also {
            updateCustomsOrderComplianceStatus(orderId)
        }
    }

    fun checkCompliance(order: CustomsOrder): Boolean {
        val failed = checkOrderAddresses(order) || checkInvoiceAddresses(order)

        if (failed) {
            updateCustomsOrderStatus(order, OrderStatus.STATUS_COMPLIANCE_CHECK_FAILED)
        }

        return !failed
    }

    private fun checkOrderAddresses(order: CustomsOrder) =
        doComplianceCheck(
            order.id,
            buildComplianceCheckRequest(order.customsOrderNo + "-head", order.customsOrderAddresses),
            order.customsOrderAddresses
        )

    private fun checkInvoiceAddresses(order: CustomsOrder): Boolean {
        val resultList = order
            .customsOrderInvoices
            .filter { it.customsOrderInvoiceAddresses.isNotEmpty() }
            .filter { it.id != null }
            .map { checkInvoiceAddress(order, it) }
        return if (resultList.isEmpty()) false else resultList.reduce(Boolean::or)
    }

    private fun checkInvoiceAddress(order: CustomsOrder, invoice: CustomsOrderInvoice) =
        doComplianceCheck(
            order.id,
            buildComplianceCheckRequest(order.customsOrderNo + "-invoice", invoice.customsOrderInvoiceAddresses),
            invoice.customsOrderInvoiceAddresses,
        )

    private fun doComplianceCheck(
        orderId: Long?,
        request: ComplianceCheckRequest,
        addresses: List<ComplianceCheckableAddress>,
    ): Boolean {
        var failedFound = false
        val result = complianceChecker.check(request)

        if (result != null) {
            result.results.forEach { entity ->
                val index = entity.clId.toInt()
                val address = addresses[index]

                if (entity.matchLevel == ComplianceMatchLevel.MATCH_FOUND) {
                    failedFound = true

                    setAddressComplianceCheckFailed(address, entity.logUrl)

                    applicationEventPublisher.publishEvent(AddressComplianceCheckFailedEvent(address, orderId))
                } else {
                    setAddressComplianceCheckOk(address)

                    applicationEventPublisher.publishEvent(AddressComplianceCheckSolvedEvent(address, orderId))
                }
            }

            if (result.results.size != addresses.size) {
                addresses
                    .indices
                    .filter { index ->
                        result.results.none { it.clId == index.toString() }
                    }
                    .forEach { index ->
                        applicationEventPublisher.publishEvent(
                            AddressComplianceCheckNotPossibleEvent(addresses[index], orderId)
                        )
                    }
            }
        } else {
            // Do not set "failedFound", continue further processing. Addresses keep as compliance checked false.
            addresses
                .map { AddressComplianceCheckNotPossibleEvent(it, orderId) }
                .forEach { applicationEventPublisher.publishEvent(it) }
        }
        return failedFound
    }

    private fun buildComplianceCheckRequest(
        requestNo: String,
        addresses: List<ComplianceCheckableAddress>,
    ) =
        ComplianceCheckRequest().apply {
            sourceId = requestNo

            addresses
                .mapIndexed { index, address ->
                    buildComplianceCheckRequestEntity(index, address)
                }
                .forEach { this.entities.add(it) }
        }

    private fun buildComplianceCheckRequestEntity(index: Int, address: ComplianceCheckableAddress) =
        ComplianceCheckRequestEntity(
            clId = index.toString(),
            name = extractName(address),
            street = address.street,
            zip = address.zipcode,
            city = address.city,
            country = address.country
        )

    private fun extractName(address: ComplianceCheckableAddress) =
        with(address) {
            listOfNotNull(name1, name2, name3).joinToString(" ")
        }

    fun updateCustomsOrderComplianceStatus(orderId: Long) {
        val customsOrder = this.customsOrderRepository.findByIdOrNull(orderId)

        if(customsOrder === null) {
            throw RuntimeException("No order was found for id $orderId. Therefore no status could be set.")
        }

        if (OrderStatus.STATUS_COMPLIANCE_CHECK_FAILED == customsOrder.customsOrderStatus) {
            val noNonCompliantCustomsOrderAddresses = customsOrderAddressRepository.countNonCompliantByCustomsOrderId(orderId) == 0L
            val noNonCompliantCustomsOrderInvoiceAddresses = customsOrderInvoiceAddressRepository.countNonCompliantByCustomsOrderId(orderId) == 0L

            if (noNonCompliantCustomsOrderAddresses && noNonCompliantCustomsOrderInvoiceAddresses) {
                updateCustomsOrderStatus(customsOrder, OrderStatus.STATUS_NEW)

                val segments = customsOrderSegmentRepository.findByCustomsOrderId(orderId)
                customsOrderSegmentTransmitService.transmitAutomaticSegments(segments)
            }
        }
    }

    private fun updateCustomsOrderStatus(order: CustomsOrder, orderStatus: OrderStatus) {
        customsOrderStatusService.create(order, orderStatus)
        order.customsOrderStatus = orderStatus
        customsOrderRepository.save(order)

        applicationEventPublisher.publishEvent(
            CustomsOrderStatusChangedEvent(order.id, orderStatus.value)
        )
    }

    fun setAddressComplianceCheckFailed(address: ComplianceCheckableAddress, reportUrl: String?): ComplianceCheckableAddress {
        address.apply {
            complianceChecked = true
            complianceFailed = true
            complianceReportUrl = reportUrl?.substring(0, min(512, reportUrl.length))
        }

        return when (address) {
            is CustomsOrderAddress -> customsOrderAddressRepository.save(address)
            is CustomsOrderInvoiceAddress -> customsOrderInvoiceAddressRepository.save(address)
            else -> address.also {
                // TODO marku 2022-01-26 throw an exception?
                log.warn("Unexpected implementation of ComplianceCheckableAddress: ${address::class}")
            }
        }
    }

    fun setAddressComplianceCheckOk(address: ComplianceCheckableAddress): ComplianceCheckableAddress {
        address.apply {
            complianceChecked = true
            complianceFailed = false
            complianceReportUrl = null
        }

        return when (address) {
            is CustomsOrderAddress -> customsOrderAddressRepository.save(address)
            is CustomsOrderInvoiceAddress -> customsOrderInvoiceAddressRepository.save(address)
            else -> address.also {
                // TODO marku 2022-01-26 throw an exception?
                log.warn("Unexpected implementation of ComplianceCheckableAddress: ${address::class}")
            }
        }
    }
}
