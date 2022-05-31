package repository

import de.rhenus.scs.customs.order.model.CustomsOrderUnit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CustomsOrderUnitRepository : JpaRepository<CustomsOrderUnit, Long> {


    @Query("select u.unitNo from CustomsOrderUnit u where u.customsOrder.id = ?1 and u.unitNo is not null")
    fun getUnitNumbersByCustomsOrderId(customsOrderId: Long?): List<String>

    @Query("select c from CustomsOrderUnit c where c.customsOrder.id = ?1")
    fun findByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderUnit>

    @Query("select c from CustomsOrderUnit c where c.customsOrder.id in ?1")
    fun findByCustomsOrderIdIn(customsOrderIdList: List<Long>?): List<CustomsOrderUnit>


}
