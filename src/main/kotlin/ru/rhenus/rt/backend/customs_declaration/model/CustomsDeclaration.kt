package ru.rhenus.rt.backend.customs_declaration.model

import ru.rhenus.rt.backend._common_.model.SyncedEntity
import ru.rhenus.rt.backend.cargo_accompanying_document.model.CargoAccompanyingDocument
import ru.rhenus.rt.backend.counterparty.model.Counterparty
import java.time.Instant
import javax.persistence.*

@Entity
class CustomsDeclaration: SyncedEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    var counterparty: Counterparty? = null

    @Column(nullable = false, length = 50)
    var number: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: CustomsDeclarationStatus? = null

    @Column
    var statusOpenDate: Instant? = null

    @Column
    var statusClearedDate: Instant? = null

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    var cargoAccompanyingDocuments: MutableSet<CargoAccompanyingDocument> = mutableSetOf()

}
