package org.example.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class WebClientConfig {

    @Bean
    fun playlistWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://127.0.0.1:8083/playlists")
            .build()
    }
    @Bean
    fun friendWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://127.0.0.1:8085/friends")
            .build()
    }
}