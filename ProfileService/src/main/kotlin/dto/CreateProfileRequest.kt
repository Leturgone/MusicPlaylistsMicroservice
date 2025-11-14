package org.example.dto

data class CreateProfileRequest(
    val userId: Long,
    val displayName: String,
    val bio: String
)
