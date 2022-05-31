package ru.rhenus.rt.backend.vehicle_entry_act.mapper

import org.mapstruct.Mapper
import ru.rhenus.rt.backend.vehicle_entry_act.model.VehicleEntryAct
import ru.rhenus.rt.backend.vehicle_entry_act.model.VehicleEntryActDto

/**
 * The main reason for this special case mapper is that we need to avoid cyclic dependencies in Mapstruct generated beans
 * 'Child to parent' mappings are done here (e.g. counterpartyId -> Counterparty in a CargoAccompanyingDocument)
 */
@Mapper(componentModel = "spring")
abstract class VehicleEntryActMapper{

    abstract fun toDto(entity: VehicleEntryAct?): VehicleEntryActDto

}