package model

import de.rhenus.scs.customs.common.model.BaseDto
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.util.*

data class CustomsOrderInvoiceDto(
    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    var addresses: List<CustomsOrderInvoiceAddressDto> = ArrayList(),
    var customsOrderId: Long? = null,
    var invoiceNo: String? = null,
    var invoiceDate: Instant? = null,
    var invoiceCode: String? = null,
    var invoiceReference: String? = null,
    var invoiceAmount: Float? = null,
    var invoiceCurrency: String? = null,
    var deliveryNoteNo: String? = null,
    var deliveryNoteDate: Instant? = null,
    var orderNo: String? = null,
    var orderReference: String? = null,
    var orderDate: Instant? = null,
) : BaseDto(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrderId
}
