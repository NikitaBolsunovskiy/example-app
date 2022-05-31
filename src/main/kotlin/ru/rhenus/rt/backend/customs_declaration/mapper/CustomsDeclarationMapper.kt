package ru.rhenus.rt.backend.customs_declaration.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.rhenus.rt.backend._common_.mapper.IdToEntityMapper
import ru.rhenus.rt.backend.customs_declaration.model.CustomsDeclaration
import ru.rhenus.rt.backend.customs_declaration.model.CustomsDeclarationDto

/**
 * The main reason for this special case mapper is that we need to avoid cyclic dependencies in Mapstruct generated beans
 * 'Child to parent' mappings are done here (e.g. counterpartyId -> Counterparty in a CargoAccompanyingDocument)
 */
@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
abstract class CustomsDeclarationMapper{

    @Mapping(target = "counterpartyId", source = "counterparty.id")
    @Mapping(target = "cargoAccompanyingDocumentIds", source = "cargoAccompanyingDocuments")
    abstract fun toDto(entity: CustomsDeclaration?): CustomsDeclarationDto

}