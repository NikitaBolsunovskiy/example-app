package controller

import de.rhenus.scs.customs.order.mapper.CustomsOrderSegmentMapper
import de.rhenus.scs.customs.order.model.CustomsOrderSegmentDto
import de.rhenus.scs.customs.order.service.CustomsOrderSegmentTransmitService
import de.rhenus.scs.customs.order.service.CustomsOrderSegmentService
import de.rhenus.scs.customs.security.permissions.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
class CustomsOrderSegmentController(
    private val customsOrderSegmentMapper: CustomsOrderSegmentMapper,
    private val customsOrderSegmentService: CustomsOrderSegmentService,
    private val customsOrderSegmentTransmitService: CustomsOrderSegmentTransmitService,
) {
    @CustomsOrderEntityReadPermission
    @GetMapping("customs-order/{customsOrderId}/segment/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<CustomsOrderSegmentDto> = ResponseEntity.ok(
        customsOrderSegmentMapper.toDto(customsOrderSegmentService.get(id))
    )

    @CustomsOrderSegmentUpdatePermission
    @PostMapping("customs-order/{customsOrderId}/segment/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody orderSegment: CustomsOrderSegmentDto
    ): ResponseEntity<CustomsOrderSegmentDto> = ResponseEntity.ok(
        customsOrderSegmentMapper.toDto(
            customsOrderSegmentService.update(customsOrderSegmentMapper.toEntity(orderSegment))
        )
    )

    @CustomsOrderSegmentUpdatePermission
    @PatchMapping("customs-order/{customsOrderId}/segment/{id}")
    fun changeStatus(
        @PathVariable id: Long,
        @RequestParam statusId: Long,
    ): ResponseEntity<Void> {
        customsOrderSegmentService.changeStatus(id, statusId)
        return ResponseEntity.ok().build()
    }

    @CustomsOrderSegmentExportPermission
    @PostMapping("customs-order-segment/transmit")
    fun transmit(
        @RequestBody data: Map<String, List<Long>>,
        @RequestParam(required = false) confirmed: Boolean
    ): ResponseEntity<Void> {
        if (data.containsKey("segmentIds")) {
            val ids = data["segmentIds"]
            customsOrderSegmentTransmitService.transmit(ids!!, confirmed)
        }
        return ResponseEntity.ok().build()
    }

    @CustomsOrderSegmentResetTransmitPermission
    @GetMapping("customs-order-segment/{id}/reset-transmit")
    fun resetTransmit(@PathVariable id: Long): ResponseEntity<Void> {
        customsOrderSegmentTransmitService.reset(id)
        return ResponseEntity.ok().build()
    }
}
