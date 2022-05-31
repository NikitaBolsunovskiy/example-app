package event

import com.fasterxml.jackson.annotation.JsonIgnore
import de.rhenus.scs.customs.common.events.CctApplicationEvent
import de.rhenus.scs.customs.order.model.CustomsOrder


class CustomsOrderCreatedEvent(
    @JsonIgnore val order: CustomsOrder?,
    origin: Origin?,
) : CctApplicationEvent(CustomsOrder::class.java.simpleName, order?.id) {
    constructor(): this(null, null)

    val origin = origin?.stringRepresentation
    val customerIdCct: String? = order?.customer?.customerIdCct
    val customsOrderNo: String? = order?.customsOrderNo

    enum class Origin(val stringRepresentation: String) {
        UI("UI"),
        CONSOLIDATION("Consolidation")
    }
}
