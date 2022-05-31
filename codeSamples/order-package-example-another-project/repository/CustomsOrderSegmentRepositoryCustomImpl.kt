package repository

import de.rhenus.scs.customs.customer.model.Customer
import de.rhenus.scs.customs.location.model.Location
import de.rhenus.scs.customs.order.model.*
import de.rhenus.scs.customs.security.permissions.DataRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.context.annotation.Lazy

@Repository
class CustomsOrderSegmentRepositoryCustomImpl(
    val entityManager: EntityManager,
): CustomsOrderSegmentRepositoryCustom {

    @Autowired
    @Lazy
    private lateinit var customsOrderSegmentRepository: CustomsOrderSegmentRepository

    @Autowired
    @Lazy
    private lateinit var customsOrderSegmentStatusRepository: CustomsOrderSegmentStatusRepository

    @Autowired
    @Lazy
    private lateinit var customsOrderSegmentReferenceRepository: CustomsOrderSegmentReferenceRepository

    override fun restrictSegments(
        permissions: List<DataRule>,
        cb: CriteriaBuilder,
        cq: CriteriaQuery<CustomsOrderSegment>,
        cqRoot: Root<CustomsOrderSegment>
    ): MutableList<Predicate> {

        val locationJoin = cqRoot.join<CustomsOrderSegment, Location>(CustomsOrderSegment_.LOCATION)

        val emptyPermissions = permissions.isEmpty()
        val hasWildcardRule = permissions.any { it.customers == DataRule.WILDCARD && it.locations == DataRule.WILDCARD && it.segments == DataRule.WILDCARD }
        val relevantPermissions = permissions.filter { it.locations != DataRule.WILDCARD || it.segments != DataRule.WILDCARD }

        if (emptyPermissions || hasWildcardRule || relevantPermissions.isEmpty()) {
            return mutableListOf()
        }

        val predicates = mutableListOf<Predicate>()

        val orPredicates = mutableListOf<Predicate>()
        for (rule in permissions) {
            val andPredicates = mutableListOf<Predicate>()
            if (rule.locations != DataRule.WILDCARD) {
                andPredicates.add(locationJoin.get<String?>("cctIdent").`in`(rule.locations))
            }
            if (rule.segments != DataRule.WILDCARD) {
                andPredicates.add(cqRoot.get<SegmentType?>("type").`in`(rule.segments.map { SegmentType.fromValue(it) }))
            }
            if (rule.customers != DataRule.WILDCARD) {
                val sqQuery = cq.subquery(CustomsOrder::class.java)
                val sqRoot = sqQuery.from(CustomsOrder::class.java)
                val customerJoin = sqRoot.join<CustomsOrder, Customer>(CustomsOrder_.CUSTOMER)
                andPredicates.add(
                    cb.exists(sqQuery.select(sqRoot)
                        .where(
                            cb.equal(sqRoot, cqRoot.get<CustomsOrder?>("customsOrder")),
                            customerJoin.get<String?>("customerIdCct").`in`(rule.customers))
                        ))
            }
            orPredicates.add(cb.and(*andPredicates.toTypedArray()))
        }
        predicates.add(cb.or(*orPredicates.toTypedArray()))
        return predicates
    }

    override fun loadSegments(customsOrders: MutableList<CustomsOrder>, rules: MutableList<DataRule>) {
        val cb = entityManager.criteriaBuilder
        val cq = cb.createQuery(CustomsOrderSegment::class.java)
        val cqRoot = cq.from(CustomsOrderSegment::class.java)

        val restrictions = restrictSegments(rules, cb, cq, cqRoot)
        restrictions.add(cqRoot.get<CustomsOrder>("customsOrder").`in`(customsOrders))
        cq.select(cqRoot).where(*restrictions.toTypedArray())

        val customsOrderSegments: List<CustomsOrderSegment> = entityManager.createQuery(cq).resultList

        for (customsOrder in customsOrders) {
            customsOrder.customsOrderSegments = customsOrderSegments.filter { it.customsOrder?.equals(customsOrder)?: false }.toMutableList()
        }
    }

    override fun findByCustomsOrderIdWithSubEntities(customsOrderId: Long?): List<CustomsOrderSegment> =
        customsOrderSegmentRepository.findByCustomsOrderId(customsOrderId).onEach {
            it.customsOrderSegmentStatuses = customsOrderSegmentStatusRepository.findBySegment(it).toMutableList()
            it.customsOrderSegmentReferences = customsOrderSegmentReferenceRepository.findBySegment(it).toMutableList()
        }
}
