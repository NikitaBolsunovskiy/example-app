package specification

import de.rhenus.scs.customs.order.model.*
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.data.jpa.domain.Specification
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.criteria.*

//TODO unused but possibly usable in future?
object CustomsOrderSearchSpecification {
    private fun isStatus(
        search: CustomsOrderSearchData, statusQueried: Boolean?, status: OrderStatus
    ): Boolean = BooleanUtils.isTrue(statusQueried) || search.orderStatus.any { e: Long -> e == status.value }

    private fun isShowFinished(search: CustomsOrderSearchData): Boolean =
        isStatus(search, search.showFinished, OrderStatus.STATUS_FINISHED) || isStatus(
            search, search.showFinished, OrderStatus.STATUS_COORDINATION_FINISHED
        )

    private fun isShowCancelled(search: CustomsOrderSearchData): Boolean =
        isStatus(search, search.showCancelled, OrderStatus.STATUS_CANCELLED)

    private fun isShowRejected(search: CustomsOrderSearchData): Boolean =
        isStatus(search, search.showRejected, OrderStatus.STATUS_REJECTED)

    private fun isShowConsolidated(search: CustomsOrderSearchData): Boolean =
        isStatus(search, search.showConsolidated, OrderStatus.STATUS_CONSOLIDATED)

    private fun getAllowedOrderStatusForSearch(
        isLead: Boolean, search: CustomsOrderSearchData
    ): List<Long?> {
        return getAllowedOrderStatusForSearch(
            isLead,
            isShowCancelled(search),
            isShowFinished(search),
            isShowRejected(search),
            isShowConsolidated(search)
        )
    }

    private fun getAllowedOrderStatusForSearch(
        isLead: Boolean,
        showCancelled: Boolean,
        showFinished: Boolean,
        showRejected: Boolean,
        showConsolidated: Boolean
    ): List<Long?> {
        val statusList: MutableList<Long?> = ArrayList()
        statusList.add(OrderStatus.STATUS_PREPARED.value)
        statusList.add(OrderStatus.STATUS_IN_PROGRESS.value)
        statusList.add(OrderStatus.STATUS_CANCELLED_BY_CUSTOMS.value)
        statusList.add(OrderStatus.STATUS_COMPLIANCE_CHECK_FAILED.value)
        if (isLead) {
            statusList.add(OrderStatus.STATUS_CREATED.value)
            statusList.add(OrderStatus.STATUS_NEW.value)
            statusList.add(OrderStatus.STATUS_INVALID.value)
        }
        if (showCancelled) {
            statusList.add(OrderStatus.STATUS_CANCELLED.value)
        }
        if (showRejected) {
            statusList.add(OrderStatus.STATUS_REJECTED.value)
        }
        if (showConsolidated) {
            statusList.add(OrderStatus.STATUS_CONSOLIDATED.value)
        }
        if (showFinished) {
            statusList.add(OrderStatus.STATUS_FINISHED.value)
            statusList.add(OrderStatus.STATUS_COORDINATION_FINISHED.value)
        }
        return statusList
    }

    private fun convertToLocalDate(date: Instant?): LocalDate? =
        date?.atZone(ZoneId.systemDefault())?.toLocalDate()

    private fun convertToLocalDateTime(date: Instant?): LocalDateTime? =
        date?.atZone(ZoneId.systemDefault())?.toLocalDateTime()

    @JvmStatic
    fun build(
        leadOffices: List<String?>, allowedOffices: List<String?>, search: CustomsOrderSearchData
    ): Specification<CustomsOrder> {
        return Specification { root: Root<CustomsOrder>, criteriaQuery: CriteriaQuery<*>, cb: CriteriaBuilder ->
            val predicates = ArrayList<Predicate>()
            predicates.add(
                buildOrderStatusPredicate(
                    leadOffices, allowedOffices, search, root, criteriaQuery, cb
                )
            )
            if (CollectionUtils.isNotEmpty(search.orderStatus)) {
                predicates.add(root.get(CustomsOrder_.customsOrderStatus).`in`(search.orderStatus))
            }
            if (StringUtils.isNotEmpty(search.customsOrderNo)) {
                predicates.add(buildLikeIgnoreCase(root.get("customsOrderNo"), search.customsOrderNo, cb))
            }
            if (StringUtils.isNotEmpty(search.consolidatedCustomsOrderNo)) {
                predicates.add(
                    buildLikeIgnoreCase(
                        root.get("consolidatedCustomsOrderNo"), search.consolidatedCustomsOrderNo, cb
                    )
                )
            }
            if (StringUtils.isNotEmpty(search.customerReference)) {
                predicates.add(buildLikeIgnoreCase(root.get("customerReference"), search.customerReference, cb))
            }
            if (StringUtils.isNotEmpty(search.shipmentNumber)) {
                predicates.add(buildLikeIgnoreCase(root.get("shipmentNo"), search.shipmentNumber, cb))
            }
            if (StringUtils.isNotEmpty(search.locationOfGoods)) {
                predicates.add(
                    cb.or(
                        buildLikeIgnoreCase(
                            root.get("locationOfGoodsSad30im"), search.locationOfGoods, cb
                        ), buildLikeIgnoreCase(
                            root.get("locationOfGoodsSad30t"), search.locationOfGoods, cb
                        ), buildLikeIgnoreCase(
                            root.get("locationOfGoodsSad30ex"), search.locationOfGoods, cb
                        )
                    )
                )
            }
            if (search.customsOrderDateTimeFrom != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(
                        root.get("customsOrderDatetime"), convertToLocalDateTime(search.customsOrderDateTimeFrom)
                    )
                )
            }
            if (search.customsOrderDateTimeTo != null) {
                predicates.add(
                    cb.lessThanOrEqualTo(
                        root.get("customsOrderDatetime"), convertToLocalDateTime(search.customsOrderDateTimeTo)
                    )
                )
            }
            if (search.consignorPickupDateFrom != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(
                        root.get("consignorPickupDate"), convertToLocalDate(search.consignorPickupDateFrom)
                    )
                )
            }
            if (search.consignorPickupDateTo != null) {
                predicates.add(
                    cb.lessThanOrEqualTo(
                        root.get("consignorPickupDate"), convertToLocalDate(search.consignorPickupDateTo)
                    )
                )
            }
            if (search.consigneeDeliveryDateFrom != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(
                        root.get("consigneeDeliveryDate"), convertToLocalDate(search.consigneeDeliveryDateFrom)
                    )
                )
            }
            if (search.consigneeDeliveryDateTo != null) {
                predicates.add(
                    cb.lessThanOrEqualTo(
                        root.get("consigneeDeliveryDate"), convertToLocalDate(search.consignorPickupDateTo)
                    )
                )
            }
            if (CollectionUtils.isNotEmpty(search.countriesOfExport)) {
                predicates.add(root.get<Any>("countryOfExportSad15Id").`in`(search.countriesOfExport))
            }
            if (CollectionUtils.isNotEmpty(search.countriesOfDestination)) {
                predicates.add(root.get<Any>("countryOfDestinationSad17Id").`in`(search.countriesOfDestination))
            }
            if (BooleanUtils.isTrue(search.transportInContainers)) {
                predicates.add(cb.equal(root.get<Any>("transportInContainersSad19"), 1))
            }
            if (StringUtils.isNotEmpty(search.portOfLoading)) {
                predicates.add(cb.equal(root.get<Any>("portOfLoading"), search.portOfLoading))
            }
            if (StringUtils.isNotEmpty(search.portOfDestination)) {
                predicates.add(cb.equal(root.get<Any>("portOfDestination"), search.portOfDestination))
            }
            if (StringUtils.isNotEmpty(search.customer)) {
                val subquery = criteriaQuery.subquery(
                    CustomsOrderAddress::class.java
                )
                val address = subquery.from(
                    CustomsOrderAddress::class.java
                )
                predicates.add(
                    cb.exists(
                        subquery.select(address.get("id")).where(
                            cb.and(
                                cb.equal(
                                    address.get<Any>("customsOrder").get<Any>("id"), root.get<Any>("id")
                                ), cb.equal(address.get<Any>("role"), "OC"), cb.like(
                                    cb.upper(address.get("name1")), "%" + search.customer + "%"
                                )
                            )
                        )
                    )
                )
            }
            if (StringUtils.isNotEmpty(search.consignor)) {
                val subquery = criteriaQuery.subquery(
                    CustomsOrderAddress::class.java
                )
                val address = subquery.from(
                    CustomsOrderAddress::class.java
                )
                predicates.add(
                    cb.exists(
                        subquery.select(address.get("id")).where(
                            cb.and(
                                cb.equal(
                                    address.get<Any>("customsOrder").get<Any>("id"), root.get<Any>("id")
                                ), cb.equal(address.get<Any>("role"), "CZ"), cb.like(
                                    cb.upper(address.get("name1")), "%" + search.consignor + "%"
                                )
                            )
                        )
                    )
                )
            }
            if (StringUtils.isNotEmpty(search.consignee)) {
                val subquery = criteriaQuery.subquery(
                    CustomsOrderAddress::class.java
                )
                val address = subquery.from(
                    CustomsOrderAddress::class.java
                )
                predicates.add(
                    cb.exists(
                        subquery.select(address.get("id")).where(
                            cb.and(
                                cb.equal(
                                    address.get<Any>("customsOrder").get<Any>("id"), root.get<Any>("id")
                                ), cb.equal(address.get<Any>("role"), "CN"), cb.like(
                                    cb.upper(address.get("name1")), "%" + search.consignee + "%"
                                )
                            )
                        )
                    )
                )
            }
            if (StringUtils.isNotEmpty(search.unitNo)) {
                val subquery = criteriaQuery.subquery(
                    CustomsOrderUnit::class.java
                )
                val unit = subquery.from(
                    CustomsOrderUnit::class.java
                )
                predicates.add(
                    cb.exists(
                        subquery.select(unit.get("id")).where(
                            cb.and(
                                cb.equal(unit.get<Any>("customsOrder").get<Any>("id"), root.get<Any>("id")),
                                cb.equal(unit.get<Any>("unitNo"), search.unitNo)
                            )
                        )
                    )
                )
            }
            if (StringUtils.isNotEmpty(search.invoiceNo)) {
                val subquery = criteriaQuery.subquery(
                    CustomsOrderInvoice::class.java
                )
                val invoice = subquery.from(
                    CustomsOrderInvoice::class.java
                )
                predicates.add(
                    cb.exists(
                        subquery.select(invoice.get("id")).where(
                            cb.and(
                                cb.equal(
                                    invoice.get<Any>("customsOrder").get<Any>("id"), root.get<Any>("id")
                                ), cb.equal(
                                    invoice.get<Any>("invoiceNo"), search.invoiceNo
                                )
                            )
                        )
                    )
                )
            }
            if (CollectionUtils.isNotEmpty(search.offices) || CollectionUtils.isNotEmpty(search.segmentStatus) || StringUtils.isNotEmpty(
                    search.localCustomsAppId
                )
            ) {
                val subquery = criteriaQuery.subquery(
                    CustomsOrderSegment::class.java
                )
                val segment = subquery.from(
                    CustomsOrderSegment::class.java
                )
                val subqueryPredicates = ArrayList<Predicate>()
                subqueryPredicates.add(cb.equal(segment.get<Any>("customsOrder").get<Any>("id"), root.get<Any>("id")))
                if (CollectionUtils.isNotEmpty(search.offices)) {
                    subqueryPredicates.add(segment.get<Any>("locationIdent").`in`(search.offices))
                }
                if (CollectionUtils.isNotEmpty(search.segmentStatus)) {
                    subqueryPredicates.add(segment.get<Any>("statusId").`in`(search.segmentStatus.map { SegmentStatus.fromLong(it) }))
                }
                if (StringUtils.isNotEmpty(search.localCustomsAppId)) {
                    subqueryPredicates.add(
                        cb.like(
                            cb.upper(segment.get("localCustomsAppId")), "%" + search.localCustomsAppId + "%"
                        )
                    )
                }
                predicates.add(
                    cb.exists(
                        subquery.select(segment.get("id")).where(*subqueryPredicates.toTypedArray())
                    )
                )
            }
            cb.and(*predicates.toTypedArray())
        }
    }

    private fun buildOrderStatusPredicate(
        leadOffices: List<String?>,
        allowedOffices: List<String?>,
        search: CustomsOrderSearchData,
        root: Root<CustomsOrder>,
        criteriaQuery: CriteriaQuery<*>,
        cb: CriteriaBuilder
    ): Predicate {

        /*
         where += " and ( ";
        where += " ( o.customs_order_status_id in (:controlTowerAllowedOrderStatus) ";
        params.addValue("controlTowerAllowedOrderStatus", controlTowerAllowedOrderStatus);
        if (CollectionUtils.isNotEmpty(controlTowerOffices)) {
            where += " and o.control_tower_ident in (:controlTowerOffices)";
            params.addValue("controlTowerOffices", controlTowerOffices);
        }
        where += " ) or ( ";
         */
        val customsOrderStatus = ArrayList<Predicate>()
        val checkAllowedStatuses =
            root.get<Any>("customsOrderStatus").`in`(getAllowedOrderStatusForSearch(true, search))
        customsOrderStatus.add(checkAllowedStatuses)
        val firstPart = cb.and(*customsOrderStatus.toTypedArray())
        val subquery = criteriaQuery.subquery(
            CustomsOrderSegment::class.java
        )
        val segment = subquery.from(
            CustomsOrderSegment::class.java
        )
        val subSelectPredicates = ArrayList<Predicate>()
        subSelectPredicates.add(cb.equal(segment.get<Any>("customsOrder").get<Any>("id"), root.get<Any>("id")))
        subSelectPredicates.add(cb.notEqual(segment.get<Any>("statusId"), OrderStatus.STATUS_REGISTERED.value))
        if (CollectionUtils.isNotEmpty(allowedOffices)) {
            subSelectPredicates.add(segment.get<Any>("locationIdent").`in`(allowedOffices))
        }
        val secondPart = cb.and(
            cb.exists(subquery.select(segment).where(cb.and(*subSelectPredicates.toTypedArray()))),
            root.get<Any>("customsOrderStatus").`in`(getAllowedOrderStatusForSearch(false, search))
        )

        //// SNIP
        return cb.or(firstPart, secondPart)
    }

    private fun buildLikeIgnoreCase(path: Path<String>, value: String?, criteriaBuilder: CriteriaBuilder): Predicate =
        criteriaBuilder.like(criteriaBuilder.upper(path), "%" + value!!.uppercase(Locale.getDefault()) + "%")
}
