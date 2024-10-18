package me.xii69.vstaffchat.shared.config

import java.nio.file.Path

class Config(path: Path, clazz: Class<*>) : ConfigModel(path, clazz, "config.toml") {
    companion object {
        lateinit var prefix: String
        lateinit var chatPrefix: String
        lateinit var format: String
        lateinit var toggleOn: String
        lateinit var toggleOff: String
        lateinit var onlyInGame: String
        lateinit var noPermission: String
    }

    override fun init() {
        prefix = configuration.getString("options.prefix")
        chatPrefix = configuration.getString("messages.prefix")
        format = configuration.getString("messages.format")
        toggleOn = configuration.getString("messages.toggle_on")
        toggleOff = configuration.getString("messages.toggle_off")
        onlyInGame = configuration.getString("messages.only_in_game")
        noPermission = configuration.getString("messages.no_permission")
    }
}
