import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.springframework.web.multipart.MultipartFile
import org.example.service.MusicService
import org.example.repository.TrackRepository
import org.example.client.RatingApiClient
import org.example.entity.Track
import org.example.service.TrackEventPublisher
import org.example.service.RecommendationNotifier
import org.example.service.FileStorageService
import java.time.LocalDateTime
import java.util.*

class MusicServiceTest {

    private val trackRepository: TrackRepository = mock(TrackRepository::class.java)
    private val ratingClient: RatingApiClient = mock(RatingApiClient::class.java)
    private val recommendationNotifier: RecommendationNotifier = mock(RecommendationNotifier::class.java)
    private val trackEventPublisher: TrackEventPublisher = mock(TrackEventPublisher::class.java)
    private val fileStorageService: FileStorageService = mock(FileStorageService::class.java)

    private val musicService = MusicService(
        trackRepository,
        ratingClient,
        recommendationNotifier,
        trackEventPublisher,
        fileStorageService
    )

    @Test
    fun `getTrack should return track metadata with rating`() {
        // Arrange
        val trackId = 1L
        val track = Track(
            id = trackId,
            title = "Test Track",
            artist = "Test Artist",
            genre = "Rock",
            durationSec = 180,
            sizeBytes = 1024,
            storagePath = "/path/to/track",
            createdAt = LocalDateTime.now()
        )
        val ratingResponse = org.example.dto.RatingResponse(avg = 4.5, count = 10)

        `when`(trackRepository.findById(trackId)).thenReturn(Optional.of(track))
        `when`(ratingClient.getRatingAvg(trackId)).thenReturn(ratingResponse)

        // Act
        val result = musicService.getTrack(trackId)

        // Assert
        assertNotNull(result)
        assertEquals(trackId, result?.id)
        assertEquals(4.5, result?.ratingAvg)
        assertEquals(10, result?.ratingCount)
    }

    @Test
    fun `uploadTrack should save track and publish event`() {
        // Arrange
        val file: MultipartFile = mock(MultipartFile::class.java)
        val title = "New Track"
        val artist = "New Artist"
        val genre = "Pop"
        val durationSec = 200
        val ownerId = 1L
        val size = 2048L
        val storagePath = "/path/to/new/track"

        val track = Track(
            title = title,
            artist = artist,
            genre = genre,
            durationSec = durationSec,
            ownerId = ownerId,
            sizeBytes = size,
            storagePath = storagePath
        )

        `when`(file.size).thenReturn(size)
        `when`(fileStorageService.saveFile(file)).thenReturn(storagePath)
        `when`(trackRepository.save(any(Track::class.java))).thenReturn(track)

        // Act
        val result = musicService.uploadTrack(file, title, artist, genre, durationSec, ownerId)

        // Assert
        assertNotNull(result)
        verify(trackRepository, times(1)).save(any(Track::class.java))
        verify(trackEventPublisher, times(1)).publishTrackCreated(any())
    }

    @Test
    fun `deleteTrack should delete track and publish event`() {
        // Arrange
        val trackId = 1L
        val requesterId = 1L
        val isAdmin = false
        val track = Track(
            id = trackId,
            title = "Test Track",
            artist = "Test Artist",
            genre = "Rock",
            durationSec = 180,
            ownerId = requesterId,
            sizeBytes = 1024,
            storagePath = "/path/to/track",
            createdAt = LocalDateTime.now()
        )

        `when`(trackRepository.findById(trackId)).thenReturn(Optional.of(track))

        // Act
        val result = musicService.deleteTrack(trackId, requesterId, isAdmin)

        // Assert
        assertTrue(result)
        verify(trackRepository, times(1)).delete(track)
        verify(trackEventPublisher, times(1)).publishTrackDeleted(any())
    }
}
