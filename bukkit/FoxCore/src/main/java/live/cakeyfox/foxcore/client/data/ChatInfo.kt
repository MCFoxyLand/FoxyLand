package live.cakeyfox.foxcore.client.data

import kotlinx.serialization.Serializable

@Serializable
data class ChatInfo(
    val content: String,
    val author: String
)