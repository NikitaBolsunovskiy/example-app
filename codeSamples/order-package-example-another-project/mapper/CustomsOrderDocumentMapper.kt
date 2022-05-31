package mapper

import de.rhenus.scs.customs.common.mapper.DateMapper
import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.documenttype.mapper.DocumentTypeMapper
import de.rhenus.scs.customs.order.model.CustomsOrderDocument
import de.rhenus.scs.customs.order.model.CustomsOrderDocumentDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring",
    uses = [DateMapper::class, DocumentTypeMapper::class, IdToEntityMapper::class]
)
interface CustomsOrderDocumentMapper {
    @Mapping(source = "customsOrder.id", target = "customsOrderId")
    @Mapping(source = "documentType.code", target = "documentType")
    fun toDto(entity: CustomsOrderDocument): CustomsOrderDocumentDto

    @Mapping(source = "customsOrderId", target = "customsOrder")
    fun toEntity(dto: CustomsOrderDocumentDto): CustomsOrderDocument
}
