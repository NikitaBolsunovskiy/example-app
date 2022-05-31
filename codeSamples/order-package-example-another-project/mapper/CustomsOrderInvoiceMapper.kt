package mapper

import de.rhenus.scs.customs.common.mapper.DateMapper
import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderInvoice
import de.rhenus.scs.customs.order.model.CustomsOrderInvoiceDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [DateMapper::class, CustomsOrderInvoiceAddressMapper::class, IdToEntityMapper::class])
interface CustomsOrderInvoiceMapper {

    @Mapping(source = "customsOrderInvoiceAddresses", target = "addresses")
    @Mapping(source = "customsOrder.id", target = "customsOrderId")
    fun toDto(entity: CustomsOrderInvoice): CustomsOrderInvoiceDto

    @Mapping(target = "customsOrderInvoiceAddresses", source = "addresses")
    @Mapping(target = "customsOrder", source = "customsOrderId")
    fun toEntity(dto: CustomsOrderInvoiceDto): CustomsOrderInvoice
}
