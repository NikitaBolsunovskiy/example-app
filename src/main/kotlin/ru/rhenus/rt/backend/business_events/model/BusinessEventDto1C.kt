package ru.rhenus.rt.backend.business_events.model

import ru.rhenus.rt.backend._common_.model.Dto1CAbstract
import java.time.Instant

@Suppress("unused")
class BusinessEventDto1C(
    var ownerUuid1c: String? = null,
    var ownerType: OwnerType? = null,
    var eventType: BusinessEventType? = null,
    var eventDateTime: Instant? = null,
    var active: Boolean? = null
): Dto1CAbstract()
