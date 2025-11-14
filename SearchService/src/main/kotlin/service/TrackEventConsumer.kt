package org.example.service

import org.example.dto.TrackDTO
import org.example.event.TrackEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class TrackEventConsumer(
    private val searchService: SearchService
) {
    @RabbitListener(queues = [$$"${rabbitmq.queues.track-events}"])
    fun consumeTrackEvent(event: TrackEvent) {
        when (event.type) {
            "CREATED" -> {
                val trackDto = event.track
                searchService.createTrack(trackDto)
            }
            "DELETED" -> {
                val trackDto = event.track
                searchService.deleteTrack(trackDto)
            }
        }
    }
}