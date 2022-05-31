package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "customs_order_position_additional_information")
@Audited(withModifiedFlag = true)
data class CustomsOrderPositionAdditionalInformation(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_position_id", nullable = false)
    var customsOrderPosition: CustomsOrderPosition? = null,

    @Column(name = "type", length = 2)
    var type: String? = null,

    @Column(name = "code", length = 15)
    var code: String? = null,

    @Column(name = "status", length = 15)
    var status: String? = null,

    @Column(name = "reference", length = 35)
    var reference: String? = null,

    @Column(name = "quantity")
    var quantity: Int? = null,

    @Column(name = "reason", length = 70)
    var reason: String? = null,

    @Column(name = "text", length = 512)
    var text: String? = null,

    @Convert(disableConversion = true)
    @Column(name = "date")
    var date: LocalDate? = null,

    @Column(name = "measurement_unit", length = 5)
    var measurementUnit: String? = null,

    @Column(name = "amount", precision = 19, scale = 2)
    var amount: BigDecimal? = null,

    @Column(name = "currency", length = 3)
    var currency: String? = null,
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrderPosition?.customsOrder?.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomsOrderPositionAdditionalInformation) return false

        if (id != other.id) return false
        if (type != other.type) return false
        if (code != other.code) return false
        if (status != other.status) return false
        if (reference != other.reference) return false
        if (quantity != other.quantity) return false
        if (reason != other.reason) return false
        if (text != other.text) return false
        if (date != other.date) return false
        if (measurementUnit != other.measurementUnit) return false
        if (amount != other.amount) return false
        if (currency != other.currency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (code?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (reference?.hashCode() ?: 0)
        result = 31 * result + (quantity ?: 0)
        result = 31 * result + (reason?.hashCode() ?: 0)
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + (measurementUnit?.hashCode() ?: 0)
        result = 31 * result + (amount?.hashCode() ?: 0)
        result = 31 * result + (currency?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "CustomsOrderPositionAdditionalInformation(type=$type, code=$code, status=$status, reference=$reference, quantity=$quantity, reason=$reason, text=$text, date=$date, measurementUnit=$measurementUnit, amount=$amount, currency=$currency)"
}
