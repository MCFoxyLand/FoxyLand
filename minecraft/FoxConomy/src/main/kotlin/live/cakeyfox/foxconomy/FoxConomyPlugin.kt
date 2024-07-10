package live.cakeyfox.foxconomy

import org.bukkit.plugin.java.JavaPlugin

class FoxConomyPlugin: JavaPlugin() {
    override fun onEnable() {
        logger.info("Starting FoxConomy plugin")
    }

    override fun onDisable() {
        logger.info("Stopping FoxConomy plugin")
    }
}