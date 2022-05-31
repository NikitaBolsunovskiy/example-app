package repository

import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderSegment
import de.rhenus.scs.customs.security.permissions.DataRule
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

interface CustomsOrderSegmentRepositoryCustom {

    fun restrictSegments(
        permissions: List<DataRule>,
        cb: CriteriaBuilder,
        cq: CriteriaQuery<CustomsOrderSegment>,
        cqRoot: Root<CustomsOrderSegment>
    ): List<Predicate>

    fun loadSegments(customsOrders: MutableList<CustomsOrder>, rules: MutableList<DataRule>)

    fun findByCustomsOrderIdWithSubEntities(customsOrderId: Long?): List<CustomsOrderSegment>

}
