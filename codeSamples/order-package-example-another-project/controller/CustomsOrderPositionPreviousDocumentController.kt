package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderPositionPreviousDocumentMapper
import de.rhenus.scs.customs.order.model.CustomsOrderPositionPreviousDocumentDto
import de.rhenus.scs.customs.order.service.CustomsOrderPositionPreviousDocumentService
import de.rhenus.scs.customs.security.permissions.CustomsOrderEntityReadPermission
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customs-order-position-previous-document")
class CustomsOrderPositionPreviousDocumentController(
    private val previousDocumentService: CustomsOrderPositionPreviousDocumentService,
    private val previousDocumentMapper: CustomsOrderPositionPreviousDocumentMapper,
) {
    @CustomsOrderEntityReadPermission
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<CustomsOrderPositionPreviousDocumentDto> {
        val information = previousDocumentService.get(id)
        return ResponseEntity(previousDocumentMapper.toDto(information), HttpStatus.OK)
    }

    @PreAuthorize("hasPermission(#info, 'UPDATE')")
    @PostMapping("/{id}")
    fun update(@RequestBody info: CustomsOrderPositionPreviousDocumentDto): ResponseEntity<CustomsOrderPositionPreviousDocumentDto> {
        val information = previousDocumentMapper.toEntity(info)
        return ResponseEntity(
            previousDocumentMapper.toDto(previousDocumentService.update(information)),
            HttpStatus.OK
        )
    }

    @PreAuthorize("hasPermission(#info, 'CREATE')")
    @PostMapping
    fun create(@RequestBody info: CustomsOrderPositionPreviousDocumentDto): ResponseEntity<CustomsOrderPositionPreviousDocumentDto> {
        val information = previousDocumentMapper.toEntity(info)
        return ResponseEntity(
            previousDocumentMapper.toDto(previousDocumentService.create(information)),
            HttpStatus.OK
        )
    }
}
