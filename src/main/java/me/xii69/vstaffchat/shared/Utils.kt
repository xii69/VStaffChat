package me.xii69.vstaffchat.shared

import net.kyori.adventure.text.minimessage.MiniMessage

class Utils {
    companion object {
        fun String.component() = MiniMessage.miniMessage().deserialize(this)
    }
}
