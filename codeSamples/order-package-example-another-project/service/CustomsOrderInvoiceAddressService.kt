package service

import de.rhenus.scs.customs.common.events.CctApplicationEventPublisher
import de.rhenus.scs.customs.order.event.AddressComplianceCheckDisabledEvent
import de.rhenus.scs.customs.order.model.CustomsOrderInvoice
import de.rhenus.scs.customs.order.model.CustomsOrderInvoiceAddress
import de.rhenus.scs.customs.order.repository.CustomsOrderInvoiceAddressRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderInvoiceRepository
import org.apache.commons.lang3.BooleanUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.ValidationException

@Service
@Validated
class CustomsOrderInvoiceAddressService(
    private val customsOrderInvoiceRepository: CustomsOrderInvoiceRepository,
    private val addressRepository: CustomsOrderInvoiceAddressRepository,
    private val customsOrderComplianceService: CustomsOrderComplianceService,
    private val applicationEventPublisher: CctApplicationEventPublisher,
) {

    fun get(id: Long): CustomsOrderInvoiceAddress =
        addressRepository.findById(id).orElseThrow{throw ValidationException("CustomsOrderInvoiceAddress not found.")}

    @Transactional
    fun update(address: @Valid CustomsOrderInvoiceAddress): CustomsOrderInvoiceAddress {

        val oldAddress: CustomsOrderInvoiceAddress = addressRepository.findByIdOrNull(address.id) ?: return addressRepository.save(address)

        if (oldAddress.hasComplianceRelatedFieldChanged(address)) {
            address.complianceDisabled = false
        }
        val oldVersion = address.version
        val savedAddress: CustomsOrderInvoiceAddress = addressRepository.save(address)
        if (oldVersion == null || oldVersion != savedAddress.version) {
            if (BooleanUtils.isFalse(savedAddress.complianceDisabled) || BooleanUtils.toBoolean(oldAddress.complianceDisabled) != BooleanUtils.toBoolean(
                    address.complianceDisabled
                )
            ) {
                val invoice: CustomsOrderInvoice? = customsOrderInvoiceRepository.findByIdOrNull(address.customsOrderInvoice?.id)
                if (address.complianceDisabled == true) {
                    customsOrderComplianceService.updateCustomsOrderComplianceStatus(invoice?.customsOrder?.id ?: -1L)
                    applicationEventPublisher.publishEvent(
                        AddressComplianceCheckDisabledEvent(address, invoice?.customsOrder?.id)
                    )
                } else {
                    invoice?.customsOrder?.id?.let {
                        customsOrderComplianceService.checkAddressCompliance(
                            it, savedAddress
                        )
                    }
                }
            }
        }
        return savedAddress
    }

    @Transactional
    fun create(address: @Valid CustomsOrderInvoiceAddress?): CustomsOrderInvoiceAddress {
        address!!.complianceChecked = false
        address.complianceDisabled = false
        address.complianceFailed = false
        val savedAddress: CustomsOrderInvoiceAddress = addressRepository.save(address)
        val invoice: CustomsOrderInvoice? = customsOrderInvoiceRepository.findByIdOrNull(address.customsOrderInvoice?.id)
        invoice?.customsOrder?.id?.let { customsOrderComplianceService.checkAddressCompliance(it, savedAddress) }
        return savedAddress
    }

}
