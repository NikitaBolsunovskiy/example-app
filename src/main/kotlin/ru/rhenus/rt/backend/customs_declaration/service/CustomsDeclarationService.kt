package ru.rhenus.rt.backend.customs_declaration.service

import org.springframework.beans.BeanUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.rhenus.rt.backend._common_.service.Service1CExtension
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocument
import ru.rhenus.rt.backend.cargo_accompanying_document.repository.CargoAccompanyingDocumentRepository
import ru.rhenus.rt.backend.counterparty.repository.CounterpartyRepository
import ru.rhenus.rt.backend.customs_declaration.model.CustomsDeclaration
import ru.rhenus.rt.backend.customs_declaration.model.CustomsDeclarationDto1C
import ru.rhenus.rt.backend.customs_declaration.repository.CustomsDeclarationRepository
import javax.persistence.EntityNotFoundException
import javax.validation.ValidationException

@Service
class CustomsDeclarationService(
    val customsDeclarationRepository: CustomsDeclarationRepository,
    val counterpartyRepository: CounterpartyRepository,
    val cargoAccompanyingDocumentRepository: CargoAccompanyingDocumentRepository,
): Service1CExtension<CustomsDeclarationDto1C> {

    @kotlin.jvm.Throws(ValidationException::class)
    fun create(customsDeclaration: CustomsDeclaration): CustomsDeclaration {
        if (customsDeclaration.id !== null) throw ValidationException("'id' must be null on 'create'")
        //customsDeclaration.populateCargoAccompanyingDocumentsReferenceToSelf()
        return customsDeclarationRepository.save(customsDeclaration)
    }

    @kotlin.jvm.Throws(ValidationException::class, EntityNotFoundException::class)
    fun update(customsDeclaration: CustomsDeclaration): CustomsDeclaration {
        customsDeclarationRepository.findByIdOrNull(
            customsDeclaration.id ?: throw ValidationException("'id' must not be null on 'update'")
        ) ?: throw EntityNotFoundException("entity with id: ${customsDeclaration.id} not found")
        //customsDeclaration.populateCargoAccompanyingDocumentsReferenceToSelf()
        return customsDeclarationRepository.save(customsDeclaration)
    }

    @kotlin.jvm.Throws(ValidationException::class, EntityNotFoundException::class)
    @Transactional
    override fun createOrUpdate(dto1C: CustomsDeclarationDto1C): Boolean {

        dto1C.uuid1c ?: throw ValidationException("'uuid1c' must not be null on 'create/update'")

        val counterparty = counterpartyRepository.findByUuid1c(
            dto1C.counterpartyUuid1c ?: throw ValidationException("'counterpartyUuid1c' must not be null on 'create/update'")
        ) ?: throw EntityNotFoundException("Counterparty with uuid1c: ${dto1C.counterpartyUuid1c} not found")

        val cargoAccompanyingDocuments = mutableSetOf<CargoAccompanyingDocument>()
        dto1C.cargoAccompanyingDocumentsUuid1c.forEach {
                cargoAccDocumentUuid1c ->

            val cargoAccompanyingDocument = cargoAccompanyingDocumentRepository.findByUuid1c(cargoAccDocumentUuid1c)
                ?: throw EntityNotFoundException("СargoAccompanyingDocument with uuid1c: $cargoAccDocumentUuid1c not found")
            cargoAccompanyingDocuments.add(cargoAccompanyingDocument)
        }

        val entityInDB = dto1C.uuid1c!!.let(customsDeclarationRepository::findByUuid1c)
        val entityToPersist = CustomsDeclaration()
        BeanUtils.copyProperties(dto1C, entityToPersist)
        entityToPersist.apply { if (entityInDB !== null) id = entityInDB.id }
        entityToPersist.apply { this.counterparty = counterparty; this.cargoAccompanyingDocuments = cargoAccompanyingDocuments }
        if (entityInDB !== null) update(entityToPersist) else create(entityToPersist)
        return entityInDB === null
    }

}