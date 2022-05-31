package ru.rhenus.rt.backend.customs_declaration.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocument
import ru.rhenus.rt.backend.customs_declaration.model.CustomsDeclaration

interface CustomsDeclarationRepository : JpaRepository<CustomsDeclaration, Long>, JpaSpecificationExecutor<CustomsDeclaration> {
    fun findByUuid1c(uuid1c: String): CustomsDeclaration?
    fun findByCargoAccompanyingDocumentsIn(cargoAccompanyingDocuments: List<CargoAccompanyingDocument>): List<CustomsDeclaration>

}