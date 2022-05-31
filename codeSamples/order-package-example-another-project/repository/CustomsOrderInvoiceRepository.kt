package repository

import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderInvoice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CustomsOrderInvoiceRepository:JpaRepository<CustomsOrderInvoice, Long> {

    @Query("select c from CustomsOrderInvoice c where c.customsOrder.id = ?1")
    fun findByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderInvoice>

    @Query("FROM CustomsOrderInvoice i JOIN FETCH i.customsOrderInvoiceAddresses WHERE i.customsOrder = :customsOrder")
    fun findByCustomsOrderWithAddresses(customsOrder: CustomsOrder?): List<CustomsOrderInvoice>

    @Query("select c from CustomsOrderInvoice c where c.customsOrder.id in ?1")
    fun findByCustomsOrderIdIn(customsOrderIdList: List<Long>): List<CustomsOrderInvoice>

    @Query("select (count(c) > 0) from CustomsOrderInvoice c where c.customsOrder.id = ?1 and c.invoiceNo = ?2 and c.id <> ?3")
    fun existsByCustomsOrderIdAndInvoiceNoAndId(customsOrderId: Long?, invoiceNo: String?, id: Long?): Boolean

    @Query("select invoiceNo from CustomsOrderInvoice where customsOrder.id = ?1")
    fun getInvoiceNumbersByCustomsOrderId(customsOrderId: Long?): List<String>

}
