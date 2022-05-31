package ru.rhenus.rt.backend._common_.exception_handling

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.io.PrintWriter
import java.io.StringWriter
import javax.persistence.EntityNotFoundException
import javax.validation.ValidationException

@ControllerAdvice
class ControllerExceptionsHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(
        ValidationException::class,
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun constraintViolationException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.BAD_REQUEST, e.message ?: "", e)
    }

    @ExceptionHandler(
        EntityNotFoundException::class,
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFoundException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.NOT_FOUND, e.message ?: "", e)
    }

    @ExceptionHandler(
        Exception::class
    )
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun internalServerErrorException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Generic internal error", e)
    }

    private fun generateErrorResponse(
        status: HttpStatus,
        message: String,
        e: Exception
    ): ResponseEntity<ErrorResponse> {
        // converting the exception stack trace to a string
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        val stackTrace = sw.toString()

        logger.error(e) { message }

        // environment-based logic
        val stackTraceMessage =
            when (System.getenv("ENV").uppercase()) {
                "DEV" -> stackTrace // returning the stack trace
                "STAGING" -> stackTrace // returning the stack trace
                "PRODUCTION" -> null // returning no stack trace
                else -> stackTrace // default behavior
            }

        return ResponseEntity(ErrorResponse(status, message, stackTraceMessage), status)
    }

}