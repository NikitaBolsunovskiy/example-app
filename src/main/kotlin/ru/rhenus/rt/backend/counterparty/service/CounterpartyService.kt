package ru.rhenus.rt.backend.counterparty.service

import org.springframework.beans.BeanUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.rhenus.rt.backend._common_.service.Service1CExtension
import ru.rhenus.rt.backend.counterparty.model.Counterparty
import ru.rhenus.rt.backend.counterparty.model.CounterpartyDto1C
import ru.rhenus.rt.backend.counterparty.repository.CounterpartyRepository
import javax.persistence.EntityNotFoundException
import javax.validation.ValidationException

@Service
class CounterpartyService(
    val counterpartyRepository: CounterpartyRepository,
): Service1CExtension<CounterpartyDto1C> {

    @kotlin.jvm.Throws(ValidationException::class)
    fun create(counterparty: Counterparty): Counterparty {
        if (counterparty.id !== null) throw ValidationException("'id' must be null on 'create'")
        return counterpartyRepository.save(counterparty)
    }

    @kotlin.jvm.Throws(ValidationException::class, EntityNotFoundException::class)
    fun update(counterparty: Counterparty): Counterparty {
        counterpartyRepository.findByIdOrNull(
            counterparty.id ?: throw ValidationException("'id' must not be null on 'update'")
        ) ?: throw EntityNotFoundException("entity with id: ${counterparty.id} not found")
        return counterpartyRepository.save(counterparty)
    }

    @kotlin.jvm.Throws(ValidationException::class)
    override fun createOrUpdate(dto1C: CounterpartyDto1C): Boolean {
        dto1C.uuid1c ?: throw ValidationException("'uuid1c' must not be null on 'create/update'")
        val entityInDB = dto1C.uuid1c!!.let(counterpartyRepository::findByUuid1c)
        val entityToPersist = Counterparty()
        BeanUtils.copyProperties(dto1C, entityToPersist)
        entityToPersist.apply { if (entityInDB !== null) id = entityInDB.id }
        if (entityInDB !== null) update(entityToPersist) else create(entityToPersist)
        return entityInDB === null
    }

}