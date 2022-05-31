package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderError
import de.rhenus.scs.customs.order.model.CustomsOrderErrorDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
interface CustomsOrderErrorMapper {

    @Mapping(source = "customsOrder.id", target = "customsOrderId")
    @Mapping(source = "customsOrderSegment.id", target = "customsOrderSegmentId")
    fun entityToDto(customsOrderError: CustomsOrderError): CustomsOrderErrorDto

    @Mapping(source = "customsOrderId", target = "customsOrder")
    @Mapping(source = "customsOrderSegmentId", target = "customsOrderSegment")
    fun dtoToEntity(customsOrderErrorDto: CustomsOrderErrorDto): CustomsOrderError
}
