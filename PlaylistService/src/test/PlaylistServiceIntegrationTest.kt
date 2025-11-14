import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.containers.PostgreSQLContainer
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class PlaylistServiceIntegrationTest {

    @Container
    companion object {
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:14")
            .apply { start() }
    }

    @DynamicPropertySource
    fun configureProperties(registry: DynamicPropertyRegistry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl)
        registry.add("spring.datasource.username", postgres::getUsername)
        registry.add("spring.datasource.password", postgres::getPassword)
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `createPlaylist should save playlist to database`() {
        // Arrange
        val requestBody = """
            {
                "title": "Test Playlist",
                "ownerId": 1,
                "tracksIds": [10, 20]
            }
        """.trimIndent()

        // Act & Assert
        mockMvc.perform(
            post("/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
    }

    @Test
    fun `getPlaylist should return playlist with tracks`() {
        // Act & Assert
        mockMvc.perform(
            get("/playlists/1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").exists())
    }

    @Test
    fun `updatePlaylist should update playlist title and tracks`() {
        // Arrange
        val requestBody = """
            {
                "title": "Updated Playlist",
                "trackIds": [30, 40]
            }
        """.trimIndent()

        // Act & Assert
        mockMvc.perform(
            patch("/playlists/1")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `getRecommendationPlayList should return recommendation playlist`() {
        // Act & Assert
        mockMvc.perform(
            get("/playlists/recommendation/1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").exists())
    }
}
