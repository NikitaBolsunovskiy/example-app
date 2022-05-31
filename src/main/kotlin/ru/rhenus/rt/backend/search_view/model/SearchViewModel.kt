package ru.rhenus.rt.backend.search_view.model

import ru.rhenus.rt.backend.business_events.model.BusinessEventDto
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocumentDto
import ru.rhenus.rt.backend.counterparty.model.CounterpartyDto
import ru.rhenus.rt.backend.customs_declaration.model.CustomsDeclarationDto
import ru.rhenus.rt.backend.vehicle_entry_act.model.VehicleEntryActDto

class SearchViewModel(
    var vehicleEntryAct: VehicleEntryActDto? = null,
    var counterparties: List<CounterpartyDto> = listOf(),
    var cargoAccompanyingDocuments: List<CargoAccompanyingDocumentDto> = listOf(),
    var customsDeclarations: List<CustomsDeclarationDto> = listOf(),
    var businessEvents: List<BusinessEventDto> = listOf(),
    )