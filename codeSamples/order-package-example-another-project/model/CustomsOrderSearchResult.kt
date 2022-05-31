package model

import java.time.Instant

class CustomsOrderSearchResult(
    var customsOrderId: Long? = null,
    var customsOrderSegmentId: Long? = null,
    var customerReference: String? = null,
    var segmentType: String? = null,
    var mainHaulDate: Instant? = null,
    var arrivalDate: Instant? = null,
    var departureDate: Instant? = null,
    var consignorName1: String? = null,
    var consignorName2: String? = null,
    var consignorName3: String? = null,
    var consigneeName1: String? = null,
    var consigneeName2: String? = null,
    var consigneeName3: String? = null,
    var countryOfDestination: String? = null,
    var countryOfExport: String? = null,
    var dateOfIssue: Instant? = null,
    var customsOrderStatusId: Long? = null,
    var customsOrderSegmentStatusId: Long? = null,
    var locationIdent: String? = null,
    var localCustomsAppId: String? = null,
    var transmitted: Boolean? = null,
)
