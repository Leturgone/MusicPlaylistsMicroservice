package org.example.service

import org.example.dto.GenreMessage
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RecommendationNotifier(private val rabbitTemplate: RabbitTemplate) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun notify(userId: Long ,genre: String) {
        val message = GenreMessage(userId,genre)
        logger.info("Sending message: {}", message)
        rabbitTemplate.convertAndSend("recommendation.queue", message)
    }
}