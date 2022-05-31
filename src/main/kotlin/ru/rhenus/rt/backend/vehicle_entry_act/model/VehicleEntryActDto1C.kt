package ru.rhenus.rt.backend.vehicle_entry_act.model

import ru.rhenus.rt.backend._common_.model.Dto1CAbstract

class VehicleEntryActDto1C(
    var consolidated: Boolean? = null,
    var actNumber: String? = null,
    var vehiclePlateNumber: String? = null
): Dto1CAbstract()
