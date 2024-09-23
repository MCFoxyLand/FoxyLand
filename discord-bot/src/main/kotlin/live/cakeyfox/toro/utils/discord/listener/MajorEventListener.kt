package live.cakeyfox.toro.utils.discord.listener

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import live.cakeyfox.toro.InoueInstance
import live.cakeyfox.toro.utils.discord.commands.structures.CommandContext
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MajorEventListener(val instance: InoueInstance): ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        GlobalScope.launch {
            val commandName = event.fullCommandName
            val registeredCommand = instance.commandHandler.commands.find { it.commandName == commandName }

            if (registeredCommand != null) {
                val context = CommandContext(event)
                registeredCommand.execute(context)
            }
        }
    }

    override fun onReady(event: ReadyEvent) {
        instance.commandHandler.handle()
        println("[READY] Bot is ready!")
    }
}