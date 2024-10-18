package me.xii69.vstaffchat.bungee

import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin

class VStaffChat() : Plugin(), Listener {
    private lateinit var metrics: Metrics

    override fun onEnable() {
        metrics = Metrics(this, 23665)
    }
}
