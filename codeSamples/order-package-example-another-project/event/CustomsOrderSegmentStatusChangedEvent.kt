package event

import de.rhenus.scs.customs.common.events.CctApplicationEvent
import de.rhenus.scs.customs.order.model.CustomsOrder


class CustomsOrderSegmentStatusChangedEvent(
    customsOrderId: Long?,
    val segmentId: Long?,
    val segmentType: String?,
    val statusId: Long?,
) : CctApplicationEvent(CustomsOrder::class.java.simpleName, customsOrderId) {
    constructor(): this(null, null, null, null)
}
