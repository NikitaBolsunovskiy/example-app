package service

import de.rhenus.scs.customs.common.events.CctApplicationEventPublisher
import de.rhenus.scs.customs.customer.model.Customer
import de.rhenus.scs.customs.order.event.AddressComplianceCheckDisabledEvent
import de.rhenus.scs.customs.order.model.AdressRole
import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderAddress
import de.rhenus.scs.customs.order.repository.CustomsOrderAddressRepository
import org.apache.commons.lang3.BooleanUtils
import org.springframework.beans.BeanUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.ValidationException


@Service
@Validated
class CustomsOrderAddressService(
    private val addressRepository: CustomsOrderAddressRepository,
    private val customsOrderComplianceService: CustomsOrderComplianceService,
    private val applicationEventPublisher: CctApplicationEventPublisher,
) {

    fun get(id: Long): CustomsOrderAddress {
        return addressRepository.findByIdOrNull(id) ?: throw ValidationException("CustomsOrderAddress not found!")
    }

    @Transactional
    fun update(address: @Valid CustomsOrderAddress): CustomsOrderAddress {
        val oldAddress: CustomsOrderAddress = addressRepository.findByIdOrNull(address.id) ?: throw ValidationException("CustomsOrderAddress not found!")
        if (address.hasComplianceRelatedFieldChanged(oldAddress)) {
            address.complianceDisabled = false
        }
        val oldVersion = oldAddress.version

        val complianceDisabledChanged = BooleanUtils.toBoolean(oldAddress.complianceDisabled) != BooleanUtils.toBoolean(address.complianceDisabled)

        val savedAddress: CustomsOrderAddress = addressRepository.saveAndFlush(address)
        if (oldVersion == null || oldVersion != savedAddress.version) {
            if (BooleanUtils.isNotTrue(savedAddress.complianceDisabled) || complianceDisabledChanged) {
                if (BooleanUtils.isTrue(address.complianceDisabled)) {
                    customsOrderComplianceService.updateCustomsOrderComplianceStatus(savedAddress.customsOrder?.id ?: -1L)
                    applicationEventPublisher.publishEvent(
                        AddressComplianceCheckDisabledEvent(address, address.customsOrder?.id)
                    )
                } else {
                    savedAddress.customsOrder?.id?.let {
                        customsOrderComplianceService.checkAddressCompliance(it, savedAddress)
                    }
                }
            }
        }
        return savedAddress
    }

    @Transactional
    fun create(address: @Valid CustomsOrderAddress?): CustomsOrderAddress {
        address!!.complianceChecked = false
        address.complianceDisabled = false
        address.complianceFailed = false
        val savedAddress: CustomsOrderAddress = addressRepository.save(address)
        savedAddress.customsOrder?.id?.let { customsOrderComplianceService.checkAddressCompliance(it, savedAddress) }
        return savedAddress
    }

    fun createCustomerAddress(customsOrder: CustomsOrder?, customer: Customer?): CustomsOrderAddress? {
        if (null != customer) {
            val address = buildFromCustomer(customer)
            address.customsOrder = customsOrder
            address.complianceFailed = false
            address.complianceChecked = false
            address.complianceDisabled = false
            return addressRepository.save(address)
        }
        return null
    }

    fun buildFromCustomer(data: Customer): CustomsOrderAddress {
        val address = CustomsOrderAddress()
        BeanUtils.copyProperties(data, address, "id", "createdBy", "createdDate", "lastModifiedDate", "lastModifiedBy", "version")
        address.role = AdressRole.ROLE_CUSTOMER.value
        return address
    }
}
