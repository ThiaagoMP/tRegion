package br.com.thiaago.tregion.spigot.listeners

import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.controller.RegionController
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val regionController: RegionController, private val repository: RegionRepository) :
    Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val playerUUID = event.player.uniqueId.toString()
        val regionPlayer = repository.getRegionPlayer(playerUUID) ?: return
        regionController.players[playerUUID] = regionPlayer
    }

}