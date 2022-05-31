package ru.rhenus.rt.backend.cargo_accompanying_document.service

import org.springframework.beans.BeanUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.rhenus.rt.backend._common_.service.Service1CExtension
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocument
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocumentDto1C
import ru.rhenus.rt.backend.cargo_accompanying_document.repository.CargoAccompanyingDocumentRepository
import ru.rhenus.rt.backend.counterparty.repository.CounterpartyRepository
import ru.rhenus.rt.backend.vehicle_entry_act.repository.VehicleEntryActRepository
import javax.persistence.EntityNotFoundException
import javax.validation.ValidationException

@Service
class CargoAccompanyingDocumentService(
    val cargoAccompanyingDocumentRepository: CargoAccompanyingDocumentRepository,
    val counterpartyRepository: CounterpartyRepository,
    val vehicleEntryActRepository: VehicleEntryActRepository,
):Service1CExtension<CargoAccompanyingDocumentDto1C> {

    @kotlin.jvm.Throws(ValidationException::class)
    fun create(cargoAccompanyingDocument: CargoAccompanyingDocument): CargoAccompanyingDocument {
        if (cargoAccompanyingDocument.id !== null) throw ValidationException("'id' must be null on 'create'")
        return cargoAccompanyingDocumentRepository.save(cargoAccompanyingDocument)
    }

    @kotlin.jvm.Throws(ValidationException::class, EntityNotFoundException::class)
    fun update(cargoAccompanyingDocument: CargoAccompanyingDocument): CargoAccompanyingDocument {
        cargoAccompanyingDocumentRepository.findByIdOrNull(
            cargoAccompanyingDocument.id ?: throw ValidationException("'id' must not be null on 'update'")
        ) ?: throw EntityNotFoundException("entity with id: ${cargoAccompanyingDocument.id} not found")
        return cargoAccompanyingDocumentRepository.save(cargoAccompanyingDocument)
    }

    @kotlin.jvm.Throws(ValidationException::class, EntityNotFoundException::class)
    override fun createOrUpdate(dto1C: CargoAccompanyingDocumentDto1C): Boolean {

        dto1C.uuid1c ?: throw ValidationException("'uuid1c' must not be null on 'create/update'")
        val counterparty = counterpartyRepository.findByUuid1c(
            dto1C.counterpartyUuid1c ?: throw ValidationException("'counterpartyUuid1c' must not be null on 'create/update'")
        ) ?: throw EntityNotFoundException("Counterparty with uuid1c: ${dto1C.counterpartyUuid1c} not found")
        val vehicleEntryAct = vehicleEntryActRepository.findByUuid1c(
            dto1C.vehicleEntryActUuid1c ?: throw ValidationException("'vehicleEntryActUuid1c' must not be null on 'create/update'")
        ) ?: throw EntityNotFoundException("VehicleEntryAct with uuid1c: ${dto1C.vehicleEntryActUuid1c} not found")

        val entityInDB = dto1C.uuid1c!!.let(cargoAccompanyingDocumentRepository::findByUuid1c)
        val entityToPersist = CargoAccompanyingDocument()
        BeanUtils.copyProperties(dto1C, entityToPersist)
        entityToPersist.apply { if (entityInDB !== null) id = entityInDB.id }
        entityToPersist.apply { this.counterparty = counterparty; this.vehicleEntryAct = vehicleEntryAct }
        if (entityInDB !== null) update(entityToPersist) else create(entityToPersist)
        return entityInDB === null
    }



}