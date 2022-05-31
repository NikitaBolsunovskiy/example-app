package repository

import de.rhenus.scs.customs.order.model.CustomsOrderError
import de.rhenus.scs.customs.ordererror.model.CustomsOrderErrorSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CustomsOrderErrorRepository: JpaRepository<CustomsOrderError, Long> {

    @Query("SELECT e FROM CustomsOrderError e WHERE e.customsOrder.id = ?1 and e.solved = false and e.confirmed = false")
    fun findActiveByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderError>


    @Query(
        """SELECT
             new de.rhenus.scs.customs.ordererror.model.CustomsOrderErrorSummary(x.customsOrder.id, x.statusId, count(x))
        FROM CustomsOrderError x 
        WHERE x.customsOrder.id in(:customsOrderIdList) and x.solved = false and x.confirmed = false  AND x.statusId = :statusId 
        GROUP BY x.customsOrder.id, x.statusId""")
    fun getSummaryByStatusId(
        statusId: String?, customsOrderIdList: List<Long>
    ): List<CustomsOrderErrorSummary>

    @Query("SELECT e FROM CustomsOrderError e WHERE e.customsOrderSegment.id = ?1 and e.solved = false and e.confirmed = false")
    fun findActiveByCustomsOrderSegmentId(customsOrderSegmentId: Long?): List<CustomsOrderError>
}
