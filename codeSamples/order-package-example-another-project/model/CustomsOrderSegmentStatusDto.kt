package model

import de.rhenus.scs.customs.common.model.BaseDto
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.util.*

data class CustomsOrderSegmentStatusDto(
    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    var customsOrderSegmentId: Long? = null,
    var statusId: Long? = null,
    var reminderSent: Boolean = false,
    var escalationSent: Boolean = false,
) : BaseDto()
