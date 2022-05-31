package service

import de.rhenus.scs.customs.order.model.CustomsOrderInvoice
import de.rhenus.scs.customs.order.repository.CustomsOrderInvoiceRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderPositionRepository
import org.apache.commons.lang3.StringUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.ValidationException

@Service
@Validated
class CustomsOrderInvoiceService(
    private val customsOrderInvoiceRepository: CustomsOrderInvoiceRepository,
    private val customsOrderPositionRepository: CustomsOrderPositionRepository,
) {

    fun get(id: Long): CustomsOrderInvoice =
        customsOrderInvoiceRepository.findById(id).orElseThrow{throw ValidationException("CustomsOrderInvoice not found")}

    fun findByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderInvoice> =
        customsOrderInvoiceRepository.findByCustomsOrderId(customsOrderId)

    fun getInvoiceNumbersByCustomsOrderId(customsOrderId: Long?): List<String> =
        customsOrderInvoiceRepository.getInvoiceNumbersByCustomsOrderId(customsOrderId)

    @Transactional
    fun update(orderInvoice: @Valid CustomsOrderInvoice): CustomsOrderInvoice? {
        val oldInvoice: CustomsOrderInvoice = customsOrderInvoiceRepository.findByIdOrNull(orderInvoice.id) ?: return customsOrderInvoiceRepository.save(orderInvoice)
        checkIfIsUnique(orderInvoice)
        customsOrderInvoiceRepository.save(orderInvoice)
        if (!StringUtils.equalsIgnoreCase(oldInvoice.invoiceNo, orderInvoice.invoiceNo)) {
            customsOrderPositionRepository.updateInvoiceNo(
                orderInvoice.customsOrder?.id, oldInvoice.invoiceNo, orderInvoice.invoiceNo
            )
        }
        return orderInvoice
    }

    @Transactional
    fun create(orderInvoice: @Valid CustomsOrderInvoice): CustomsOrderInvoice {
        checkIfIsUnique(orderInvoice)
        return customsOrderInvoiceRepository.save(orderInvoice)
    }

    private fun checkIfIsUnique(orderInvoice: CustomsOrderInvoice) {
        if (customsOrderInvoiceRepository.existsByCustomsOrderIdAndInvoiceNoAndId(
                orderInvoice.customsOrder?.id, orderInvoice.invoiceNo, orderInvoice.id
            )
        ) {
            throw ValidationException("validation.customsOrderInvoice.unique")
        }
    }
}
