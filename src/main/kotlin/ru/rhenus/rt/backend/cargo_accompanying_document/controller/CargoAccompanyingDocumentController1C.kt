package ru.rhenus.rt.backend.cargo_accompanying_document.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocumentDto1C
import ru.rhenus.rt.backend.cargo_accompanying_document.service.CargoAccompanyingDocumentService

@RestController
@RequestMapping("/cargo-accompanying-document-1c")
class CargoAccompanyingDocumentController1C(
    private val cargoAccompanyingDocumentService: CargoAccompanyingDocumentService,
) {

    @PostMapping
    fun createUpdate(@RequestBody CargoAccompanyingDocumentDto1C: CargoAccompanyingDocumentDto1C): ResponseEntity<Unit> {
        val created = cargoAccompanyingDocumentService.createOrUpdate(CargoAccompanyingDocumentDto1C)
        return ResponseEntity( if(created) HttpStatus.CREATED else HttpStatus.ACCEPTED )
    }

    @PostMapping("/batch")
    fun createUpdateBatch(@RequestBody CargoAccompanyingDocumentDto1Cs: List<CargoAccompanyingDocumentDto1C>): ResponseEntity<Map<String, Boolean>> {
        return ResponseEntity(cargoAccompanyingDocumentService.createOrUpdateBatch(CargoAccompanyingDocumentDto1Cs), HttpStatus.ACCEPTED )
    }

}