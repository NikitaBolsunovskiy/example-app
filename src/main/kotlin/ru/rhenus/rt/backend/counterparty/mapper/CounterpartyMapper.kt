package ru.rhenus.rt.backend.counterparty.mapper

import org.mapstruct.Mapper
import ru.rhenus.rt.backend.counterparty.model.Counterparty
import ru.rhenus.rt.backend.counterparty.model.CounterpartyDto

/**
 * The main reason for this special case mapper is that we need to avoid cyclic dependencies in Mapstruct generated beans
 * 'Child to parent' mappings are done here (e.g. counterpartyId -> Counterparty in a CargoAccompanyingDocument)
 */
@Mapper(componentModel = "spring")
abstract class CounterpartyMapper{

    abstract fun toDto(entity: Counterparty?): CounterpartyDto

}