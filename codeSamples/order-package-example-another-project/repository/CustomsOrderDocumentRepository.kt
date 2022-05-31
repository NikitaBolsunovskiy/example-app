package repository

import de.rhenus.scs.customs.documenttype.model.DocumentType
import de.rhenus.scs.customs.order.model.CustomsOrderDocument
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface CustomsOrderDocumentRepository : JpaRepository<CustomsOrderDocument, Long> {

    fun findByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderDocument>
    fun findByCustomsOrderIdIn(customsOrderIdList: List<Long>): List<CustomsOrderDocument>

    @Query("select (count(c) > 0) from CustomsOrderDocument c where c.customsOrder.id = ?1 and c.documentNo = ?2 and c.documentType = ?3 and c.id <> ?4")
    fun existsByCustomsOrderIdAndDocumentNoAndDocumentTypeAndId(
        customsOrderId: Long?, documentNo: String?, documentType: DocumentType?, id: Long?
    ): Boolean

    @Query("SELECT d FROM CustomsOrderDocument d WHERE d.createdDate >= ?1 and d.archiveError = true and d.archiveReference is null")
    fun findByCreatedAfterAndArchiveError(createdAt: Instant?): List<CustomsOrderDocument>


}
