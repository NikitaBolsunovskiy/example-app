package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderAddress
import de.rhenus.scs.customs.order.model.CustomsOrderAddressDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
interface CustomsOrderAddressMapper {
    @Mapping(source = "customsOrder.id", target = "customsOrderId")
    fun toDto(entity: CustomsOrderAddress): CustomsOrderAddressDto

    @Mapping(source = "customsOrderId", target = "customsOrder")
    fun toEntity(dto: CustomsOrderAddressDto): CustomsOrderAddress
}
