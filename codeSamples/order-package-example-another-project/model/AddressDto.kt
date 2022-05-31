package model

import de.rhenus.scs.customs.common.model.BaseDto

abstract class AddressDto : BaseDto() {
    abstract var role: String?
    abstract var name1: String?
    abstract var name2: String?
    abstract var name3: String?
    abstract var street: String?
    abstract var houseNo: String?
    abstract var zipcode: String?
    abstract var city: String?
    abstract var province: String?
    abstract var country: String?
    abstract var postofficeBox: String?
    abstract var reference: String?
    abstract var email: String?
    abstract var phone: String?
    abstract var contactName: String?
    abstract var complianceFailed: Boolean?
    abstract var complianceReportUrl: String?
    abstract var complianceDisabled: Boolean?
    abstract var complianceChecked: Boolean?
}
