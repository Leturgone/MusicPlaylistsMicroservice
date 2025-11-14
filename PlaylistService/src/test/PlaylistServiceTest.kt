import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.example.service.PlaylistService
import org.example.repository.PlaylistRepository
import org.example.repository.PlaylistTrackRepository
import org.example.client.MusicApiClient
import org.example.client.ProfileApiClient
import org.example.client.RecApiClient
import org.example.entity.Playlist
import org.example.entity.PlaylistTrack
import org.example.entity.PlaylistTrackId
import org.example.dto.PlaylistRespond
import org.example.dto.TrackMetadataResponse
import org.example.dto.RecommendationsRespond
import java.time.LocalDateTime
import java.util.*

class PlaylistServiceTest {

    private val playlistRepository: PlaylistRepository = mock(PlaylistRepository::class.java)
    private val playlistTrackRepository: PlaylistTrackRepository = mock(PlaylistTrackRepository::class.java)
    private val musicApiClient: MusicApiClient = mock(MusicApiClient::class.java)
    private val profileApiClient: ProfileApiClient = mock(ProfileApiClient::class.java)
    private val recApiClient: RecApiClient = mock(RecApiClient::class.java)

    private val playlistService = PlaylistService(
        playlistRepository,
        playlistTrackRepository,
        musicApiClient,
        profileApiClient,
        recApiClient
    )

    @Test
    fun `getPlaylistWithTracks should return playlist with tracks`() {
        // Arrange
        val playlistId = 1L
        val playlist = Playlist(id = playlistId, title = "Test Playlist", ownerId = 1L)
        val playlistTrack = PlaylistTrack(id = PlaylistTrackId(playlistId, 10L), position = 0)
        val trackMetadata = TrackMetadataResponse(id = 10L, title = "Test Track")

        `when`(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist))
        `when`(playlistTrackRepository.findAllByIdPlaylistId(playlistId)).thenReturn(listOf(playlistTrack))
        `when`(profileApiClient.getProfileNameById(playlist.ownerId)).thenReturn("Test User")
        `when`(musicApiClient.getTrackById(10L)).thenReturn(trackMetadata)

        // Act
        val result = playlistService.getPlaylistWithTracks(playlistId)

        // Assert
        assertNotNull(result)
        assertEquals(playlistId, result?.id)
        assertEquals("Test Playlist", result?.title)
        assertEquals(1, result?.tracks?.size)
    }

    @Test
    fun `createPlaylist should save playlist and tracks`() {
        // Arrange
        val title = "New Playlist"
        val ownerId = 1L
        val tracksIds = listOf(10L, 20L)
        val playlist = Playlist(id = 1L, title = title, ownerId = ownerId)

        `when`(playlistRepository.save(any(Playlist::class.java))).thenReturn(playlist)

        // Act
        val result = playlistService.createPlaylist(title, ownerId, tracksIds)

        // Assert
        assertNotNull(result)
        assertEquals(1L, result)
        verify(playlistRepository, times(1)).save(any(Playlist::class.java))
        verify(playlistTrackRepository, times(2)).save(any(PlaylistTrack::class.java))
    }

    @Test
    fun `updatePlaylist should update playlist title and tracks`() {
        // Arrange
        val playlistId = 1L
        val newTitle = "Updated Playlist"
        val newTrackIds = listOf(30L, 40L)
        val userId = 1L
        val playlist = Playlist(id = playlistId, title = "Old Playlist", ownerId = userId)

        `when`(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist))

        // Act
        val result = playlistService.updatePlaylist(playlistId, newTitle, newTrackIds, userId)

        // Assert
        assertTrue(result)
        assertEquals(newTitle, playlist.title)
        verify(playlistTrackRepository, times(1)).deleteAll(any())
        verify(playlistTrackRepository, times(2)).save(any(PlaylistTrack::class.java))
    }

    @Test
    fun `getRecommendationPlayList should return recommendation playlist`() {
        // Arrange
        val userId = 1L
        val playlist = Playlist(id = 1L, title = "Recommendations", ownerId = 1L)
        val recommendations = RecommendationsRespond(recommendationsIdsList = listOf(10L, 20L))
        val trackMetadata = TrackMetadataResponse(id = 10L, title = "Test Track")

        `when`(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist))
        `when`(recApiClient.analyze(userId)).thenReturn("success")
        `when`(recApiClient.getRecommendations(userId)).thenReturn(recommendations)
        `when`(musicApiClient.getTrackById(10L)).thenReturn(trackMetadata)
        `when`(musicApiClient.getTrackById(20L)).thenReturn(trackMetadata.copy(id = 20L))

        // Act
        val result = playlistService.getRecommendationPlayList(userId)

        // Assert
        assertNotNull(result)
        assertEquals(2, result?.tracks?.size)
    }
}
