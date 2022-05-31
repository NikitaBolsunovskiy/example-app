package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.converter.OrderStatusConverter
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import javax.persistence.*

@Entity
@Table(name = "customs_order_status")
class CustomsOrderStatus(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_id", nullable = false)
    var customsOrder: CustomsOrder? = null,

    @Column(name = "status_id", nullable = false)
    @Convert(converter = OrderStatusConverter::class)
    var status: OrderStatus? = null,
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrder?.id
}
