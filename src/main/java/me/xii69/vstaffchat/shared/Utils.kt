package me.xii69.vstaffchat.shared

import me.xii69.vstaffchat.bukkit.adventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Utils {
    companion object {
        fun String.component() = MiniMessage.miniMessage().deserialize(this)

        fun Player.sendMessage(component: Component) = adventure.player(this).sendMessage(component)

        fun CommandSender.sendMessage(component: Component) = adventure.console().sendMessage(component)
    }
}
