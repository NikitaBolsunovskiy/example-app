package ru.rhenus.rt.backend.vehicle_entry_act.service

import org.springframework.beans.BeanUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.rhenus.rt.backend._common_.service.Service1CExtension
import ru.rhenus.rt.backend.vehicle_entry_act.model.VehicleEntryAct
import ru.rhenus.rt.backend.vehicle_entry_act.model.VehicleEntryActDto1C
import ru.rhenus.rt.backend.vehicle_entry_act.repository.VehicleEntryActRepository
import javax.persistence.EntityNotFoundException
import javax.validation.ValidationException

@Service
class VehicleEntryActService(
    val vehicleEntryActRepository: VehicleEntryActRepository,
): Service1CExtension<VehicleEntryActDto1C> {

    @kotlin.jvm.Throws(ValidationException::class)
    fun create(vehicleEntryAct: VehicleEntryAct): VehicleEntryAct {
        if (vehicleEntryAct.id !== null) throw ValidationException("'id' must be null on 'create'")
        return vehicleEntryActRepository.save(vehicleEntryAct)
    }

    @kotlin.jvm.Throws(ValidationException::class, EntityNotFoundException::class)
    fun update(vehicleEntryAct: VehicleEntryAct): VehicleEntryAct {
        vehicleEntryActRepository.findByIdOrNull(
            vehicleEntryAct.id ?: throw ValidationException("'id' must not be null on 'update'")
        ) ?: throw EntityNotFoundException("entity with id: ${vehicleEntryAct.id} not found")
        return vehicleEntryActRepository.save(vehicleEntryAct)
    }

    @kotlin.jvm.Throws(ValidationException::class)
    override fun createOrUpdate(dto1C: VehicleEntryActDto1C): Boolean {
        dto1C.uuid1c ?: throw ValidationException("'uuid1c' must not be null on 'create/update'")
        val entityInDB = dto1C.uuid1c!!.let(vehicleEntryActRepository::findByUuid1c)
        val entityToPersist = VehicleEntryAct()
        BeanUtils.copyProperties(dto1C, entityToPersist)
        entityToPersist.apply { if (entityInDB !== null) id = entityInDB.id }
        if (entityInDB !== null) update(entityToPersist) else create(entityToPersist)
        return entityInDB === null
    }

}