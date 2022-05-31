package mapper

import de.rhenus.scs.customs.common.mapper.DateMapper
import de.rhenus.scs.customs.common.mapper.IdToEntityMapper
import de.rhenus.scs.customs.customer.mapper.CustomerMapper
import de.rhenus.scs.customs.documenttype.mapper.DocumentTypeMapper
import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderDto
import de.rhenus.scs.customs.order.model.ModeOfTransport
import de.rhenus.scs.customs.order.model.OrderStatus
import de.rhenus.scs.customs.order.repository.CustomsOrderRepository
import de.rhenus.scs.customs.security.permissions.CustomsOrderPermissions
import de.rhenus.scs.customs.security.service.AuthHelperService
import de.rhenus.scs.customs.tenant.mapper.TenantMapper
import org.mapstruct.*
import org.springframework.beans.factory.annotation.Autowired

@Mapper(
    componentModel = "spring",
    uses = [
        DateMapper::class,
        TenantMapper::class,
        DocumentTypeMapper::class,
        CustomsOrderSegmentStatusMapper::class,
        CustomsOrderSegmentMapper::class,
        CustomsOrderInvoiceMapper::class,
        CustomsOrderPositionMapper::class,
        CustomsOrderStatusMapper::class,
        CustomsOrderDocumentMapper::class,
        CustomsOrderAddressMapper::class,
        CustomsOrderUnitMapper::class,
        CustomerMapper::class,
        IdToEntityMapper::class,
    ]
)
abstract class CustomsOrderMapper {

    @Autowired
    private lateinit var customsOrderRepository: CustomsOrderRepository
    @Autowired
    private lateinit var authHelperService: AuthHelperService

    @Mapping(source = "customsOrderSegments", target = "segments")
    @Mapping(source = "customsOrderUnits", target = "units")
    @Mapping(source = "customsOrderAddresses", target = "addresses")
    @Mapping(source = "customsOrderDocuments", target = "documents")
    @Mapping(source = "customsOrderPositions", target = "positions")
    @Mapping(source = "customsOrderInvoices", target = "invoices")
    @Mapping(source = "customsOrderStatuses", target = "statuses")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "leadOffice.id", target = "leadOfficeId")
    @Mapping(source = "customsOrderStatus.value", target = "customsOrderStatusId")
    @Mapping(source = ".", target = "permissions", qualifiedByName = ["determinePermissions"])
    abstract fun toDto(entity: CustomsOrder): CustomsOrderDto

    @Mapping(target = "units", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "positions", ignore = true)
    @Mapping(target = "invoices", ignore = true)
    @Mapping(target = "statuses", ignore = true)
    @Mapping(source = "customsOrderSegments", target = "segments")
    @Mapping(source = "customsOrderAddresses", target = "addresses")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "leadOffice.id", target = "leadOfficeId")
    @Mapping(source = "customsOrderStatus.value", target = "customsOrderStatusId")
    @Mapping(source = ".", target = "permissions", qualifiedByName = ["determinePermissions"])
    abstract fun toMinimalDto(entity: CustomsOrder): CustomsOrderDto

    @Mapping(target = "customsOrderSegments", source = "segments")
    @Mapping(target = "customsOrderUnits", source = "units")
    @Mapping(target = "customsOrderAddresses", source = "addresses")
    @Mapping(target = "customsOrderDocuments", source = "documents")
    @Mapping(target = "customsOrderPositions", source = "positions")
    @Mapping(target = "customsOrderInvoices", source = "invoices")
    @Mapping(target = "customsOrderStatuses", source = "statuses")
    @Mapping(target = "customer", source = "customerId")
    @Mapping(target = "leadOffice", source = "leadOfficeId")
    @Mapping(target = "customsOrderStatus", source = "customsOrderStatusId")
    @Mapping(target = "automatic", ignore = true)
    abstract fun toEntity(dto: CustomsOrderDto): CustomsOrder

    fun longToOrderStatus(long: Long): OrderStatus? = OrderStatus.values().find { it.value == long }

    @Named("determinePermissions")
    fun determinePermissions(entity: CustomsOrder): CustomsOrderPermissions {
        return CustomsOrderPermissions.create(entity, authHelperService.authorities)
    }

    fun motToCode(mot: ModeOfTransport?): Long? = mot?.code

    fun codeToMot(code: Long?): ModeOfTransport? = ModeOfTransport.values().firstOrNull { it.code == code }

}
