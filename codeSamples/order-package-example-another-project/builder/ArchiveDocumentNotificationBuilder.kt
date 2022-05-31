package builder

import de.rhenus.scs.customs.notification.model.NotificationDto
import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderDocument
import de.rhenus.scs.customs.util.CurrentUserUtil
import org.joda.time.DateTime
import org.springframework.util.Assert
import java.time.Instant

class ArchiveDocumentNotificationBuilder(
    private var customsOrderId: Long? = null,
    private var document: CustomsOrderDocument? = null,
    private var event: String? = null,
    private var type: String? = null,
    private var solved: Boolean? = null,
) {

    fun build(): NotificationDto {
        Assert.notNull(customsOrderId, "Must not be null!")
        Assert.notNull(document, "Must not be null!")
        Assert.notNull(document!!.id, "Must not be null!")
        Assert.notNull(event, "Must not be null!")
        Assert.notNull(type, "Must not be null!")
        val notification = NotificationDto()
        notification.aggregateId = customsOrderId
        notification.aggregateType = CustomsOrder::class.java.simpleName
        notification.event = event
        notification.correlationId = "CustomsOrderDocument-" + document!!.id
        notification.type = type
        notification.date = Instant.now()
        notification.user = CurrentUserUtil.currentUserLogin
        notification.data = buildData(document!!).toMutableMap()
        notification.solved = solved!!
        return notification
    }

    private fun buildData(document: CustomsOrderDocument): Map<String, String> {
        val data: MutableMap<String, String> = HashMap()
        data["documentNo"] = document.documentNo ?: ""
        data["documentCode"] = document.documentCode ?: ""
        data["documentType"] = document.documentType?.code ?: ""
        data["documentReference"] = document.documentReference ?: ""
        data["documentFilename"] = document.documentFilename ?: ""
        return data
    }
}
