package ru.rhenus.rt.backend.customs_declaration.model

import ru.rhenus.rt.backend._common_.model.Dto1CAbstract
import java.time.Instant

class CustomsDeclarationDto1C(
    var counterpartyUuid1c: String? = null,
    var number: String? = null,
    var status: CustomsDeclarationStatus? = null,
    var statusOpenDate: Instant? = null,
    var statusClearedDate: Instant? = null,
    var cargoAccompanyingDocumentsUuid1c: MutableSet<String> = mutableSetOf()
): Dto1CAbstract()
