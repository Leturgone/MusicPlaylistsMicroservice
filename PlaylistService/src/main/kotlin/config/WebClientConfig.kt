package org.example.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class WebClientConfig {

    @Bean
    fun musicWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://127.0.0.1:8080/music")
            .build()
    }
    @Bean
    fun profileWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://127.0.0.1:8084/profile")
            .build()
    }
    @Bean
    fun recWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://127.0.0.1:8082/recommendations")
            .build()
    }
}