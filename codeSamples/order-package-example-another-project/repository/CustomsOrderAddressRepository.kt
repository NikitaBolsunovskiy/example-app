package repository

import de.rhenus.scs.customs.order.model.CustomsOrderAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CustomsOrderAddressRepository : JpaRepository<CustomsOrderAddress, Long> {

    @Query("SELECT a FROM CustomsOrderAddress a WHERE a.complianceChecked = false")
    fun findNotCheckedAddresses(): List<CustomsOrderAddress>

    @Query("select c from CustomsOrderAddress c where c.customsOrder.id = ?1")
    fun findByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderAddress>

    @Query("select c from CustomsOrderAddress c where c.customsOrder.id in ?1 and c.role = ?2")
    fun findByCustomsOrderIdInAndRole(customsOrderIdList: List<Long>, role: String): List<CustomsOrderAddress>

    @Query("select c from CustomsOrderAddress c where c.customsOrder.id = ?1 and c.role = ?2")
    fun findByCustomsOrderIdAndRole(customsOrderId: Long?, role: String?): CustomsOrderAddress?
    @Query("SELECT count(a) FROM CustomsOrderAddress a where a.customsOrder.id = ?1 and a.complianceDisabled = false and (a.complianceFailed = true or a.complianceChecked = false)")
    fun countNonCompliantByCustomsOrderId(customsOrderId: Long?): Long?

}
