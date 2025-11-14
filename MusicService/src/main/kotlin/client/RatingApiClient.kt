package org.example.client

import org.example.dto.RatingResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RatingApiClient(private val webClient: WebClient) {

    fun setRating(trackId: Long, stars: Int = 5) {
        webClient.post()
            .uri("")
            .bodyValue(mapOf("trackId" to trackId, "stars" to stars))
            .retrieve()
            .bodyToMono(Void::class.java)
            .block()
    }


    fun getRatingAvg(trackId: Long): RatingResponse? {
        return webClient.get()
            .uri("/avg/$trackId")
            .retrieve()
            .bodyToMono(RatingResponse::class.java)
            .block()
    }


    fun registerDownload(trackId: Long) {
        webClient.post()
            .uri("/download")
            .bodyValue(mapOf("trackId" to trackId))
            .retrieve()
            .bodyToMono(Void::class.java)
            .block()
    }
}