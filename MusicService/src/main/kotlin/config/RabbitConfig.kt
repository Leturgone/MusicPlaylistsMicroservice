package org.example.config


import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = jsonMessageConverter()
        return template
    }

    // Для рекомендаций
    @Bean
    fun recommendationExchange(): DirectExchange {
        return DirectExchange("recommendation.exchange")
    }

    @Bean
    fun recommendationQueue(): Queue {
        return Queue("recommendation.queue", true)
    }

    @Bean
    fun recommendationBinding(
        @Qualifier("recommendationQueue") queue: Queue,
        @Qualifier("recommendationExchange") exchange: DirectExchange): Binding {

        return BindingBuilder.bind(queue).to(exchange).with("recommendation.routingkey")
    }

    // Для поиска

    @Bean
    fun trackEventsExchange(): DirectExchange {
        return DirectExchange("track.events.exchange")
    }

    // Одна очередь для всех событий треков
    @Bean
    fun trackEventsQueue(): Queue {
        return Queue("track.events.queue", true)
    }

    @Bean
    fun trackEventsBinding(
        @Qualifier("trackEventsQueue") queue: Queue,
        @Qualifier("trackEventsExchange") exchange: DirectExchange
    ): Binding {
        return BindingBuilder.bind(trackEventsQueue())
            .to(trackEventsExchange())
            .with("track.events.routingkey")
    }

    @Bean
    fun jsonMessageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

}