package ru.rhenus.rt.backend.business_events.service

import org.springframework.beans.BeanUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.rhenus.rt.backend._common_.service.Service1CExtension
import ru.rhenus.rt.backend.business_events.model.BusinessEvent
import ru.rhenus.rt.backend.business_events.model.BusinessEventDto1C
import ru.rhenus.rt.backend.business_events.repository.BusinessEventRepository
import javax.persistence.EntityNotFoundException
import javax.validation.ValidationException

@Service
class BusinessEventService(
    val businessEventRepository: BusinessEventRepository,
): Service1CExtension<BusinessEventDto1C> {

    @kotlin.jvm.Throws(ValidationException::class)
    fun create(businessEvent: BusinessEvent): BusinessEvent {
        if (businessEvent.id !== null) throw ValidationException("'id' must be null on 'create'")
        return businessEventRepository.save(businessEvent)
    }

    @kotlin.jvm.Throws(ValidationException::class, EntityNotFoundException::class)
    fun update(businessEvent: BusinessEvent): BusinessEvent {
        businessEventRepository.findByIdOrNull(
            businessEvent.id ?: throw ValidationException("'id' must not be null on 'update'")
        ) ?: throw EntityNotFoundException("entity with id: ${businessEvent.id} not found")
        return businessEventRepository.save(businessEvent)
    }

    @kotlin.jvm.Throws(ValidationException::class)
    override fun createOrUpdate(dto1C: BusinessEventDto1C): Boolean {
        dto1C.uuid1c ?: throw ValidationException("'uuid1c' must not be null on 'create/update'")
        val entityInDB = dto1C.uuid1c!!.let(businessEventRepository::findByUuid1c)
        val entityToPersist = BusinessEvent()
        BeanUtils.copyProperties(dto1C, entityToPersist)
        entityToPersist.apply { if (entityInDB !== null) id = entityInDB.id }
        if (entityInDB !== null) update(entityToPersist) else create(entityToPersist)
        return entityInDB === null
    }



}