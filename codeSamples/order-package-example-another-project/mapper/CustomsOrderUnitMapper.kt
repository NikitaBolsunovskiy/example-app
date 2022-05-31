package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderUnit
import de.rhenus.scs.customs.order.model.CustomsOrderUnitDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
interface CustomsOrderUnitMapper {
    @Mapping(source = "customsOrder.id", target = "customsOrderId")
    fun toDto(entity: CustomsOrderUnit): CustomsOrderUnitDto

    @Mapping(source = "customsOrderId", target = "customsOrder")
    fun toEntity(dto: CustomsOrderUnitDto): CustomsOrderUnit

}
