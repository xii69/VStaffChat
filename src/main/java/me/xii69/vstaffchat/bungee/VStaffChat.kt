package me.xii69.vstaffchat.bungee

import me.xii69.vstaffchat.shared.Utils.Companion.component
import me.xii69.vstaffchat.shared.Utils.Companion.sendMessage
import me.xii69.vstaffchat.shared.config.Config
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler
import java.util.UUID

lateinit var bungeeAdventure: BungeeAudiences

class VStaffChat() : Plugin(), Listener {
    private lateinit var metrics: Metrics
    private val toggledChat = mutableListOf<UUID>()

    override fun onEnable() {
        metrics = Metrics(this, 23665)
        bungeeAdventure = BungeeAudiences.create(this)
        Config(dataFolder.toPath(), this.javaClass).load()
        proxy.pluginManager.registerListener(this, this)
        proxy.pluginManager.registerCommand(this, object : Command("vstaffchat", "vsc", "sc") {
            override fun execute(sender: CommandSender, args: Array<out String>) = onCommand(sender, args)
        })
    }

    fun onCommand(sender: CommandSender, args: Array<out String>) {
        if (sender !is ProxiedPlayer) return sender.sendMessage(Config.onlyInGame.component())
        if (!sender.hasPermission("vstaffchat.send")) return sender.sendMessage(Config.noPermission.component())
        if (args.isEmpty()) return if (toggledChat.remove(sender.uniqueId)) sender.sendMessage(Config.toggleOff.component()) else {
            toggledChat.add(sender.uniqueId)
            sender.sendMessage(Config.toggleOn.component())
        }

        sendChat(args.joinToString(" "), sender)
    }

    @EventHandler
    fun onChat(event: ChatEvent) {
        var message = event.message
        val player = event.sender as ProxiedPlayer

        if (event.isCommand || event.isCancelled) return
        if (!player.hasPermission("vstaffchat.send")) return

        if (toggledChat.contains(player.uniqueId)) {
            event.isCancelled = true
            return sendChat(message, player)
        }

        if (!message.startsWith(Config.prefix)) return

        event.isCancelled = true

        message = message.replace(Config.prefix, "").trimStart()

        if (message != "") sendChat(message, player)
    }

    fun sendChat(message: String, sender: ProxiedPlayer) {
        proxy.players
            .filter { it.hasPermission("vstaffchat.receive") }
            .forEach {
                it.sendMessage(
                    Config.format
                        .replace("[PREFIX]", Config.chatPrefix)
                        .replace("[USERNAME]", sender.name)
                        .replace("[MESSAGE]", message)
                        .component()
                )
            }
    }
}
