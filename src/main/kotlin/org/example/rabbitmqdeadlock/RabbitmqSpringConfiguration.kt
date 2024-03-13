package org.example.rabbitmqdeadlock

import org.springframework.amqp.rabbit.AsyncRabbitTemplate
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.SimpleMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.RabbitMQContainer

@Configuration
@EnableRabbit
class RabbitmqSpringConfiguration {
    @Bean(destroyMethod = "close")
    fun rabbitMqTestContainer(): RabbitMQContainer {
        val container = RabbitMQContainer("rabbitmq:3.7.6-management")
        container.start()
        return container
    }

    @Bean
    fun rabbitConnectionFactory(): ConnectionFactory {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.setUri(rabbitMqTestContainer().amqpUrl)
        connectionFactory.channelCacheSize = 2
        connectionFactory.setChannelCheckoutTimeout(500L)
        return connectionFactory
    }

    @Bean
    fun rabbitListenerContainerFactory(): DirectRabbitListenerContainerFactory {
        val factory = DirectRabbitListenerContainerFactory()
        factory.setConnectionFactory(rabbitConnectionFactory())
        return factory
    }

    @Bean
    fun asyncRabbitTemplate(): AsyncRabbitTemplate {
        val rabbitTemplate = RabbitTemplate(rabbitConnectionFactory())
        rabbitTemplate.messageConverter = SimpleMessageConverter()
        rabbitTemplate.setReceiveTimeout(2000L)
        return AsyncRabbitTemplate(rabbitTemplate)
    }
}