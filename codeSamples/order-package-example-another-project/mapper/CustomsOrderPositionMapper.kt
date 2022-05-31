package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderPosition
import de.rhenus.scs.customs.order.model.CustomsOrderPositionDto
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring",
    uses = [
        CustomsOrderPositionAdditionalInformationMapper::class,
        CustomsOrderPositionTaxMapper::class,
        CustomsOrderPositionPreviousDocumentMapper::class,
        IdToEntityMapper::class
    ]
)
interface CustomsOrderPositionMapper {
    @Mapping(source = "customsOrderPositionAdditionalInformations", target = "additionalInformations")
    @Mapping(source = "customsOrderPositionTaxes", target = "taxes")
    @Mapping(source = "customsOrderPositionPreviousDocuments", target = "previousDocuments")
    @Mapping(source = "customsOrder.id", target = "customsOrderId")
    fun toDto(entity: CustomsOrderPosition): CustomsOrderPositionDto

    @InheritInverseConfiguration
    @Mapping(source = "customsOrderId", target = "customsOrder")
    fun toEntity(dto: CustomsOrderPositionDto): CustomsOrderPosition
}
