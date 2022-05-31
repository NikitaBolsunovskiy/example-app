package repository

import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.OrderStatus
import de.rhenus.scs.customs.security.permissions.DataRule
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

interface CustomsOrderRepositoryCustom {

    fun search(
        rulesForLead: List<DataRule>?,
        rulesForDeclarant: List<DataRule>?,
        offices: List<Long>?,

        leadAllowedOrderStatus: List<OrderStatus>?,
        allowedOrderStatus: List<OrderStatus>?,

        customsOrderNo: String?,
        consolidatedCustomsOrderNo: String?,
        orderStatus: List<Long>?,
        segmentStatus: List<Long>?,
        customsOrderDatetimeFrom: LocalDateTime?,
        customsOrderDatetimeTo: LocalDateTime?,
        countriesOfExport: List<String>?,
        countriesOfDestination: List<String>?,
        localCustomsAppId: String?,
        customer: String?,
        consignor: String?,
        consignee: String?,
        consignorPickupDateFrom: LocalDate?,
        consignorPickupDateTo: LocalDate?,
        consigneeDeliveryDateFrom: LocalDate?,
        consigneeDeliveryDateTo: LocalDate?,
        portOfLoading: String?,
        portOfDestination: String?,
        customerReference: String?,
        shipmentNo: String?,
        locationOfGoodsSad30: String?,
        transportInContainersSad19: Boolean?,
        unitNo: String?,
        invoiceNo: String?,
        pageable: Pageable,
    ): Page<CustomsOrder>

    fun searchForRegistrations(
        customerIdCct: String?,
        customsOrderNo: String?,
        customsOrderDatetimeFrom: Instant?,
        customsOrderDatetimeTo: Instant?,
        deliveryTerm: String?,
        countryOfExport: String?,
        countryOfDestination: String?,
        customerReference: String?,
        text: String?,
        pageable: Pageable,
    ): Page<CustomsOrder>

    fun findByCustomsOrderNoWithSubEntities(
        customsOrderNo: String,
    ): CustomsOrder?
}
