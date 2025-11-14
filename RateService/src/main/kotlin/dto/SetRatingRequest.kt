package org.example.dto

data class SetRatingRequest(
    val trackId: Long,
    val stars: Int,
    val userId: Long
)
