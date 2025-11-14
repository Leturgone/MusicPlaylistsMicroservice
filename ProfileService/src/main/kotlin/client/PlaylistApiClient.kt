package org.example.client

import org.example.dto.PlaylistsRespond
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class PlaylistApiClient(
    @Qualifier("playlistWebClient") private val playlistWebClient: WebClient
) {
    fun getPlaylists(userId: Long): PlaylistsRespond? {
        return try {
            playlistWebClient.get()
                .uri("/all/$userId")
                .retrieve()
                .bodyToMono(PlaylistsRespond::class.java)
                .block()
        } catch (e: Exception) {
            null
        }
    }
}