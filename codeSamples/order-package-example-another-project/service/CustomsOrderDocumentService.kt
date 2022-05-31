package service

import de.rhenus.scs.customs.archive.client.ArchiveClient
import de.rhenus.scs.customs.order.model.CustomsOrder
import de.rhenus.scs.customs.order.model.CustomsOrderDocument
import de.rhenus.scs.customs.order.repository.CustomsOrderDocumentRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderRepository
import org.apache.commons.lang3.StringUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid
import javax.validation.ValidationException

@Service
@Validated
class CustomsOrderDocumentService(
    private val archiveClient: ArchiveClient,
    private val documentArchiveService: DocumentArchiveService,
    private val customsOrderRepository: CustomsOrderRepository,
    private val customsOrderDocumentRepository: CustomsOrderDocumentRepository,
) {
    fun get(id: Long): CustomsOrderDocument {
        return customsOrderDocumentRepository.findByIdOrNull(id) ?: throw ValidationException("CustomsOrderDocument not found!")
    }

    @Transactional
    fun update(
        orderDocument: @Valid CustomsOrderDocument, file: MultipartFile?
    ): CustomsOrderDocument {
        checkIfIsUnique(orderDocument)
        if (file != null) {
            val document: CustomsOrderDocument? = customsOrderDocumentRepository.findByIdOrNull(orderDocument.id)
            if (StringUtils.isNotBlank(document?.archiveReference)) {
                throw ValidationException("validation.customsOrderDocument.alreadyArchived")
            }
            val customsOrder: CustomsOrder = customsOrderRepository.findByIdOrNull(orderDocument.customsOrder?.id)!!
            if (!documentArchiveService.archiveFile(customsOrder, orderDocument, file.bytes)) {
                throw ValidationException("validation.customsOrderDocument.archiveError")
            }
        }
        return customsOrderDocumentRepository.save(orderDocument)
    }

    @Transactional
    fun create(
        orderDocument: @Valid CustomsOrderDocument, file: MultipartFile?
    ): CustomsOrderDocument {
        checkIfIsUnique(orderDocument)
        val document: CustomsOrderDocument = customsOrderDocumentRepository.save(orderDocument)
        if (file != null) {
            val customsOrder: CustomsOrder = customsOrderRepository.findByIdOrNull(orderDocument.customsOrder?.id)!!
            if (!documentArchiveService.archiveFile(customsOrder, document, file.bytes)) {
                throw ValidationException("validation.customsOrderDocument.archiveError")
            }
            customsOrderDocumentRepository.save(document)
        }
        return document
    }

    fun getDocument(id: Long): CustomsOrderDocument? = customsOrderDocumentRepository.findByIdOrNull(id)

    fun getFile(reference: String): ByteArray? = archiveClient.getDocumentFromArchive(reference)

    fun getCustomsOrderDocumentsWithFiles(customsOrderId: Long): Map<CustomsOrderDocument, ByteArray> {
        val documentMap: MutableMap<CustomsOrderDocument, ByteArray> = mutableMapOf()
        val documentList: List<CustomsOrderDocument> = customsOrderDocumentRepository.findByCustomsOrderId(
            customsOrderId
        )
        documentList.forEach { document ->
            val reference = document.archiveReference
            documentMap[document] = if(reference !== null) archiveClient.getDocumentFromArchive(reference) else byteArrayOf() }
        return documentMap
    }

    private fun checkIfIsUnique(orderDocument: CustomsOrderDocument) {
        if (customsOrderDocumentRepository.existsByCustomsOrderIdAndDocumentNoAndDocumentTypeAndId(
                orderDocument.customsOrder?.id, orderDocument.documentNo, orderDocument.documentType, orderDocument.id
            )
        ) {
            throw ValidationException("validation.customsOrderDocument.unique")
        }
    }
}
