package br.com.thiaago.tregion.spigot.inventories

import br.com.thiaago.tregion.model.controller.RegionController
import br.com.thiaago.tregion.spigot.constants.RegionInventorySkullTextures
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.components.InteractionModifier
import dev.triumphteam.gui.guis.PaginatedGui
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import java.util.*

class RegionsInventory(private val regionController: RegionController) :
    PaginatedGui(
        6,
        45,
        ChatColor.translateAlternateColorCodes('&', "&7Regions"),
        EnumSet.noneOf(InteractionModifier::class.java)
    ) {

    init {
        this.setDefaultClickAction { event -> event.isCancelled = true }

        this.setItem(
            48,
            ItemBuilder.skull().texture(RegionInventorySkullTextures.PREVIOUS_PAGE.texture)
                .name(Component.text(ChatColor.translateAlternateColorCodes('&', "&7Previous page")))
                .asGuiItem { this.previous() })
        this.setItem(
            50,
            ItemBuilder.skull().texture(RegionInventorySkullTextures.NEXT_PAGE.texture)
                .name(Component.text(ChatColor.translateAlternateColorCodes('&', "&7Next page")))
                .asGuiItem { this.next() })
    }

    override fun open(player: HumanEntity) {
        val regionPlayer = regionController.players[player.uniqueId.toString()]

        regionPlayer?.regions?.forEach { region ->
            val guiItem = ItemBuilder.from(Material.DIRT)
                .name(
                    Component.text(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            "&7Region: &a${region.regionName}"
                        )
                    )
                ).asGuiItem()
            guiItem.setAction {
                it.isCancelled = true
                RegionInventory(region).open(player)
            }

            this.addItem(guiItem)
        }
        super.open(player)
    }

}