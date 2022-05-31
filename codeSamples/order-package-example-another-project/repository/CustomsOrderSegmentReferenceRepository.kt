package repository

import de.rhenus.scs.customs.order.model.CustomsOrderSegment
import de.rhenus.scs.customs.order.model.CustomsOrderSegmentReference
import org.springframework.data.jpa.repository.JpaRepository

interface CustomsOrderSegmentReferenceRepository : JpaRepository<CustomsOrderSegmentReference, Long> {
    fun findBySegment(segment: CustomsOrderSegment?): List<CustomsOrderSegmentReference>
}
