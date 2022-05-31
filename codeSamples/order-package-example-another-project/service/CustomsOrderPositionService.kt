package service

import de.rhenus.scs.customs.order.model.CustomsOrderPosition
import de.rhenus.scs.customs.order.repository.CustomsOrderPositionRepository
import de.rhenus.scs.customs.order.repository.CustomsOrderRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.ValidationException
import javax.validation.Validator
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField


@Service
class CustomsOrderPositionService(
    private val csvToStringArrayReaderService: CsvToStringArrayReaderService,
    private val customsOrderPositionRepository: CustomsOrderPositionRepository,
    private val customsOrderRepository: CustomsOrderRepository,
    private val validator: Validator,
) {

    fun get(id: Long?): CustomsOrderPosition = customsOrderPositionRepository.findByIdOrNull(id)
           ?: throw ValidationException("CustomsOrderPosition not found!")

    fun findByCustomsOrderId(customsOrderId: Long?): List<CustomsOrderPosition> =
        customsOrderPositionRepository.findByCustomsOrderId(customsOrderId)

    @Transactional
    fun update(orderPosition: @Valid CustomsOrderPosition): CustomsOrderPosition =
        customsOrderPositionRepository.save(orderPosition)

    @Transactional
    fun create(orderPosition: @Valid CustomsOrderPosition): CustomsOrderPosition {
        if (orderPosition.sequentialNoSad32 == null) {
            orderPosition.sequentialNoSad32 =
                customsOrderPositionRepository.getNextSequentialNo(orderPosition.customsOrder?.id).toInt()
        }
        return customsOrderPositionRepository.save(orderPosition)
    }

    @Transactional
    fun createByCsv(
        customsOrderId: Long?,
        fieldMappings: List<String>,
        goodsValueCurrency: String?,
        itemPriceSad42: Float?,
        netMassSad38: Float?,
        firstRowIsHeader: Boolean,
        file: MultipartFile
    ) {
        val customsOrder = customsOrderRepository.findByIdOrNull(customsOrderId).also { customsOrder ->
            val data = csvToStringArrayReaderService.read(file)
            for ((index, row) in data.withIndex()) {
                if (!firstRowIsHeader || index > 0) {
                    val position = csvDataToEntity(row, fieldMappings)
                    position.customsOrder = customsOrder
                    if (goodsValueCurrency != null) {
                        position.goodsValueCurrency = goodsValueCurrency
                    }
                    if (itemPriceSad42 != null) {
                        position.itemPriceSad42 = itemPriceSad42.toBigDecimal()
                    }
                    if (netMassSad38 != null) {
                        position.netMassSad38 = netMassSad38.toBigDecimal()
                    }
                    val violations = validator.validate(position)
                    if (violations.isNotEmpty()) {
                        throw ConstraintViolationException(HashSet(violations))
                    }
                    create(position)
                }
            }
        }
    }

    fun csvDataToEntity(
        data: Array<String>, fieldNames: List<String>
    ): CustomsOrderPosition {
        val customsOrderPosition = CustomsOrderPosition()

        fun setField(field: KMutableProperty1<CustomsOrderPosition, *>?, value: Any?) {

            try {
                if (field != null) {
                    field.isAccessible = true
                    if (value != null) {
                        if (field.javaField?.type?.isAssignableFrom(Float::class.java) == true) {
                            field.setter.call(customsOrderPosition, value.toString().toFloat())
                        } else if (field.javaField?.type?.isAssignableFrom(Long::class.java) == true) {
                            field.setter.call(customsOrderPosition, value.toString().toLong())
                        } else if (field.javaField?.type?.isAssignableFrom(Double::class.java) == true) {
                            field.setter.call(customsOrderPosition, value.toString().toDouble())
                        } else if (field.javaField?.type?.isAssignableFrom(String::class.java) == true) {
                            field.setter.call(customsOrderPosition, value)
                        }
                    } else {
                        field.setter.call(customsOrderPosition, null)
                    }
                }
            } catch (e: Exception) { // de.rhenus.scs.customs.order.model.entity.CustomsOrderPosition.log.error(e.message, e)
            }
        }


        val fields = CustomsOrderPosition::class.declaredMemberProperties
            .filterIsInstance<KMutableProperty1<CustomsOrderPosition, *>>()
            .associateBy({ it.name }, { it })

        for (i in fieldNames.indices) {
            if (fieldNames[i].isNotBlank()) {
                setField(fields[fieldNames[i]], data[i])
            }
        }
        customsOrderPosition.netMassUnitSad38 = "KG"
        customsOrderPosition.grossMassUnitSad35 = "KG"
        return customsOrderPosition
    }
}
