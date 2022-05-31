package repository

import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.OrderStatus
import de.rhenus.scs.customs.order.model.SegmentType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CustomsOrderRepository: JpaRepository<CustomsOrder, Long>, JpaSpecificationExecutor<CustomsOrder>,
    CustomsOrderRepositoryCustom {

    fun findByCustomsOrderNo(customsOrderNo: String): CustomsOrder?

    @Query("FROM CustomsOrder o JOIN FETCH o.customsOrderSegments WHERE o.customsOrderNo = :customsOrderNo")
    fun findByCustomsOrderNoWithSegments(customsOrderNo: String): CustomsOrder?

    fun findByIdIn(customsOrderIdList: List<Long>): List<CustomsOrder>

    @Query("""SELECT count(o) 
        FROM CustomsOrder o JOIN o.customer c 
        WHERE c.customerIdCct = :customerIdCct 
        AND upper(o.customerReference) = upper(:customerReference) 
        AND o.customsOrderStatus not in (:excludedStatus)""")
    fun countNonConsolidatedByCustomerIdCctAndCustomerReference(
        customerIdCct: String?,
        customerReference: String?,
        excludedStatus: List<OrderStatus>,
    ): Long

    @Query(
        value = """
        SELECT count(o) 
        FROM CustomsOrder o JOIN o.customer c 
        WHERE c.customerIdCct = :customerIdCct 
        AND UPPER(o.customerReference) = UPPER(:customerReference) 
        AND o.customsOrderStatus not in (de.rhenus.scs.customs.order.model.OrderStatus.STATUS_CONSOLIDATED, de.rhenus.scs.customs.order.model.OrderStatus.STATUS_REJECTED)
        AND exists(SELECT s FROM CustomsOrderSegment as s where s.customsOrder = o and s.type = :segmentType)
        """
    )
    fun countNonConsolidatedByCustomerIdCctAndCustomerReferenceAndSegment(
        customerIdCct: String?,
        customerReference: String?,
        segmentType: SegmentType?
    ): Long

    @Query(
        """
        FROM CustomsOrder o JOIN o.customer c
        WHERE c.customerIdCct = :customerIdCct
        AND NOT exists(SELECT s.id from CustomsOrderSegment s where s.customsOrder.id = o.id)
        """
    )
    fun findWithoutSegmentsForCustomerIdCct(customerIdCct: String?): List<CustomsOrder>

    @Modifying
    @Query(
        value = """
        UPDATE customs_order o 
        SET o.customs_order_status_id = 96, 
         o.consolidated_to_customs_order_id = ?2,   
         o.consolidated_to_customs_order_no = ?3
        where id in (?1)
        """,
        nativeQuery = true
    )
    fun setConsolidated(
        customsOrderIdList: List<Long?>?,
        consolidatedCustomsOrder: CustomsOrder,
        consolidatedCustomsOrderNo: String?
    )

    fun findByConsolidatedToCustomsOrderId(id: Long): List<CustomsOrder>

}
