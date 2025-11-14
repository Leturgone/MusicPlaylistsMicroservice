package org.example.service

import kotlinx.serialization.Serializable
import org.example.dto.TrackDTO
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service


data class TrackEvent(
    val type: String,  // CREATED, DELETED
    val track: TrackDTO,
)

@Service
class TrackEventPublisher(private val rabbitTemplate: RabbitTemplate) {
    private val log = LoggerFactory.getLogger(TrackEventPublisher::class.java)

    fun publishTrackCreated(trackDTO: TrackDTO) {
        val event = TrackEvent(
            type = "CREATED",
            track = trackDTO
        )
        rabbitTemplate.convertAndSend(
            "track.events.exchange",
            "track.events.routingkey",
            event
        )
    }


    fun publishTrackDeleted(trackDTO: TrackDTO) {
        val event = TrackEvent(
            type = "DELETED",
            track = trackDTO
        )
        rabbitTemplate.convertAndSend(
            "track.events.exchange",
            "track.events.routingkey",
            event
        )
    }

}