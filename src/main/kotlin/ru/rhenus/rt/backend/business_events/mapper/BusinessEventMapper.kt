package ru.rhenus.rt.backend.business_events.mapper

import org.mapstruct.Mapper
import ru.rhenus.rt.backend.business_events.model.BusinessEvent
import ru.rhenus.rt.backend.business_events.model.BusinessEventDto

/**
 * The main reason for this special case mapper is that we need to avoid cyclic dependencies in Mapstruct generated beans
 * 'Child to parent' mappings are done here (e.g. counterpartyId -> Counterparty in a CargoAccompanyingDocument)
 */
@Mapper(componentModel = "spring")
abstract class BusinessEventMapper{

    abstract fun toDto(entity: BusinessEvent?): BusinessEventDto

}