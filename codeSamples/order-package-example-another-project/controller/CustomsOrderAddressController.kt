package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderAddressMapper
import de.rhenus.scs.customs.order.model.CustomsOrderAddressDto
import de.rhenus.scs.customs.order.service.CustomsOrderAddressService
import de.rhenus.scs.customs.security.permissions.CustomsOrderEntityReadPermission
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customs-order/{customsOrderId}/address")
class CustomsOrderAddressController(
    private val customsOrderAddressMapper: CustomsOrderAddressMapper,
    private val customsOrderAddressService: CustomsOrderAddressService,
) {
    @CustomsOrderEntityReadPermission
    @RequestMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<CustomsOrderAddressDto> {
        return ResponseEntity(
            customsOrderAddressMapper.toDto(
                customsOrderAddressService.get(
                    id
                )
            ), HttpStatus.OK
        )
    }

    @PreAuthorize("hasPermission(#addressDto, 'UPDATE')")
    @PostMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody addressDto: CustomsOrderAddressDto
    ): ResponseEntity<CustomsOrderAddressDto> {
        val customsOrderAddress = customsOrderAddressMapper.toEntity(addressDto)
        return ResponseEntity(
            customsOrderAddressMapper.toDto(customsOrderAddressService.update(customsOrderAddress)),
            HttpStatus.OK
        )
    }

    @PreAuthorize("hasPermission(#addressDto, 'CREATE')")
    @PostMapping
    fun create(@RequestBody addressDto: CustomsOrderAddressDto): ResponseEntity<CustomsOrderAddressDto> {
        val customsOrderAddress = customsOrderAddressMapper.toEntity(addressDto)
        return ResponseEntity(
            customsOrderAddressMapper.toDto(customsOrderAddressService.create(customsOrderAddress)),
            HttpStatus.OK
        )
    }
}
