package repository

import de.rhenus.scs.customs.customer.model.Customer
import de.rhenus.scs.customs.customer.model.Customer_
import de.rhenus.scs.customs.location.model.Location
import de.rhenus.scs.customs.location.model.Location_
import de.rhenus.scs.customs.order.converter.OrderSegmentStatusConverter
import de.rhenus.scs.customs.order.converter.OrderStatusConverter
import de.rhenus.scs.customs.order.model.*
import de.rhenus.scs.customs.security.permissions.DataRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.persistence.EntityManager
import javax.persistence.criteria.Predicate


@Repository
class CustomsOrderRepositoryCustomImpl(
    val entityManager: EntityManager,
    val customsOrderSegmentRepository: CustomsOrderSegmentRepository,
) : CustomsOrderRepositoryCustom {
    @Autowired
    @Lazy
    private lateinit var customsOrderRepository: CustomsOrderRepository

    @Autowired
    @Lazy
    private lateinit var customsOrderAddressRepository: CustomsOrderAddressRepository

    @Autowired
    @Lazy
    private lateinit var customsOrderUnitRepository: CustomsOrderUnitRepository

    @Autowired
    @Lazy
    private lateinit var customsOrderPositionRepository: CustomsOrderPositionRepository

    @Autowired
    @Lazy
    private lateinit var customsOrderInvoiceRepostiory: CustomsOrderInvoiceRepository

    @Autowired
    @Lazy
    private lateinit var customsOrderDocumentRepository: CustomsOrderDocumentRepository

    @Autowired
    @Lazy
    private lateinit var customsOrderStatusRepository: CustomsOrderStatusRepository

    override fun search(
        rulesForLead: List<DataRule>?,
        rulesForDeclarant: List<DataRule>?,
        offices: List<Long>?,
        leadAllowedOrderStatus: List<OrderStatus>?,
        allowedOrderStatus: List<OrderStatus>?,
        customsOrderNo: String?,
        consolidatedCustomsOrderNo: String?,
        orderStatus: List<Long>?,
        segmentStatus: List<Long>?,
        customsOrderDatetimeFrom: LocalDateTime?,
        customsOrderDatetimeTo: LocalDateTime?,
        countriesOfExport: List<String>?,
        countriesOfDestination: List<String>?,
        localCustomsAppId: String?,
        customer: String?,
        consignor: String?,
        consignee: String?,
        consignorPickupDateFrom: LocalDate?,
        consignorPickupDateTo: LocalDate?,
        consigneeDeliveryDateFrom: LocalDate?,
        consigneeDeliveryDateTo: LocalDate?,
        portOfLoading: String?,
        portOfDestination: String?,
        customerReference: String?,
        shipmentNo: String?,
        locationOfGoodsSad30: String?,
        transportInContainersSad19: Boolean?,
        unitNo: String?,
        invoiceNo: String?,
        pageable: Pageable
    ): Page<CustomsOrder> {

        val entity = CustomsOrder::class.java
        val cb = entityManager.criteriaBuilder
        val cq = cb.createQuery(entity)
        val customsOrderRoot = cq.from(entity)
        customsOrderRoot.alias("customsOrderRoot")
        val customerJoin = customsOrderRoot.join<CustomsOrder, Customer>(CustomsOrder_.CUSTOMER)
        val locationJoin = customsOrderRoot.join<CustomsOrder, Location>(CustomsOrder_.LEAD_OFFICE)

        val predicates = mutableListOf<Predicate>()

        val leadPredicates = mutableListOf<Predicate>()
        if (!rulesForLead.isNullOrEmpty()) {
            val orPredicates = mutableListOf<Predicate>()
            for (dataRule: DataRule in rulesForLead) {
                if (dataRule.locations == DataRule.WILDCARD && dataRule.customers == DataRule.WILDCARD) {
                    orPredicates.clear()
                    break
                }
                val andPredicates = mutableListOf<Predicate>()
                if (dataRule.locations != DataRule.WILDCARD) {
                    andPredicates.add(locationJoin.get<String?>(Location_.CCT_IDENT).`in`(dataRule.locations))
                }
                if (dataRule.customers != DataRule.WILDCARD) {
                    andPredicates.add(customerJoin.get<String?>(Customer_.CUSTOMER_ID_CCT).`in`(dataRule.customers))
                }
                orPredicates.add(cb.and(*andPredicates.toTypedArray()))
            }
            if (orPredicates.isNotEmpty()) {
                leadPredicates.add(
                    cb.and(
                        cb.or(*orPredicates.toTypedArray()),
                        customsOrderRoot.get<OrderStatus?>("customsOrderStatus").`in`(leadAllowedOrderStatus)
                    )
                )
            } else {
                leadPredicates.add(
                    customsOrderRoot.get<OrderStatus?>("customsOrderStatus").`in`(leadAllowedOrderStatus)
                )
            }
        }

        val declarantPredicates = mutableListOf<Predicate>()
        if (!rulesForDeclarant.isNullOrEmpty()) {
            val sqQuery = cq.subquery(CustomsOrderSegment::class.java)
            val sqRoot = sqQuery.from(CustomsOrderSegment::class.java)
            val sqLocationJoin = sqRoot.join<CustomsOrderSegment, Location>(CustomsOrderSegment_.LOCATION)
            val orPredicates = mutableListOf<Predicate>()
            for (dataRule: DataRule in rulesForDeclarant) {
                if (dataRule.locations == DataRule.WILDCARD
                    && dataRule.customers == DataRule.WILDCARD
                    && dataRule.segments == DataRule.WILDCARD
                ) {
                    orPredicates.clear()
                    break
                }
                val andPredicates = mutableListOf<Predicate>()
                if (dataRule.locations != DataRule.WILDCARD) {
                    andPredicates.add(sqLocationJoin.get<String?>(Location_.CCT_IDENT).`in`(dataRule.locations))
                }
                if (dataRule.customers != DataRule.WILDCARD) {
                    andPredicates.add(customerJoin.get<String?>(Customer_.CUSTOMER_ID_CCT).`in`(dataRule.customers))
                }
                if (dataRule.segments != DataRule.WILDCARD) {
                    andPredicates.add(sqRoot.get<SegmentType?>("type").`in`(dataRule.segments.map { SegmentType.fromValue(it) }))
                }
                orPredicates.add(cb.and(*andPredicates.toTypedArray()))
            }
            if (orPredicates.isNotEmpty()) {
                declarantPredicates.add(
                    cb.exists(
                        sqQuery.select(sqRoot)
                            .where(
                                cb.equal(sqRoot.get<CustomsOrder?>("customsOrder"), customsOrderRoot),
                                cb.notEqual(sqRoot.get<SegmentStatus?>("status"), SegmentStatus.NOT_STARTED),
                                customsOrderRoot.get<OrderStatus>("customsOrderStatus").`in`(allowedOrderStatus),
                                cb.or(*orPredicates.toTypedArray())
                            )
                    )
                )
            } else {
                declarantPredicates.add(
                    cb.exists(
                        sqQuery.select(sqRoot)
                            .where(
                                cb.equal(sqRoot.get<CustomsOrder?>("customsOrder"), customsOrderRoot),
                                cb.notEqual(sqRoot.get<SegmentStatus?>("status"), SegmentStatus.NOT_STARTED),
                                customsOrderRoot.get<OrderStatus>("customsOrderStatus").`in`(allowedOrderStatus)
                            )
                    )
                )
            }
        }

        if (leadPredicates.isNotEmpty() && declarantPredicates.isNotEmpty()) {
            predicates.add(
                cb.or(
                    cb.and(*leadPredicates.toTypedArray()),
                    cb.and(*declarantPredicates.toTypedArray()),
                )
            )
        } else if (leadPredicates.isNotEmpty()) {
            predicates.add(cb.and(*leadPredicates.toTypedArray()))
        } else if (declarantPredicates.isNotEmpty()) {
            predicates.add(cb.and(*declarantPredicates.toTypedArray()))
        }

        if (orderStatus?.isNotEmpty() == true) {
            predicates.add(customsOrderRoot.get<OrderStatus?>("customsOrderStatus").`in`(orderStatus.map {
                OrderStatusConverter.convertToEntityAttribute(it)
            }))
        }
        if (customsOrderNo?.isNotEmpty() == true) {
            predicates.add(cb.like(customsOrderRoot.get("customsOrderNo"), "%$customsOrderNo%"))
        }
        if (consolidatedCustomsOrderNo?.isNotEmpty() == true) {
            predicates.add(
                cb.like(
                    customsOrderRoot.get("consolidatedToCustomsOrderNo"),
                    "%$consolidatedCustomsOrderNo%"
                )
            )
        }
        if (customsOrderDatetimeFrom != null) {
            predicates.add(
                cb.greaterThanOrEqualTo(
                    customsOrderRoot.get("customsOrderDatetime"),
                    customsOrderDatetimeFrom.toInstant(ZoneOffset.UTC)
                )
            )
        }
        if (customsOrderDatetimeTo != null) {
            predicates.add(
                cb.lessThanOrEqualTo(
                    customsOrderRoot.get("customsOrderDatetime"),
                    customsOrderDatetimeTo.toInstant(ZoneOffset.UTC)
                )
            )
        }
        if (countriesOfExport?.isNotEmpty() == true) {
            predicates.add(customsOrderRoot.get<String?>("countryOfExportSad15").`in`(countriesOfExport))
        }
        if (countriesOfDestination?.isNotEmpty() == true) {
            predicates.add(customsOrderRoot.get<String?>("countryOfDestinationSad17").`in`(countriesOfDestination))
        }

        if (customer?.isNotEmpty() == true) {
            val sqQuery = cq.subquery(CustomsOrderAddress::class.java)
            val sqRoot = sqQuery.from(CustomsOrderAddress::class.java)
            predicates.add(
                cb.exists(
                    sqQuery.select(sqRoot)
                        .where(
                            cb.equal(sqRoot.get<CustomsOrder?>("customsOrder"), customsOrderRoot),
                            cb.equal(sqRoot.get<String?>("role"), "OC"),
                            cb.like(cb.upper(sqRoot.get("name1")), "%$customer%".uppercase())
                        )
                )
            )
        }
        if (consignor?.isNotEmpty() == true) {
            val sqQuery = cq.subquery(CustomsOrderAddress::class.java)
            val sqRoot = sqQuery.from(CustomsOrderAddress::class.java)
            predicates.add(
                cb.exists(
                    sqQuery.select(sqRoot)
                        .where(
                            cb.equal(sqRoot.get<CustomsOrder?>("customsOrder"), customsOrderRoot),
                            cb.equal(sqRoot.get<String?>("role"), "CZ"),
                            cb.like(cb.upper(sqRoot.get("name1")), "%$consignor%".uppercase())
                        )
                )
            )
        }
        if (consignee?.isNotEmpty() == true) {
            val sqQuery = cq.subquery(CustomsOrderAddress::class.java)
            val sqRoot = sqQuery.from(CustomsOrderAddress::class.java)
            predicates.add(
                cb.exists(
                    sqQuery.select(sqRoot)
                        .where(
                            cb.equal(sqRoot.get<CustomsOrder?>("customsOrder"), customsOrderRoot),
                            cb.equal(sqRoot.get<String?>("role"), "CN"),
                            cb.like(cb.upper(sqRoot.get("name1")), "%$consignee%".uppercase())
                        )
                )
            )
        }

        if (consignorPickupDateFrom != null) {
            predicates.add(
                cb.greaterThanOrEqualTo(
                    customsOrderRoot.get("consignorPickupDate"),
                    consignorPickupDateFrom
                )
            )
        }
        if (consignorPickupDateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(customsOrderRoot.get("consignorPickupDate"), consignorPickupDateTo))
        }
        if (consigneeDeliveryDateFrom != null) {
            predicates.add(
                cb.greaterThanOrEqualTo(
                    customsOrderRoot.get("consigneeDeliveryDate"),
                    consigneeDeliveryDateFrom
                )
            )
        }
        if (consigneeDeliveryDateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(customsOrderRoot.get("consigneeDeliveryDate"), consigneeDeliveryDateTo))
        }
        if (portOfLoading?.isNotEmpty() == true) {
            predicates.add(cb.equal(customsOrderRoot.get<String?>("portOfLoading"), portOfLoading))
        }
        if (portOfDestination?.isNotEmpty() == true) {
            predicates.add(cb.equal(customsOrderRoot.get<String?>("portOfDestination"), portOfDestination))
        }
        if (customerReference?.isNotEmpty() == true) {
            predicates.add(
                cb.like(
                    cb.upper(customsOrderRoot.get("customerReference")),
                    "%$customerReference%".uppercase()
                )
            )
        }
        if (shipmentNo?.isNotEmpty() == true) {
            predicates.add(
                cb.like(
                    cb.upper(customsOrderRoot.get("shipmentNo")),
                    "%$shipmentNo%".uppercase()
                )
            )
        }
        if (locationOfGoodsSad30?.isNotEmpty() == true) {
            predicates.add(
                cb.or(
                    cb.like(
                        cb.upper(customsOrderRoot.get("locationOfGoodsSad30im")),
                        "%$locationOfGoodsSad30%".uppercase()
                    ),
                    cb.like(
                        cb.upper(customsOrderRoot.get("locationOfGoodsSad30t")),
                        "%$locationOfGoodsSad30%".uppercase()
                    ),
                    cb.like(
                        cb.upper(customsOrderRoot.get("locationOfGoodsSad30ex")),
                        "%$locationOfGoodsSad30%".uppercase()
                    ),
                )
            )
        }
        if (transportInContainersSad19 == true) {
            predicates.add(cb.equal(customsOrderRoot.get<Boolean?>("transportInContainersSad19"), true))
        }
        if (unitNo?.isNotEmpty() == true) {
            val sqQuery = cq.subquery(CustomsOrderUnit::class.java)
            val sqRoot = sqQuery.from(CustomsOrderUnit::class.java)
            predicates.add(
                cb.exists(
                    sqQuery.select(sqRoot)
                        .where(
                            cb.equal(sqRoot.get<CustomsOrder?>("customsOrder"), customsOrderRoot),
                            cb.equal(sqRoot.get<String?>("unitNo"), unitNo)
                        )
                )
            )
        }
        if (invoiceNo?.isNotEmpty() == true) {
            val sqQuery = cq.subquery(CustomsOrderInvoice::class.java)
            val sqRoot = sqQuery.from(CustomsOrderInvoice::class.java)
            predicates.add(
                cb.exists(
                    sqQuery.select(sqRoot)
                        .where(
                            cb.equal(sqRoot.get<CustomsOrder?>("customsOrder"), customsOrderRoot),
                            cb.equal(sqRoot.get<String?>("invoiceNo"), invoiceNo)
                        )
                )
            )
        }
        if (offices?.isNotEmpty() == true || segmentStatus?.isNotEmpty() == true || localCustomsAppId?.isNotEmpty() == true) {
            val sqQuery = cq.subquery(CustomsOrderSegment::class.java)
            val sqRoot = sqQuery.from(CustomsOrderSegment::class.java)
            val innerPredicates = mutableListOf<Predicate>()

            innerPredicates.add(cb.equal(sqRoot.get<CustomsOrder?>(CustomsOrderSegment_.CUSTOMS_ORDER), customsOrderRoot))
            if (offices?.isNotEmpty() == true) innerPredicates.add(sqRoot.get<Long?>(CustomsOrderSegment_.LOCATION).`in`(offices))
            if (segmentStatus?.isNotEmpty() == true) innerPredicates.add(
                sqRoot.get<SegmentStatus?>("status").`in`(
                    segmentStatus.map(OrderSegmentStatusConverter::convertToEntityAttribute)
                )
            )
            if (localCustomsAppId?.isNotEmpty() == true) innerPredicates.add(
                sqRoot.get<String?>("localCustomsAppId").`in`(localCustomsAppId)
            )

            predicates.add(cb.exists(sqQuery.select(sqRoot).where(*innerPredicates.toTypedArray())))
        }

        cq.select(customsOrderRoot).where(*predicates.toTypedArray())
        cq.orderBy(cb.desc(customsOrderRoot.get<Instant>("customsOrderDatetime")))
        val query = entityManager.createQuery(cq)
        query.firstResult = pageable.pageSize * pageable.pageNumber
        query.maxResults = pageable.pageSize
        val results = query.resultList
        return PageableExecutionUtils.getPage(results, pageable) {
            val countQuery = cb.createQuery(Long::class.java)
            val entityQ = countQuery.from(cq.resultType)
            entityQ.join<CustomsOrder, Customer>(CustomsOrder_.CUSTOMER)
            entityQ.join<CustomsOrder, Location>(CustomsOrder_.LEAD_OFFICE)
            entityQ.alias("customsOrderRoot")
            countQuery.select(cb.count(entityQ)).where(*predicates.toTypedArray())
            entityManager.createQuery(countQuery).singleResult
        }

    }

    override fun searchForRegistrations(
        customerIdCct: String?,
        customsOrderNo: String?,
        customsOrderDatetimeFrom: Instant?,
        customsOrderDatetimeTo: Instant?,
        deliveryTerm: String?,
        countryOfExport: String?,
        countryOfDestination: String?,
        customerReference: String?,
        text: String?,
        pageable: Pageable
    ): Page<CustomsOrder> {

        val entity = CustomsOrder::class.java
        val cb = entityManager.criteriaBuilder
        val cq = cb.createQuery(entity)
        val root = cq.from(entity)
        val predicates = mutableListOf<Predicate>()
        val customerJoin = root.join<CustomsOrder, Customer>(CustomsOrder_.CUSTOMER)

        predicates.add(cb.equal(root.get<OrderStatus?>("customsOrderStatus"), OrderStatus.STATUS_REGISTERED))
        predicates.add(cb.equal(customerJoin.get<String?>(Customer_.CUSTOMER_ID_CCT), customerIdCct))

        if (!customsOrderNo.isNullOrBlank()) {
            predicates.add(cb.like(cb.upper(root.get("customsOrderNo")), customsOrderNo.uppercase()))
        }

        if (customsOrderDatetimeFrom != null) {
            predicates.add(
                cb.greaterThanOrEqualTo(
                    root.get("customsOrderDatetime"),
                    customsOrderDatetimeFrom
                )
            )
        }
        if (customsOrderDatetimeTo != null) {
            predicates.add(
                cb.lessThanOrEqualTo(
                    root.get("customsOrderDatetime"),
                    customsOrderDatetimeTo
                )
            )
        }
        if (!countryOfExport.isNullOrBlank()) {
            predicates.add(cb.equal(root.get<String?>("countryOfExportSad15"), countryOfExport))
        }
        if (!countryOfDestination.isNullOrBlank()) {
            predicates.add(cb.equal(root.get<String?>("countryOfDestinationSad17"), countryOfDestination))
        }
        if (!deliveryTerm.isNullOrBlank()) {
            predicates.add(cb.like(cb.upper(root.get("deliveryTermSad20")), deliveryTerm.uppercase()))
        }
        if (!customerReference.isNullOrBlank()) {
            predicates.add(cb.like(cb.upper(root.get("customerReference")), customerReference.uppercase()))
        }
        if (!text.isNullOrBlank()) {
            predicates.add(
                cb.or(
                    cb.like(cb.upper(root.get("customsOrderNo")), text.uppercase()),
                    cb.like(cb.upper(root.get("customerReference")), text.uppercase()),
                    cb.like(cb.upper(root.get("deliveryTermSad20")), text.uppercase()),
                )
            )
        }

        cq.select(root).where(*predicates.toTypedArray())
        cq.orderBy(cb.desc(root.get<Instant>("customsOrderDatetime")))
        val query = entityManager.createQuery(cq)
        query.firstResult = pageable.pageSize * pageable.pageNumber
        query.maxResults = pageable.pageSize
        val results = query.resultList

        return PageableExecutionUtils.getPage(results, pageable) {
            val countQuery = cb.createQuery(Long::class.java)
            countQuery.select(cb.count(countQuery.from(entity).join<CustomsOrder, Customer>(CustomsOrder_.CUSTOMER)))
                .where(*predicates.toTypedArray())
            entityManager.createQuery(countQuery).singleResult
        }
    }

    //Neccessary, as HQL does not support multiple JOIN FETCH operations (MultipleBagFetchException)
    override fun findByCustomsOrderNoWithSubEntities(customsOrderNo: String): CustomsOrder? =
        customsOrderRepository.findByCustomsOrderNo(customsOrderNo)?.also {
            it.customsOrderSegments = customsOrderSegmentRepository.findByCustomsOrderIdWithSubEntities(it.id).toMutableList()
            it.customsOrderAddresses = customsOrderAddressRepository.findByCustomsOrderId(it.id).toMutableList()
            it.customsOrderUnits = customsOrderUnitRepository.findByCustomsOrderId(it.id).toMutableList()
            it.customsOrderPositions = customsOrderPositionRepository.findByCustomsOrderWithAdditionalInformation(it).toMutableList()
            it.customsOrderInvoices = customsOrderInvoiceRepostiory.findByCustomsOrderWithAddresses(it).toMutableList()
            it.customsOrderDocuments = customsOrderDocumentRepository.findByCustomsOrderId(it.id).toMutableList()
            it.customsOrderStatuses = customsOrderStatusRepository.findByCustomsOrderId(it.id).toMutableList()
        }
}
