package ru.rhenus.rt.backend.business_events.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.rhenus.rt.backend.business_events.model.BusinessEvent

interface BusinessEventRepository : JpaRepository<BusinessEvent, Long>, JpaSpecificationExecutor<BusinessEvent> {
    fun findByUuid1c(uuid1c: String): BusinessEvent?
    fun findByActiveTrueAndOwnerUuid1cIn(
        ownerUuid1c: MutableCollection<String>
    ): List<BusinessEvent>
}