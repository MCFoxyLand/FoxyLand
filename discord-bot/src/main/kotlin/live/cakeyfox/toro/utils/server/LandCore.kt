package live.cakeyfox.toro.utils.server

import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import live.cakeyfox.toro.InoueInstance
import net.dv8tion.jda.api.entities.Activity
import org.slf4j.LoggerFactory

@Serializable
data class ServerStatus(
    val players: Int,
    val tps: Double
)

@Serializable
data class ChatInfo(
    val content: String,
    val author: String
)

class LandCore(private val instance: InoueInstance) {
    private val logger = LoggerFactory.getLogger(LandCore::class.java)
    private val server = embeddedServer(Netty, port = 3000) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(WebSockets)

        routing {
            post("/status") {
                try {
                    val rawJson = call.receive<String>()
                    val status = Json.decodeFromString<ServerStatus>(rawJson)
                    instance.jda.presence.activity = Activity.playing("${status.players} players online | mc.cakeyfox.live")
                    call.respondText(HttpStatusCode.OK.toString())
                } catch (e: SerializationException) {
                    logger.error("Serialization error: ${e.message}", e)
                    call.respondText("Serialization error: ${e.message}", status = HttpStatusCode.BadRequest)
                } catch (e: Exception) {
                    logger.error("Error: ${e.message}", e)
                    call.respondText("Error: ${e.message}", status = HttpStatusCode.BadRequest)
                }
            }

            webSocket("/ws") {
                try {
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                val message = frame.readText()
                                try {
                                    val chatInfo = Json.decodeFromString<ChatInfo>(message)
                                    instance.chat.sendMessage(ChatInfo(chatInfo.content, chatInfo.author))
                                } catch (e: Exception) {
                                    println("Failed to parse JSON: ${e.message}")
                                }
                            }
                            else -> {
                                println("Unsupported frame type")
                            }
                        }
                    }
                } catch (e: Exception) {
                    println("WebSocket error: ${e.message}")
                }
            }
        }
    }

    fun start() {
        logger.info("Starting LandCore server")
        server.start(wait = true)
    }
}
