import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
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
class MusicServiceIntegrationTest {

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
    fun `uploadTrack should save track to database`() {
        // Arrange
        val file = MockMultipartFile(
            "file",
            "test.mp3",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "test data".toByteArray()
        )

        // Act & Assert
        mockMvc.perform(
            multipart("/music")
                .file(file)
                .param("title", "Test Track")
                .param("artist", "Test Artist")
                .param("genre", "Rock")
                .param("durationSec", "180")
                .header("X-User-Id", "1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
    }

    @Test
    fun `getTrack should return track metadata`() {
        // Act & Assert
        mockMvc.perform(
            get("/music/1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").exists())
    }

    @Test
    fun `deleteTrack should delete track from database`() {
        // Act & Assert
        mockMvc.perform(
            delete("/music/1")
                .header("X-User-Id", "1")
                .header("X-User-Role", "admin")
        )
            .andExpect(status().isOk)
    }
}
