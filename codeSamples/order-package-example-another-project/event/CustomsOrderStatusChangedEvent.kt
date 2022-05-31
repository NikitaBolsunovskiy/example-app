package event

import de.rhenus.scs.customs.common.events.CctApplicationEvent
import de.rhenus.scs.customs.order.model.CustomsOrder


class CustomsOrderStatusChangedEvent(
    customsOrderId: Long?,
    val statusId: Long?,
) : CctApplicationEvent(CustomsOrder::class.java.simpleName, customsOrderId) {
    constructor(): this(null, null)
}
