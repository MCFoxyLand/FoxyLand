package live.cakeyfox.toro.utils.discord.commands.structures

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.InlineMessage
import dev.minn.jda.ktx.messages.MessageCreateBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class CommandContext(
    val event: SlashCommandInteractionEvent
) {
    suspend fun reply(ephemeral: Boolean = false, block: InlineMessage<*>.() -> Unit): Message? {
        val msg = MessageCreateBuilder {
            apply(block)
        }

        if (event.isAcknowledged) {
            return event.hook.sendMessage(msg.build()).await()
        } else {
            val defer = event.deferReply(ephemeral).await()

            return defer.sendMessage(msg.build()).await()
        }
    }
}