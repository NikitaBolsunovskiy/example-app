package repository

import de.rhenus.scs.customs.order.model.CustomsOrderPositionTax
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CustomsOrderPositionTaxRepository : JpaRepository<CustomsOrderPositionTax, Long> {
    @Query("SELECT COUNT(t) FROM CustomsOrderPositionTax t WHERE t.customsOrderPosition.id = :posId and t.type = :type and (t.id is null or t.id <> :id)" )
    fun countByCustomsOrderPositionIdAndTypeAndIdIsNot(posId: Long?, type: String?, id: Long?): Long
}
