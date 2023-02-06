package br.com.thiaago.tregion.spigot.listeners

import br.com.thiaago.tregion.model.controller.RegionController
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(private val regionController: RegionController) : Listener {

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        regionController.players.remove(event.player.uniqueId.toString())
    }

}