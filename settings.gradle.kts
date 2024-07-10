plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "foxland"
include("bukkit:FoxCore")
include("bukkit:FoxConomy")
include("discord-bot")
include("bukkit:FoxAuth")
