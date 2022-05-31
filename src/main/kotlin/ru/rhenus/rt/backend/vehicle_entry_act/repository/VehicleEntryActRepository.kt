package ru.rhenus.rt.backend.vehicle_entry_act.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.rhenus.rt.backend.vehicle_entry_act.model.VehicleEntryAct

interface VehicleEntryActRepository : JpaRepository<VehicleEntryAct, Long>, JpaSpecificationExecutor<VehicleEntryAct> {
    fun findByUuid1c(uuid1c: String): VehicleEntryAct?

}