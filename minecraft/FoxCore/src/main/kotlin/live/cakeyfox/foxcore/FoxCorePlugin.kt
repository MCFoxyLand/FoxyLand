package live.cakeyfox.foxcore

import live.cakeyfox.foxcore.client.FoxCoreClient
import live.cakeyfox.foxcore.client.data.ServerStatus
import live.cakeyfox.foxcore.commands.ConnectToWebsocketCommand
import live.cakeyfox.foxcore.events.MessageEventListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class FoxCorePlugin : JavaPlugin() {
    val wsClient = FoxCoreClient()

    override fun onEnable() {
        logger.info("Starting FoxCore plugin")
        wsClient.start()
        wsClient.sendServerStatus(ServerStatus(players= server.onlinePlayers.size, tps = 20.0))

        server.pluginManager.registerEvents(MessageEventListener(this), this)

        getCommand("connectToWebsocket")?.setExecutor(ConnectToWebsocketCommand(this))
        scheduleStatusUpdateTask()
    }

    private fun scheduleStatusUpdateTask() {
        val interval = 5 * 60 * 20L
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            wsClient.sendServerStatus(ServerStatus(players= server.onlinePlayers.size, tps = 20.0))
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
}