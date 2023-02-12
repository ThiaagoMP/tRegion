package br.com.thiaago.tregion.spigot.actions

import br.com.thiaago.tregion.RegionPlugin
import br.com.thiaago.tregion.model.region.Region
import br.com.thiaago.tregion.spigot.constants.RegionPlayerSelected
import br.com.thiaago.tregion.spigot.item.RegionExtenderItem
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

class ChangeLocationAction {

    companion object {
        fun execute(player: Player, region: Region) {
            player.inventory.addItem(RegionExtenderItem.get())
            player.setMetadata(
                RegionPlayerSelected.REGION_PLAYER_SELECTED.value,
                FixedMetadataValue(RegionPlugin.getInstance(), region)
            )
            player.closeInventory()
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Item added to your inventory!"))
        }

    }
}