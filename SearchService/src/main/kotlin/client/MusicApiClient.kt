package org.example.client

import org.example.dto.TrackMetadataResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import kotlin.jvm.java

@Service
class MusicApiClient(
    @Qualifier("musicWebClient") private val musicWebClient: WebClient
) {
    fun getTrackById(trackId: Long): TrackMetadataResponse?{
        return try {
            musicWebClient.get()
                .uri("/$trackId")
                .retrieve()
                .bodyToMono(TrackMetadataResponse::class.java)
                .block()
        } catch (e: Exception) {
            null
        }
    }
}