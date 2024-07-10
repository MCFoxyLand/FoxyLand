package live.cakeyfox.toro.utils.discord

import live.cakeyfox.toro.InoueInstance
import live.cakeyfox.toro.utils.server.ChatInfo

class ChatManager(val instance: InoueInstance) {
    fun sendMessage(message: ChatInfo) {
        instance.jda.getTextChannelById(instance.config.getLogChannel())!!.sendMessage(message.content).queue()
    }
}