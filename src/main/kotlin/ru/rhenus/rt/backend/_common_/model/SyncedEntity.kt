package ru.rhenus.rt.backend._common_.model

import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class SyncedEntity {

    @Column(nullable = false, length = 36, unique = true)
    var uuid1c: String? = null

}