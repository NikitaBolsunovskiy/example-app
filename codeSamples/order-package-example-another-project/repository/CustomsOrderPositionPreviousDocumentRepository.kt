package repository

import de.rhenus.scs.customs.order.model.CustomsOrderPositionPreviousDocument
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CustomsOrderPositionPreviousDocumentRepository: JpaRepository<CustomsOrderPositionPreviousDocument, Long>{
    @Query("SELECT COUNT(pd) > 0 FROM CustomsOrderPositionPreviousDocument pd WHERE pd.customsOrderPosition.id = :posId and pd.reference = :reference and (pd.id is null or pd.id <> :id)" )
    fun existsByCustomsOrderPositionIdAndReferenceAndIdNot(posId: Long?, reference: String?, id: Long?): Boolean
}
