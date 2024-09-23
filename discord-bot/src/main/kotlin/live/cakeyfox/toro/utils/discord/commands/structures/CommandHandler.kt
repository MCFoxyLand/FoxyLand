package live.cakeyfox.toro.utils.discord.commands.structures

import live.cakeyfox.toro.InoueInstance
import dev.minn.jda.ktx.interactions.commands.Command
import live.cakeyfox.toro.utils.discord.commands.commands.LinkCommand

class CommandHandler(private val instance: InoueInstance) {
    val commands = mutableListOf<CommandBase>()

    fun register(commandBase: CommandBase) {
        commands.add(commandBase)
    }

    fun handle() {
        val commandUpdater = instance.jda.updateCommands()

        commands.forEach {
            commandUpdater.addCommands(
                Command(it.commandName, it.commandDescription) {
                    addOptions(it.options)
                }
            )
        }

        commandUpdater.queue()
    }

    init {
        register(LinkCommand(instance))
    }}