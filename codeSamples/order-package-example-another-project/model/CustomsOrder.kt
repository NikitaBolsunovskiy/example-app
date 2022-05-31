package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.customer.model.Customer
import de.rhenus.scs.customs.location.model.Location
import de.rhenus.scs.customs.order.converter.ModeOfTransportConverter
import de.rhenus.scs.customs.order.converter.OrderStatusConverter
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import de.rhenus.scs.customs.tenant.model.Tenant
import org.apache.commons.collections.CollectionUtils
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "customs_order")
@EntityListeners(AuditingEntityListener::class)
@Audited(withModifiedFlag = true)
data class CustomsOrder(
    @Column(name = "workflow_id")
    var workflowId: String? = null,

    @Column(name = "customs_order_no", length = 35)
    var customsOrderNo: String? = null,

    @Column(name = "shipment_no", length = 35)
    var shipmentNo: String? = null,

    @Column(name = "customer_email")
    var customerEmail: String? = null,

    @Column(name = "customer_reference", length = 50)
    var customerReference: String? = null,

    @Column(name = "delivery_term_sad20", length = 3)
    var deliveryTermSad20: String? = null,

    @Column(name = "delivery_term_sad20_id")
    var deliveryTermSad20Id: Long? = null,

    @Column(name = "delivery_term_place_sad20", length = 70)
    var deliveryTermPlaceSad20: String? = null,

    @Column(name = "country_of_export_sad15", length = 3)
    var countryOfExportSad15: String? = null,

    @Column(name = "region_code_of_export_sad15", length = 15)
    var regionCodeOfExportSad15: String? = null,

    @Column(name = "country_of_destination_sad17", length = 3)
    var countryOfDestinationSad17: String? = null,

    @Column(name = "region_code_of_destination_sad17", length = 15)
    var regionCodeOfDestinationSad17: String? = null,

    @Column(name = "country_of_origin_sad16", length = 3)
    var countryOfOriginSad16: String? = null,

    @Column(name = "port_of_loading", length = 6)
    var portOfLoading: String? = null,

    @Column(name = "port_of_destination", length = 6)
    var portOfDestination: String? = null,

    @Column(name = "place_of_loading_sad27ex", length = 70)
    var placeOfLoadingSad27ex: String? = null,

    @Column(name = "place_of_discharge_sad27im", length = 70)
    var placeOfDischargeSad27im: String? = null,

    @Convert(disableConversion = true)
    @Column(name = "consignor_pickup_date")
    var consignorPickupDate: LocalDate? = null,

    @Column(name = "transport_means_departure_sad18ex", length = 30)
    var transportMeansDepartureSad18ex: String? = null,

    @Column(name = "transport_means_nationality_departure_sad18ex", length = 3)
    var transportMeansNationalityDepartureSad18ex: String? = null,

    @Column(name = "transport_means_arrival_sad18im", length = 30)
    var transportMeansArrivalSad18im: String? = null,

    @Column(name = "transport_means_nationality_arrival_sad18im", length = 3)
    var transportMeansNationalityArrivalSad18im: String? = null,

    @Column(name = "transport_in_containers_sad19")
    var transportInContainersSad19: Boolean? = null,

    @Column(name = "main_haul_eta")
    var mainHaulEta: Instant? = null,

    @Convert(disableConversion = true)
    @Column(name = "consignee_delivery_date")
    var consigneeDeliveryDate: LocalDate? = null,

    @Column(name = "consignor_order_no", length = 35)
    var consignorOrderNo: String? = null,

    @Column(name = "consignee_order_no", length = 35)
    var consigneeOrderNo: String? = null,

    @Column(name = "customs_office_of_exit_sad29ex", length = 8)
    var customsOfficeOfExitSad29ex: String? = null,

    @Column(name = "customs_office_of_entry_sad29im", length = 8)
    var customsOfficeOfEntrySad29im: String? = null,

    @Column(name = "customs_office_of_arrival", length = 8)
    var customsOfficeOfArrival: String? = null,

    @Column(name = "customs_office_of_departure", length = 8)
    var customsOfficeOfDeparture: String? = null,

    @Column(name = "total_packages_sad06")
    var totalPackagesSad06: Int? = null,

    @Column(name = "declarant_reference_no_sad07", length = 35)
    var declarantReferenceNoSad07: String? = null,

    @Column(name = "country_first_destination_sad10ex", length = 3)
    var countryFirstDestinationSad10ex: String? = null,

    @Column(name = "country_last_load_sad10im", length = 3)
    var countryLastLoadSad10im: String? = null,

    @Column(name = "purchase_country_sad11", length = 3)
    var purchaseCountrySad11: String? = null,

    @Column(name = "load_list_count_sad04")
    var loadListCountSad04: Int? = null,

    @Column(name = "value_details_sad12", length = 1)
    var valueDetailsSad12: String? = null,

    @Column(name = "total_amount_invoiced_sad22", precision = 19, scale = 2)
    var totalAmountInvoicedSad22: BigDecimal? = null,

    @Column(name = "total_amount_invoiced_currency_sad22", length = 3)
    var totalAmountInvoicedCurrencySad22: String? = null,

    @Column(name = "total_gross_mass", precision = 19, scale = 3)
    var totalGrossMass: BigDecimal? = null,

    @ManyToOne
    @JoinColumn(name = "lead_office_id")
    @NotAudited
    var leadOffice: Location? = null,

    @Column(name = "transport_means_at_border_sad21ex", length = 30)
    var transportMeansAtBorderSad21ex: String? = null,

    @Column(name = "transport_means_nationality_at_border_SAD21ex", length = 3)
    var transportMeansNationalityAtBorderSad21ex: String? = null,

    @Column(name = "transport_means_at_border_sad21im", length = 30)
    var transportMeansAtBorderSad21im: String? = null,

    @Column(name = "transport_means_nationality_at_border_SAD21im", length = 3)
    var transportMeansNationalityAtBorderSad21im: String? = null,

    @Column(name = "customs_order_datetime")
    var customsOrderDatetime: Instant? = null,

    @Column(name = "nature_of_transaction_sad24", length = 2)
    var natureOfTransactionSad24: String? = null,

    @Column(name = "customer_customer_no", length = 35)
    var customerCustomerNo: String? = null,

    @Column(name = "total_gross_mass_unit", length = 10)
    var totalGrossMassUnit: String? = null,

    @Column(name = "local_transportation_charges_amount", precision = 19, scale = 2)
    var localTransportationChargesAmount: BigDecimal? = null,

    @Column(name = "local_transportation_charges_currency", length = 3)
    var localTransportationChargesCurrency: String? = null,

    @Column(name = "main_haul_charges_amount", precision = 19, scale = 2)
    var mainHaulChargesAmount: BigDecimal? = null,

    @Column(name = "main_haul_charges_currency", length = 3)
    var mainHaulChargesCurrency: String? = null,

    @Column(name = "insurance_charges_amount", precision = 19, scale = 2)
    var insuranceChargesAmount: BigDecimal? = null,

    @Column(name = "insurance_charges_currency", length = 3)
    var insuranceChargesCurrency: String? = null,

    @Column(name = "other_charges_amount", precision = 19, scale = 2)
    var otherChargesAmount: BigDecimal? = null,

    @Column(name = "other_charges_currency", length = 3)
    var otherChargesCurrency: String? = null,

    @Column(name = "vat_charges_amount", precision = 19, scale = 2)
    var vatChargesAmount: BigDecimal? = null,

    @Column(name = "vat_charges_currency", length = 3)
    var vatChargesCurrency: String? = null,

    @Column(name = "location_of_goods_sad30ex", length = 70)
    var locationOfGoodsSad30ex: String? = null,

    @Column(name = "location_of_goods_sad30t", length = 70)
    var locationOfGoodsSad30t: String? = null,

    @Column(name = "location_of_goods_sad30im", length = 70)
    var locationOfGoodsSad30im: String? = null,

    @Column(name = "automatic")
    var automatic: Boolean? = null,

    @Column(name = "customs_office_of_transit_sad51t", length = 8)
    var customsOfficeOfTransitSad51t: String? = null,

    @Column(name = "type_of_declaration", length = 10)
    var typeOfDeclaration: String? = null,

    @Column(name = "interface_version", length = 5)
    var interfaceVersion: String? = null,

    @Column(name = "first_deferment_approval_number_code_sad48", length = 1)
    var firstDefermentApprovalNumberCodeSad48: String? = null,

    @Column(name = "first_deferment_approval_number_sad48", length = 35)
    var firstDefermentApprovalNumberSad48: String? = null,

    @Column(name = "second_deferment_approval_number_code_sad48", length = 35)
    var secondDefermentApprovalNumberCodeSad48: String? = null,

    @Column(name = "second_deferment_approval_number_sad48", length = 35)
    var secondDefermentApprovalNumberSad48: String? = null,

    @Column(name = "customs_office_of_transit2_sad51t", length = 8)
    var customsOfficeOfTransit2Sad51t: String? = null,

    @Column(name = "customs_office_of_transit3_sad51t", length = 8)
    var customsOfficeOfTransit3Sad51t: String? = null,

    @Column(name = "customs_office_of_transit4_sad51t", length = 8)
    var customsOfficeOfTransit4Sad51t: String? = null,

    @Column(name = "customs_office_of_transit5_sad51t", length = 8)
    var customsOfficeOfTransit5Sad51t: String? = null,

    @Column(name = "customs_office_of_transit6_sad51t", length = 8)
    var customsOfficeOfTransit6Sad51t: String? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    @NotAudited
    var customer: Customer? = null,

    @Column(name = "preference")
    var preference: Boolean? = null,

    @Column(name = "information", length = 16384)
    var information: String? = null,

    @Column(name = "import_way", length = 10)
    var importWay: String? = null,

    @Column(name = "soo_on_invoice")
    var sooOnInvoice: Boolean? = null,

    @Column(name = "transport_means_transit_sad18t", length = 30)
    var transportMeansTransitSad18t: String? = null,

    @Column(name = "transport_means_nationality_transit_sad18t", length = 3)
    var transportMeansNationalityTransitSad18t: String? = null,

    @Column(name = "transport_means_at_border_sad21t", length = 30)
    var transportMeansAtBorderSad21t: String? = null,

    @Column(name = "transport_means_nationality_at_border_sad21t", length = 3)
    var transportMeansNationalityAtBorderSad21t: String? = null,

    @Column(name = "consolidated_to_customs_order_no", length = 35)
    var consolidatedToCustomsOrderNo: String? = null,

    @Column(name = "consolidated_to_customs_order_id")
    var consolidatedToCustomsOrderId: Long? = null,

    @Column(name = "consolidated_order")
    var consolidatedOrder: Boolean? = null,

    @Column(name = "customs_order_status_id")
    @Convert(converter = OrderStatusConverter::class)
    var customsOrderStatus: OrderStatus? = null,

    @ManyToOne
    @JoinColumn(name = "tenant")
    @NotAudited
    var tenant: Tenant? = null,

    @Column(name = "mode_of_transport_at_border_sad25ex_id")
    @Convert(converter = ModeOfTransportConverter::class)
    var modeOfTransportAtBorderSad25ex: ModeOfTransport? = null,

    @Column(name = "mode_of_transport_inland_sad26ex_id")
    @Convert(converter = ModeOfTransportConverter::class)
    var modeOfTransportInlandSad26ex: ModeOfTransport? = null,

    @Column(name = "mode_of_transport_at_border_sad25im_id")
    @Convert(converter = ModeOfTransportConverter::class)
    var modeOfTransportAtBorderSad25im: ModeOfTransport? = null,

    @Column(name = "mode_of_transport_inland_sad26im_id")
    @Convert(converter = ModeOfTransportConverter::class)
    var modeOfTransportInlandSad26im: ModeOfTransport? = null,

    @Column(name = "mode_of_transport_at_border_sad25t_id")
    @Convert(converter = ModeOfTransportConverter::class)
    var modeOfTransportAtBorderSad25t: ModeOfTransport? = null,

    @Column(name = "mode_of_transport_inland_sad26t_id")
    @Convert(converter = ModeOfTransportConverter::class)
    var modeOfTransportInlandSad26t: ModeOfTransport? = null,

    @OneToMany(mappedBy = "customsOrder", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderSegments: MutableList<CustomsOrderSegment> = mutableListOf(),

    @OneToMany(mappedBy = "customsOrder", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderAddresses: MutableList<CustomsOrderAddress> = mutableListOf(),

    @OneToMany(mappedBy = "customsOrder", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderPositions: MutableList<CustomsOrderPosition> = mutableListOf(),

    @OneToMany(mappedBy = "customsOrder", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderUnits: MutableList<CustomsOrderUnit> = mutableListOf(),

    @OneToMany(mappedBy = "customsOrder", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderInvoices: MutableList<CustomsOrderInvoice> = mutableListOf(),

    @OneToMany(mappedBy = "customsOrder", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderDocuments: MutableList<CustomsOrderDocument> = mutableListOf(),

    @OneToMany(mappedBy = "customsOrder", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderStatuses: MutableList<CustomsOrderStatus> = mutableListOf(),

    ) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {

    fun getSegmentByType(segmentType: SegmentType?): CustomsOrderSegment? =
        this.customsOrderSegments.find { it.type == segmentType }

    fun hasAutomaticSegment(): Boolean = this.customsOrderSegments.any { it.automatic ?: false }

    fun hasArchivedDocuments(): Boolean = this.customsOrderDocuments.any { !it.archiveReference.isNullOrBlank() }

    fun hasStatus(status: OrderStatus): Boolean = this.customsOrderStatus != null && this.customsOrderStatus == status

    fun isCancelable(): Boolean = (OrderStatus.STATUS_FINISHED != this.customsOrderStatus
            && OrderStatus.STATUS_CANCELLED != this.customsOrderStatus
            && OrderStatus.STATUS_REJECTED != this.customsOrderStatus
            && OrderStatus.STATUS_CONSOLIDATED != this.customsOrderStatus)

    fun isReopenable(): Boolean = OrderStatus.STATUS_CANCELLED == this.customsOrderStatus

    fun isStartable(): Boolean = (CollectionUtils.isNotEmpty(this.customsOrderSegments)
            && OrderStatus.STATUS_COMPLIANCE_CHECK_FAILED != this.customsOrderStatus
            && OrderStatus.STATUS_FINISHED != this.customsOrderStatus
            && OrderStatus.STATUS_CANCELLED != this.customsOrderStatus
            && OrderStatus.STATUS_REJECTED != this.customsOrderStatus
            && consolidatedToCustomsOrderId == null)

    fun isTransmittable(): Boolean = isStartable()

    fun isConsolidatable(): Boolean =
        (consolidatedToCustomsOrderId == null && java.lang.Boolean.TRUE != consolidatedOrder
                && hasStatus(OrderStatus.STATUS_NEW))



    override fun toString(): String =
        "CustomsOrder(workflowId=$workflowId, customsOrderNo=$customsOrderNo, shipmentNo=$shipmentNo, customerEmail=$customerEmail, customerReference=$customerReference, deliveryTermSad20=$deliveryTermSad20, deliveryTermSad20Id=$deliveryTermSad20Id, deliveryTermPlaceSad20=$deliveryTermPlaceSad20, countryOfExportSad15=$countryOfExportSad15, regionCodeOfExportSad15=$regionCodeOfExportSad15, countryOfDestinationSad17=$countryOfDestinationSad17, regionCodeOfDestinationSad17=$regionCodeOfDestinationSad17, countryOfOriginSad16=$countryOfOriginSad16, portOfLoading=$portOfLoading, portOfDestination=$portOfDestination, placeOfLoadingSad27ex=$placeOfLoadingSad27ex, placeOfDischargeSad27im=$placeOfDischargeSad27im, consignorPickupDate=$consignorPickupDate, transportMeansDepartureSad18ex=$transportMeansDepartureSad18ex, transportMeansNationalityDepartureSad18ex=$transportMeansNationalityDepartureSad18ex, transportMeansArrivalSad18im=$transportMeansArrivalSad18im, transportMeansNationalityArrivalSad18im=$transportMeansNationalityArrivalSad18im, transportInContainersSad19=$transportInContainersSad19, mainHaulEta=$mainHaulEta, consigneeDeliveryDate=$consigneeDeliveryDate, consignorOrderNo=$consignorOrderNo, consigneeOrderNo=$consigneeOrderNo, customsOfficeOfExitSad29ex=$customsOfficeOfExitSad29ex, customsOfficeOfEntrySad29im=$customsOfficeOfEntrySad29im, customsOfficeOfArrival=$customsOfficeOfArrival, customsOfficeOfDeparture=$customsOfficeOfDeparture, totalPackagesSad06=$totalPackagesSad06, declarantReferenceNoSad07=$declarantReferenceNoSad07, countryFirstDestinationSad10ex=$countryFirstDestinationSad10ex, countryLastLoadSad10im=$countryLastLoadSad10im, purchaseCountrySad11=$purchaseCountrySad11, loadListCountSad04=$loadListCountSad04, valueDetailsSad12=$valueDetailsSad12, totalAmountInvoicedSad22=$totalAmountInvoicedSad22, totalAmountInvoicedCurrencySad22=$totalAmountInvoicedCurrencySad22, totalGrossMass=$totalGrossMass, leadOffice=$leadOffice, transportMeansAtBorderSad21ex=$transportMeansAtBorderSad21ex, transportMeansNationalityAtBorderSad21ex=$transportMeansNationalityAtBorderSad21ex, transportMeansAtBorderSad21im=$transportMeansAtBorderSad21im, transportMeansNationalityAtBorderSad21im=$transportMeansNationalityAtBorderSad21im, customsOrderDatetime=$customsOrderDatetime, natureOfTransactionSad24=$natureOfTransactionSad24, customerCustomerNo=$customerCustomerNo, totalGrossMassUnit=$totalGrossMassUnit, localTransportationChargesAmount=$localTransportationChargesAmount, localTransportationChargesCurrency=$localTransportationChargesCurrency, mainHaulChargesAmount=$mainHaulChargesAmount, mainHaulChargesCurrency=$mainHaulChargesCurrency, insuranceChargesAmount=$insuranceChargesAmount, insuranceChargesCurrency=$insuranceChargesCurrency, otherChargesAmount=$otherChargesAmount, otherChargesCurrency=$otherChargesCurrency, vatChargesAmount=$vatChargesAmount, vatChargesCurrency=$vatChargesCurrency, locationOfGoodsSad30ex=$locationOfGoodsSad30ex, locationOfGoodsSad30t=$locationOfGoodsSad30t, locationOfGoodsSad30im=$locationOfGoodsSad30im, automatic=$automatic, customsOfficeOfTransitSad51t=$customsOfficeOfTransitSad51t, typeOfDeclaration=$typeOfDeclaration, interfaceVersion=$interfaceVersion, firstDefermentApprovalNumberCodeSad48=$firstDefermentApprovalNumberCodeSad48, firstDefermentApprovalNumberSad48=$firstDefermentApprovalNumberSad48, secondDefermentApprovalNumberCodeSad48=$secondDefermentApprovalNumberCodeSad48, secondDefermentApprovalNumberSad48=$secondDefermentApprovalNumberSad48, customsOfficeOfTransit2Sad51t=$customsOfficeOfTransit2Sad51t, customsOfficeOfTransit3Sad51t=$customsOfficeOfTransit3Sad51t, customsOfficeOfTransit4Sad51t=$customsOfficeOfTransit4Sad51t, customsOfficeOfTransit5Sad51t=$customsOfficeOfTransit5Sad51t, customsOfficeOfTransit6Sad51t=$customsOfficeOfTransit6Sad51t, preference=$preference, information=$information, importWay=$importWay, sooOnInvoice=$sooOnInvoice, transportMeansTransitSad18t=$transportMeansTransitSad18t, transportMeansNationalityTransitSad18t=$transportMeansNationalityTransitSad18t, transportMeansAtBorderSad21t=$transportMeansAtBorderSad21t, transportMeansNationalityAtBorderSad21t=$transportMeansNationalityAtBorderSad21t, consolidatedToCustomsOrderNo=$consolidatedToCustomsOrderNo, consolidatedToCustomsOrderId=$consolidatedToCustomsOrderId, consolidatedOrder=$consolidatedOrder, customsOrderStatus=$customsOrderStatus, modeOfTransportAtBorderSad25ex=$modeOfTransportAtBorderSad25ex, modeOfTransportInlandSad26ex=$modeOfTransportInlandSad26ex, modeOfTransportAtBorderSad25im=$modeOfTransportAtBorderSad25im, modeOfTransportInlandSad26im=$modeOfTransportInlandSad26im, modeOfTransportAtBorderSad25t=$modeOfTransportAtBorderSad25t, modeOfTransportInlandSad26t=$modeOfTransportInlandSad26t)"

    override fun retrieveCustomsOrderId() = id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomsOrder) return false

        if (id != other.id) return false
        if (workflowId != other.workflowId) return false
        if (customsOrderNo != other.customsOrderNo) return false
        if (shipmentNo != other.shipmentNo) return false
        if (customerEmail != other.customerEmail) return false
        if (customerReference != other.customerReference) return false
        if (deliveryTermSad20 != other.deliveryTermSad20) return false
        if (deliveryTermSad20Id != other.deliveryTermSad20Id) return false
        if (deliveryTermPlaceSad20 != other.deliveryTermPlaceSad20) return false
        if (countryOfExportSad15 != other.countryOfExportSad15) return false
        if (regionCodeOfExportSad15 != other.regionCodeOfExportSad15) return false
        if (countryOfDestinationSad17 != other.countryOfDestinationSad17) return false
        if (regionCodeOfDestinationSad17 != other.regionCodeOfDestinationSad17) return false
        if (countryOfOriginSad16 != other.countryOfOriginSad16) return false
        if (portOfLoading != other.portOfLoading) return false
        if (portOfDestination != other.portOfDestination) return false
        if (placeOfLoadingSad27ex != other.placeOfLoadingSad27ex) return false
        if (placeOfDischargeSad27im != other.placeOfDischargeSad27im) return false
        if (consignorPickupDate != other.consignorPickupDate) return false
        if (transportMeansDepartureSad18ex != other.transportMeansDepartureSad18ex) return false
        if (transportMeansNationalityDepartureSad18ex != other.transportMeansNationalityDepartureSad18ex) return false
        if (transportMeansArrivalSad18im != other.transportMeansArrivalSad18im) return false
        if (transportMeansNationalityArrivalSad18im != other.transportMeansNationalityArrivalSad18im) return false
        if (transportInContainersSad19 != other.transportInContainersSad19) return false
        if (mainHaulEta != other.mainHaulEta) return false
        if (consigneeDeliveryDate != other.consigneeDeliveryDate) return false
        if (consignorOrderNo != other.consignorOrderNo) return false
        if (consigneeOrderNo != other.consigneeOrderNo) return false
        if (customsOfficeOfExitSad29ex != other.customsOfficeOfExitSad29ex) return false
        if (customsOfficeOfEntrySad29im != other.customsOfficeOfEntrySad29im) return false
        if (customsOfficeOfArrival != other.customsOfficeOfArrival) return false
        if (customsOfficeOfDeparture != other.customsOfficeOfDeparture) return false
        if (totalPackagesSad06 != other.totalPackagesSad06) return false
        if (declarantReferenceNoSad07 != other.declarantReferenceNoSad07) return false
        if (countryFirstDestinationSad10ex != other.countryFirstDestinationSad10ex) return false
        if (countryLastLoadSad10im != other.countryLastLoadSad10im) return false
        if (purchaseCountrySad11 != other.purchaseCountrySad11) return false
        if (loadListCountSad04 != other.loadListCountSad04) return false
        if (valueDetailsSad12 != other.valueDetailsSad12) return false
        if (totalAmountInvoicedSad22 != other.totalAmountInvoicedSad22) return false
        if (totalAmountInvoicedCurrencySad22 != other.totalAmountInvoicedCurrencySad22) return false
        if (totalGrossMass != other.totalGrossMass) return false
        if (transportMeansAtBorderSad21ex != other.transportMeansAtBorderSad21ex) return false
        if (transportMeansNationalityAtBorderSad21ex != other.transportMeansNationalityAtBorderSad21ex) return false
        if (transportMeansAtBorderSad21im != other.transportMeansAtBorderSad21im) return false
        if (transportMeansNationalityAtBorderSad21im != other.transportMeansNationalityAtBorderSad21im) return false
        if (customsOrderDatetime != other.customsOrderDatetime) return false
        if (natureOfTransactionSad24 != other.natureOfTransactionSad24) return false
        if (customerCustomerNo != other.customerCustomerNo) return false
        if (totalGrossMassUnit != other.totalGrossMassUnit) return false
        if (localTransportationChargesAmount != other.localTransportationChargesAmount) return false
        if (localTransportationChargesCurrency != other.localTransportationChargesCurrency) return false
        if (mainHaulChargesAmount != other.mainHaulChargesAmount) return false
        if (mainHaulChargesCurrency != other.mainHaulChargesCurrency) return false
        if (insuranceChargesAmount != other.insuranceChargesAmount) return false
        if (insuranceChargesCurrency != other.insuranceChargesCurrency) return false
        if (otherChargesAmount != other.otherChargesAmount) return false
        if (otherChargesCurrency != other.otherChargesCurrency) return false
        if (vatChargesAmount != other.vatChargesAmount) return false
        if (vatChargesCurrency != other.vatChargesCurrency) return false
        if (locationOfGoodsSad30ex != other.locationOfGoodsSad30ex) return false
        if (locationOfGoodsSad30t != other.locationOfGoodsSad30t) return false
        if (locationOfGoodsSad30im != other.locationOfGoodsSad30im) return false
        if (automatic != other.automatic) return false
        if (customsOfficeOfTransitSad51t != other.customsOfficeOfTransitSad51t) return false
        if (typeOfDeclaration != other.typeOfDeclaration) return false
        if (interfaceVersion != other.interfaceVersion) return false
        if (firstDefermentApprovalNumberCodeSad48 != other.firstDefermentApprovalNumberCodeSad48) return false
        if (firstDefermentApprovalNumberSad48 != other.firstDefermentApprovalNumberSad48) return false
        if (secondDefermentApprovalNumberCodeSad48 != other.secondDefermentApprovalNumberCodeSad48) return false
        if (secondDefermentApprovalNumberSad48 != other.secondDefermentApprovalNumberSad48) return false
        if (customsOfficeOfTransit2Sad51t != other.customsOfficeOfTransit2Sad51t) return false
        if (customsOfficeOfTransit3Sad51t != other.customsOfficeOfTransit3Sad51t) return false
        if (customsOfficeOfTransit4Sad51t != other.customsOfficeOfTransit4Sad51t) return false
        if (customsOfficeOfTransit5Sad51t != other.customsOfficeOfTransit5Sad51t) return false
        if (customsOfficeOfTransit6Sad51t != other.customsOfficeOfTransit6Sad51t) return false
        if (preference != other.preference) return false
        if (information != other.information) return false
        if (importWay != other.importWay) return false
        if (sooOnInvoice != other.sooOnInvoice) return false
        if (transportMeansTransitSad18t != other.transportMeansTransitSad18t) return false
        if (transportMeansNationalityTransitSad18t != other.transportMeansNationalityTransitSad18t) return false
        if (transportMeansAtBorderSad21t != other.transportMeansAtBorderSad21t) return false
        if (transportMeansNationalityAtBorderSad21t != other.transportMeansNationalityAtBorderSad21t) return false
        if (consolidatedToCustomsOrderNo != other.consolidatedToCustomsOrderNo) return false
        if (consolidatedToCustomsOrderId != other.consolidatedToCustomsOrderId) return false
        if (consolidatedOrder != other.consolidatedOrder) return false
        if (customsOrderStatus != other.customsOrderStatus) return false
        if (modeOfTransportAtBorderSad25ex != other.modeOfTransportAtBorderSad25ex) return false
        if (modeOfTransportInlandSad26ex != other.modeOfTransportInlandSad26ex) return false
        if (modeOfTransportAtBorderSad25im != other.modeOfTransportAtBorderSad25im) return false
        if (modeOfTransportInlandSad26im != other.modeOfTransportInlandSad26im) return false
        if (modeOfTransportAtBorderSad25t != other.modeOfTransportAtBorderSad25t) return false
        if (modeOfTransportInlandSad26t != other.modeOfTransportInlandSad26t) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (workflowId?.hashCode() ?: 0)
        result = 31 * result + (customsOrderNo?.hashCode() ?: 0)
        result = 31 * result + (shipmentNo?.hashCode() ?: 0)
        result = 31 * result + (customerEmail?.hashCode() ?: 0)
        result = 31 * result + (customerReference?.hashCode() ?: 0)
        result = 31 * result + (deliveryTermSad20?.hashCode() ?: 0)
        result = 31 * result + (deliveryTermSad20Id?.hashCode() ?: 0)
        result = 31 * result + (deliveryTermPlaceSad20?.hashCode() ?: 0)
        result = 31 * result + (countryOfExportSad15?.hashCode() ?: 0)
        result = 31 * result + (regionCodeOfExportSad15?.hashCode() ?: 0)
        result = 31 * result + (countryOfDestinationSad17?.hashCode() ?: 0)
        result = 31 * result + (regionCodeOfDestinationSad17?.hashCode() ?: 0)
        result = 31 * result + (countryOfOriginSad16?.hashCode() ?: 0)
        result = 31 * result + (portOfLoading?.hashCode() ?: 0)
        result = 31 * result + (portOfDestination?.hashCode() ?: 0)
        result = 31 * result + (placeOfLoadingSad27ex?.hashCode() ?: 0)
        result = 31 * result + (placeOfDischargeSad27im?.hashCode() ?: 0)
        result = 31 * result + (consignorPickupDate?.hashCode() ?: 0)
        result = 31 * result + (transportMeansDepartureSad18ex?.hashCode() ?: 0)
        result = 31 * result + (transportMeansNationalityDepartureSad18ex?.hashCode() ?: 0)
        result = 31 * result + (transportMeansArrivalSad18im?.hashCode() ?: 0)
        result = 31 * result + (transportMeansNationalityArrivalSad18im?.hashCode() ?: 0)
        result = 31 * result + (transportInContainersSad19?.hashCode() ?: 0)
        result = 31 * result + (mainHaulEta?.hashCode() ?: 0)
        result = 31 * result + (consigneeDeliveryDate?.hashCode() ?: 0)
        result = 31 * result + (consignorOrderNo?.hashCode() ?: 0)
        result = 31 * result + (consigneeOrderNo?.hashCode() ?: 0)
        result = 31 * result + (customsOfficeOfExitSad29ex?.hashCode() ?: 0)
        result = 31 * result + (customsOfficeOfEntrySad29im?.hashCode() ?: 0)
        result = 31 * result + (customsOfficeOfArrival?.hashCode() ?: 0)
        result = 31 * result + (customsOfficeOfDeparture?.hashCode() ?: 0)
        result = 31 * result + (totalPackagesSad06 ?: 0)
        result = 31 * result + (declarantReferenceNoSad07?.hashCode() ?: 0)
        result = 31 * result + (countryFirstDestinationSad10ex?.hashCode() ?: 0)
        result = 31 * result + (countryLastLoadSad10im?.hashCode() ?: 0)
        result = 31 * result + (purchaseCountrySad11?.hashCode() ?: 0)
        result = 31 * result + (loadListCountSad04 ?: 0)
        result = 31 * result + (valueDetailsSad12?.hashCode() ?: 0)
        result = 31 * result + (totalAmountInvoicedSad22?.hashCode() ?: 0)
        result = 31 * result + (totalAmountInvoicedCurrencySad22?.hashCode() ?: 0)
        result = 31 * result + (totalGrossMass?.hashCode() ?: 0)
        result = 31 * result + (transportMeansAtBorderSad21ex?.hashCode() ?: 0)
        result = 31 * result + (transportMeansNationalityAtBorderSad21ex?.hashCode() ?: 0)
        result = 31 * result + (transportMeansAtBorderSad21im?.hashCode() ?: 0)
        result = 31 * result + (transportMeansNationalityAtBorderSad21im?.hashCode() ?: 0)
        result = 31 * result + (customsOrderDatetime?.hashCode() ?: 0)
        result = 31 * result + (natureOfTransactionSad24?.hashCode() ?: 0)
        result = 31 * result + (customerCustomerNo?.hashCode() ?: 0)
        result = 31 * result + (totalGrossMassUnit?.hashCode() ?: 0)
        result = 31 * result + (localTransportationChargesAmount?.hashCode() ?: 0)
        result = 31 * result + (localTransportationChargesCurrency?.hashCode() ?: 0)
        result = 31 * result + (mainHaulChargesAmount?.hashCode() ?: 0)
        result = 31 * result + (mainHaulChargesCurrency?.hashCode() ?: 0)
        result = 31 * result + (insuranceChargesAmount?.hashCode() ?: 0)
        result = 31 * result + (insuranceChargesCurrency?.hashCode() ?: 0)
        result = 31 * result + (otherChargesAmount?.hashCode() ?: 0)
        result = 31 * result + (otherChargesCurrency?.hashCode() ?: 0)
        result = 31 * result + (vatChargesAmount?.hashCode() ?: 0)
        result = 31 * result + (vatChargesCurrency?.hashCode() ?: 0)
        result = 31 * result + (locationOfGoodsSad30ex?.hashCode() ?: 0)
        result = 31 * result + (locationOfGoodsSad30t?.hashCode() ?: 0)
        result = 31 * result + (locationOfGoodsSad30im?.hashCode() ?: 0)
        result = 31 * result + (automatic?.hashCode() ?: 0)
        result = 31 * result + (customsOfficeOfTransitSad51t?.hashCode() ?: 0)
        result = 31 * result + (typeOfDeclaration?.hashCode() ?: 0)
        result = 31 * result + (interfaceVersion?.hashCode() ?: 0)
        result = 31 * result + (firstDefermentApprovalNumberCodeSad48?.hashCode() ?: 0)
        result = 31 * result + (firstDefermentApprovalNumberSad48?.hashCode() ?: 0)
        result = 31 * result + (secondDefermentApprovalNumberCodeSad48?.hashCode() ?: 0)
        result = 31 * result + (secondDefermentApprovalNumberSad48?.hashCode() ?: 0)
        result = 31 * result + (customsOfficeOfTransit2Sad51t?.hashCode() ?: 0)
        result = 31 * result + (customsOfficeOfTransit3Sad51t?.hashCode() ?: 0)
        result = 31 * result + (customsOfficeOfTransit4Sad51t?.hashCode() ?: 0)
        result = 31 * result + (customsOfficeOfTransit5Sad51t?.hashCode() ?: 0)
        result = 31 * result + (customsOfficeOfTransit6Sad51t?.hashCode() ?: 0)
        result = 31 * result + (customer?.hashCode() ?: 0)
        result = 31 * result + (preference?.hashCode() ?: 0)
        result = 31 * result + (information?.hashCode() ?: 0)
        result = 31 * result + (importWay?.hashCode() ?: 0)
        result = 31 * result + (sooOnInvoice?.hashCode() ?: 0)
        result = 31 * result + (transportMeansTransitSad18t?.hashCode() ?: 0)
        result = 31 * result + (transportMeansNationalityTransitSad18t?.hashCode() ?: 0)
        result = 31 * result + (transportMeansAtBorderSad21t?.hashCode() ?: 0)
        result = 31 * result + (transportMeansNationalityAtBorderSad21t?.hashCode() ?: 0)
        result = 31 * result + (consolidatedToCustomsOrderNo?.hashCode() ?: 0)
        result = 31 * result + (consolidatedToCustomsOrderId?.hashCode() ?: 0)
        result = 31 * result + (consolidatedOrder?.hashCode() ?: 0)
        result = 31 * result + (customsOrderStatus?.hashCode() ?: 0)
        result = 31 * result + (modeOfTransportAtBorderSad25ex?.hashCode() ?: 0)
        result = 31 * result + (modeOfTransportInlandSad26ex?.hashCode() ?: 0)
        result = 31 * result + (modeOfTransportAtBorderSad25im?.hashCode() ?: 0)
        result = 31 * result + (modeOfTransportInlandSad26im?.hashCode() ?: 0)
        result = 31 * result + (modeOfTransportAtBorderSad25t?.hashCode() ?: 0)
        result = 31 * result + (modeOfTransportInlandSad26t?.hashCode() ?: 0)
        return result
    }

}
