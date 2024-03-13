package org.example.rabbitmqdeadlock

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.AsyncRabbitTemplate
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class TaskSubmitter(private val asyncRabbitTemplate: AsyncRabbitTemplate) {
    private val logger = LoggerFactory.getLogger(TaskSubmitter::class.java)

    @EventListener
    fun process(event: ContextRefreshedEvent) {
        repeat(5) {
            logger.info("Sending request: $it")
            sendRequest(it)
        }
    }

    private fun sendRequest(i: Int) {
        val future = asyncRabbitTemplate.convertSendAndReceive<String>("queue", "echo$i")
        future.whenComplete { t, u ->
            logger.info("Received reply: $t, $u")
        }
    }
}