package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderSegment
import de.rhenus.scs.customs.order.model.CustomsOrderSegmentDto
import de.rhenus.scs.customs.order.model.SegmentType
import de.rhenus.scs.customs.security.permissions.CustomsOrderSegmentPermissions
import de.rhenus.scs.customs.security.service.AuthHelperService
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired

@Mapper(
    componentModel = "spring",
    uses = [CustomsOrderSegmentStatusMapper::class, CustomsOrderSegmentReferenceMapper::class, IdToEntityMapper::class]
)
abstract class CustomsOrderSegmentMapper {
    @Autowired
    private lateinit var authHelperService: AuthHelperService

    @Mapping(source = "customsOrder.id", target = "customsOrderId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "customsOrderSegmentStatuses", target = "statuses")
    @Mapping(source = "customsOrderSegmentReferences", target = "references")
    @Mapping(source = ".", target = "permissions", qualifiedByName = ["determinePermissions"])
    @Mapping(source = "status", target = "statusId")
    @Mapping(source = "type.value", target = "type")
    abstract fun toDto(entity: CustomsOrderSegment?): CustomsOrderSegmentDto

    @Mapping(source = "customsOrderId", target = "customsOrder")
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "statuses", target = "customsOrderSegmentStatuses")
    @Mapping(source = "references", target = "customsOrderSegmentReferences")
    @Mapping(source = "statusId", target = "status")
    @Mapping(source = "type", target = "type")
    abstract fun toEntity(dto: CustomsOrderSegmentDto): CustomsOrderSegment

    @Named("determinePermissions")
    fun determinePermissions(entity: CustomsOrderSegment): CustomsOrderSegmentPermissions =
        CustomsOrderSegmentPermissions.create(entity.customsOrder, entity, authHelperService.authorities)

    fun typeStringToTypeEnum(type: String?): SegmentType? = SegmentType.fromValue(type)
}
