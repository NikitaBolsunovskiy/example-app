package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import javax.persistence.*

@Entity
@Table(name = "customs_order_position_previous_document")
@Audited(withModifiedFlag = true)
data class CustomsOrderPositionPreviousDocument(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_position_id", nullable = false)
    var customsOrderPosition: CustomsOrderPosition? = null,

    @Column(name = "type", length = 1)
    var type: String? = null,

    @Column(name = "code", length = 5)
    var code: String? = null,

    @Column(name = "reference", length = 70)
    var reference: String? = null,
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrderPosition?.customsOrder?.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomsOrderPositionPreviousDocument) return false

        if (id != other.id) return false
        if (type != other.type) return false
        if (code != other.code) return false
        if (reference != other.reference) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (code?.hashCode() ?: 0)
        result = 31 * result + (reference?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "CustomsOrderPositionPreviousDocument(type=$type, code=$code, reference=$reference)"
}
