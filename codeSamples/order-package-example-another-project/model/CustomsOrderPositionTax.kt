package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "customs_order_position_tax")
@Audited(withModifiedFlag = true)
data class CustomsOrderPositionTax(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_position_id", nullable = false)
    var customsOrderPosition: CustomsOrderPosition? = null,

    @Column(name = "type", length = 3)
    var type: String? = null,

    @Column(name = "tax_base", precision = 19, scale = 2)
    var taxBase: BigDecimal? = null,

    @Column(name = "measurement_unit_code", length = 35)
    var measurementUnitCode: String? = null,

    @Column(name = "rate", length = 15)
    var rate: String? = null,

    @Column(name = "override_code", length = 3)
    var overrideCode: String? = null,

    @Column(name = "amount", precision = 19, scale = 2)
    var amount: BigDecimal? = null,

    @Column(name = "method_of_payment", length = 1)
    var methodOfPayment: String? = null,

    @Column(name = "currency", length = 3)
    var currency: String? = null,
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrderPosition?.customsOrder?.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomsOrderPositionTax) return false

        if (id != other.id) return false
        if (type != other.type) return false
        if (taxBase != other.taxBase) return false
        if (measurementUnitCode != other.measurementUnitCode) return false
        if (rate != other.rate) return false
        if (overrideCode != other.overrideCode) return false
        if (amount != other.amount) return false
        if (methodOfPayment != other.methodOfPayment) return false
        if (currency != other.currency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (taxBase?.hashCode() ?: 0)
        result = 31 * result + (measurementUnitCode?.hashCode() ?: 0)
        result = 31 * result + (rate?.hashCode() ?: 0)
        result = 31 * result + (overrideCode?.hashCode() ?: 0)
        result = 31 * result + (amount?.hashCode() ?: 0)
        result = 31 * result + (methodOfPayment?.hashCode() ?: 0)
        result = 31 * result + (currency?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "CustomsOrderPositionTax(type=$type, taxBase=$taxBase, measurementUnitCode=$measurementUnitCode, rate=$rate, overrideCode=$overrideCode, amount=$amount, methodOfPayment=$methodOfPayment, currency=$currency)"
}
