package br.com.thiaago.tregion.spigot.listeners

import br.com.thiaago.tregion.model.controller.RegionController
import br.com.thiaago.tregion.spigot.constants.RegionPermissions
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class PlayerBlockEvent(private val regionController: RegionController) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player

        if (player.hasPermission(RegionPermissions.REGION_BYPASS.text)) return

        if (cancel(player, event.block.location)) event.isCancelled = true
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (cancel(event.player, event.block.location)) event.isCancelled = true
    }

    private fun cancel(player: Player, location: Location): Boolean {
        regionController.players.values.forEach { regionPlayer ->
            regionPlayer.regions.forEach { region ->
                if (region.cuboid.contains(location) && !region.whiteListedPlayers.contains(player.name))
                    return true
            }
        }
        return false
    }

}