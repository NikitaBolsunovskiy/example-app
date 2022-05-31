package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.documenttype.model.DocumentType
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import org.hibernate.envers.RelationTargetAuditMode
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "customs_order_document")
@Audited(withModifiedFlag = true)
data class CustomsOrderDocument(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_id", nullable = false)
    var customsOrder: CustomsOrder? = null,

    @Column(name = "document_no", length = 70)
    var documentNo: String? = null,

    @Convert(disableConversion = true)
    @Column(name = "document_date")
    var documentDate: LocalDate? = null,

    @Column(name = "document_filename", length = 70)
    var documentFilename: String? = null,

    @Column(name = "document_code", length = 15)
    var documentCode: String? = null,

    @Column(name = "document_reference", length = 70)
    var documentReference: String? = null,

    @Column(name = "document_description", length = 512)
    var documentDescription: String? = null,

    @Column(name = "document_segment", length = 15)
    var documentSegment: String? = null,

    @Column(name = "direct_link", length = 512)
    var directLink: String? = null,

    @Column(name = "document_special_information", length = 35)
    var documentSpecialInformation: String? = null,

    @Column(name = "archive_reference", length = 512)
    var archiveReference: String? = null,

    @Column(name = "archive_error")
    var archiveError: Boolean? = null,

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    var documentType: DocumentType? = null,
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrder?.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomsOrderDocument) return false

        if (id != other.id) return false
        if (documentNo != other.documentNo) return false
        if (documentDate != other.documentDate) return false
        if (documentFilename != other.documentFilename) return false
        if (documentCode != other.documentCode) return false
        if (documentReference != other.documentReference) return false
        if (documentDescription != other.documentDescription) return false
        if (documentSegment != other.documentSegment) return false
        if (directLink != other.directLink) return false
        if (documentSpecialInformation != other.documentSpecialInformation) return false
        if (archiveReference != other.archiveReference) return false
        if (archiveError != other.archiveError) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (documentNo?.hashCode() ?: 0)
        result = 31 * result + (documentDate?.hashCode() ?: 0)
        result = 31 * result + (documentFilename?.hashCode() ?: 0)
        result = 31 * result + (documentCode?.hashCode() ?: 0)
        result = 31 * result + (documentReference?.hashCode() ?: 0)
        result = 31 * result + (documentDescription?.hashCode() ?: 0)
        result = 31 * result + (documentSegment?.hashCode() ?: 0)
        result = 31 * result + (directLink?.hashCode() ?: 0)
        result = 31 * result + (documentSpecialInformation?.hashCode() ?: 0)
        result = 31 * result + (archiveReference?.hashCode() ?: 0)
        result = 31 * result + (archiveError?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "CustomsOrderDocument(documentNo=$documentNo, documentDate=$documentDate, documentFilename=$documentFilename, documentCode=$documentCode, documentReference=$documentReference, documentDescription=$documentDescription, documentSegment=$documentSegment, directLink=$directLink, documentSpecialInformation=$documentSpecialInformation, archiveReference=$archiveReference, archiveError=$archiveError)"
}
