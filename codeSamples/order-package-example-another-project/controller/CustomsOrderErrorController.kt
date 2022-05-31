package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderErrorMapper
import de.rhenus.scs.customs.order.model.CustomsOrderError
import de.rhenus.scs.customs.order.model.CustomsOrderErrorDto
import de.rhenus.scs.customs.order.service.CustomsOrderErrorService
import org.apache.commons.collections.CollectionUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/v1/customs-order")
class CustomsOrderErrorController(
    private val customsOrderErrorMapper: CustomsOrderErrorMapper,
    private val customsOrderErrorService: CustomsOrderErrorService,
) {

    @PostMapping("/error/overview")
    fun getOverview(@RequestBody customsOrderIdList: List<Long>): ResponseEntity<Map<Long, Long>> {
        return ResponseEntity(
            customsOrderErrorService.getEDIErrorOverview(customsOrderIdList),
            HttpStatus.OK
        )
    }

    @GetMapping("/{id}/error")
    fun find(@PathVariable id: Long): ResponseEntity<List<CustomsOrderErrorDto?>> {
        val errorList = customsOrderErrorService.findActiveByCustomsOrderId(id)
        return ResponseEntity(errorList.map { customsOrderErrorMapper.entityToDto(it) }, HttpStatus.OK)
    }

    @GetMapping("/{customsOrderId}/first-error")
    fun getFirst(@PathVariable customsOrderId: Long): ResponseEntity<CustomsOrderErrorDto> {
        val errors: List<CustomsOrderError> = customsOrderErrorService.findActiveByCustomsOrderId(customsOrderId)
        return if (CollectionUtils.isNotEmpty(errors)) {
            ResponseEntity(
                customsOrderErrorMapper.entityToDto(errors[0]),
                HttpStatus.OK
            )
        } else ResponseEntity(
            null,
            HttpStatus.NOT_FOUND
        )
    }
}
