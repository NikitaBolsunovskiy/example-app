package mapper

import de.rhenus.scs.customs.common.mapper.DateMapper
import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderPositionAdditionalInformation
import de.rhenus.scs.customs.order.model.CustomsOrderPositionAdditionalInformationDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [DateMapper::class, IdToEntityMapper::class])
interface CustomsOrderPositionAdditionalInformationMapper {
    @Mapping(source = "customsOrderPosition.id", target = "customsOrderPositionId")
    fun toDto(entity: CustomsOrderPositionAdditionalInformation): CustomsOrderPositionAdditionalInformationDto

    @Mapping(source = "customsOrderPositionId", target = "customsOrderPosition")
    fun toEntity(dto: CustomsOrderPositionAdditionalInformationDto): CustomsOrderPositionAdditionalInformation
}
