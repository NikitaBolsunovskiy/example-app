package ru.rhenus.rt.backend.counterparty.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rhenus.rt.backend.counterparty.model.CounterpartyDto1C
import ru.rhenus.rt.backend.counterparty.service.CounterpartyService

@RestController
@RequestMapping("/counterparty-1c")
class CounterpartyController1C(
    private val counterpartyService: CounterpartyService,
) {

    @PostMapping
    fun createUpdate(@RequestBody counterpartyDto1C: CounterpartyDto1C): ResponseEntity<Unit> {
        val created = counterpartyService.createOrUpdate(counterpartyDto1C)
        return ResponseEntity( if(created) HttpStatus.CREATED else HttpStatus.ACCEPTED )
    }

    @PostMapping("/batch")
    fun createUpdateBatch(@RequestBody counterpartyDto1Cs: List<CounterpartyDto1C>): ResponseEntity<Map<String, Boolean>> {
        return ResponseEntity(counterpartyService.createOrUpdateBatch(counterpartyDto1Cs), HttpStatus.ACCEPTED )
    }

}