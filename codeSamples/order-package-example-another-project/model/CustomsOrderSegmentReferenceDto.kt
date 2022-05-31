package model

import de.rhenus.scs.customs.common.model.BaseDto
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant

class CustomsOrderSegmentReferenceDto(

    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    var segmentId: Long? = null,
    var reference: String? = null,
    var referenceType: String? = null,

) : BaseDto()
