package br.com.thiaago.tregion.spigot.listeners

import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.region.Region
import br.com.thiaago.tregion.model.region.RegionCuboid
import br.com.thiaago.tregion.spigot.constants.RegionExtenderItemNBT
import br.com.thiaago.tregion.spigot.constants.RegionPlayerSelected
import io.github.bananapuncher714.nbteditor.NBTEditor
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener(
    private val regionRepository: RegionRepository
) : Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (!event.hasItem()) return
        if (!NBTEditor.contains(event.item, RegionExtenderItemNBT.REGION_EXTENDER_NBT.toString())) return
        if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_BLOCK) {

            event.isCancelled = true

            val clickedBlock = event.clickedBlock ?: return
            val player = event.player

            if (!player.hasMetadata(RegionPlayerSelected.REGION_PLAYER_SELECTED.value)) {
                player.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&', "&cYou don't have any region selected! Use /region select (region name)"
                    )
                )
                return
            }

            val region = player.getMetadata(RegionPlayerSelected.REGION_PLAYER_SELECTED.value)[0].value() as Region

            if (event.action == Action.LEFT_CLICK_BLOCK) {
                if (checkWorlds(region.cuboid.minimumLocation, clickedBlock, player)) return
                region.cuboid.maximumLocation = clickedBlock.location.toBlockLocation()
            } else if (event.action == Action.RIGHT_CLICK_BLOCK) {
                if (checkWorlds(region.cuboid.maximumLocation, clickedBlock, player)) return
                region.cuboid.minimumLocation = clickedBlock.location.toBlockLocation()
            }

            region.cuboid = RegionCuboid(region.cuboid.minimumLocation, region.cuboid.maximumLocation)

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Changed location!"))

            regionRepository.save(region)
        }
    }

    private fun checkWorlds(
        location: Location, clickedBlock: Block, player: Player
    ): Boolean {
        if (!location.world.equals(clickedBlock.location.world)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDifferent worlds!"))
            return true
        }
        return false
    }

}