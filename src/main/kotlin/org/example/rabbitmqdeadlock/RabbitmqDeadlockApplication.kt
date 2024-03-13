package org.example.rabbitmqdeadlock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RabbitmqDeadlockApplication

fun main(args: Array<String>) {
    runApplication<RabbitmqDeadlockApplication>(*args)
}
