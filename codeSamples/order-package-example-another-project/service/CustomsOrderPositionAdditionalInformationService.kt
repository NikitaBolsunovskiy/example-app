package service

import de.rhenus.scs.customs.order.model.CustomsOrderPositionAdditionalInformation
import de.rhenus.scs.customs.order.repository.CustomsOrderPositionAdditionalInformationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.ValidationException

@Service
@Validated
class CustomsOrderPositionAdditionalInformationService(
    private val additionalInformationRepository: CustomsOrderPositionAdditionalInformationRepository
) {
    fun get(id: Long): CustomsOrderPositionAdditionalInformation =
        additionalInformationRepository.findById(id).orElseThrow { throw ValidationException("CustomsOrderPositionAdditionalInformation not found") }

    @Transactional
    fun update(additionalInformation: @Valid CustomsOrderPositionAdditionalInformation): CustomsOrderPositionAdditionalInformation {
        checkIfIsUnique(additionalInformation)
        return additionalInformationRepository.save(additionalInformation)
    }

    @Transactional
    fun create(additionalInformation: @Valid CustomsOrderPositionAdditionalInformation): CustomsOrderPositionAdditionalInformation {
        checkIfIsUnique(additionalInformation)
        return additionalInformationRepository.save(additionalInformation)
    }

    private fun checkIfIsUnique(additionalInformation: CustomsOrderPositionAdditionalInformation) {
        if (additionalInformationRepository.existsByCustomsOrderPositionIdAndTypeAndCodeAndIdNot(
                additionalInformation.customsOrderPosition?.id,
                additionalInformation.type,
                additionalInformation.code,
                additionalInformation.id
            )
        ) {
            throw ValidationException("validation.customsOrderPositionAdditionalInformation.unique")
        }
    }
}
