import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.example.service.RatingService
import org.example.repository.RatingRepository
import org.example.repository.DownloadCountRepository
import org.example.entities.Rating
import org.example.entities.DownloadCount
import java.time.LocalDateTime

class RatingServiceTest {

    private val ratingRepository: RatingRepository = mock(RatingRepository::class.java)
    private val downloadCountRepository: DownloadCountRepository = mock(DownloadCountRepository::class.java)
    private val ratingService = RatingService(ratingRepository, downloadCountRepository)

    @Test
    fun `set rating should save rating`() {
        // Arrange
        val userId = 1L
        val trackId = 10L
        val stars = 5
        val rating = Rating(userId = userId, trackId = trackId, stars = stars, updatedAt = LocalDateTime.now())

        `when`(ratingRepository.save(any(Rating::class.java))).thenReturn(rating)

        // Act
        val result = ratingService.setRating(trackId, stars, userId)

        // Assert
        assertNotNull(result)
        verify(ratingRepository, times(1)).save(any(Rating::class.java))
    }

    @Test
    fun `delete rating should remove rating`() {
        // Arrange
        val userId = 1L
        val trackId = 10L
        val rating = Rating(userId = userId, trackId = trackId, stars = 5, updatedAt = LocalDateTime.now())

        `when`(ratingRepository.findRatingByUserIdAndTrackId(userId, trackId)).thenReturn(java.util.Optional.of(rating))

        // Act
        val result = ratingService.deleteRating(trackId, userId)

        // Assert
        assertTrue(result)
        verify(ratingRepository, times(1)).delete(any(Rating::class.java))
    }
}
