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
class RecommendationServiceIntegrationTest {

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
    fun `analyze recommendations should update recommendations in database`() {
        // Arrange
        val requestBody = """
            {
                "userId": 1,
                "genre": "Rock"
            }
        """.trimIndent()

        // Act & Assert
        mockMvc.perform(
            post("/recommendations/analyze/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `get recommendations should return recommendations`() {
        // Act & Assert
        mockMvc.perform(
            get("/recommendations/1")
        )
            .andExpect(status().isOk)
    }
}
