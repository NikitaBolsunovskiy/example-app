package model

import com.fasterxml.jackson.annotation.JsonFormat
import de.rhenus.scs.customs.common.model.BaseDto
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant

data class CustomsOrderErrorDto(
    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    var customsOrderId: Long? = null,
    var customsOrderSegmentId: Long? = null,
    var interfaceVersion: String? = null,
    var sendingSystem: String? = null,
    var customsOrderNo: String? = null,
    var typeOfDeclaration: String? = null,
    var statusId: String? = null,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var errorDate: Instant? = null,
    var errorCode: String? = null,
    var errorLevel: String? = null,
    var errorMessage: String? = null,
    var confirmed: Boolean = false,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var confirmedAt: Instant? = null,
    var solved: Boolean = false,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var solvedAt: Instant? = null,
) : BaseDto()
