package service

import de.rhenus.scs.customs.common.events.CctApplicationEventPublisher
import de.rhenus.scs.customs.common.tenant.TenantStorage
import de.rhenus.scs.customs.customer.model.Customer
import de.rhenus.scs.customs.customer.model.Rule
import de.rhenus.scs.customs.customer.service.CustomerService
import de.rhenus.scs.customs.customer.service.RuleService
import de.rhenus.scs.customs.order.event.CustomsOrderCreatedEvent
import de.rhenus.scs.customs.order.event.CustomsOrderEntityCreatedEvent
import de.rhenus.scs.customs.order.event.CustomsOrderEntityUpdatedEvent
import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderCreateRequest
import de.rhenus.scs.customs.order.model.CustomsOrderImportWay
import de.rhenus.scs.customs.order.model.OrderStatus
import de.rhenus.scs.customs.order.repository.CustomsOrderRepository
import de.rhenus.scs.customs.tenant.model.Tenant
import de.rhenus.scs.customs.tenant.service.TenantService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.time.Instant

@Service
@Validated
class CustomsOrderCreateService(
    private val applicationEventPublisher: CctApplicationEventPublisher,
    private val customsOrderRepository: CustomsOrderRepository,
    private val customsOrderStatusService: CustomsOrderStatusService,
    private val customsOrderAddressService: CustomsOrderAddressService,
    private val customsOrderSegmentBuildService: CustomsOrderSegmentBuildService,
    private val tenantService: TenantService,
    private val customerService: CustomerService,
    private val ruleService: RuleService,
) {
    @Transactional
    fun create(createRequest: CustomsOrderCreateRequest): CustomsOrder? {
        try {
            applicationEventPublisher.disable(
                listOf(
                    CustomsOrderEntityCreatedEvent::class.java, CustomsOrderEntityUpdatedEvent::class.java
                )
            )
            val customsOrder = CustomsOrder()
            customsOrder.importWay = CustomsOrderImportWay.IMPORT_WAY_WEB.value
            customsOrder.customsOrderStatus = OrderStatus.STATUS_CREATED
            customsOrder.customsOrderDatetime = Instant.now()
            customsOrder.transportInContainersSad19 = false

            val tenant: Tenant? = tenantService.findByTenant(TenantStorage.tenantName)
            customsOrder.tenant = tenant
            customsOrder.customsOrderNo = CustomsOrderNoGenerator.generateOrderNo(tenant?.tenantCode)
            val customer: Customer? = createRequest.customerId?.let { customerService.find(it) }
            if (null != customer) {
                customsOrder.customer = customer
                customsOrder.customerEmail = customer.email
            }
            val rule: Rule? = createRequest.ruleId?.let { ruleService.find(it) }
            if (null != rule) {
                customsOrder.leadOffice = rule.leadOffice
                customsOrder.deliveryTermSad20 = rule.deliveryTermSad20
                if (rule.countryOfImport != null) {
                    customsOrder.countryOfDestinationSad17 = rule.countryOfImport
                }
                if (rule.countryOfExport != null) {
                    customsOrder.countryOfExportSad15 = rule.countryOfExport
                }
                if (rule.countryOfOriginSad16 != null) {
                    customsOrder.countryOfOriginSad16 = rule.countryOfOriginSad16
                }
                customsOrder.typeOfDeclaration = rule.typeOfDeclaration
                customsOrder.customerCustomerNo = rule.customerCustomerNo
                customsOrderRepository.save(customsOrder)
                customsOrderStatusService.create(customsOrder, OrderStatus.STATUS_CREATED)
                customsOrderAddressService.createCustomerAddress(
                    customsOrder, customsOrder.customer
                )
                customsOrderSegmentBuildService.buildSegments(customsOrder, rule)
                applicationEventPublisher.publishEvent(
                    CustomsOrderCreatedEvent(
                        customsOrder, CustomsOrderCreatedEvent.Origin.UI
                    )
                )
                return customsOrder
            }
        } finally {
            applicationEventPublisher.enable(
                listOf<Class<*>>(
                    CustomsOrderEntityCreatedEvent::class.java, CustomsOrderEntityUpdatedEvent::class.java
                )
            )
        }
        return null
    }
}
