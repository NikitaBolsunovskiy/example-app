package ru.rhenus.rt.backend.business_events.model

import ru.rhenus.rt.backend._common_.model.SyncedEntity
import java.time.Instant
import javax.persistence.*

@Entity
class BusinessEvent : SyncedEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null

    @Column(nullable = false, length = 36)
    var ownerUuid1c: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    var ownerType: OwnerType? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    var eventType: BusinessEventType? = null

    @Column(nullable = false)
    var eventDateTime: Instant? = null

    @Column(nullable = false)
    var active: Boolean? = null

}