import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.example.service.FriendService
import org.example.repository.FriendRepository
import org.example.entities.Friend
import org.example.entities.FriendId
import org.example.clients.ProfileApiClient
import java.time.LocalDateTime

class FriendServiceTest {

    private val friendRepository: FriendRepository = mock(FriendRepository::class.java)
    private val profileApiClient: ProfileApiClient = mock(ProfileApiClient::class.java)
    private val friendService = FriendService(friendRepository, profileApiClient)

    @Test
    fun `add friend should save friend`() {
        // Arrange
        val userId = 1L
        val friendUserId = 2L
        val friendId = FriendId(userId, friendUserId)
        val friend = Friend(id = friendId, createdAt = LocalDateTime.now())

        `when`(profileApiClient.getProfileNameById(friendUserId)).thenReturn("Friend User")
        `when`(friendRepository.save(any(Friend::class.java))).thenReturn(friend)

        // Act
        val result = friendService.addFriend(userId, friendUserId)

        // Assert
        assertTrue(result)
        verify(friendRepository, times(1)).save(any(Friend::class.java))
    }

    @Test
    fun `remove friend should delete friend`() {
        // Arrange
        val userId = 1L
        val friendUserId = 2L
        val friendId = FriendId(userId, friendUserId)

        // Act
        val result = friendService.removeFriend(userId, friendUserId)

        // Assert
        assertTrue(result)
        verify(friendRepository, times(1)).deleteById(friendId)
    }
}
