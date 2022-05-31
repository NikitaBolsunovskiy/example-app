package model

import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import javax.persistence.*

@Entity
@Table(name = "customs_order_address")
@Audited(withModifiedFlag = true)
data class CustomsOrderAddress(
    override var country: String? = null,
    override var province: String? = null,
    override var city: String? = null,
    override var zipcode: String? = null,
    override var street: String? = null,
    override var houseNo: String? = null,
    override var postofficeBox: String? = null,
    override var name1: String? = null,
    override var name2: String? = null,
    override var name3: String? = null,
    override var contactName: String? = null,
    override var email: String? = null,
    override var phone: String? = null,
    override var role: String? = null,
    override var reference: String? = null,
    override var vatNo: String? = null,
    override var eoriNo: String? = null,
    override var complianceReportUrl: String? = null,
    override var complianceFailed: Boolean? = null,
    override var complianceChecked: Boolean? = null,
    override var complianceDisabled: Boolean? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_id", nullable = false)
    var customsOrder: CustomsOrder? = null,

    @Column(name = "customer_address_no", length = 35)
    var customerAddressNo: String? = null,

    @Column(name = "trader_id", length = 15)
    var traderId: String? = null,

    @Column(name = "unlocode", length = 5)
    var unlocode: String? = null,

    @Column(name = "invoicing_address_no", length = 35)
    var invoicingAddressNo: String? = null,

    @Column(name = "account_no", length = 35)
    var accountNo: String? = null,
) : ComplianceCheckableAddress(
    country,
    province,
    city,
    zipcode,
    street,
    houseNo,
    postofficeBox,
    name1,
    name2,
    name3,
    contactName,
    email,
    phone,
    role,
    reference,
    vatNo,
    eoriNo,
    complianceReportUrl,
    complianceFailed,
    complianceChecked,
    complianceDisabled,
), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrder?.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomsOrderAddress) return false

        if (id != other.id) return false
        if (country != other.country) return false
        if (province != other.province) return false
        if (city != other.city) return false
        if (zipcode != other.zipcode) return false
        if (street != other.street) return false
        if (houseNo != other.houseNo) return false
        if (postofficeBox != other.postofficeBox) return false
        if (name1 != other.name1) return false
        if (name2 != other.name2) return false
        if (name3 != other.name3) return false
        if (contactName != other.contactName) return false
        if (email != other.email) return false
        if (phone != other.phone) return false
        if (role != other.role) return false
        if (reference != other.reference) return false
        if (vatNo != other.vatNo) return false
        if (eoriNo != other.eoriNo) return false
        if (complianceReportUrl != other.complianceReportUrl) return false
        if (complianceFailed != other.complianceFailed) return false
        if (complianceChecked != other.complianceChecked) return false
        if (complianceDisabled != other.complianceDisabled) return false
        if (customerAddressNo != other.customerAddressNo) return false
        if (traderId != other.traderId) return false
        if (unlocode != other.unlocode) return false
        if (invoicingAddressNo != other.invoicingAddressNo) return false
        if (accountNo != other.accountNo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + (province?.hashCode() ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (zipcode?.hashCode() ?: 0)
        result = 31 * result + (street?.hashCode() ?: 0)
        result = 31 * result + (houseNo?.hashCode() ?: 0)
        result = 31 * result + (postofficeBox?.hashCode() ?: 0)
        result = 31 * result + (name1?.hashCode() ?: 0)
        result = 31 * result + (name2?.hashCode() ?: 0)
        result = 31 * result + (name3?.hashCode() ?: 0)
        result = 31 * result + (contactName?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (role?.hashCode() ?: 0)
        result = 31 * result + (reference?.hashCode() ?: 0)
        result = 31 * result + (vatNo?.hashCode() ?: 0)
        result = 31 * result + (eoriNo?.hashCode() ?: 0)
        result = 31 * result + (complianceReportUrl?.hashCode() ?: 0)
        result = 31 * result + (complianceFailed?.hashCode() ?: 0)
        result = 31 * result + (complianceChecked?.hashCode() ?: 0)
        result = 31 * result + (complianceDisabled?.hashCode() ?: 0)
        result = 31 * result + (customerAddressNo?.hashCode() ?: 0)
        result = 31 * result + (traderId?.hashCode() ?: 0)
        result = 31 * result + (unlocode?.hashCode() ?: 0)
        result = 31 * result + (invoicingAddressNo?.hashCode() ?: 0)
        result = 31 * result + (accountNo?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "CustomsOrderAddress(country=$country, province=$province, city=$city, zipcode=$zipcode, street=$street, houseNo=$houseNo, postofficeBox=$postofficeBox, name1=$name1, name2=$name2, name3=$name3, contactName=$contactName, email=$email, phone=$phone, role=$role, reference=$reference, vatNo=$vatNo, eoriNo=$eoriNo, complianceReportUrl=$complianceReportUrl, complianceFailed=$complianceFailed, complianceChecked=$complianceChecked, complianceDisabled=$complianceDisabled, customerAddressNo=$customerAddressNo, traderId=$traderId, unlocode=$unlocode, invoicingAddressNo=$invoicingAddressNo, accountNo=$accountNo)"
}
