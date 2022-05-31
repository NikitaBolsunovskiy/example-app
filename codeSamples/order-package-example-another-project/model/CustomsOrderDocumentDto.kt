package model

import de.rhenus.scs.customs.common.model.BaseDto
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.util.*

class CustomsOrderDocumentDto(
    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    var customsOrderId: Long? = null,
    var documentNo: String? = null,

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    var documentDate: Instant? = null,
    var documentFilename: String? = null,
    var documentCode: String? = null,
    var documentSpecialInformation: String? = null,
    var documentType: String? = null,
    var documentReference: String? = null,
    var documentDescription: String? = null,
    var documentSegment: String? = null,
    var directLink: String? = null,
    var archiveReference: String? = null,
    var archiveError: Boolean? = null,
) : BaseDto(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrderId
}
