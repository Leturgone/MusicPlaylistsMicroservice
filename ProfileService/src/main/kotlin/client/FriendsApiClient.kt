package org.example.client

import org.example.dto.FriendsRespond
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


@Service
class FriendsApiClient(
    @Qualifier("friendWebClient") private val friendsWebClient: WebClient
) {

    fun getFriends(userId: Long): FriendsRespond? {
        return try {
            friendsWebClient.get()
                .uri("").header(
                    "X-User-Id",userId.toString()
                )
                .retrieve()
                .bodyToMono(FriendsRespond::class.java)
                .block()
        } catch (e: Exception) {
            null
        }
    }
}

