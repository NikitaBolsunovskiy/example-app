package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "customs_order_invoice")
@Audited(withModifiedFlag = true)
data class CustomsOrderInvoice(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_id", nullable = false)
    var customsOrder: CustomsOrder? = null,

    @Column(name = "invoice_no", length = 35)
    var invoiceNo: String? = null,

    @Convert(disableConversion = true)
    @Column(name = "invoice_date")
    var invoiceDate: LocalDate? = null,

    @Column(name = "invoice_amount", precision = 19, scale = 2)
    var invoiceAmount: BigDecimal? = null,

    @Column(name = "invoice_currency", length = 3)
    var invoiceCurrency: String? = null,

    @Column(name = "delivery_note_no", length = 35)
    var deliveryNoteNo: String? = null,

    @Convert(disableConversion = true)
    @Column(name = "delivery_note_date")
    var deliveryNoteDate: LocalDate? = null,

    @Column(name = "order_no", length = 35)
    var orderNo: String? = null,

    @Column(name = "order_reference", length = 35)
    var orderReference: String? = null,

    @Convert(disableConversion = true)
    @Column(name = "order_date")
    var orderDate: LocalDate? = null,

    @Column(name = "invoice_reference", length = 35)
    var invoiceReference: String? = null,

    @Column(name = "invoice_code", length = 15)
    var invoiceCode: String? = null,

    @OneToMany(mappedBy = "customsOrderInvoice", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderInvoiceAddresses: MutableList<CustomsOrderInvoiceAddress> = mutableListOf(),
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrder?.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomsOrderInvoice) return false

        if (id != other.id) return false
        if (invoiceNo != other.invoiceNo) return false
        if (invoiceDate != other.invoiceDate) return false
        if (invoiceAmount != other.invoiceAmount) return false
        if (invoiceCurrency != other.invoiceCurrency) return false
        if (deliveryNoteNo != other.deliveryNoteNo) return false
        if (deliveryNoteDate != other.deliveryNoteDate) return false
        if (orderNo != other.orderNo) return false
        if (orderReference != other.orderReference) return false
        if (orderDate != other.orderDate) return false
        if (invoiceReference != other.invoiceReference) return false
        if (invoiceCode != other.invoiceCode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (invoiceNo?.hashCode() ?: 0)
        result = 31 * result + (invoiceDate?.hashCode() ?: 0)
        result = 31 * result + (invoiceAmount?.hashCode() ?: 0)
        result = 31 * result + (invoiceCurrency?.hashCode() ?: 0)
        result = 31 * result + (deliveryNoteNo?.hashCode() ?: 0)
        result = 31 * result + (deliveryNoteDate?.hashCode() ?: 0)
        result = 31 * result + (orderNo?.hashCode() ?: 0)
        result = 31 * result + (orderReference?.hashCode() ?: 0)
        result = 31 * result + (orderDate?.hashCode() ?: 0)
        result = 31 * result + (invoiceReference?.hashCode() ?: 0)
        result = 31 * result + (invoiceCode?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "CustomsOrderInvoice(invoiceNo=$invoiceNo, invoiceDate=$invoiceDate, invoiceAmount=$invoiceAmount, invoiceCurrency=$invoiceCurrency, deliveryNoteNo=$deliveryNoteNo, deliveryNoteDate=$deliveryNoteDate, orderNo=$orderNo, orderReference=$orderReference, orderDate=$orderDate, invoiceReference=$invoiceReference, invoiceCode=$invoiceCode)"


}
