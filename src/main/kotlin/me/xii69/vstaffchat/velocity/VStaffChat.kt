package me.xii69.vstaffchat.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import me.xii69.vstaffchat.velocity.Metrics.Factory

class VStaffChat @Inject constructor(
    private val metricsFactory: Factory
) {
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        metricsFactory.make(this, 23664)
    }
}
