package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderUnitMapper
import de.rhenus.scs.customs.order.model.CustomsOrderUnitDto
import de.rhenus.scs.customs.order.service.CustomsOrderUnitService
import de.rhenus.scs.customs.security.permissions.CustomsOrderEntityReadPermission
import de.rhenus.scs.customs.security.permissions.CustomsOrderReadPermission
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customs-order/{customsOrderId}/unit")
class CustomsOrderUnitController(
    private val customsOrderUnitMapper: CustomsOrderUnitMapper,
    private val customsOrderUnitService: CustomsOrderUnitService,
) {
    @CustomsOrderEntityReadPermission
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<CustomsOrderUnitDto> =
        ResponseEntity(customsOrderUnitMapper.toDto(customsOrderUnitService.get(id)), HttpStatus.OK)

    @PreAuthorize("hasPermission(#unitDto, 'UPDATE')")
    @PostMapping("/{id}")
    fun update(
        @PathVariable id: Long?,
        @RequestBody unitDto: CustomsOrderUnitDto
    ): ResponseEntity<CustomsOrderUnitDto> {
        val customsOrderUnit = customsOrderUnitMapper.toEntity(unitDto)
        return ResponseEntity(
            customsOrderUnitMapper.toDto(customsOrderUnitService.update(customsOrderUnit)),
            HttpStatus.OK
        )
    }

    @PreAuthorize("hasPermission(#unitDto, 'CREATE')")
    @PostMapping
    fun create(@RequestBody unitDto: CustomsOrderUnitDto): ResponseEntity<CustomsOrderUnitDto> {
        val customsOrderUnit = customsOrderUnitMapper.toEntity(unitDto)
        return ResponseEntity(
            customsOrderUnitMapper.toDto(customsOrderUnitService.create(customsOrderUnit)),
            HttpStatus.OK
        )
    }

    @CustomsOrderReadPermission
    @GetMapping
    fun getUnitNumbers(@PathVariable customsOrderId: Long?): ResponseEntity<List<String>> {
        return ResponseEntity(customsOrderUnitService.getUnitNumbersByCustomsOrderId(customsOrderId), HttpStatus.OK)
    }
}
