package service

import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderSearchData
import de.rhenus.scs.customs.order.model.OrderStatus
import de.rhenus.scs.customs.order.repository.CustomsOrderRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderSegmentRepository
import de.rhenus.scs.customs.security.permissions.DataRule
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.BooleanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
@Validated
class CustomsOrderSearchService(
    private val customsOrderRepository: CustomsOrderRepository,
    private val customsOrderSegmentRepository: CustomsOrderSegmentRepository,
) {
    fun search(
        search: CustomsOrderSearchData, rulesForLead: List<DataRule>, rulesForDeclarant: List<DataRule>,
        pageRequest: Pageable
    ): Page<CustomsOrder> {
        val result: Page<CustomsOrder> = if (BooleanUtils.isTrue(search.quickSearch)) {
            doQuickSearch(search, rulesForLead, rulesForDeclarant, pageRequest)
        } else {
            doFullSearch(search, rulesForLead, rulesForDeclarant, pageRequest)
        }
        // CCTD-730. If a user has only 'lead' rule, then we do not need to additionally restrict segments...
        customsOrderSegmentRepository.loadSegments(result.content,
            if (rulesForDeclarant.isNotEmpty()) { (rulesForDeclarant + rulesForLead).toMutableList() } else {
                mutableListOf()
            }
        )
        return result
    }

    private fun doQuickSearch(
        search: CustomsOrderSearchData, rulesForLead: List<DataRule>, rulesForDeclarant: List<DataRule>,
        pageRequest: Pageable
    ): Page<CustomsOrder> {
        return customsOrderRepository.search(
            rulesForLead,
            rulesForDeclarant,
            search.offices,
            getAllowedOrderStatusForSearch(true, true, true, true, true),
            getAllowedOrderStatusForSearch(false, true, true, true, true),
            search.customsOrderNo,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null, null,
            pageRequest
        )
    }

    private fun doFullSearch(
        search: CustomsOrderSearchData, rulesForLead: List<DataRule>, rulesForDeclarant: List<DataRule>,
        pageRequest: Pageable
    ): Page<CustomsOrder> {
        return customsOrderRepository.search(
            rulesForLead,
            rulesForDeclarant,
            search.offices,
            getAllowedOrderStatusForSearch(
                true, isShowCancelled(search), isShowFinished(search),
                isShowRejected(search), isShowConsolidated(search)
            ),
            getAllowedOrderStatusForSearch(
                false, isShowCancelled(search), isShowFinished(search),
                isShowRejected(search), isShowConsolidated(search)
            ),
            search.customsOrderNo,
            search.consolidatedCustomsOrderNo,
            search.orderStatus,
            search.segmentStatus,
            convertToLocalDateTime(search.customsOrderDateTimeFrom),
            convertToLocalDateTime(search.customsOrderDateTimeTo),
            search.countriesOfExport,
            search.countriesOfDestination,
            search.localCustomsAppId,
            search.customer,
            search.consignor,
            search.consignee,
            convertToLocalDate(search.consignorPickupDateFrom),
            convertToLocalDate(search.consignorPickupDateTo),
            convertToLocalDate(search.consigneeDeliveryDateFrom),
            convertToLocalDate(search.consigneeDeliveryDateTo),
            search.portOfLoading,
            search.portOfDestination,
            search.customerReference,
            search.shipmentNumber,
            search.locationOfGoods,
            search.transportInContainers,
            search.unitNo,
            search.invoiceNo,
            pageRequest
        )
    }

    private fun isShowFinished(search: CustomsOrderSearchData): Boolean {
        var showFinished = BooleanUtils.isTrue(search.showFinished)
        if (CollectionUtils.isNotEmpty(search.orderStatus)) {
            if (CollectionUtils.containsAny(search.orderStatus, listOf(OrderStatus.STATUS_FINISHED))) {
                showFinished = true
            }
            if (CollectionUtils.containsAny(search.orderStatus, listOf(OrderStatus.STATUS_COORDINATION_FINISHED))) {
                showFinished = true
            }
        }
        return showFinished
    }

    private fun isShowCancelled(search: CustomsOrderSearchData): Boolean {
        var showCancelled = BooleanUtils.isTrue(search.showCancelled)
        if (CollectionUtils.isNotEmpty(search.orderStatus)) {
            if (CollectionUtils.containsAny(search.orderStatus, listOf(OrderStatus.STATUS_CANCELLED))) {
                showCancelled = true
            }
        }
        return showCancelled
    }

    private fun isShowRejected(search: CustomsOrderSearchData): Boolean {
        var showCancelled = BooleanUtils.isTrue(search.showRejected)
        if (CollectionUtils.isNotEmpty(search.orderStatus)) {
            if (CollectionUtils.containsAny(search.orderStatus, listOf(OrderStatus.STATUS_REJECTED))) {
                showCancelled = true
            }
        }
        return showCancelled
    }

    private fun isShowConsolidated(search: CustomsOrderSearchData): Boolean {
        var showConsolidated = BooleanUtils.isTrue(search.showConsolidated)
        if (CollectionUtils.isNotEmpty(search.orderStatus)) {
            if (CollectionUtils.containsAny(search.orderStatus, listOf(OrderStatus.STATUS_CONSOLIDATED))) {
                showConsolidated = true
            }
        }
        return showConsolidated
    }

    private fun getAllowedOrderStatusForSearch(
        isLead: Boolean, showCancelled: Boolean, showFinished: Boolean,
        showRejected: Boolean, showConsolidated: Boolean
    ): List<OrderStatus> {
        val statusList: MutableList<OrderStatus> = ArrayList()
        statusList.add(OrderStatus.STATUS_PREPARED)
        statusList.add(OrderStatus.STATUS_IN_PROGRESS)
        statusList.add(OrderStatus.STATUS_CANCELLED_BY_CUSTOMS)
        statusList.add(OrderStatus.STATUS_COMPLIANCE_CHECK_FAILED)
        if (BooleanUtils.isTrue(isLead)) {
            statusList.add(OrderStatus.STATUS_CREATED)
            statusList.add(OrderStatus.STATUS_NEW)
            statusList.add(OrderStatus.STATUS_INVALID)
        }
        if (BooleanUtils.isTrue(showCancelled)) {
            statusList.add(OrderStatus.STATUS_CANCELLED)
        }
        if (BooleanUtils.isTrue(showRejected)) {
            statusList.add(OrderStatus.STATUS_REJECTED)
        }
        if (BooleanUtils.isTrue(showConsolidated)) {
            statusList.add(OrderStatus.STATUS_CONSOLIDATED)
        }
        if (BooleanUtils.isTrue(showFinished)) {
            statusList.add(OrderStatus.STATUS_FINISHED)
            statusList.add(OrderStatus.STATUS_COORDINATION_FINISHED)
        }
        return statusList
    }

    private fun convertToLocalDate(date: Instant?): LocalDate? {
        return date?.atZone(ZoneId.systemDefault())?.toLocalDate()
    }

    private fun convertToLocalDateTime(date: Instant?): LocalDateTime? {
        return date?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
    }
}
