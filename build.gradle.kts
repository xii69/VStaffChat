plugins {
    kotlin("jvm") version "2.0.21"
    id("com.gradleup.shadow") version "8.3.3"
}

group = "me.xii69"
version = "2.0.0"
description = "VStaffChat"
val author = "xii69"
val slug = "vstaffchat"
val bukkitMain = "me.xii69.vstaffchat.bukkit.VStaffChat"
val bungeeMain = "me.xii69.vstaffchat.bungee.VStaffChat"
val velocityMain = "me.xii69.vstaffchat.velocity.VStaffChat"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    processResources {
        filesMatching(listOf("plugin.yml", "bungee.yml", "velocity-plugin.json")) {
            expand(
                "name" to rootProject.name,
                "version" to version,
                "author" to author,
                "slug" to slug,
                "bukkitMain" to bukkitMain,
                "bungeeMain" to bungeeMain,
                "velocityMain" to velocityMain,
                "description" to rootProject.description
            )
        }
    }
}
