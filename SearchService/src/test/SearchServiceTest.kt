import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.example.service.SearchService
import org.example.repository.TrackRepository
import org.example.entities.Track

class SearchServiceTest {

    private val trackRepository: TrackRepository = mock(TrackRepository::class.java)
    private val searchService = SearchService(trackRepository)

    @Test
    fun `search by artist should return tracks`() {
        // Arrange
        val artist = "The Beatles"
        val track = Track(id = 1L, title = "Hey Jude", artist = artist, genre = "Rock")
        val tracks = listOf(track)

        `when`(trackRepository.findByArtistContainingIgnoreCase(artist)).thenReturn(tracks)

        // Act
        val result = searchService.searchByArtist(artist)

        // Assert
        assertNotNull(result)
        assertEquals(1, result.tracks.size)
    }
}
