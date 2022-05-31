package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderSegmentReference
import de.rhenus.scs.customs.order.model.CustomsOrderSegmentReferenceDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
abstract class CustomsOrderSegmentReferenceMapper {

    @Mapping(source = "segment.id", target = "segmentId")
    abstract fun toDto(entity: CustomsOrderSegmentReference?): CustomsOrderSegmentReferenceDto

    @Mapping(source = "segmentId", target = "segment")
    abstract fun toEntity(dto: CustomsOrderSegmentReferenceDto): CustomsOrderSegmentReference

}
