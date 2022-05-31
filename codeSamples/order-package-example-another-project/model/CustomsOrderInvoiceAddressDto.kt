package model

import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.util.*

data class CustomsOrderInvoiceAddressDto(
    override var id: Long? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var createdDate: Instant? = null,
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    override var lastModifiedDate: Instant? = null,
    override var lastModifiedBy: String? = null,
    override var createdBy: String? = null,
    override var version: Int? = null,

    override var role: String? = null,
    override var name1: String? = null,
    override var name2: String? = null,
    override var name3: String? = null,
    override var street: String? = null,
    override var houseNo: String? = null,
    override var zipcode: String? = null,
    override var city: String? = null,
    override var province: String? = null,
    override var country: String? = null,
    override var postofficeBox: String? = null,
    override var reference: String? = null,
    override var email: String? = null,
    override var phone: String? = null,
    override var contactName: String? = null,
    override var complianceFailed: Boolean? = null,
    override var complianceReportUrl: String? = null,
    override var complianceDisabled: Boolean? = null,
    override var complianceChecked: Boolean? = null,

    var customsOrderInvoiceId: Long? = null,
    var eoriNo: String? = null,
    var vatNo: String? = null,
) : AddressDto()
