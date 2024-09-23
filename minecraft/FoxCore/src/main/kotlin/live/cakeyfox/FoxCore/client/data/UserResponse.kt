package live.cakeyfox.FoxCore.client.data

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val status: Int,
    val user: User
)

@Serializable
data class User(
    val minecraft: MinecraftUser,
    val foxy: FoxyUser
)

@Serializable
data class MinecraftUser(
    val username: String
)

@Serializable
data class FoxyUser(
    val id: String,
    val isBanned: Boolean,
    val banDate: String?,
    val banReason: String?,
)