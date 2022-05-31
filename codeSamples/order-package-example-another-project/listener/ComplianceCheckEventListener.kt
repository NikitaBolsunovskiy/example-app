package listener

import de.rhenus.scs.customs.common.events.CctApplicationEvent
import de.rhenus.scs.customs.notification.model.Notification
import de.rhenus.scs.customs.notification.model.NotificationDto
import de.rhenus.scs.customs.notification.publisher.NotificationPublisher
import de.rhenus.scs.customs.order.event.AddressComplianceCheckDisabledEvent
import de.rhenus.scs.customs.order.event.AddressComplianceCheckFailedEvent
import de.rhenus.scs.customs.order.event.AddressComplianceCheckNotPossibleEvent
import de.rhenus.scs.customs.order.event.AddressComplianceCheckSolvedEvent
import de.rhenus.scs.customs.order.model.ComplianceCheckableAddress
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener
import java.time.ZoneOffset

@Service
class ComplianceCheckEventListener(
    private val notificationPublisher: NotificationPublisher
) {
    @TransactionalEventListener
    fun handleComplianceCheckNotPossible(event: AddressComplianceCheckNotPossibleEvent) {
        notificationPublisher.send(
            buildNotification(
                event,
                event.address,
                Notification.NOTIFICATION_TYPE_WARNING
            )
        )
    }

    @TransactionalEventListener
    fun handleComplianceCheckFailed(event: AddressComplianceCheckFailedEvent) {
        notificationPublisher.send(
            buildSolvedNotification(
                event,
                AddressComplianceCheckNotPossibleEvent::class.java.simpleName,
                event.address
            )
        )
        notificationPublisher.send(
            buildNotification(
                event,
                event.address,
                Notification.NOTIFICATION_TYPE_ERROR
            )
        )
    }

    @TransactionalEventListener
    fun handleComplianceCheckSolved(event: AddressComplianceCheckSolvedEvent) {
        solve(event, event.address)
    }

    @TransactionalEventListener
    fun handleComplianceCheckDisabled(event: AddressComplianceCheckDisabledEvent) {
        solve(event, event.address)
    }

    private fun solve(event: CctApplicationEvent, address: ComplianceCheckableAddress?) {
        notificationPublisher.send(
            buildSolvedNotification(
                event,
                AddressComplianceCheckNotPossibleEvent::class.java.simpleName,
                address
            )
        )
        notificationPublisher.send(
            buildSolvedNotification(
                event,
                AddressComplianceCheckFailedEvent::class.java.simpleName,
                address
            )
        )
    }

    private fun buildSolvedNotification(
        event: CctApplicationEvent,
        solvedEvent: String,
        address: ComplianceCheckableAddress?
    ): NotificationDto {
        val notification = NotificationDto()
        notification.user = if (StringUtils.isNotBlank(event.user)) event.user else "system.check"
        notification.solved = true
        notification.aggregateId = event.aggregateId
        notification.aggregateType = event.aggregateType
        notification.event = solvedEvent
        notification.correlationId = address!!.javaClass.simpleName + "-" + address.id
        return notification
    }

    private fun buildNotification(
        event: CctApplicationEvent,
        address: ComplianceCheckableAddress?,
        type: String
    ): NotificationDto {
        val notification = NotificationDto()
        notification.aggregateId = event.aggregateId
        notification.aggregateType = event.aggregateType
        notification.event = event.javaClass.simpleName
        notification.correlationId = address!!.javaClass.simpleName + "-" + address.id
        notification.type = type
        notification.date = event.created
        notification.user = event.user
        notification.addData("customsOrderAddressId", address.id.toString())
        notification.addData("role", address.role ?: "")
        return notification
    }
}
