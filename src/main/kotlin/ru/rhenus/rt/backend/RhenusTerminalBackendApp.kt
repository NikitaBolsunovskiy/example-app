package ru.rhenus.rt.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RhenusTerminalBackendApp

fun main(args: Array<String>) {
    runApplication<RhenusTerminalBackendApp>(*args)
}
