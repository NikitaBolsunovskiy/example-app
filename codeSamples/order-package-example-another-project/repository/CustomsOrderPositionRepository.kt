package repository

import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderPosition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CustomsOrderPositionRepository: JpaRepository<CustomsOrderPosition, Long>{
    fun findByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderPosition>
    fun findByCustomsOrderIdIn(customsOrderIdList: List<Long>): List<CustomsOrderPosition>

    @Query("FROM CustomsOrderPosition p JOIN FETCH p.customsOrderPositionAdditionalInformations WHERE p.customsOrder = :customsOrder")
    fun findByCustomsOrderWithAdditionalInformation(customsOrder: CustomsOrder?): List<CustomsOrderPosition>

    @Query("SELECT COALESCE(MAX(p.sequentialNoSad32), 0)+1 from CustomsOrderPosition p where p.customsOrder.id = ?1")
    fun getNextSequentialNo(customsOrderId: Long?): Long

    @Modifying
    @Query("update CustomsOrderPosition p set p.invoiceNo = ?3 where p.customsOrder.id = ?1 and p.invoiceNo = ?2")
    fun updateInvoiceNo(customsOrderId: Long?, oldInvoiceNo: String?, newInvoiceNo: String?)
}
