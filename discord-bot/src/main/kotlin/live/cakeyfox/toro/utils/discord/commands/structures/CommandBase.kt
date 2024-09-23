package live.cakeyfox.toro.utils.discord.commands.structures

import net.dv8tion.jda.api.interactions.commands.build.OptionData

abstract class CommandBase {
    abstract val commandName: String
    abstract val commandDescription: String
    abstract val options: List<OptionData>
    abstract suspend fun execute(context: CommandContext)
}