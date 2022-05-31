package service

import de.rhenus.scs.customs.order.model.CustomsOrderError
import de.rhenus.scs.customs.order.repository.CustomsOrderErrorRepository
import org.apache.commons.collections.CollectionUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.time.Instant
import javax.validation.Valid
import javax.validation.ValidationException

@Service
@Validated
class CustomsOrderErrorService(
    private val customsOrderErrorRepository: CustomsOrderErrorRepository
) {

    fun get(id: Long): CustomsOrderError {
        return customsOrderErrorRepository.findById(id)
            .orElseThrow { throw ValidationException("CustomsOrderError not found."); }
    }

    fun findActiveByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderError> =
        customsOrderErrorRepository.findActiveByCustomsOrderId(customsOrderId)

    @Transactional
    fun solveByCustomsOrderSegmentId(customsOrderSegmentId: Long?) {
        val errorList: List<CustomsOrderError> = customsOrderErrorRepository.findActiveByCustomsOrderSegmentId(
            customsOrderSegmentId
        )
        if (CollectionUtils.isNotEmpty(errorList)) {
            for (error in errorList) {
                error.solved = true
                error.solvedAt = Instant.now()
                customsOrderErrorRepository.save(error)
            }
        }
    }

    @Transactional
    fun create(orderError: @Valid CustomsOrderError): CustomsOrderError = customsOrderErrorRepository.save(orderError)

    fun getEDIErrorOverview(customsOrderIdList: List<Long>): Map<Long, Long> {
        val overview: MutableMap<Long, Long> = HashMap()
        val summaries = customsOrderErrorRepository.getSummaryByStatusId(
            "EDIError", customsOrderIdList
        )
        for (customsOrderId in customsOrderIdList) {
            val errorSummaryList = summaries.filter {
                it.customsOrderId?.equals(customsOrderId) == true
            }
            if (errorSummaryList.isEmpty()) {
                overview[customsOrderId] = 0L
            } else {
                overview[customsOrderId] = errorSummaryList[0].count ?: 0L
            }
        }
        return overview
    }
}
