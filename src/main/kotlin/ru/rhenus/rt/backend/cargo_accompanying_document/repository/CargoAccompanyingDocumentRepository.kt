package ru.rhenus.rt.backend.cargo_accompanying_document.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocument
import ru.rhenus.rt.backend.vehicle_entry_act.model.VehicleEntryAct

interface CargoAccompanyingDocumentRepository: JpaRepository<CargoAccompanyingDocument, Long>, JpaSpecificationExecutor<CargoAccompanyingDocument> {
    fun findByUuid1c(uuid1c: String): CargoAccompanyingDocument?

    fun findByVehicleEntryActIn(vehicleEntryAct: MutableCollection<VehicleEntryAct>): List<CargoAccompanyingDocument>
}