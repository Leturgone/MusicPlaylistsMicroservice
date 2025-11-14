import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.example.service.ProfileService
import org.example.repository.ProfileRepository
import org.example.entities.Profile
import org.example.clients.FriendsApiClient
import org.example.clients.PlaylistApiClient

class ProfileServiceTest {

    private val profileRepository: ProfileRepository = mock(ProfileRepository::class.java)
    private val friendsApiClient: FriendsApiClient = mock(FriendsApiClient::class.java)
    private val playlistApiClient: PlaylistApiClient = mock(PlaylistApiClient::class.java)
    private val profileService = ProfileService(profileRepository, friendsApiClient, playlistApiClient)

    @Test
    fun `create profile should save profile`() {
        // Arrange
        val userId = 1L
        val displayName = "Test User"
        val bio = "Test Bio"
        val profile = Profile(userId = userId, displayName = displayName, bio = bio)

        `when`(profileRepository.save(any(Profile::class.java))).thenReturn(profile)

        val result = profileService.createProfile(userId, displayName, bio)

        assertNotNull(result)
        assertEquals(displayName, result.displayName)
        verify(profileRepository, times(1)).save(any(Profile::class.java))
    }

    @Test
    fun `get profile should return profile data`() {
        // Arrange
        val userId = 1L
        val profile = Profile(userId = userId, displayName = "Test User", bio = "Test Bio")

        `when`(profileRepository.findById(userId)).thenReturn(java.util.Optional.of(profile))

        // Act
        val result = profileService.getProfile(userId)

        // Assert
        assertNotNull(result)
        assertEquals("Test User", result?.displayName)
    }
}
