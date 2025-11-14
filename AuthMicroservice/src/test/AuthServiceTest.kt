import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.example.service.AuthService
import org.example.repository.UserRepository
import org.example.entities.User
import java.time.LocalDateTime

class AuthServiceTest {

    private val userRepository: UserRepository = mock(UserRepository::class.java)
    private val passwordEncoder: PasswordEncoder = mock(PasswordEncoder::class.java)
    private val authService = AuthService(userRepository, passwordEncoder)

    @Test
    fun `register user should save user with encoded password`() {
        // Arrange
        val email = "test@example.com"
        val password = "password1337"
        val encodedPassword = "encodedPassword"
        val user = User(email = email, passwordHash = encodedPassword)

        `when`(passwordEncoder.encode(password)).thenReturn(encodedPassword)
        `when`(userRepository.save(any(User::class.java))).thenReturn(user)

        // Act
        val result = authService.register(email, password)

        // Assert
        assertNotNull(result)
        assertEquals(email, result.email)
        verify(userRepository, times(1)).save(any(User::class.java))
    }

    @Test
    fun `register user with invalid email should throw exception`() {
        // Arrange
        val invalidEmail = "invalidEmail"
        val password = "password1337"

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            authService.register(invalidEmail, password)
        }
    }
}
