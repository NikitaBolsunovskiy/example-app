package model

import com.fasterxml.jackson.annotation.JsonFormat
import de.rhenus.scs.customs.common.model.BaseDto
import de.rhenus.scs.customs.security.permissions.CustomsOrderPermissions
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant

data class CustomsOrderDto(
    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    var tenant: Long? = null,
    var importWay: String? = null,
    var interfaceVersion: String? = null,
    var workflowId: String? = null,
    var customsOrderNo: String? = null,
    var consolidatedToCustomsOrderNo: String? = null,
    var consolidatedToCustomsOrderId: Long? = null,
    var consolidatedOrder: Boolean? = null,
    @JsonFormat(
        shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    var customsOrderDatetime: Instant? = null,
    var customsOrderStatusId: Long? = null,
    var customerId: Long? = null,
    var shipmentNo: String? = null,
    var typeOfDeclaration: String? = null,
    var customerEmail: String? = null,
    var customerReference: String? = null,
    var deliveryTermSad20: String? = null,
    var deliveryTermSad20Id: Long? = null,
    var deliveryTermPlaceSad20: String? = null,
    var countryOfExportSad15: String? = null,
    var regionCodeOfExportSad15: String? = null,
    var countryOfDestinationSad17: String? = null,
    var regionCodeOfDestinationSad17: String? = null,
    var countryOfOriginSad16: String? = null,
    var portOfLoading: String? = null,
    var portOfDestination: String? = null,
    var placeOfLoadingSad27ex: String? = null,
    var placeOfDischargeSad27im: String? = null,
    var modeOfTransportAtBorderSad25ex: Long? = null,
    var modeOfTransportAtBorderSad25t: Long? = null,
    var modeOfTransportInlandSad26ex: Long? = null,
    var modeOfTransportAtBorderSad25im: Long? = null,
    var modeOfTransportInlandSad26t: Long? = null,
    var modeOfTransportInlandSad26im: Long? = null,

    @JsonFormat(
        shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    var consignorPickupDate: Instant? = null,
    var transportMeansDepartureSad18ex: String? = null,
    var transportMeansNationalityDepartureSad18ex: String? = null,
    var transportMeansTransitSad18t: String? = null,
    var transportMeansNationalityTransitSad18t: String? = null,
    var transportMeansArrivalSad18im: String? = null,
    var transportMeansNationalityArrivalSad18im: String? = null,
    var transportMeansAtBorderSad21ex: String? = null,
    var transportMeansNationalityAtBorderSad21ex: String? = null,
    var transportMeansAtBorderSad21t: String? = null,
    var transportMeansNationalityAtBorderSad21t: String? = null,
    var transportMeansAtBorderSad21im: String? = null,
    var transportMeansNationalityAtBorderSad21im: String? = null,
    var transportInContainersSad19: Boolean? = null,

    @JsonFormat(
        shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    var mainHaulEta: Instant? = null,

    @JsonFormat(
        shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    var consigneeDeliveryDate: Instant? = null,
    var consignorOrderNo: String? = null,
    var consigneeOrderNo: String? = null,
    var customsOfficeOfExitSad29ex: String? = null,
    var customsOfficeOfEntrySad29im: String? = null,
    var customsOfficeOfArrival: String? = null,
    var customsOfficeOfDeparture: String? = null,
    var customsOfficeOfTransitSad51t: String? = null,
    var customsOfficeOfTransit2Sad51t: String? = null,
    var customsOfficeOfTransit3Sad51t: String? = null,
    var customsOfficeOfTransit4Sad51t: String? = null,
    var customsOfficeOfTransit5Sad51t: String? = null,
    var customsOfficeOfTransit6Sad51t: String? = null,
    var totalPackagesSad06: Long? = null,
    var declarantReferenceNoSad07: String? = null,
    var countryFirstDestinationSad10ex: String? = null,
    var countryLastLoadSad10im: String? = null,
    var purchaseCountrySad11: String? = null,
    var natureOfTransactionSad24: String? = null,
    var loadListCountSad04: Long? = null,
    var valueDetailsSad12: String? = null,
    var totalAmountInvoicedSad22: Float? = null,
    var totalAmountInvoicedCurrencySad22: String? = null,
    var totalGrossMass: Float? = null,
    var leadOfficeId: Long? = null,
    var customerCustomerNo: String? = null,
    var totalGrossMassUnit: String? = null,
    var localTransportationChargesAmount: Float? = null,
    var localTransportationChargesCurrency: String? = null,
    var mainHaulChargesAmount: Float? = null,
    var mainHaulChargesCurrency: String? = null,
    var insuranceChargesAmount: Float? = null,
    var insuranceChargesCurrency: String? = null,
    var otherChargesAmount: Float? = null,
    var otherChargesCurrency: String? = null,
    var vatChargesAmount: Float? = null,
    var vatChargesCurrency: String? = null,
    var locationOfGoodsSad30ex: String? = null,
    var locationOfGoodsSad30t: String? = null,
    var locationOfGoodsSad30im: String? = null,
    var firstDefermentApprovalNumberCodeSad48: String? = null,
    var firstDefermentApprovalNumberSad48: String? = null,
    var secondDefermentApprovalNumberCodeSad48: String? = null,
    var secondDefermentApprovalNumberSad48: String? = null,
    var preference: Boolean? = null,
    var information: String? = null,
    var sooOnInvoice: Boolean? = null,
    var units: List<CustomsOrderUnitDto> = mutableListOf(),
    var addresses: List<CustomsOrderAddressDto> = mutableListOf(),
    var documents: List<CustomsOrderDocumentDto> = mutableListOf(),
    var positions: List<CustomsOrderPositionDto> = mutableListOf(),
    var invoices: List<CustomsOrderInvoiceDto> = mutableListOf(),
    var segments: List<CustomsOrderSegmentDto> = mutableListOf(),
    var statuses: List<CustomsOrderStatusDto> = mutableListOf(),

    // granted authorities...
    var permissions: CustomsOrderPermissions? = null,
    var startable: Boolean = false,
    var cancelable: Boolean = false,
    var transmittable: Boolean = false,
    var consolidatable: Boolean = false,

    ) : BaseDto()
