package live.cakeyfox.FoxCore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import live.cakeyfox.FoxCore.client.FoxCoreClient
import live.cakeyfox.FoxCore.client.data.ServerStatus
import live.cakeyfox.FoxCore.commands.ConnectToWebsocketCommand
import live.cakeyfox.FoxCore.events.EventListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class FoxCorePlugin : JavaPlugin() {
    val wsClient = FoxCoreClient(this)
    val pluginScope = CoroutineScope(Dispatchers.IO)

    override fun onEnable() {
        logger.info("Starting FoxCore plugin")

        server.pluginManager.registerEvents(EventListener(this), this)
        pluginScope.launch {
            wsClient.start()
            wsClient.sendServerStatus(ServerStatus(players = server.onlinePlayers.size, tps = 20.0))
        }
        getCommand("connectToWebsocket")?.setExecutor(ConnectToWebsocketCommand(this))
        scheduleStatusUpdateTask()
    }

    private fun scheduleStatusUpdateTask() {
        val interval = 5 * 60 * 20L
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            wsClient.sendServerStatus(ServerStatus(players = server.onlinePlayers.size, tps = 20.0))
            wsClient.session ?: run {
                logger.warning("No websocket session found, attempting to start a new one.")
                try {
                    wsClient.start()
                    logger.info("Websocket session started")
                } catch (e: Exception) {
                    logger.warning("Failed to start the websocket session")
                }
            }
        }, interval, interval)
    }

    override fun onDisable() {
        pluginScope.cancel()
    }
    public fun makeReply(message: String): String {
        return "§d§l[§r§c§lFoxCore§d§l] §r$message"
    }
}