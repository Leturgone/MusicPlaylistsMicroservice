import data.DatabaseRepository
import data.UserRecommendation
import data.UserRecommendationsTable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import rabbit.RabbitConsumer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import data.RecommendationsRespond

fun main() {
    embeddedServer(Netty, port = 8082, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    val client = HttpClient {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json()
        }
    }

    val logger = LoggerFactory.getLogger(this::class.java)

    val databse = Database.connect(
        url = System.getenv("DB_URL"),
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER") ?: "postgres",
        password = System.getenv("DB_PASSWORD") ?: "root"
    )
    val repository = DatabaseRepository(databse)


    transaction {
        SchemaUtils.create(UserRecommendationsTable)
    }

    val rabbitConsumer = RabbitConsumer("recommendation.queue") { genreMessage ->
        logger.info("Received message: {}", genreMessage)
        val userId = genreMessage.userId
        val genre = genreMessage.genre
        repository.saveRecommendations(UserRecommendation(userId,genre))
    }
    rabbitConsumer.subscribe()

    routing {
        route("/recommendations") {
            post("/analyze/{userId}") {
                val userIdParam = call.parameters["userId"]
                val userId = userIdParam?.toLongOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid userId")
                val recommendationGenre = repository.getRecommendationGenre(userId)
                if (recommendationGenre == null) {
                    return@post call.respond(HttpStatusCode.NotFound, "Recommendation not found")
                }
                val songIds: List<Long> = client.get("http://127.0.0.1:8080/music/tracks/$recommendationGenre").body()
                val selectedIds = songIds.shuffled().take(5)

                val responseObject = RecommendationsRespond(selectedIds)
                val jsonString = Json.encodeToString(responseObject)
                repository.setRecommendationJson(userId,jsonString )

                call.respond(HttpStatusCode.OK, "Recommendations analysed")

            }
            get("/{userId}") {
                val mapper = jacksonObjectMapper()
                val userIdParam = call.parameters["userId"]
                val userId = userIdParam?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid userId")
                val selectedIdsJson =
                    repository.getRecommendationJson(userId = userId) ?: ("{" + "\"recommendationsIdsList\": []" + "}")

                val response: RecommendationsRespond = try {
                    mapper.readValue(selectedIdsJson, RecommendationsRespond::class.java)
                } catch (e: Exception) {
                    return@get call.respond(HttpStatusCode.InternalServerError, "Invalid JSON format")
                }
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}