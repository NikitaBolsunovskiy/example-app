package ru.rhenus.rt.backend.business_events.contoller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rhenus.rt.backend.business_events.model.BusinessEventDto1C
import ru.rhenus.rt.backend.business_events.service.BusinessEventService

@RestController
@RequestMapping("/business-event-1c")
class BusinessEventController1C(
    private val businessEventService: BusinessEventService,
) {

    @PostMapping
    fun createUpdate(@RequestBody businessEventDto1C: BusinessEventDto1C): ResponseEntity<Unit> {
        val created = businessEventService.createOrUpdate(businessEventDto1C)
        return ResponseEntity( if(created) HttpStatus.CREATED else HttpStatus.ACCEPTED )
    }

    @PostMapping("/batch")
    fun createUpdateBatch(@RequestBody businessEventDto1Cs: List<BusinessEventDto1C>): ResponseEntity<Map<String, Boolean>> {
        return ResponseEntity(businessEventService.createOrUpdateBatch(businessEventDto1Cs), HttpStatus.ACCEPTED)
    }

}