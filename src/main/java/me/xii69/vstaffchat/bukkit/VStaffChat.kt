package me.xii69.vstaffchat.bukkit

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class VStaffChat() : JavaPlugin(), Listener {
    private lateinit var metrics: Metrics

    override fun onEnable() {
        metrics = Metrics(this, 23666)
    }

    override fun onDisable() {

    }
}
