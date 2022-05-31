package ru.rhenus.rt.backend.cargo_accompanying_document.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.rhenus.rt.backend._common_.mapper.IdToEntityMapper
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocument
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocumentDto

/**
 * The main reason for this special case mapper is that we need to avoid cyclic dependencies in Mapstruct generated beans
 * 'Child to parent' mappings are done here (e.g. counterpartyId -> Counterparty in a CargoAccompanyingDocument)
 */
@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
abstract class CargoAccompanyingDocumentMapper{

    @Mapping(target = "counterpartyId", source = "counterparty.id")
    @Mapping(target = "vehicleEntryActId", source = "vehicleEntryAct.id")
    abstract fun toDto(entity: CargoAccompanyingDocument?): CargoAccompanyingDocumentDto

}