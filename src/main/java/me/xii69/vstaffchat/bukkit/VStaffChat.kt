package me.xii69.vstaffchat.bukkit

import me.xii69.vstaffchat.shared.Utils.Companion.component
import me.xii69.vstaffchat.shared.Utils.Companion.sendMessage
import me.xii69.vstaffchat.shared.config.Config
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

lateinit var bukkitAdventure: BukkitAudiences

class VStaffChat() : JavaPlugin(), Listener, CommandExecutor {
    private lateinit var metrics: Metrics
    private val toggledChat = mutableListOf<UUID>()

    override fun onEnable() {
        metrics = Metrics(this, 23666)
        bukkitAdventure = BukkitAudiences.create(this)
        Config(dataFolder.toPath(), this.javaClass).load()
        getCommand("vstaffchat")!!.setExecutor(this)
        server.pluginManager.registerEvents(this, this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Config.onlyInGame.component())
            return true
        }

        if (!sender.hasPermission("vstaffchat.send")) {
            sender.sendMessage(Config.noPermission.component())
            return true
        }

        if (args.isEmpty()) {
            if (toggledChat.remove(sender.uniqueId)) sender.sendMessage(Config.toggleOff.component()) else {
                toggledChat.add(sender.uniqueId)
                sender.sendMessage(Config.toggleOn.component())
            }
            return true
        }

        sendChat(args.joinToString(" "), sender)
        return false
    }

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        if (event.isCancelled) return
        if (!event.player.hasPermission("vstaffchat.send")) return

        var message = event.message

        if (toggledChat.contains(event.player.uniqueId)) {
            sendChat(message, event.player)
            event.isCancelled = true
            return
        }

        if (!message.startsWith(Config.prefix)) return

        event.isCancelled = true

        message = message.replace(Config.prefix, "").trimStart()

        if (message != "") sendChat(message, event.player)
    }

    fun sendChat(message: String, sender: Player) {
        Bukkit.getOnlinePlayers().filter { it.hasPermission("vstaffchat.receive") }.forEach {
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
