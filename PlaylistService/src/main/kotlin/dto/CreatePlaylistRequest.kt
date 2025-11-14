package org.example.dto

data class CreatePlaylistRequest(
    val title: String,
    val ownerId: Long,
    val tracksIds: List<Long>
)
