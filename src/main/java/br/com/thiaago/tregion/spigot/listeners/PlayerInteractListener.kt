package br.com.thiaago.tregion.spigot.listeners

import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.controller.RegionController
import br.com.thiaago.tregion.spigot.constants.RegionExtenderItemNBT
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
    private val regionController: RegionController,
    private val regionRepository: RegionRepository
) : Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK || event.action != Action.LEFT_CLICK_BLOCK) return
        if (!event.hasItem()) return
        if (!NBTEditor.contains(event.item, RegionExtenderItemNBT.REGION_EXTENDER_NBT.toString())) return

        val clickedBlock = event.clickedBlock
        val player = event.player

        val region = regionController.getRegion(clickedBlock?.location ?: return) ?: return

        if (event.action == Action.LEFT_CLICK_BLOCK) {
            if (checkWorlds(region.cuboid.minimumLocation, clickedBlock, player)) return
            region.cuboid.maximumLocation = clickedBlock.location.toBlockLocation()
        } else if (event.action == Action.RIGHT_CLICK_BLOCK) {
            if (checkWorlds(region.cuboid.maximumLocation, clickedBlock, player)) return
            region.cuboid.minimumLocation = clickedBlock.location.toBlockLocation()
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Changed location!"))

        regionRepository.save(region)
    }

    private fun checkWorlds(
        location: Location,
        clickedBlock: Block,
        player: Player
    ): Boolean {
        if (!location.world.equals(clickedBlock.location.world)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDifferent worlds!"))
            return true
        }
        return false
    }

}