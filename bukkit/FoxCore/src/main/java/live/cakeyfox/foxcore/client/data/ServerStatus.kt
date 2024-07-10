package live.cakeyfox.foxcore.client.data

import kotlinx.serialization.Serializable

@Serializable

data class ServerStatus(
    val players: Int,
    val tps: Double
)