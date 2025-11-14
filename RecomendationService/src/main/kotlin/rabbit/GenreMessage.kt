package rabbit

import kotlinx.serialization.Serializable

@Serializable
data class GenreMessage(
    val userId: Long,
    val genre: String
)
