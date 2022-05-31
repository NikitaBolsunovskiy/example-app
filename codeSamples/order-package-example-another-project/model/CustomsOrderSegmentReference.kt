package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.converter.SegmentReferenceTypeConverter
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.hibernate.envers.Audited
import javax.persistence.*

@Entity
@Table(name = "customs_order_segment_reference")
@Audited(withModifiedFlag = true)
class CustomsOrderSegmentReference(

    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_segment_id", nullable = false)
    var segment: CustomsOrderSegment? = null,

    @Column(name = "reference")
    var reference: String? = null,

    @Column(name = "reference_type")
    @Convert(converter = SegmentReferenceTypeConverter::class)
    var referenceType: CustomsOrderSegmentReferenceType? = null,
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = segment?.customsOrder?.id
}
