package event

import de.rhenus.scs.customs.common.events.CctApplicationEvent
import de.rhenus.scs.customs.order.model.CustomsOrder

class CustomsOrderEntityCreatedEvent(
    var entityName: String?,
    customsOrderId: Long?,
) : CctApplicationEvent(CustomsOrder::class.java.simpleName, customsOrderId) {
    constructor(): this(null, null)
}
