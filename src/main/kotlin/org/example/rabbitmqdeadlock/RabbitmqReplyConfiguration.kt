package org.example.rabbitmqdeadlock

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableRabbit
class RabbitmqReplyConfiguration {
    private val logger = LoggerFactory.getLogger(RabbitmqReplyConfiguration::class.java)

    @Bean
    fun queue(): Queue {
        return Queue("queue", true, false, false)
    }

    @RabbitListener(queues = ["queue"])
    fun processMessage(message: String): String {
        logger.info("Received message on thread ${Thread.currentThread()}: $message")
        Thread.sleep(1000)
        return message
    }
}