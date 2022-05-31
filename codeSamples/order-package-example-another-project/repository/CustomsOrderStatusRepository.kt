package repository

import de.rhenus.scs.customs.order.model.CustomsOrderStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CustomsOrderStatusRepository : JpaRepository<CustomsOrderStatus, Long> {
    fun findByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderStatus>

    fun findByCustomsOrderIdOrderByIdDesc(customsOrderId: Long?): List<CustomsOrderStatus>
}
