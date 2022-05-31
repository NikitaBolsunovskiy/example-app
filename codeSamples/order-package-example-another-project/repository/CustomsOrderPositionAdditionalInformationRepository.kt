package repository

import de.rhenus.scs.customs.order.model.CustomsOrderPosition
import de.rhenus.scs.customs.order.model.CustomsOrderPositionAdditionalInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CustomsOrderPositionAdditionalInformationRepository: JpaRepository<CustomsOrderPositionAdditionalInformation, Long> {
    @Query("""SELECT count(ai) > 0
        FROM CustomsOrderPositionAdditionalInformation ai
        WHERE ai.customsOrderPosition.id = :customsOrderPositionId and ai.type = :type and ai.code = :code and (ai.id is null or ai.id <> :id)""")
    fun existsByCustomsOrderPositionIdAndTypeAndCodeAndIdNot(customsOrderPositionId: Long?, type: String?, code: String?, id: Long?): Boolean
    fun findByCustomsOrderPosition(customsOrderPosition: CustomsOrderPosition)
}
