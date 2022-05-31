package ru.rhenus.rt.backend.counterparty.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.rhenus.rt.backend.counterparty.model.Counterparty

interface CounterpartyRepository : JpaRepository<Counterparty, Long>, JpaSpecificationExecutor<Counterparty> {
    fun findByUuid1c(uuid1c: String): Counterparty?

}