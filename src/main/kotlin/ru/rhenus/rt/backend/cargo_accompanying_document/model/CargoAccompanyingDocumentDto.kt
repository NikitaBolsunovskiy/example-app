package ru.rhenus.rt.backend.cargo_accompanying_document.model

import java.time.Instant

class CargoAccompanyingDocumentDto(
    var uuid1c: String? = null,
    var id: Long? = null,
    var counterpartyId: Long? = null,
    var vehicleEntryActId: Long? = null,
    var documentType: CargoAccompanyingDocumentType? = null,
    var documentNumber: String? = null,
    var cmrNumber: String? = null,
    var invoiceNumber: String? = null,
    var placesCount: String? = null,
    var additionalInformation: String? = null,
    var managerName: String? = null,
    var services: MutableSet<String> = mutableSetOf(),
    var controlMeasures: MutableSet<String> = mutableSetOf(),
    var readyToOpenCustomsDeclarationProcedure: Boolean? = null,
    var readyToOpenCustomsDeclarationProcedureDate: Instant? = null
)
