package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderPositionTax
import de.rhenus.scs.customs.order.model.CustomsOrderPositionTaxDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
interface CustomsOrderPositionTaxMapper {
    @Mapping(source = "customsOrderPosition.id", target = "customsOrderPositionId")
    fun toDto(entity: CustomsOrderPositionTax): CustomsOrderPositionTaxDto

    @Mapping(source = "customsOrderPositionId", target = "customsOrderPosition")
    fun toEntity(dto: CustomsOrderPositionTaxDto): CustomsOrderPositionTax
}
