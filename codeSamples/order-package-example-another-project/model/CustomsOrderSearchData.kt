package model

import de.rhenus.scs.customs.common.model.BaseDto.Companion.DATE_TIME_FORMAT
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.util.*

class CustomsOrderSearchData(
    var quickSearch: Boolean? = null,
    var customsOrderNo: String? = null,
    var consolidatedCustomsOrderNo: String? = null,
    var showCancelled: Boolean? = null,
    var showFinished: Boolean? = null,
    var showRejected: Boolean? = null,
    var showConsolidated: Boolean? = null,
    var role: String? = null,
    var orderStatus: List<Long> = ArrayList(),
    var segmentStatus: List<Long> = ArrayList(),
    var offices: List<Long> = ArrayList(),

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    var customsOrderDateTimeFrom: Instant? = null,

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    var customsOrderDateTimeTo: Instant? = null,
    var countriesOfExport: List<String> = ArrayList(),
    var countriesOfDestination: List<String> = ArrayList(),
    var customer: String? = null,
    var consignor: String? = null,

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    var consignorPickupDateFrom: Instant? = null,

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    var consignorPickupDateTo: Instant? = null,
    var consignee: String? = null,

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    var consigneeDeliveryDateFrom: Instant? = null,

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    var consigneeDeliveryDateTo: Instant? = null,
    var portOfLoading: String? = null,
    var portOfDestination: String? = null,
    var customerReference: String? = null,
    var localCustomsAppId: String? = null,
    var shipmentNumber: String? = null,
    var locationOfGoods: String? = null,
    var transportInContainers: Boolean? = null,
    var unitNo: String? = null,
    var invoiceNo: String? = null,
)
