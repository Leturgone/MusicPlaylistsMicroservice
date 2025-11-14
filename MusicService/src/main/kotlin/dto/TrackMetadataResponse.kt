package org.example.dto

import java.time.LocalDateTime

data class TrackMetadataResponse(
    val id: Long?,
    val title: String,
    val artist: String?,
    val durationSec: Int?,
    val sizeBytes: Long?,
    val createdAt: LocalDateTime,
    val ratingAvg: Double?,
    val ratingCount: Long?
)
