package model

enum class SegmentType(val value: String) {
    TRANSIT("Transit"), IMPORT("Import"), EXPORT("Export");

    companion object {
        fun fromValue(value: String?) = values().find { it.value == value || it.value.uppercase() == value }
    }
}
