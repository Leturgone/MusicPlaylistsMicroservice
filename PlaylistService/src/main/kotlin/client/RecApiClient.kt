package org.example.client

import org.example.dto.RecommendationsRespond
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RecApiClient(
    @Qualifier("recWebClient") private val recWebClient: WebClient
){

    fun analyze(userId: Long): String? {

        return try {
            recWebClient.post()
                .uri("/analyze/$userId")
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
        } catch (e: Exception) {
            return null
        }
    }

    fun getRecommendations(userId: Long): RecommendationsRespond? {
        return try {
            recWebClient.get()
                .uri("/$userId")
                .retrieve()
                .bodyToMono(RecommendationsRespond::class.java)
                .block()
        }catch (e: Exception){
            return null
        }
    }

}
