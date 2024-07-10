package live.cakeyfox.foxcore.events

import live.cakeyfox.foxcore.FoxCorePlugin
import live.cakeyfox.foxcore.client.data.ChatInfo
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChatEvent

class MessageEventListener(val instance: FoxCorePlugin): Listener {
    @EventHandler
    fun onPlayerChat(event: PlayerChatEvent) {
        val message = event.message
        println("Received message: $message")
        instance.wsClient.sendMessage(ChatInfo(content = message, author = event.player.name))
    }
}