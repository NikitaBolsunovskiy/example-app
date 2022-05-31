package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderPositionTaxMapper
import de.rhenus.scs.customs.order.model.CustomsOrderPositionTaxDto
import de.rhenus.scs.customs.order.service.CustomsOrderPositionTaxService
import de.rhenus.scs.customs.security.permissions.CustomsOrderEntityReadPermission
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customs-order-position-tax")
class CustomsOrderPositionTaxController(
    private val taxService: CustomsOrderPositionTaxService,
    private val taxMapper: CustomsOrderPositionTaxMapper,
) {

    @CustomsOrderEntityReadPermission
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<CustomsOrderPositionTaxDto> {
        val information = taxService.get(id)
        return ResponseEntity(taxMapper.toDto(information), HttpStatus.OK)
    }

    @PreAuthorize("hasPermission(#info, 'UPDATE')")
    @PostMapping("/{id}")
    fun update(@RequestBody info: CustomsOrderPositionTaxDto): ResponseEntity<CustomsOrderPositionTaxDto> {
        val information = taxMapper.toEntity(info)
        return ResponseEntity(taxMapper.toDto(taxService.update(information)), HttpStatus.OK)
    }

    @PreAuthorize("hasPermission(#info, 'CREATE')")
    @PostMapping
    fun create(@RequestBody info: CustomsOrderPositionTaxDto): ResponseEntity<CustomsOrderPositionTaxDto> {
        val information = taxMapper.toEntity(info)
        return ResponseEntity(taxMapper.toDto(taxService.create(information)), HttpStatus.OK)
    }
}
