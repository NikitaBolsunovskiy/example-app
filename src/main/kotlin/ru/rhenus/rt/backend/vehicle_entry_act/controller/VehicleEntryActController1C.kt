package ru.rhenus.rt.backend.vehicle_entry_act.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rhenus.rt.backend.vehicle_entry_act.model.VehicleEntryActDto1C
import ru.rhenus.rt.backend.vehicle_entry_act.service.VehicleEntryActService

@RestController
@RequestMapping("/vehicle-entry-act-1c")
class VehicleEntryActController1C(
    private val vehicleEntryActService: VehicleEntryActService,
) {

    @PostMapping
    fun createUpdate(@RequestBody vehicleEntryActDto1C: VehicleEntryActDto1C): ResponseEntity<Unit> {
        val created = vehicleEntryActService.createOrUpdate(vehicleEntryActDto1C)
        return ResponseEntity( if(created) HttpStatus.CREATED else HttpStatus.ACCEPTED )
    }

    @PostMapping("/batch")
    fun createUpdateBatch(@RequestBody vehicleEntryActDto1Cs: List<VehicleEntryActDto1C>): ResponseEntity<Map<String, Boolean>> {
        return ResponseEntity(vehicleEntryActService.createOrUpdateBatch(vehicleEntryActDto1Cs), HttpStatus.ACCEPTED )
    }

}