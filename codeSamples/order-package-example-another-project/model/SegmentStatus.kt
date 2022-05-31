package model

enum class SegmentStatus(val code: Long, val statusText: String?) {
    UNKNOWN(0L, "Unknown"),
    WAITING_FOR_ARRIVAL(1L, "WaitingForArrival"),
    ARRIVED(2L, "Arrived"),
    APPOINTMENT_NEEDED(3L, "AppointmentNeeded"),
    APPOINTMENT_DONE(4L, "AppointmentDone"),
    WAITING_FOR_DECLARATION(5L, "WaitingForDeclaration"),
    WAITING_FOR_CLARIFICATION(6L, "WaitingForClarification"),
    WAITING_FOR_CUSTOMS_RELEASE(7L, "WaitingForCustomsRelease"),
    DECLARATION_SUCCESSFUL(8L, "DeclarationSuccessful"),
    TRANSPORT_INSTRUCTIONS_NEEDED(9L, "TransportInstructionsNeeded"),
    FINISHED (10L, "Finished"),
    CANCELLED_BY_CUSTOMS (11L, "CancelledByCustoms"),
    EDI_ERROR (98L, "EdiError"),
    NOT_STARTED (99L, "NotStarted");

    companion object {
        fun fromLong(value: Long?) = values().find { it.code == value }
        fun fromStatusText(statusText: String?) = values().find { it.statusText == statusText } ?: UNKNOWN
    }
}
