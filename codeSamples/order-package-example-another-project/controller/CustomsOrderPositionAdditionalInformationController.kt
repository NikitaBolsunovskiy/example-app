package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderPositionAdditionalInformationMapper
import de.rhenus.scs.customs.order.model.CustomsOrderPositionAdditionalInformationDto
import de.rhenus.scs.customs.order.service.CustomsOrderPositionAdditionalInformationService
import de.rhenus.scs.customs.security.permissions.CustomsOrderEntityReadPermission
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customs-order-position-additional-information")
class CustomsOrderPositionAdditionalInformationController(
    private val additionalInformationService: CustomsOrderPositionAdditionalInformationService,
    private val additionalInformationMapper: CustomsOrderPositionAdditionalInformationMapper,
) {
    @CustomsOrderEntityReadPermission
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<CustomsOrderPositionAdditionalInformationDto> {
        val information = additionalInformationService.get(id)
        return ResponseEntity(additionalInformationMapper.toDto(information), HttpStatus.OK)
    }

    @PreAuthorize("hasPermission(#info, 'UPDATE')")
    @PostMapping("/{id}")
    fun update(@RequestBody info: CustomsOrderPositionAdditionalInformationDto): ResponseEntity<CustomsOrderPositionAdditionalInformationDto> {
        val information = additionalInformationMapper.toEntity(info)
        return ResponseEntity(
            additionalInformationMapper.toDto(additionalInformationService.update(information)),
            HttpStatus.OK
        )
    }

    @PreAuthorize("hasPermission(#info, 'CREATE')")
    @PostMapping
    fun create(@RequestBody info: CustomsOrderPositionAdditionalInformationDto): ResponseEntity<CustomsOrderPositionAdditionalInformationDto> {
        val information = additionalInformationMapper.toEntity(info)
        return ResponseEntity(
            additionalInformationMapper.toDto(additionalInformationService.create(information)),
            HttpStatus.OK
        )
    }
}
