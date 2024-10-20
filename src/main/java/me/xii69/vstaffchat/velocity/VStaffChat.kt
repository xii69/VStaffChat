package me.xii69.vstaffchat.velocity

import com.google.inject.Inject
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import me.xii69.vstaffchat.shared.Utils.Companion.component
import me.xii69.vstaffchat.shared.config.Config
import me.xii69.vstaffchat.velocity.Metrics.Factory
import java.nio.file.Path
import java.util.UUID

class VStaffChat @Inject constructor(
    val server: ProxyServer,
    val metricsFactory: Factory,
    @DataDirectory val dataDirectory: Path
) : SimpleCommand {
    private val toggledChat = mutableListOf<UUID>()

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        metricsFactory.make(this, 23664)
        Config(dataDirectory, javaClass).load()
        server.commandManager.register(server.commandManager.metaBuilder("vstaffchat").aliases("vsc", "sc").build(), this)
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source: CommandSource = invocation.source()
        val args: Array<String> = invocation.arguments()

        if (source !is Player) return source.sendMessage(Config.onlyInGame.component())
        if (!source.hasPermission("vstaffchat.send")) return source.sendMessage(Config.noPermission.component())
        if (args.isEmpty()) return if (toggledChat.remove(source.uniqueId)) source.sendMessage(Config.toggleOff.component()) else {
            toggledChat.add(source.uniqueId)
            source.sendMessage(Config.toggleOn.component())
        }

        sendChat(args.joinToString(" "), source)
    }

    @Subscribe
    fun onPlayerChat(event: PlayerChatEvent) {
        var message = event.message

        if (!event.result.isAllowed) return
        if (!event.player.hasPermission("vstaffchat.send")) return

        if (toggledChat.contains(event.player.uniqueId)) {
            event.result = PlayerChatEvent.ChatResult.denied()
            return sendChat(message, event.player)
        }

        if (!message.startsWith(Config.prefix)) return

        event.result = PlayerChatEvent.ChatResult.denied()

        message = message.replace(Config.prefix, "").trimStart()

        if (message != "") sendChat(message, event.player)
    }

    fun sendChat(message: String, sender: Player) {
        server.allPlayers.filter { it.hasPermission("vstaffchat.receive") }.forEach {
            it.sendMessage(
                Config.format
                    .replace("[PREFIX]", Config.chatPrefix)
                    .replace("[USERNAME]", sender.username)
                    .replace("[MESSAGE]", message)
                    .component()
            )
        }
    }
}
