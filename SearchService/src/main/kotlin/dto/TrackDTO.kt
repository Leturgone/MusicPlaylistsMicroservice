package org.example.dto

data class TrackDTO (
    val id: Long? = null,
    val title: String,
    val artist: String,
    val genre: String? = null
)