package event

import com.fasterxml.jackson.annotation.JsonIgnore
import de.rhenus.scs.customs.common.events.CctApplicationEvent
import de.rhenus.scs.customs.order.model.ComplianceCheckableAddress
import de.rhenus.scs.customs.order.model.CustomsOrder

abstract class AddressComplianceCheckEvent(
    @JsonIgnore val address: ComplianceCheckableAddress?,
    customsOrderId: Long?,
) : CctApplicationEvent(CustomsOrder::class.java.simpleName, customsOrderId) {
    @Suppress("unused")
    val customsOrderAddressId: Long? = address?.id
    val role: String? = address?.role
}
