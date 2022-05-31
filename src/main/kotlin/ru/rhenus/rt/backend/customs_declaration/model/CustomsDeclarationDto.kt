package ru.rhenus.rt.backend.customs_declaration.model

import java.io.Serializable
import java.time.Instant

data class CustomsDeclarationDto(
    var uuid1c: String? = null,
    var id: Long? = null,
    var counterpartyId: Long? = null,
    var number: String? = null,
    var status: CustomsDeclarationStatus? = null,
    var statusOpenDate: Instant? = null,
    var cargoAccompanyingDocumentIds: MutableSet<Long>?
) : Serializable
