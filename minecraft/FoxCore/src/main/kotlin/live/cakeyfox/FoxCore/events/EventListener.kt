package live.cakeyfox.FoxCore.events

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import live.cakeyfox.FoxCore.FoxCorePlugin
import live.cakeyfox.FoxCore.client.data.ChatInfo
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.ban.ProfileBanList
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent

class EventListener(val instance: FoxCorePlugin) : Listener {
    @EventHandler
    fun onPlayerChat(event: PlayerChatEvent) {
        val message = event.message
        println("Received message: $message")
        instance.wsClient.sendMessage(ChatInfo(content = message, author = event.player.name))
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        instance.pluginScope.launch {
            try {
                val userResponse = instance.wsClient.getUser(player.name)

                if (userResponse.user.foxy.isBanned) {
                    val banReason = userResponse.user.foxy.banReason ?: "Banido de utilizar a Foxy"
                    val kickMessage = "§c§lVocê foi banido do servidor por: §r§c§l$banReason"
                    Bukkit.getBanList<ProfileBanList>(BanList.Type.PROFILE).apply {
                        addBan(player.name, "§c§l$banReason§r", null, null)
                    }

                    Bukkit.getScheduler().runTask(instance, Runnable {
                        player.kickPlayer(kickMessage)
                    })
                }
            } catch (e: Exception) {
                instance.logger.severe("Erro ao obter informações do jogador: ${e.message}")
                e.printStackTrace()
            }
        }
    }

}