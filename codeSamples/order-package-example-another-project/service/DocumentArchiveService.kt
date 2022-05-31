package service

import de.rhenus.scs.customs.archive.exception.ArchiveException
import de.rhenus.scs.customs.archive.client.ArchiveClient
import de.rhenus.scs.customs.documenttype.service.DocumentTypeService
import de.rhenus.scs.customs.notification.model.Notification
import de.rhenus.scs.customs.notification.publisher.NotificationPublisher
import de.rhenus.scs.customs.order.builder.ArchiveDocumentNotificationBuilder
import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderDocument
import de.rhenus.scs.customs.order.repository.CustomsOrderDocumentRepository
import de.rhenus.scs.customs.util.service.SFtpService
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.BooleanUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneId

@Service
class DocumentArchiveService(
    private val customsOrderDocumentRepository: CustomsOrderDocumentRepository,
    private val sFtpService: SFtpService,
    private val documentTypeService: DocumentTypeService,
    private val archiveClient: ArchiveClient,
    private val notificationPublisher: NotificationPublisher,
    private val customsOrderService: CustomsOrderService,
) {

    @Transactional
    fun archivePendingDocuments() {

        // only for 2 days, after that we ignore it
        var localDate = LocalDate.now()
        localDate = localDate.minusDays(2)
        val date = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val documentList = customsOrderDocumentRepository.findByCreatedAfterAndArchiveError(date)
        if (CollectionUtils.isNotEmpty(documentList)) {
            for (document in documentList) {
                if (document.documentNo != null) {
                    val customsOrder: CustomsOrder = customsOrderService.getByCustomsOrderNo(document.customsOrder?.customsOrderNo!!)
                    archiveFileFromSFtpServer(customsOrder, document)
                    customsOrderDocumentRepository.save(document)
                }
            }
        }
    }

    fun archiveFile(customsOrder: CustomsOrder, document: CustomsOrderDocument, fileContent: ByteArray): Boolean {
        try {
            val documentType = documentTypeService.findByCode(document.documentType?.code ?: throw java.lang.IllegalArgumentException("No document type code provided!"))
            if (documentType?.archiveType != null) {
                archiveClient.archiveDocument(document, fileContent)
                if (BooleanUtils.isTrue(document.archiveError)) {
                    sendNotification(
                        document, "DocumentArchivingNotFoundEvent", Notification.NOTIFICATION_TYPE_WARNING, true
                    )
                    sendNotification(
                        document, "DocumentArchivingErrorEvent", Notification.NOTIFICATION_TYPE_ERROR, true
                    )
                }
                document.archiveError = false
                return true
            }
        } catch (e: ArchiveException) {
            //DocumentArchiveService.log.error("Error while archiving file: " + document!!.documentFilename, e)
        }
        return false
    }

    fun archiveFileFromSFtpServer(customsOrder: CustomsOrder, document: CustomsOrderDocument?) {
        try {
            val fileContent = sFtpService.readFile(
                customsOrder.tenant?.tenant!!, "import", document!!.documentFilename!!
            )
            if (fileContent != null) {
                if (archiveFile(customsOrder, document, fileContent)) {
                    sFtpService.deleteFile(customsOrder.tenant?.tenant!!, "import", document.documentFilename!!)
                    if (BooleanUtils.isTrue(document.archiveError)) {
                        sendNotification(
                            document, "DocumentArchivingNotFoundEvent", Notification.NOTIFICATION_TYPE_WARNING, true
                        )
                        sendNotification(
                            document, "DocumentArchivingErrorEvent", Notification.NOTIFICATION_TYPE_ERROR, true
                        )
                    }
                    return
                } else {
                    if (BooleanUtils.isNotTrue(document.archiveError)) {
                        sendNotification(
                            document, "DocumentArchivingErrorEvent", Notification.NOTIFICATION_TYPE_ERROR, false
                        )
                    }
                }
            } else {
                //DocumentArchiveService.log.error("File with name " + document.documentFilename + " does not exist on SFtp Server!")
                if (BooleanUtils.isNotTrue(document.archiveError)) {
                    sendNotification(
                        document, "DocumentArchivingNotFoundEvent", Notification.NOTIFICATION_TYPE_WARNING, false
                    )
                }
            }
        } catch (e: IOException) {
            //DocumentArchiveService.log.error("Error while archiving file: " + document!!.documentFilename, e)
            if (null != document && document.archiveError != true) {
                sendNotification(document, "DocumentArchivingErrorEvent", Notification.NOTIFICATION_TYPE_ERROR, false)
            }
        }
        if (BooleanUtils.isNotTrue(document!!.archiveError)) {
            document.archiveError = true
            customsOrderDocumentRepository.save(document)
        }
    }

    private fun sendNotification(
        document: CustomsOrderDocument, event: String, type: String, solved: Boolean,
    ) {
        ArchiveDocumentNotificationBuilder(
            document.customsOrder?.id,
            document,
            event,
            type,
            solved,
        ).build().also(notificationPublisher::send)
    }
}
