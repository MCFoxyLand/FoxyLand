package live.cakeyfox.toro

import live.cakeyfox.toro.utils.ConfigManager
import live.cakeyfox.toro.utils.discord.ChatManager
import live.cakeyfox.toro.utils.server.LandCore
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder

class InoueInstance {
    lateinit var jda: JDA
    lateinit var chat: ChatManager
    lateinit var config: ConfigManager

    fun start() {
        config = ConfigManager()
        jda = JDABuilder.createDefault(config.getToken()).build().also {
            it.awaitReady()
        }
        chat = ChatManager(this)
        LandCore(this).start()
    }
}