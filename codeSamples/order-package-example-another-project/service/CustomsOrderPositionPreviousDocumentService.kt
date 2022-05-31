package service

import de.rhenus.scs.customs.order.model.CustomsOrderPositionPreviousDocument
import de.rhenus.scs.customs.order.repository.CustomsOrderPositionPreviousDocumentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.ValidationException

@Service
@Validated
class CustomsOrderPositionPreviousDocumentService(
    private val previousDocumentRepository: CustomsOrderPositionPreviousDocumentRepository
) {

    fun get(id: Long): CustomsOrderPositionPreviousDocument =
        previousDocumentRepository.findById(id).orElseThrow { throw ValidationException("CustomsOrderPositionPreviousDocument not found.") }

    @Transactional
    fun update(previousDocument: @Valid CustomsOrderPositionPreviousDocument): CustomsOrderPositionPreviousDocument {
        checkIfIsUnique(previousDocument)
        return previousDocumentRepository.save(previousDocument)
    }

    @Transactional
    fun create(previousDocument: @Valid CustomsOrderPositionPreviousDocument): CustomsOrderPositionPreviousDocument {
        checkIfIsUnique(previousDocument)
        return previousDocumentRepository.save(previousDocument)
    }

    private fun checkIfIsUnique(previousDocument: CustomsOrderPositionPreviousDocument) {
        if (previousDocumentRepository.existsByCustomsOrderPositionIdAndReferenceAndIdNot(
                previousDocument.customsOrderPosition?.id, previousDocument.reference, previousDocument.id
            )
        ) {
            throw ValidationException("validation.customsOrderPositionPreviousDocument.unique")
        }
    }
}
