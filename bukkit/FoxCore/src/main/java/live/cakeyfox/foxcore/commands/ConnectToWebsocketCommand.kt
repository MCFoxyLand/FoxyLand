package live.cakeyfox.foxcore.commands

import live.cakeyfox.foxcore.FoxCorePlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ConnectToWebsocketCommand(val instance: FoxCorePlugin): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (instance.wsClient.session != null) {
            sender.sendMessage("Active session already exists")
            return false
        } else {
            instance.wsClient.start()
            sender.sendMessage("Session started")
        }
        return true
    }
}