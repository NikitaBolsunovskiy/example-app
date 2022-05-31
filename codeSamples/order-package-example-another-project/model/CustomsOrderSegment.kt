package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.location.model.Location
import de.rhenus.scs.customs.order.converter.OrderSegmentStatusConverter
import de.rhenus.scs.customs.order.converter.OrderSegmentTypeConverter
import de.rhenus.scs.customs.order.converter.OrderStatusConverter
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "customs_order_segment")
@Audited(withModifiedFlag = true)
data class CustomsOrderSegment(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_id", nullable = false)
    var customsOrder: CustomsOrder? = null,

    @Column(name = "sequential_no")
    var sequentialNo: Int? = null,

    @Column(name = "type", length = 15)
    @Convert(converter = OrderSegmentTypeConverter::class)
    var type: SegmentType? = null,

    @ManyToOne
    @JoinColumn(name = "location_id")
    @NotAudited
    var location: Location? = null,

    @Column(name = "local_customs_app_id", length = 35)
    var localCustomsAppId: String? = null,

    @Column(name = "customs_reference_no", length = 35)
    var customsReferenceNo: String? = null,

    @Column(name = "status_id")
    @Convert(converter = OrderSegmentStatusConverter::class)
    var status: SegmentStatus? = null,

    @Column(name = "cancelled_by_customs")
    var cancelledByCustoms: Boolean? = null,

    @Column(name = "transmitted")
    var transmitted: Boolean? = null,

    @Column(name = "automatic")
    var automatic: Boolean? = null,

    @Column(name = "customs_clearance_date_time")
    var customsClearanceDateTime: Instant? = null,

    @OneToMany(mappedBy = "segment", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderSegmentStatuses: MutableList<CustomsOrderSegmentStatus> = mutableListOf(),

    @OneToMany(mappedBy = "segment", cascade = [CascadeType.ALL])
    @NotAudited
    var customsOrderSegmentReferences: MutableList<CustomsOrderSegmentReference> = mutableListOf(),
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrder?.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomsOrderSegment

        if (id != other.id) return false
        if (sequentialNo != other.sequentialNo) return false
        if (type != other.type) return false
        if (location != other.location) return false
        if (localCustomsAppId != other.localCustomsAppId) return false
        if (customsReferenceNo != other.customsReferenceNo) return false
        if (status != other.status) return false
        if (cancelledByCustoms != other.cancelledByCustoms) return false
        if (transmitted != other.transmitted) return false
        if (automatic != other.automatic) return false
        if (customsClearanceDateTime != other.customsClearanceDateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (sequentialNo ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (localCustomsAppId?.hashCode() ?: 0)
        result = 31 * result + (customsReferenceNo?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (cancelledByCustoms?.hashCode() ?: 0)
        result = 31 * result + (transmitted?.hashCode() ?: 0)
        result = 31 * result + (automatic?.hashCode() ?: 0)
        result = 31 * result + (customsClearanceDateTime?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "CustomsOrderSegment(sequentialNo=$sequentialNo, type=$type, location=$location, localCustomsAppId=$localCustomsAppId, customsReferenceNo=$customsReferenceNo, status=$status, cancelledByCustoms=$cancelledByCustoms, transmitted=$transmitted, automatic=$automatic, customsClearanceDateTime=$customsClearanceDateTime)"
}
