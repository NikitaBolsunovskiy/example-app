package service

import de.rhenus.scs.customs.order.model.CustomsOrderUnit
import de.rhenus.scs.customs.order.repository.CustomsOrderRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderUnitRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.Valid
import javax.validation.ValidationException

@Service
class CustomsOrderUnitService(
    private val customsOrderRepository: CustomsOrderRepository,
    private val customsOrderUnitRepository: CustomsOrderUnitRepository,
) {

    fun get(id: Long?): CustomsOrderUnit =
        customsOrderUnitRepository.findByIdOrNull(id) ?: throw ValidationException("CustomsOrderUnit not found!")

    fun getUnitNumbersByCustomsOrderId(customsOrderId: Long?): List<String> =
        customsOrderUnitRepository.getUnitNumbersByCustomsOrderId(customsOrderId)

    fun findByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderUnit> =
        customsOrderUnitRepository.findByCustomsOrderId(customsOrderId)

    @Transactional
    fun update(unit: @Valid CustomsOrderUnit): CustomsOrderUnit = try {
        customsOrderUnitRepository.save(unit)
    } catch (e: DataIntegrityViolationException) {
        if (e.message!!.contains("uc_customs_order_unit_unit_no")) {
            throw ValidationException("customsOrderUnit.validation.unitNo.alreadyExists")
        } else {
            throw e
        }
    }

    @Transactional
    fun create(unit: @Valid CustomsOrderUnit): CustomsOrderUnit = try {
        unit.customsOrder?.apply {
            transportInContainersSad19 = true
        }
        customsOrderUnitRepository.save(unit)
    } catch (e: DataIntegrityViolationException) {
        if (e.message!!.contains("uc_customs_order_unit_unit_no")) {
            throw ValidationException("customsOrderUnit.validation.unitNo.alreadyExists")
        } else {
            throw e
        }
    }
}
