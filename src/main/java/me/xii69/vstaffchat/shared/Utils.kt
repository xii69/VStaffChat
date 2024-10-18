package me.xii69.vstaffchat.shared

import me.xii69.vstaffchat.bukkit.bukkitAdventure
import me.xii69.vstaffchat.bungee.bungeeAdventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.connection.ProxiedPlayer
import org.bukkit.entity.Player

class Utils {
    companion object {
        fun String.component() = MiniMessage.miniMessage().deserialize(this)

        fun Player.sendMessage(component: Component) = bukkitAdventure.player(this).sendMessage(component)

        fun org.bukkit.command.CommandSender.sendMessage(component: Component) = bukkitAdventure.console().sendMessage(component)

        fun net.md_5.bungee.api.CommandSender.sendMessage(component: Component) = bungeeAdventure.console().sendMessage(component)

        fun ProxiedPlayer.sendMessage(component: Component) = bungeeAdventure.player(this).sendMessage(component)
    }
}
