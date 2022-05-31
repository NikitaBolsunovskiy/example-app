package repository

import de.rhenus.scs.customs.order.model.CustomsOrderSegment
import de.rhenus.scs.customs.order.model.CustomsOrderSegmentStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CustomsOrderSegmentStatusRepository : JpaRepository<CustomsOrderSegmentStatus, Long> {
    fun findBySegment(segment: CustomsOrderSegment?): List<CustomsOrderSegmentStatus>
}
