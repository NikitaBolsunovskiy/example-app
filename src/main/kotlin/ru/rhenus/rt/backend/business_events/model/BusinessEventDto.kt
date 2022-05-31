package ru.rhenus.rt.backend.business_events.model

import java.time.Instant

class BusinessEventDto(var eventType: BusinessEventType? = null, var eventDateTime: Instant? = null)
