package model

enum class ModeOfTransport(val code: Long) {
    NONE(0),
    MARITIME(1),
    RAIL(2),
    ROAD(3),
    AIR(4),
    MAIL(5),
    MULTIMODAL(6),
    FIXED_TRANSPORT_INSTALLATIONS(7),
    INLAND_WATER(8),
    UNKNOWN(9),
}
