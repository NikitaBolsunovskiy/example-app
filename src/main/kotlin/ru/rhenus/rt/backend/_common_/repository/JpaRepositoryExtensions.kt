package ru.rhenus.rt.backend._common_.repository

import org.springframework.data.jpa.repository.JpaRepository

object JpaRepositoryExtensions {
    fun <T, ID> JpaRepository<T, ID>.findByNullableIdOrNull(id: ID?): T? =
        if(id === null) null else findById(id).orElse(null)
}
