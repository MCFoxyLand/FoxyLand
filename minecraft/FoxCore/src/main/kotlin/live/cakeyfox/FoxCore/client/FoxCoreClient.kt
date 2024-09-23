package live.cakeyfox.FoxCore.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import live.cakeyfox.FoxCore.FoxCorePlugin
import live.cakeyfox.FoxCore.client.data.*
import org.bukkit.Bukkit

class FoxCoreClient(val instance: FoxCorePlugin) {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }
    var session: WebSocketSession? = null

    fun start() {
        instance.logger.info("Starting FoxCore client")
        instance.pluginScope.launch {
            try {
                client.webSocket(HttpMethod.Get, host = "localhost", port = 3000, path = "/ws") {
                    session = this
                    handleIncomingFrames()
                }
            } catch (e: Exception) {
                instance.logger.severe("Error starting FoxCore client: ${e.message}")
            }
        }
    }

    private suspend fun DefaultWebSocketSession.handleIncomingFrames() {
        incoming.consumeEach { frame ->
            if (frame is Frame.Text) {
                val message = frame.readText()
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), message)
                send("Command executed!")
            } else {
                instance.logger.warning("Unsupported frame type: ${frame.javaClass.simpleName}")
            }
        }
    }

    fun sendMessage(chatInfo: ChatInfo) {
        instance.pluginScope.launch {
            session?.let {
                try {
                    val jsonMessage = Json.encodeToString(chatInfo)
                    instance.logger.info("Sending message: $jsonMessage")
                    it.send(Frame.Text(jsonMessage))
                } catch (e: Exception) {
                    instance.logger.warning("Error sending message: ${e.message}")
                }
            } ?: instance.logger.warning("No active session to send message")
        }
    }

    fun sendServerStatus(status: ServerStatus) {
        instance.pluginScope.launch {
            try {
                val response = client.post("http://localhost:3000/status") {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(status))
                }
                instance.logger.info("Server status sent: ${response.status}")
            } catch (e: Exception) {
                instance.logger.warning("Error sending server status: ${e.message}")
            }
        }
    }

    suspend fun getUser(username: String): UserResponse {
        return try {
            val response = client.get("${instance.config.getString("api-url")}/user/minecraft/$username") {
                headers {
                    append("Authorization", instance.config.getString("api-key") ?: "")
                }
            }
            Json.decodeFromString<UserResponse>(response.bodyAsText())
        } catch (e: Exception) {
            instance.logger.severe("Failed to fetch user data for $username: ${e.message}")
            UserResponse(status = 500, user = User(MinecraftUser(username = ""), FoxyUser(id = "", isBanned = false, banDate = null, banReason = null)))
        }
    }
}