plugins {
    id("java")
    kotlin("plugin.serialization") version "1.8.0"
}

group = "live.cakeyfox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "live.cakeyfox.foxcore.FoxCorePlugin"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(configurations.compileClasspath.get().filter { it.name.endsWith(".jar") }.map { zipTree(it) })
}