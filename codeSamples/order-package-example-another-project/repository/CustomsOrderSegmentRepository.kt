package repository

import de.rhenus.scs.customs.order.model.CustomsOrderSegment
import de.rhenus.scs.customs.order.model.SegmentStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CustomsOrderSegmentRepository : JpaRepository<CustomsOrderSegment, Long>,
    CustomsOrderSegmentRepositoryCustom {
    fun findByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderSegment>
    fun findByCustomsOrderIdAndStatusNotIn(customsOrderId: Long, status: List<SegmentStatus>): List<CustomsOrderSegment>
}
