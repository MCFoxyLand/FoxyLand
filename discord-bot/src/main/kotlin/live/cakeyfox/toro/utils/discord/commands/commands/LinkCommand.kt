package live.cakeyfox.toro.utils.discord.commands.commands

import live.cakeyfox.toro.InoueInstance
import live.cakeyfox.toro.utils.discord.commands.structures.CommandBase
import live.cakeyfox.toro.utils.discord.commands.structures.CommandContext
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class LinkCommand(val instance: InoueInstance): CommandBase() {
    override val commandName = "link"
    override val commandDescription = "Vincule sua conta do Minecraft com a do Discord"
    override val options = listOf<OptionData>(
        OptionData(OptionType.STRING, "nick", "Seu nick no Minecraft", true)
    )

    override suspend fun execute(context: CommandContext) {
        val messageContent = context.event.getOption("nick")!!.asString
        val result = instance.mongoClient.linkMinecraftAccount(context.event.user.id, messageContent)

        if (result == true) {
            context.reply(true) {
                content = "Sua conta foi vinculada com sucesso!"
            }
        } else {
            context.reply(true) {
                content = "Não foi possível vincular sua conta. Verifique se você já vinculou sua conta ou se o nick está correto."
            }
        }
    }
}