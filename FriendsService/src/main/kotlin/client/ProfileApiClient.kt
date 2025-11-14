package org.example.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import kotlin.jvm.java


@Service
class ProfileApiClient(
    @Qualifier("profileWebClient") private val profileWebClient: WebClient
) {

    fun getProfileNameById(userId: Long): String? {
        return try {
            profileWebClient.get()
                .uri("/$userId")
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
        } catch (e: Exception) {
            null
        }
    }
}

