package ru.rhenus.rt.backend.counterparty.model

import ru.rhenus.rt.backend._common_.model.SyncedEntity
import javax.persistence.*

@Entity
class Counterparty: SyncedEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, length = 250)
    var name: String? = null

}