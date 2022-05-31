package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderSegmentStatus
import de.rhenus.scs.customs.order.model.CustomsOrderSegmentStatusDto
import de.rhenus.scs.customs.order.model.SegmentStatus
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * Needs to be an abstract class as Enum <-> Long mapping fails when being an interface
 * https://github.com/mapstruct/mapstruct/issues/2421
 */
@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
abstract class CustomsOrderSegmentStatusMapper {
    @Mapping(source = "segment.id", target = "customsOrderSegmentId")
    abstract fun toDto(entity: CustomsOrderSegmentStatus): CustomsOrderSegmentStatusDto

    @Mapping(source = "customsOrderSegmentId", target = "segment")
    abstract fun toEntity(dto: CustomsOrderSegmentStatusDto): CustomsOrderSegmentStatus

    fun segmentStatusIdToSegmentStatus(id: Long?): SegmentStatus? = SegmentStatus.fromLong(id)

    fun segmentStatusToSegmentStatusId(segmentStatus: SegmentStatus?): Long? = segmentStatus?.code
}
