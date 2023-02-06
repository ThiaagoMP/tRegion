package br.com.thiaago.tregion.spigot.item

import br.com.thiaago.tregion.spigot.constants.RegionExtenderItemNBT
import dev.triumphteam.gui.builder.item.ItemBuilder
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class RegionExtenderItem {

    companion object {
        fun get(): ItemStack =
            ItemBuilder.from(Material.STICK).name(
                Component.text(
                    ChatColor.translateAlternateColorCodes(
                        '&', "&7Region Extender"
                    )
                )
            ).lore(
                Component.text(""), Component.text(
                    ChatColor.translateAlternateColorCodes(
                        '&', "&7Use right and left clicks to choose the boundaries of the region!"
                    )
                )
            ).setNbt(
                RegionExtenderItemNBT.REGION_EXTENDER_NBT.toString(),
                RegionExtenderItemNBT.REGION_EXTENDER_NBT.toString()
            ).build()

    }

}