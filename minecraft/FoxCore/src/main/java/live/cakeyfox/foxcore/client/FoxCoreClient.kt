package live.cakeyfox.foxcore.client

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
import live.cakeyfox.foxcore.FoxCorePlugin
import live.cakeyfox.foxcore.client.data.ChatInfo
import live.cakeyfox.foxcore.client.data.ServerStatus

class FoxCoreClient(private val instance: FoxCorePlugin) {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }
    var session: WebSocketSession? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    fun start() {
        println("Starting FoxCore client")
        scope.launch {
            client.webSocket(
                method = HttpMethod.Get,
                host = "localhost",
                port = 3000,
                path = "/ws"
            ) {
                session = this
                try {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            println("Received: ${frame.readText()}")
                        } else {
                            println("Unsupported frame type")
                        }
                    }
                } catch (e: Exception) {
                    println("WebSocket error: ${e.message}")
                    e.printStackTrace()
                } finally {
                    session = null
                }
            }
        }
    }

    fun sendMessage(chatInfo: ChatInfo) {
        scope.launch {
            session?.let {
                try {
                    val jsonMessage = Json.encodeToString(chatInfo)
                    println("Sending message: $jsonMessage")
                    it.send(Frame.Text(jsonMessage))
                } catch (e: Exception) {
                    println("Error sending message: ${e.message}")
                    e.printStackTrace()
                }
            } ?: println("No active session")
        }
    }

    fun sendServerStatus(status: ServerStatus) {
        scope.launch {
            try {
                val response = client.post("http://localhost:3000/status") {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(status))
                }
                println("Status update response: ${response.status}")
            } catch (e: Exception) {
                println("Error sending server status: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
