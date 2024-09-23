package live.cakeyfox.FoxCore.commands

import live.cakeyfox.FoxCore.FoxCorePlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ConnectToWebsocketCommand(val instance: FoxCorePlugin): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        return if (instance.wsClient.session != null) {
            sender.sendMessage(instance.makeReply("Já existe uma sessão ativa"))
            false
        } else {
            try {
                instance.wsClient.start()
                sender.sendMessage(instance.makeReply("A sessão foi iniciada!"))
                true
            } catch (e: Exception) {
                sender.sendMessage(instance.makeReply("Falha ao iniciar a sessão: ${e.message}"))
                false
            }
        }
    }
}