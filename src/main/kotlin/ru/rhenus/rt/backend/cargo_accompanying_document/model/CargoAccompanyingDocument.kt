package ru.rhenus.rt.backend.cargo_accompanying_document.model

import ru.rhenus.rt.backend._common_.model.SyncedEntity
import ru.rhenus.rt.backend.counterparty.model.Counterparty
import ru.rhenus.rt.backend.vehicle_entry_act.model.VehicleEntryAct
import java.time.Instant
import javax.persistence.*

@Entity
class CargoAccompanyingDocument: SyncedEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    var counterparty: Counterparty? = null

    @ManyToOne
    var vehicleEntryAct: VehicleEntryAct? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    var documentType: CargoAccompanyingDocumentType? = null

    @Column(nullable = false, length = 50)
    var documentNumber: String? = null

    @Column(nullable = false, length = 50)
    var cmrNumber: String? = null

    @Column(nullable = false, length = 50)
    var invoiceNumber: String? = null

    @Column(nullable = false, length = 50)
    var placesCount: String? = null

    @Column(nullable = false, length = 150)
    var additionalInformation: String? = null

    @Column(nullable = false, length = 50)
    var managerName: String? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "cargo_accompanying_document_services",
        joinColumns = [JoinColumn(name = "cargo_accompanying_document_id")]
    )
    @Column(name = "service")
    var services: MutableSet<String> = mutableSetOf()

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "cargo_accompanying_document_control_measures",
        joinColumns = [JoinColumn(name = "cargo_accompanying_document_id")]
    )
    @Column(name = "control_measure")
    var controlMeasures: MutableSet<String> = mutableSetOf()

    @Column
    var readyToOpenCustomsDeclarationProcedure: Boolean? = null

    @Column
    var readyToOpenCustomsDeclarationProcedureDate: Instant? = null

}