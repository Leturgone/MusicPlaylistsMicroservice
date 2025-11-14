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
class ProfileServiceIntegrationTest {

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
    fun `create profile should save profile to database`() {
        // Arrange
        val requestBody = """
            {
                "userId": 1,
                "displayName": "Test User",
                "bio": "Test Bio"
            }
        """.trimIndent()

        // Act & Assert
        mockMvc.perform(
            post("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.displayName").value("Test User"))
    }

    @Test
    fun `get profile should return profile data`() {
        // Act & Assert
        mockMvc.perform(
            get("/profile/1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.displayName").value("Test User"))
    }
}
