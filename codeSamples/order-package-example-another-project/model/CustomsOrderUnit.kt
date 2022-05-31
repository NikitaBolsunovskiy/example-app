package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import javax.persistence.*

@Entity
@Table(name = "customs_order_unit")
@Audited(withModifiedFlag = true)
data class CustomsOrderUnit(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_id", nullable = false)
    var customsOrder: CustomsOrder? = null,

    @Column(name = "unit_no", length = 35)
    var unitNo: String? = null,

    @Column(name = "unit_type", length = 10)
    var unitType: String? = null,

    @Column(name = "seal_no", length = 8)
    var sealNo: String? = null,
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrder?.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomsOrderUnit) return false

        if (id != other.id) return false
        if (unitNo != other.unitNo) return false
        if (unitType != other.unitType) return false
        if (sealNo != other.sealNo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (unitNo?.hashCode() ?: 0)
        result = 31 * result + (unitType?.hashCode() ?: 0)
        result = 31 * result + (sealNo?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = "CustomsOrderUnit(unitNo=$unitNo, unitType=$unitType, sealNo=$sealNo)"
}
