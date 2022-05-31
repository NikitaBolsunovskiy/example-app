package event

import de.rhenus.scs.customs.order.model.ComplianceCheckableAddress


class AddressComplianceCheckSolvedEvent(
    address: ComplianceCheckableAddress?,
    customsOrderId: Long?
) : AddressComplianceCheckEvent(address, customsOrderId) {
    // empty constructor is needed, or the JSON deserializer
    // involved in event listeners can not construct an event entity
    @Suppress("unused")
    constructor(): this(null, null)
}
