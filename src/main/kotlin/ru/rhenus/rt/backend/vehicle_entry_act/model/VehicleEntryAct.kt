package ru.rhenus.rt.backend.vehicle_entry_act.model

import ru.rhenus.rt.backend._common_.model.SyncedEntity
import javax.persistence.*

@Entity
class VehicleEntryAct: SyncedEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var consolidated: Boolean? = null

    @Column(nullable = false, length = 10)
    var actNumber: String? = null

    @Column(nullable = false, length = 50)
    var vehiclePlateNumber: String? = null

}