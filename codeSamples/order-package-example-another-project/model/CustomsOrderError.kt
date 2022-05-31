package model

import de.rhenus.scs.customs.common.model.OptimisticLockingAuditableEntity
import de.rhenus.scs.customs.order.model.dependant.CustomsOrderDependant
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "customs_order_error")
class CustomsOrderError(
    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_id", nullable = false)
    var customsOrder: CustomsOrder? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "customs_order_segment_id", nullable = false)
    var customsOrderSegment: CustomsOrderSegment? = null,

    @Column(name = "interface_version", nullable = false, length = 5)
    var interfaceVersion: String? = null,

    @Column(name = "sending_system", nullable = false, length = 35)
    var sendingSystem: String? = null,

    @Column(name = "customs_order_no", nullable = false, length = 35)
    var customsOrderNo: String? = null,

    @Column(name = "type_of_declaration", nullable = false, length = 15)
    var typeOfDeclaration: String? = null,

    @Column(name = "status_id", nullable = false, length = 15)
    var statusId: String? = null,

    @Column(name = "error_date", nullable = false)
    var errorDate: Instant? = null,

    @Column(name = "error_code", length = 35)
    var errorCode: String? = null,

    @Column(name = "error_level", length = 15)
    var errorLevel: String? = null,

    @Column(name = "error_message", columnDefinition = "TEXT") // "LONGVARCHAR"
    var errorMessage: String? = null,

    @Column(name = "confirmed")
    var confirmed: Boolean? = null,

    @Column(name = "confirmed_at")
    var confirmedAt: Instant? = null,

    @Column(name = "solved")
    var solved: Boolean? = null,

    @Column(name = "solved_at")
    var solvedAt: Instant? = null,
) : OptimisticLockingAuditableEntity(), CustomsOrderDependant {
    override fun retrieveCustomsOrderId(): Long? = customsOrder?.id
}
