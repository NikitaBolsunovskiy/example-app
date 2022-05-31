package ru.rhenus.rt.backend.search_view.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.rhenus.rt.backend.business_events.mapper.BusinessEventMapper
import ru.rhenus.rt.backend.business_events.repository.BusinessEventRepository
import ru.rhenus.rt.backend.cargo_accompanying_document.mapper.CargoAccompanyingDocumentMapper
import ru.rhenus.rt.backend.cargo_accompanying_document.repository.CargoAccompanyingDocumentRepository
import ru.rhenus.rt.backend.counterparty.mapper.CounterpartyMapper
import ru.rhenus.rt.backend.customs_declaration.mapper.CustomsDeclarationMapper
import ru.rhenus.rt.backend.customs_declaration.repository.CustomsDeclarationRepository
import ru.rhenus.rt.backend.search_view.model.SearchViewModel
import ru.rhenus.rt.backend.vehicle_entry_act.mapper.VehicleEntryActMapper
import ru.rhenus.rt.backend.vehicle_entry_act.repository.VehicleEntryActRepository

@Service
class SearchViewService(
    val counterpartyMapper: CounterpartyMapper,
    val vehicleEntryActRepository: VehicleEntryActRepository,
    val vehicleEntryActMapper: VehicleEntryActMapper,
    val cargoAccompanyingDocumentRepository: CargoAccompanyingDocumentRepository,
    val cargoAccompanyingDocumentMapper: CargoAccompanyingDocumentMapper,
    val customsDeclarationRepository: CustomsDeclarationRepository,
    val customsDeclarationMapper: CustomsDeclarationMapper,
    val businessEventRepository: BusinessEventRepository,
    val businessEventMapper: BusinessEventMapper,
) {
    fun search(pageRequest: Pageable): Page<SearchViewModel> {
        val searchViewModels = mutableListOf<SearchViewModel>()
        val vehicleEntryActsPage = vehicleEntryActRepository.findAll(pageRequest)
        val cargoAccompanyingDocuments = cargoAccompanyingDocumentRepository.findByVehicleEntryActIn(vehicleEntryActsPage.content)
        vehicleEntryActsPage.forEach { vehicleEntryAct ->
            searchViewModels.add(SearchViewModel().apply {
                this.vehicleEntryAct = vehicleEntryActMapper.toDto(vehicleEntryAct)
                val cargoAccompanyingDocumentsEntities = cargoAccompanyingDocuments
                    .filter { it.vehicleEntryAct!!.id == vehicleEntryAct.id }
                this.cargoAccompanyingDocuments = cargoAccompanyingDocumentsEntities
                    .map(cargoAccompanyingDocumentMapper::toDto)
                this.counterparties = cargoAccompanyingDocumentsEntities.map { it.counterparty }.map(counterpartyMapper::toDto)
                val customsDeclarationsEntities = customsDeclarationRepository
                    .findByCargoAccompanyingDocumentsIn(cargoAccompanyingDocumentsEntities)
                this.customsDeclarations = customsDeclarationsEntities.map(customsDeclarationMapper::toDto)
            })

            searchViewModels.forEach {
                val ownerUuids = mutableSetOf<String>()
                it.vehicleEntryAct?.uuid1c?.let(ownerUuids::add)
                it.cargoAccompanyingDocuments.forEach { cad -> ownerUuids.add(cad.uuid1c!!) }
                it.customsDeclarations.forEach { cd -> ownerUuids.add(cd.uuid1c!!) }
                it.businessEvents = businessEventRepository.findByActiveTrueAndOwnerUuid1cIn(ownerUuids).map(businessEventMapper::toDto)
            }
        }

        return PageImpl(
            searchViewModels,
            pageRequest,
            vehicleEntryActsPage.totalElements
        )
    }

}