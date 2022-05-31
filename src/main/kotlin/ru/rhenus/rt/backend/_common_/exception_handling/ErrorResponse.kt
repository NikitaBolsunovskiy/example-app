package ru.rhenus.rt.backend._common_.exception_handling

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@Suppress("unused")
class ErrorResponse(
    status: HttpStatus,
    val message: String? = null,
    val stackTrace: String? = null,
) {

    val code: Int = status.value()
    val status: String = status.name
    val timestamp: LocalDateTime = LocalDateTime.now()

}