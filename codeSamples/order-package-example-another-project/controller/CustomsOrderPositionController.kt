package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderPositionMapper
import de.rhenus.scs.customs.order.model.CustomsOrderPositionDto
import de.rhenus.scs.customs.order.service.CsvToStringArrayReaderService
import de.rhenus.scs.customs.order.service.CustomsOrderPositionService
import de.rhenus.scs.customs.security.permissions.CustomsOrderEntityReadPermission
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/customs-order/{customsOrderId}/position")
class CustomsOrderPositionController(
    private val csvToStringArrayReaderService: CsvToStringArrayReaderService,
    private val customsOrderPositionService: CustomsOrderPositionService,
    private val customsOrderPositionMapper: CustomsOrderPositionMapper,
) {

    @CustomsOrderEntityReadPermission
    @GetMapping("/{id}")
    fun get(@PathVariable id: Long?): ResponseEntity<CustomsOrderPositionDto> {
        val customsOrderPosition = customsOrderPositionService.get(id)
        return ResponseEntity(customsOrderPositionMapper.toDto(customsOrderPosition), HttpStatus.OK)
    }

    @PreAuthorize("hasPermission(#customsOrderPositionDto, 'UPDATE')")
    @PostMapping("/{id}")
    fun update(@RequestBody customsOrderPositionDto: CustomsOrderPositionDto): ResponseEntity<CustomsOrderPositionDto> {
        val customsOrderPosition = customsOrderPositionMapper.toEntity(customsOrderPositionDto)
        return ResponseEntity(
            customsOrderPositionMapper.toDto(customsOrderPositionService.update(customsOrderPosition)),
            HttpStatus.OK
        )
    }

    @PreAuthorize("hasPermission(#customsOrderPositionDto, 'CREATE')")
    @PostMapping
    fun create(@RequestBody customsOrderPositionDto: CustomsOrderPositionDto): ResponseEntity<CustomsOrderPositionDto> {
        val customsOrderPosition = customsOrderPositionMapper.toEntity(customsOrderPositionDto)
        return ResponseEntity(
            customsOrderPositionMapper.toDto(customsOrderPositionService.create(customsOrderPosition)),
            HttpStatus.OK
        )
    }

    @PreAuthorize("hasPermission(#customsOrderId, 'de.rhenus.scs.customs.order.model.CustomsOrder', 'UPDATE')")
    @PostMapping(
        path = ["/csv"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun parseCsv(
        @PathVariable customsOrderId: Long?,
        @RequestPart file: MultipartFile?
    ): ResponseEntity<List<Array<String>>> {
        val result = csvToStringArrayReaderService.read(
            file!!
        )
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PreAuthorize("hasPermission(#customsOrderId, 'de.rhenus.scs.customs.order.model.CustomsOrder', 'UPDATE')")
    @PostMapping(
        path = ["/csv-import"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun createByCsv(
        @PathVariable customsOrderId: Long,
        @RequestParam fieldMappings: Array<String>,
        @RequestParam firstRowIsHeader: Boolean,
        @RequestParam(required = false) goodsValueCurrency: String?,
        @RequestParam(required = false) itemPriceSad42: Float?,
        @RequestParam(required = false) netMassSad38: Float?,
        @RequestPart file: MultipartFile?
    ): ResponseEntity<Void> {
        customsOrderPositionService.createByCsv(
            customsOrderId, listOf(*fieldMappings), goodsValueCurrency, itemPriceSad42, netMassSad38,
            firstRowIsHeader, file!!
        )
        return ResponseEntity.ok().build()
    }
}
