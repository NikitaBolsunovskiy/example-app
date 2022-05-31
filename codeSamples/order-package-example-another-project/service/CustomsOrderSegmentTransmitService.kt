package service

import de.rhenus.scs.customs.common.events.CctApplicationEventPublisher
import de.rhenus.scs.customs.notification.service.NotificationService
import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderSegment
import de.rhenus.scs.customs.order.model.OrderStatus
import de.rhenus.scs.customs.orderexport.event.CustomsOrderSegmentTransmitEvent
import de.rhenus.scs.customs.orderexport.event.CustomsOrderSegmentResetEvent
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.ValidationException

@Service
class CustomsOrderSegmentTransmitService(
    private val applicationEventPublisher: CctApplicationEventPublisher,
    private val customsOrderUnitService: CustomsOrderUnitService,
    private val customsOrderInvoiceService: CustomsOrderInvoiceService,
    private val customsOrderPositionService: CustomsOrderPositionService,
    private val customsOrderSegmentService: CustomsOrderSegmentService,
    private val notificationService: NotificationService,
) {

    @Transactional
    fun transmit(segmentIds: List<Long>, unitAndInvoiceUsageConfirmed: Boolean) {
        segmentIds.forEach { id ->
            val segment: CustomsOrderSegment? = customsOrderSegmentService.get(id)
            if (null != segment && segment.transmitted != true) {
                if (segment.customsOrder?.let { customsOrderHasComplianceError(it) } == true) {
                    throw ValidationException("validation.customsOrderSegment.transmitNotPossibleDueToComplianceError")
                }
                if (!unitAndInvoiceUsageConfirmed) {
                    segment.customsOrder?.id?.let { checkAllUnitsAndInvoicesAreUsed(it) }
                }
                notificationService.confirmByAggregateTypeAndId(
                    CustomsOrder::class.java.simpleName,
                    segment.customsOrder?.id!!
                )
                applicationEventPublisher.publishEvent(
                    CustomsOrderSegmentTransmitEvent(
                        segment.id!!,
                        false
                    )
                )
            }
        }
    }

    @Transactional
    fun transmitAutomaticSegments(segmentList: List<CustomsOrderSegment?>) {
        if (CollectionUtils.isNotEmpty(segmentList)) {
            for (segment in segmentList) {
                if (BooleanUtils.isTrue(segment!!.automatic)) {

                    segment.customsOrder?.let {
                        if (!customsOrderHasComplianceError(it) && !customsOrderHasErrors(it.id!!)) {
                            applicationEventPublisher.publishEvent(
                                CustomsOrderSegmentTransmitEvent(
                                    segment.id!!,
                                    true
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    @Transactional
    fun reset(id: Long) {
        customsOrderSegmentService.get(id)?.let { this.reset(it) }
    }

    @Transactional
    fun reset(segment: CustomsOrderSegment) {
        if (segment.transmitted == true) {
            segment.transmitted = false
            customsOrderSegmentService.update(segment)
            applicationEventPublisher.publishEvent(
                CustomsOrderSegmentResetEvent(
                    segment.customsOrder?.id!!,
                    segment.type!!,
                    false
                )
            )
        }
    }

    private fun customsOrderHasErrors(customsOrderId: Long): Boolean =
        notificationService.existsError(CustomsOrder::class.java.simpleName, customsOrderId)

    private fun customsOrderHasComplianceError(customsOrder: CustomsOrder): Boolean =
        customsOrder.customsOrderStatus == OrderStatus.STATUS_COMPLIANCE_CHECK_FAILED

    private fun checkAllUnitsAndInvoicesAreUsed(customsOrderId: Long) {
        val units = customsOrderUnitService.findByCustomsOrderId(customsOrderId)
        val invoices = customsOrderInvoiceService.findByCustomsOrderId(customsOrderId)
        val positions = customsOrderPositionService.findByCustomsOrderId(customsOrderId)

        for (unit in units) {
            if (positions.none { StringUtils.equalsIgnoreCase(unit.unitNo, it.containerNoSad31) }) {
                throw ValidationException("validation.customsOrderSegment.transmitNotPossibleDueToUnusedUnit")
            }
        }
        for (invoice in invoices) {
            if (positions.none { StringUtils.equalsIgnoreCase(invoice.invoiceNo, it.invoiceNo) }) {
                throw ValidationException("validation.customsOrderSegment.transmitNotPossibleDueToUnusedInvoice")
            }
        }
    }
}
