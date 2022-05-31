package ru.rhenus.rt.backend.business_events.model

@Suppress("unused")
enum class BusinessEventType {
    READY_TO_OPEN_CD_PROC,
    VEHICLE_ARRIVED,
    CD_OPEN,
    INSPECTION,
    INSPECTION_DONE,
    FITO_INSPECTION_DONE,
    CD_CLEARED,
    CD_NOT_OPEN,
    CD_CLEARED_PARTIALLY,
    CD_DECISION_VARIES,
    CD_RECALL,
    CD_REFUSAL,
    VEHICLE_DEPARTURE,
}