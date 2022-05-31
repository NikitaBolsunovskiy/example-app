package model

import de.rhenus.scs.customs.common.model.BaseDto
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderPositionDependant
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant

data class CustomsOrderPositionAdditionalInformationDto(
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
    var date: Instant? = null,
    var status: String? = null,
    var reference: String? = null,
    var quantity: Long? = null,
    var measurementUnit: String? = null,
    var amount: Float? = null,
    var currency: String? = null,
    var reason: String? = null,
    var text: String? = null,
) : BaseDto(), CustomsOrderPositionDependant {
    override fun retrieveCustomsOrderPositionId(): Long? = customsOrderPositionId
}
