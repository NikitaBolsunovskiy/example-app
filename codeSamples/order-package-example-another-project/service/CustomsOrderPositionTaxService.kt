package service

import de.rhenus.scs.customs.order.model.CustomsOrderPositionTax
import de.rhenus.scs.customs.order.repository.CustomsOrderPositionTaxRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.ValidationException

@Service
@Validated
class CustomsOrderPositionTaxService(
    private val taxRepository: CustomsOrderPositionTaxRepository
) {

    fun get(id: Long): CustomsOrderPositionTax =
        taxRepository.findById(id).orElseThrow { throw ValidationException("CustomsOrderPositionTax not found") }

    @Transactional
    fun update(orderPositionTax: @Valid CustomsOrderPositionTax): CustomsOrderPositionTax {
        checkIfTypeIsUnique(orderPositionTax)
        return taxRepository.save(orderPositionTax)
    }

    @Transactional
    fun create(orderPositionTax: @Valid CustomsOrderPositionTax): CustomsOrderPositionTax {
        checkIfTypeIsUnique(orderPositionTax)
        return taxRepository.save(orderPositionTax)
    }

    private fun checkIfTypeIsUnique(orderPositionTax: CustomsOrderPositionTax) {
        val count: Long = taxRepository.countByCustomsOrderPositionIdAndTypeAndIdIsNot(
            orderPositionTax.customsOrderPosition?.id, orderPositionTax.type, orderPositionTax.id
        )
        if (count > 0) {
            throw ValidationException("customsOrderPositionTax.validation.type.alreadyExists")
        }
    }
}
