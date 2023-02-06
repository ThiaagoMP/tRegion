package br.com.thiaago.tregion.spigot.inventories.actions

import br.com.thiaago.tregion.spigot.item.RegionExtenderItem
import org.bukkit.entity.Player

class ChangeLocationAction {

    companion object {
        fun execute(player: Player) {
            player.inventory.addItem(RegionExtenderItem.get())
            player.closeInventory()
        }

    }
}