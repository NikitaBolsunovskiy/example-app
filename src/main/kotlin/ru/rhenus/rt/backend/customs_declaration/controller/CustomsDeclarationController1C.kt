package ru.rhenus.rt.backend.customs_declaration.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rhenus.rt.backend.customs_declaration.model.CustomsDeclarationDto1C
import ru.rhenus.rt.backend.customs_declaration.service.CustomsDeclarationService

@RestController
@RequestMapping("/customs-declaration-1c")
class CustomsDeclarationController1C(
    private val customsDeclarationService: CustomsDeclarationService,
) {

    @PostMapping
    fun createUpdate(@RequestBody customsDeclarationDto1C: CustomsDeclarationDto1C): ResponseEntity<Unit> {
        val created = customsDeclarationService.createOrUpdate(customsDeclarationDto1C)
        return ResponseEntity(if (created) HttpStatus.CREATED else HttpStatus.ACCEPTED)
    }

    @PostMapping("/batch")
    fun createUpdateBatch(@RequestBody customsDeclarationDto1Cs: List<CustomsDeclarationDto1C>): ResponseEntity<Map<String, Boolean>> {
        return ResponseEntity(
            customsDeclarationService.createOrUpdateBatch(customsDeclarationDto1Cs),
            HttpStatus.ACCEPTED
        )
    }

}