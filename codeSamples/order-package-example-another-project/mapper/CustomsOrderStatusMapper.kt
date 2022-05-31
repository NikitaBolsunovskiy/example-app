package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderStatus
import de.rhenus.scs.customs.order.model.CustomsOrderStatusDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
interface CustomsOrderStatusMapper {
    @Mapping(source = "customsOrder.id", target = "customsOrderId")
    fun toDto(entity: CustomsOrderStatus): CustomsOrderStatusDto

    @Mapping(source = "customsOrderId", target = "customsOrder")
    fun toEntity(dto: CustomsOrderStatusDto): CustomsOrderStatus
}
