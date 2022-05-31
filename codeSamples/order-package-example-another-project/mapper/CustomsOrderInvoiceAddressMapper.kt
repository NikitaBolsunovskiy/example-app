package mapper

import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.order.model.CustomsOrderInvoiceAddress
import de.rhenus.scs.customs.order.model.CustomsOrderInvoiceAddressDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [IdToEntityMapper::class])
interface CustomsOrderInvoiceAddressMapper{
    @Mapping(source = "customsOrderInvoice.id", target = "customsOrderInvoiceId")
    fun toDto(entity: CustomsOrderInvoiceAddress): CustomsOrderInvoiceAddressDto

    @Mapping(source = "customsOrderInvoiceId", target = "customsOrderInvoice")
    fun toEntity(dto: CustomsOrderInvoiceAddressDto): CustomsOrderInvoiceAddress
}
