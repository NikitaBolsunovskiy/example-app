package ru.rhenus.rt.backend._common_.service

import ru.rhenus.rt.backend._common_.model.Dto1CAbstract
import javax.persistence.EntityNotFoundException
import javax.validation.ValidationException

interface Service1CExtension<T: Dto1CAbstract> {

    fun createOrUpdate(dto1C: T): Boolean

    fun createOrUpdateBatch(dto1Cs: List<T>): Map<String, Boolean> {

        val resultMap = mutableMapOf<String, Boolean>()
        dto1Cs.forEach {
            var resultForOne = true
            try {
                createOrUpdate(it)
            } catch (_: ValidationException) {
                resultForOne = false
            } catch (_: EntityNotFoundException) {
                resultForOne = false
            } finally {
                if (it.uuid1c != null) resultMap[it.uuid1c!!] = resultForOne
            }
        }

        return resultMap.toMap()
    }

}