package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderInvoiceMapper
import de.rhenus.scs.customs.order.model.CustomsOrderInvoiceDto
import de.rhenus.scs.customs.order.service.CustomsOrderInvoiceService
import de.rhenus.scs.customs.security.permissions.CustomsOrderEntityReadPermission
import de.rhenus.scs.customs.security.permissions.CustomsOrderReadPermission
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customs-order/{customsOrderId}/invoice")
class CustomsOrderInvoiceController(
    private val customsOrderInvoiceService: CustomsOrderInvoiceService,
    private val customsOrderInvoiceMapper: CustomsOrderInvoiceMapper,
) {

    @CustomsOrderEntityReadPermission
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<CustomsOrderInvoiceDto> {
        val customsOrderInvoice = customsOrderInvoiceService.get(id)
        return ResponseEntity(customsOrderInvoiceMapper.toDto(customsOrderInvoice), HttpStatus.OK)
    }

    @PreAuthorize("hasPermission(#customsOrderInvoiceDto, 'UPDATE')")
    @PostMapping("/{id}")
    fun update(@RequestBody customsOrderInvoiceDto: CustomsOrderInvoiceDto): ResponseEntity<CustomsOrderInvoiceDto> {
        val customsOrderInvoice = customsOrderInvoiceMapper.toEntity(customsOrderInvoiceDto)
        return ResponseEntity(
            customsOrderInvoiceMapper.toDto(customsOrderInvoiceService.update(customsOrderInvoice)!!),
            HttpStatus.OK
        )
    }

    @PreAuthorize("hasPermission(#customsOrderInvoiceDto, 'CREATE')")
    @PostMapping
    fun create(@RequestBody customsOrderInvoiceDto: CustomsOrderInvoiceDto): ResponseEntity<CustomsOrderInvoiceDto> {
        val customsOrderInvoice = customsOrderInvoiceMapper.toEntity(customsOrderInvoiceDto)
        return ResponseEntity(
            customsOrderInvoiceMapper.toDto(customsOrderInvoiceService.create(customsOrderInvoice)),
            HttpStatus.OK
        )
    }

    @CustomsOrderReadPermission
    @GetMapping
    fun getInvoiceNumbers(@PathVariable customsOrderId: Long?): ResponseEntity<List<String>> {
        return ResponseEntity(
            customsOrderInvoiceService.getInvoiceNumbersByCustomsOrderId(customsOrderId),
            HttpStatus.OK
        )
    }
}
