package repository

import de.rhenus.scs.customs.order.model.CustomsOrderInvoiceAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CustomsOrderInvoiceAddressRepository : JpaRepository<CustomsOrderInvoiceAddress, Long> {

    @Query("SELECT a FROM CustomsOrderInvoiceAddress a WHERE a.complianceChecked = false")
    fun findNotCheckedAddresses(): List<CustomsOrderInvoiceAddress>

    @Query("""SELECT 
                  count(adr) 
                FROM
                    CustomsOrderInvoiceAddress adr 
                WHERE 
                    adr.customsOrderInvoice.customsOrder.id = ?1 and adr.complianceDisabled = false and ( adr.complianceFailed = true or adr.complianceChecked = false )""")
    fun countNonCompliantByCustomsOrderId(customsOrderId: Long?): Long
}
