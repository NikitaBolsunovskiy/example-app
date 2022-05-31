package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderInvoiceAddressMapper
import de.rhenus.scs.customs.order.model.CustomsOrderInvoiceAddressDto
import de.rhenus.scs.customs.order.service.CustomsOrderInvoiceAddressService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customs-order/{customsOrderId}/invoice/{customsOrderInvoiceId}/address")
class CustomsOrderInvoiceAddressController(
    private val customsOrderInvoiceAddressMapper: CustomsOrderInvoiceAddressMapper,
    private val customsOrderInvoiceAddressService: CustomsOrderInvoiceAddressService,
) {

    @PreAuthorize("hasPermission(#addressDto, 'UPDATE')")
    @PostMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody addressDto: CustomsOrderInvoiceAddressDto
    ): ResponseEntity<CustomsOrderInvoiceAddressDto> {
        val customsOrderInvoiceAddress = customsOrderInvoiceAddressMapper.toEntity(addressDto)
        return ResponseEntity(
            customsOrderInvoiceAddressMapper.toDto(
                customsOrderInvoiceAddressService.update(customsOrderInvoiceAddress)
            ), HttpStatus.OK
        )
    }

    @PreAuthorize("hasPermission(#addressDto, 'CREATE')")
    @PostMapping
    fun create(@RequestBody addressDto: CustomsOrderInvoiceAddressDto): ResponseEntity<CustomsOrderInvoiceAddressDto> {
        val customsOrderInvoiceAddress = customsOrderInvoiceAddressMapper.toEntity(addressDto)
        return ResponseEntity(
            customsOrderInvoiceAddressMapper.toDto(
                customsOrderInvoiceAddressService.create(customsOrderInvoiceAddress)
            ), HttpStatus.OK
        )
    }
}
