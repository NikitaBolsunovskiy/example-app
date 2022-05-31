package service

import de.rhenus.scs.customs.common.events.CctApplicationEventPublisher
import de.rhenus.scs.customs.logbook.repository.EventRepository
import de.rhenus.scs.customs.notification.repository.NotificationRepository
import de.rhenus.scs.customs.order.event.CustomsOrderStatusChangedEvent
import de.rhenus.scs.customs.order.exception.CancellationRuleFoundException
import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderSegment
import de.rhenus.scs.customs.order.model.OrderStatus
import de.rhenus.scs.customs.order.model.SegmentType
import de.rhenus.scs.customs.order.repository.CustomsOrderRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderSegmentRepository
import org.apache.commons.collections.CollectionUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.validation.Valid
import javax.validation.ValidationException

@Service
class CustomsOrderService(
    private val applicationEventPublisher: CctApplicationEventPublisher,
    private val customsOrderRepository: CustomsOrderRepository,
    private val customsOrderSegmentRepository: CustomsOrderSegmentRepository,
    private val customsOrderSegmentBuildService: CustomsOrderSegmentBuildService,
    private val customsOrderStatusService: CustomsOrderStatusService,
    private val notificationRepository: NotificationRepository,
    private val eventRepository: EventRepository,
) {

    fun getByCustomsOrderNoWithSegments(customsOrderNo: String): CustomsOrder =
        customsOrderRepository.findByCustomsOrderNoWithSegments(customsOrderNo) ?: throw ValidationException("CustomsOrder not found!")

    fun getByCustomsOrderNo(customsOrderNo: String, withSubEntities: Boolean = false): CustomsOrder {
        val order = if (withSubEntities) customsOrderRepository.findByCustomsOrderNoWithSubEntities(customsOrderNo)
        else customsOrderRepository.findByCustomsOrderNo(customsOrderNo)

        return order ?: throw ValidationException("CustomsOrder not found!")
    }

    //TODO why does a single get needs to be executed in a transaction?
    @Transactional
    fun get(id: Long?): CustomsOrder =
        customsOrderRepository.findByIdOrNull(id) ?: throw ValidationException("CustomsOrder not found!")

    fun find(id: Long?): CustomsOrder? = customsOrderRepository.findByIdOrNull(id)

    @Transactional
    fun update(order: @Valid CustomsOrder): CustomsOrder? { // REVIEW: Isn't that a condition to do a safe call to save?
        val old = customsOrderRepository.findByIdOrNull(order.id)

        val oldStatusId = old?.customsOrderStatus
        val savedOrder = customsOrderRepository.save(order)
        if (oldStatusId != savedOrder.customsOrderStatus) {
            if (oldStatusId == OrderStatus.STATUS_COMPLIANCE_CHECK_FAILED) {
                throw ValidationException("validation.customsOrder.statusChangeNotPossibleDueToComplianceError")
            }

            savedOrder.customsOrderStatus?.let { customsOrderStatusService.create(savedOrder, it) }
        }

        if (CollectionUtils.isNotEmpty(savedOrder.customsOrderSegments)) {
            for (segment in savedOrder.customsOrderSegments) {
                customsOrderSegmentRepository.save(segment)
            }
        }

        val segmentList: List<CustomsOrderSegment> = customsOrderSegmentRepository.findByCustomsOrderId(savedOrder.id!!)
        try {
            if (CollectionUtils.isEmpty(segmentList) && CollectionUtils.isEmpty(
                    customsOrderSegmentBuildService.buildSegments(savedOrder)
                )
            ) {
                if (savedOrder.customsOrderStatus != OrderStatus.STATUS_INVALID) {
                    savedOrder.customsOrderStatus = OrderStatus.STATUS_INVALID
                    val invalidOrder = customsOrderRepository.save(savedOrder)
                    customsOrderStatusService.create(invalidOrder, OrderStatus.STATUS_INVALID)
                    applicationEventPublisher.publishEvent(
                        CustomsOrderStatusChangedEvent(
                            invalidOrder.id, OrderStatus.STATUS_INVALID.value
                        )
                    )
                }
            } else {
                if (savedOrder.customsOrderStatus == OrderStatus.STATUS_INVALID) {
                    savedOrder.customsOrderStatus = OrderStatus.STATUS_NEW
                    val invalidOrder = customsOrderRepository.save(savedOrder)
                    customsOrderStatusService.create(invalidOrder, OrderStatus.STATUS_NEW)
                }
            }
        } catch (e: CancellationRuleFoundException) { // ignore, should not happen!
        }
        return order
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun retriggerCustomsOrdersAfterRuleCreation(customerIdCCT: String?) {
        val orders = customsOrderRepository.findWithoutSegmentsForCustomerIdCct(customerIdCCT)
        for (order in orders) {
            update(order)
        }
    }

    fun countNonConsolidatedByCustomerIdCctAndCustomerReference(
        customerIdCct: String?, customerReference: String?
    ): Long {
        return customsOrderRepository.countNonConsolidatedByCustomerIdCctAndCustomerReference(
            customerIdCct, customerReference, listOf(OrderStatus.STATUS_CONSOLIDATED, OrderStatus.STATUS_REJECTED)
        )
    }

    fun countNonConsolidatedByCustomerIdCctAndCustomerReferenceAndSegment(
        customerIdCct: String?, customerReference: String?, segmentType: SegmentType?
    ): Long {
        return customsOrderRepository.countNonConsolidatedByCustomerIdCctAndCustomerReferenceAndSegment(
            customerIdCct, customerReference, segmentType
        )
    }

    @Transactional
    fun delete(id: Long) {
        customsOrderRepository.deleteById(id)
        notificationRepository.deleteByAggregateId(id)
        eventRepository.deleteByAggregateId(id)
    }

    fun getSegment(order: CustomsOrder, typeOfDeclaration: SegmentType?): CustomsOrderSegment? =
        order.customsOrderSegments.firstOrNull { it.type == typeOfDeclaration }

}
