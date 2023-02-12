package br.com.thiaago.tregion.spigot.listeners

import br.com.thiaago.tregion.model.controller.RegionController
import br.com.thiaago.tregion.spigot.constants.RegionPermissions
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class PlayerBlockListener(private val regionController: RegionController) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player

        if (player.hasPermission(RegionPermissions.REGION_BYPASS.text)) return

        if (!playerIsAuthorizedInteract(player, event.block.location)) event.isCancelled = true
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (!playerIsAuthorizedInteract(event.player, event.block.location)) event.isCancelled = true
    }

    private fun playerIsAuthorizedInteract(player: Player, location: Location): Boolean {
        regionController.players.values.forEach { regionPlayer ->
            regionPlayer.regions.forEach { region ->
                val uuid = Bukkit.getPlayerUniqueId(player.name).toString()
                if (region.cuboid.contains(location) && region.playerUUID != uuid
                    && !region.whiteListedPlayers.contains(uuid)
                )
                    return false
            }
        }
        return true
    }

}