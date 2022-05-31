package model

import de.rhenus.scs.customs.common.model.BaseDto
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderPositionDependant
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant

data class CustomsOrderPositionPreviousDocumentDto(
    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    var customsOrderPositionId: Long? = null,
    var type: String? = null,
    var code: String? = null,
    var reference: String? = null,
) : BaseDto(), CustomsOrderPositionDependant {
    override fun retrieveCustomsOrderPositionId(): Long? = customsOrderPositionId
}
