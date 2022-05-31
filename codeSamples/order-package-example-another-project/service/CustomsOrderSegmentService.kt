package service

import de.rhenus.scs.customs.order.model.*
import de.rhenus.scs.customs.order.repository.CustomsOrderRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderSegmentRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderSegmentStatusRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.Valid
import javax.validation.ValidationException

@Service
class CustomsOrderSegmentService(
    private val customsOrderSegmentRepository: CustomsOrderSegmentRepository,
    private val customsOrderRepository: CustomsOrderRepository,
    private val customsOrderSegmentStatusRepository: CustomsOrderSegmentStatusRepository,
    private val customsOrderStatusService: CustomsOrderStatusService,
) {
    fun get(id: Long): CustomsOrderSegment? = customsOrderSegmentRepository.findByIdOrNull(id)

    fun update(segment: @Valid CustomsOrderSegment) = customsOrderSegmentRepository.save(segment)

    fun changeStatus(segmentId: Long?, statusId: Long) {
        val segment = customsOrderSegmentRepository.findByIdOrNull(segmentId)
        if(segment === null) {
            throw RuntimeException("No segment could be found for id $segmentId")
        }
        val segmentStatus = SegmentStatus.fromLong(statusId)
        if(segmentStatus === null) {
            throw RuntimeException("Invalid status-id provided for segment: $statusId")
        }
        this.changeStatus(segment, segmentStatus )
    }

    @Transactional
    fun changeStatus(segment: CustomsOrderSegment, segmentStatus: SegmentStatus) {
        if (segment.status != segmentStatus) {
            val savedSegment = updateSegmentStatus(segment, segmentStatus)

            if (savedSegment.status == SegmentStatus.EDI_ERROR) {
                return
            }
            if (savedSegment.status == SegmentStatus.APPOINTMENT_DONE) {
                updateSegmentStatus(savedSegment, SegmentStatus.WAITING_FOR_DECLARATION)
            }
            if (savedSegment.status == SegmentStatus.CANCELLED_BY_CUSTOMS) {
                cancelOtherSegments(savedSegment)
                savedSegment.customsOrder?.also { setCustomsOrderStatus(it, OrderStatus.STATUS_CANCELLED_BY_CUSTOMS) }
            }
            if (savedSegment.status == SegmentStatus.WAITING_FOR_ARRIVAL) {
                savedSegment.customsOrder?.also { setCustomsOrderStatus(it, OrderStatus.STATUS_IN_PROGRESS) }
            } else {
                savedSegment.customsOrder?.also { closeOrderIfAllSegmentsFinished(it) }
            }
        }
    }

    /**
     * Close Orders if all Segments are finished.
     */
    private fun closeOrderIfAllSegmentsFinished(order: CustomsOrder) {
        order.id?.let { customsOrderSegmentRepository.findByCustomsOrderId(it) }?.let { list ->
            list.forEach { segment ->
                if (segment.status != SegmentStatus.CANCELLED_BY_CUSTOMS
                    && segment.status != SegmentStatus.DECLARATION_SUCCESSFUL
                    && segment.status != SegmentStatus.FINISHED) {
                    return
                }
            }
        }

        setCustomsOrderStatus(order, OrderStatus.STATUS_COORDINATION_FINISHED)
    }

    /**
     * Cancel other Segments if a Segment was "cancelled by customs".
     */
    private fun cancelOtherSegments(segment: CustomsOrderSegment) {
        segment.customsOrder?.id?.let { id ->
            customsOrderSegmentRepository
                .findByCustomsOrderId(id)
                .filter { it.id != segment.id && it.status != SegmentStatus.FINISHED && it.status != SegmentStatus.CANCELLED_BY_CUSTOMS }
                .forEach {
                    updateSegmentStatus(it, SegmentStatus.CANCELLED_BY_CUSTOMS)
                }
        }
    }

    private fun updateSegmentStatus(
        segment: CustomsOrderSegment, statusId: SegmentStatus
    ): CustomsOrderSegment {
        segment.status = statusId
        val savedSegment = customsOrderSegmentRepository.save(segment)

        customsOrderSegmentStatusRepository.save(
            CustomsOrderSegmentStatus(
            segment = savedSegment,
            statusId = statusId,
            )
        )
        return savedSegment
    }

    private fun setCustomsOrderStatus(order: CustomsOrder, status: OrderStatus) {
        if (order.customsOrderStatus != status) {
            if (order.customsOrderStatus == OrderStatus.STATUS_COMPLIANCE_CHECK_FAILED) {
                throw ValidationException("validation.customsOrder.statusChangeNotPossibleDueToComplianceError")
            }

            order.customsOrderStatus = status
            customsOrderRepository.save(order)

            customsOrderStatusService.create(order = order, status = status)

            // applicationEventPublisher.publishEvent(CustomsOrderStatusChangedEvent(order.id, status))
        }
    }
}
