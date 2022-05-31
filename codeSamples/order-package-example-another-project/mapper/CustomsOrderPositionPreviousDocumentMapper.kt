package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderPositionPreviousDocument
import de.rhenus.scs.customs.order.model.CustomsOrderPositionPreviousDocumentDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
interface CustomsOrderPositionPreviousDocumentMapper {
    @Mapping(source = "customsOrderPosition.id", target = "customsOrderPositionId")
    fun toDto(entity: CustomsOrderPositionPreviousDocument): CustomsOrderPositionPreviousDocumentDto

    @Mapping(source = "customsOrderPositionId", target = "customsOrderPosition")
    fun toEntity(dto: CustomsOrderPositionPreviousDocumentDto): CustomsOrderPositionPreviousDocument
}
