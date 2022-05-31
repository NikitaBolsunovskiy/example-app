package model

import de.rhenus.scs.customs.common.model.BaseDto
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant

data class CustomsOrderUnitDto(
    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    var customsOrderId: Long? = null,
    var unitNo: String? = null,
    var unitType: String? = null,
    var sealNo: String? = null,
) : BaseDto(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrderId
}
