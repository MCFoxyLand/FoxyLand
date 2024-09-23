package live.cakeyfox.FoxCore.client.data

import kotlinx.serialization.Serializable

@Serializable
data class ChatInfo(
    val content: String,
    val author: String,
)