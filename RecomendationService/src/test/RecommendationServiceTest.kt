import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.example.service.RecommendationService
import org.example.repository.UserRecommendationsRepository
import org.example.entities.UserRecommendations

class RecommendationServiceTest {

    private val userRecommendationsRepository: UserRecommendationsRepository = mock(UserRecommendationsRepository::class.java)
    private val recommendationService = RecommendationService(userRecommendationsRepository)

    @Test
    fun `analyze recommendations should update recommendations`() {
        // Arrange
        val userId = 1L
        val genre = "Rock"
        val recommendations = UserRecommendations(userId = userId, recGenre = genre, recommendationJson = "[]")

        `when`(userRecommendationsRepository.save(any(UserRecommendations::class.java))).thenReturn(recommendations)

        // Act
        val result = recommendationService.analyzeRecommendations(userId, genre)

        // Assert
        assertNotNull(result)
        verify(userRecommendationsRepository, times(1)).save(any(UserRecommendations::class.java))
    }
}
