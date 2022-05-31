package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.converter.OrderSegmentStatusConverter
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import javax.persistence.*

@Entity
@Table(name = "customs_order_segment_status")
class CustomsOrderSegmentStatus(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_segment_id", nullable = false)
    var segment: CustomsOrderSegment? = null,

    @Column(name = "status_id")
    @Convert(converter = OrderSegmentStatusConverter::class)
    var statusId: SegmentStatus? = null,

    @Column(name = "reminder_sent")
    var reminderSent: Boolean? = null,

    @Column(name = "escalation_sent")
    var escalationSent: Boolean? = null,
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = segment?.customsOrder?.id
}
