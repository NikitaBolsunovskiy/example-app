package service

import de.rhenus.scs.customs.customer.model.Rule
import de.rhenus.scs.customs.customer.repository.RuleRepository
import de.rhenus.scs.customs.location.model.Location
import de.rhenus.scs.customs.order.exception.CancellationRuleFoundException
import de.rhenus.scs.customs.order.model.*
import de.rhenus.scs.customs.order.repository.CustomsOrderSegmentRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderSegmentStatusRepository
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

@Service
class CustomsOrderSegmentBuildService(
    private val ruleRepository: RuleRepository,
    private val customsOrderSegmentRepository: CustomsOrderSegmentRepository,
    private val customsOrderSegmentStatusRepository: CustomsOrderSegmentStatusRepository,
) {

    fun buildSegments(order: CustomsOrder): List<CustomsOrderSegment> {
        val rules: List<Rule> = ruleRepository.find(
            order.customer?.id,
            order.customerCustomerNo,
            order.deliveryTermSad20,
            order.countryOfExportSad15,
            order.countryOfDestinationSad17,
            order.countryOfOriginSad16,
            order.typeOfDeclaration
        )
        if (CollectionUtils.isNotEmpty(rules)) {
            val rule = rules[0]
            if (BooleanUtils.isTrue(rule.cancelOrders)) {
                throw CancellationRuleFoundException()
            }
            order.leadOffice = rule.leadOffice
//            val savedOrder = customsOrderRepository.save(order) This line broke documents before (to be refactored)
            return this.buildSegments(order, rule)
        }
        return emptyList()
    }

    fun buildSegments(order: CustomsOrder, rule: Rule?): List<CustomsOrderSegment> {
        val segmentList: MutableList<CustomsOrderSegment> = ArrayList()
        var sequentialNo = 1L
        if (!StringUtils.isBlank(rule!!.exportLocation?.cctIdent)) {
            segmentList.add(
                buildSegment(
                    SegmentType.EXPORT,
                    order,
                    rule.exportLocation,
                    sequentialNo,
                    rule.automaticTransmitForExport
                )
            )
            sequentialNo++
        }
        if (!StringUtils.isBlank(rule.transitLocation?.cctIdent)) {
            segmentList.add(
                buildSegment(
                    SegmentType.TRANSIT,
                    order,
                    rule.transitLocation,
                    sequentialNo,
                    rule.automaticTransmitForTransit
                )
            )
            sequentialNo++
        }
        if (!StringUtils.isBlank(rule.importLocation?.cctIdent)) {
            segmentList.add(
                buildSegment(
                    SegmentType.IMPORT,
                    order,
                    rule.importLocation,
                    sequentialNo,
                    rule.automaticTransmitForImport
                )
            )
        }
        return segmentList
    }

    private fun buildSegment(
        type: SegmentType, customsOrder: CustomsOrder, location: Location?, sequentialNo: Long, directStart: Boolean?
    ): CustomsOrderSegment {

        val segmentStatus = if (directStart == true) {
            SegmentStatus.WAITING_FOR_ARRIVAL
        } else {
            SegmentStatus.NOT_STARTED
        }

        var segment = CustomsOrderSegment(
            type = type,
            customsOrder = customsOrder,
            sequentialNo = sequentialNo.toInt(),
            location = location,
            automatic = BooleanUtils.isTrue(directStart),
            status = segmentStatus,
        )

        segment = customsOrderSegmentRepository.save(segment)
        val newStatus = CustomsOrderSegmentStatus(
            segment = segment,
            statusId = segment.status,
        )
        customsOrderSegmentStatusRepository.save(newStatus)

        return segment
    }
}
