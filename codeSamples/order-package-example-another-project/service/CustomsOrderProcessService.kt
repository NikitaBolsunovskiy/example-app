package service

import de.rhenus.scs.customs.common.events.CctApplicationEventPublisher
import de.rhenus.scs.customs.order.event.CustomsOrderStatusChangedEvent
import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderStatus
import de.rhenus.scs.customs.order.model.OrderStatus
import de.rhenus.scs.customs.order.repository.CustomsOrderRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderStatusRepository
import org.apache.commons.lang3.BooleanUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomsOrderProcessService(
    private val customsOrderRepository: CustomsOrderRepository,
    private val customsOrderStatusRepository: CustomsOrderStatusRepository,
    private val customsOrderStatusService: CustomsOrderStatusService,
    private val applicationEventPublisher: CctApplicationEventPublisher,
) {
    @Transactional
    fun changeStatus(id: Long?, statusId: Long): CustomsOrder? {
        val order: CustomsOrder? = customsOrderRepository.findByIdOrNull(id)
        return if (null != order) {
            val status = OrderStatus.values().single { it.value == statusId }
            this.changeStatus(order, status)
        } else null
    }

    @Transactional
    fun changeStatus(order: CustomsOrder, orderStatus: OrderStatus): CustomsOrder {
        if (order.customsOrderStatus == OrderStatus.STATUS_NEW && orderStatus == OrderStatus.STATUS_PREPARED) {
            customsOrderStatusService.create(order, orderStatus)
            applicationEventPublisher.publishEvent(CustomsOrderStatusChangedEvent(order.id, orderStatus.value))
            customsOrderStatusService.create(order, OrderStatus.STATUS_IN_PROGRESS)
            applicationEventPublisher.publishEvent(
                CustomsOrderStatusChangedEvent(order.id, OrderStatus.STATUS_IN_PROGRESS.value)
            )
            order.customsOrderStatus = OrderStatus.STATUS_IN_PROGRESS
            customsOrderRepository.save(order)
        } else {
            order.customsOrderStatus = orderStatus
            val savedOrder = customsOrderRepository.save(order)
            customsOrderStatusService.create(savedOrder, orderStatus)
            applicationEventPublisher.publishEvent(CustomsOrderStatusChangedEvent(savedOrder.id, orderStatus.value))
        }
        return order
    }

    @Transactional
    fun cancel(id: Long?) {
        val customsOrder: CustomsOrder? = customsOrderRepository.findByIdOrNull(id)
        if (null != customsOrder && customsOrder.customsOrderStatus != OrderStatus.STATUS_CANCELLED) {
            customsOrderStatusService.create(customsOrder, OrderStatus.STATUS_CANCELLED)
            customsOrder.customsOrderStatus = OrderStatus.STATUS_CANCELLED
            customsOrderRepository.save(customsOrder)
            applicationEventPublisher.publishEvent(
                CustomsOrderStatusChangedEvent(id, OrderStatus.STATUS_CANCELLED.value)
            )
        }
    }

    @Transactional
    fun finishOrderIfAllSegmentsTransmittedAndAutomatic(customsOrder: CustomsOrder) {
        var allTransmitted = true
        var allAutomatic = true
        val segmentList = customsOrder.customsOrderSegments
        for (customsOrderSegment in segmentList) {
            if (BooleanUtils.isFalse(customsOrderSegment.transmitted)) {
                allTransmitted = false
                break
            }
            if (BooleanUtils.isNotTrue(customsOrderSegment.automatic)) {
                allAutomatic = false
            }
        }
        if (allAutomatic && allTransmitted) {
            finishCustomsOrder(customsOrder)
        }
    }

    @Transactional
    fun reopen(id: Long?) {
        val customsOrder: CustomsOrder? = customsOrderRepository.findByIdOrNull(id)
        if (null != customsOrder && (customsOrder.customsOrderStatus == OrderStatus.STATUS_CANCELLED)) {
            val statusList: List<CustomsOrderStatus> = customsOrderStatusRepository.findByCustomsOrderIdOrderByIdDesc(id)

            if (statusList.size > 1) {

                // get back to status before last status...
                val previousStatus = statusList[1].status
                previousStatus?.let { customsOrderStatusService.create(customsOrder, it) }
                customsOrder.customsOrderStatus = previousStatus
                val statusValue = previousStatus?.value
                if(statusValue !== null) {
                    customsOrderRepository.save(customsOrder)
                    applicationEventPublisher.publishEvent(CustomsOrderStatusChangedEvent(id, statusValue))
                }
            }
        }
    }

    private fun finishCustomsOrder(customsOrder: CustomsOrder) {
        customsOrder.customsOrderStatus = OrderStatus.STATUS_COORDINATION_FINISHED
        val savedOrder = customsOrderRepository.save(customsOrder)
        customsOrderStatusService.create(customsOrder, OrderStatus.STATUS_COORDINATION_FINISHED)
        applicationEventPublisher.publishEvent(
            CustomsOrderStatusChangedEvent(
                savedOrder.id, OrderStatus.STATUS_COORDINATION_FINISHED.value
            )
        )
    }
}
