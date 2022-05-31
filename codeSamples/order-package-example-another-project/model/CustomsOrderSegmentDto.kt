package model

import com.fasterxml.jackson.annotation.JsonFormat
import de.rhenus.scs.customs.common.model.BaseDto
import de.rhenus.scs.customs.security.permissions.CustomsOrderSegmentPermissions
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant

data class CustomsOrderSegmentDto(
    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    var customsOrderId: Long? = null,
    var sequentialNo: Long? = null,
    var type: String? = "",
    var locationId: String? = null,
    var localCustomsAppId: String? = null,
    var customsReferenceNo: String? = null,
    var statusId: Long? = null,
    var cancelledByCustoms: Boolean? = false,
    var automatic: Boolean? = null,
    var transmitted: Boolean? = false,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var customsClearanceDateTime: Instant? = null,
    var statuses: List<CustomsOrderSegmentStatusDto> = mutableListOf(),
    var references: List<CustomsOrderSegmentReferenceDto> = mutableListOf(),
    var permissions: CustomsOrderSegmentPermissions? = null
) : BaseDto()
