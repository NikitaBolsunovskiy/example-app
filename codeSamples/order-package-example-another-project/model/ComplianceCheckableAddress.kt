package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import javax.persistence.Column

abstract class ComplianceCheckableAddress(
    @Column(name = "country", length = 3)
    open var country: String? = null,

    @Column(name = "province", length = 70)
    open var province: String? = null,

    @Column(name = "city", length = 70)
    open var city: String? = null,

    @Column(name = "zipcode", length = 10)
    open var zipcode: String? = null,

    @Column(name = "street", length = 100)
    open var street: String? = null,

    @Column(name = "house_no", length = 5)
    open var houseNo: String? = null,

    @Column(name = "postoffice_box", length = 15)
    open var postofficeBox: String? = null,

    @Column(name = "name1", length = 70)
    open var name1: String? = null,

    @Column(name = "name2", length = 70)
    open var name2: String? = null,

    @Column(name = "name3", length = 70)
    open var name3: String? = null,

    @Column(name = "contact_name", length = 70)
    open var contactName: String? = null,

    @Column(name = "email")
    open var email: String? = null,

    @Column(name = "phone", length = 20)
    open var phone: String? = null,

    @Column(name = "role", length = 2)
    open var role: String? = null,

    @Column(name = "reference", length = 15)
    open var reference: String? = null,

    @Column(name = "vat_no", length = 35)
    open var vatNo: String? = null,

    @Column(name = "eori_no", length = 17)
    open var eoriNo: String? = null,

    @Column(name = "compliance_report_url", length = 512)
    open var complianceReportUrl: String? = null,

    @Column(name = "compliance_failed")
    open var complianceFailed: Boolean? = null,

    @Column(name = "compliance_checked")
    open var complianceChecked: Boolean? = null,

    @Column(name = "compliance_disabled")
    open var complianceDisabled: Boolean? = null,
) : OptimisticLockingAuditableEntity() {
    fun hasComplianceRelatedFieldChanged(other: ComplianceCheckableAddress) =
        !name1.equals(other.name1, true)
                || !street.equals(other.street, true)
                || !zipcode.equals(other.zipcode, true)
                || !city.equals(other.city, true)
                || country != other.country
}
