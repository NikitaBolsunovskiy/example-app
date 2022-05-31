package ru.rhenus.rt.backend.cargo_accompanying_document.model

import ru.rhenus.rt.backend._common_.model.Dto1CAbstract
import java.time.Instant

class CargoAccompanyingDocumentDto1C(
    var counterpartyUuid1c: String? = null,
    var vehicleEntryActUuid1c: String? = null,
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
    var readyToOpenCustomsDeclarationProcedureDate: Instant? = null,
): Dto1CAbstract()
