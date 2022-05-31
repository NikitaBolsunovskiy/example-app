package ru.rhenus.rt.backend._common_.mapper

import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired
import ru.rhenus.rt.backend._common_.repository.JpaRepositoryExtensions.findByNullableIdOrNull
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocument
import ru.rhenus.rt.backend.counterparty.model.Counterparty
import ru.rhenus.rt.backend.counterparty.repository.CounterpartyRepository
import ru.rhenus.rt.backend.vehicle_entry_act.model.VehicleEntryAct
import ru.rhenus.rt.backend.vehicle_entry_act.repository.VehicleEntryActRepository

/**
 * The main reason for this special case mapper is that we need to avoid cyclic dependencies in Mapstruct generated beans
 * 'Child to parent' mappings are done here (e.g. counterpartyId -> Counterparty in a CargoAccompanyingDocument)
 */
@Mapper(componentModel = "spring")
abstract class IdToEntityMapper{

    @Autowired
    private lateinit var counterpartyRepository: CounterpartyRepository
    @Autowired
    private lateinit var vehicleEntryActRepository: VehicleEntryActRepository

    @Suppress("unused")
    fun idToCounterparty(id: Long?): Counterparty? = counterpartyRepository.findByNullableIdOrNull(id)
    @Suppress("unused")
    fun idToVehicleEntryAct(id: Long?): VehicleEntryAct? = vehicleEntryActRepository.findByNullableIdOrNull(id)
    fun CargoAccompanyingDocumentToId(entity: CargoAccompanyingDocument?): Long? = entity?.id
}