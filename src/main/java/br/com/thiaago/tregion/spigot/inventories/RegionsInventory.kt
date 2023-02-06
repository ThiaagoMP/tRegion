package br.com.thiaago.tregion.spigot.inventories

import br.com.thiaago.tregion.model.controller.RegionController
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.components.InteractionModifier
import dev.triumphteam.gui.components.ScrollType
import dev.triumphteam.gui.guis.ScrollingGui
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import java.util.*

class RegionsInventory(private val regionController: RegionController) :
    ScrollingGui(
        6,
        45,
        ChatColor.translateAlternateColorCodes('&', "&7Regions"),
        ScrollType.VERTICAL,
        EnumSet.noneOf(InteractionModifier::class.java)
    ) {

    init {
        this.setDefaultClickAction { event -> event.isCancelled = true }
    }

    override fun open(player: HumanEntity) {
        val regionPlayer = regionController.players[player.uniqueId.toString()] ?: return

        regionPlayer.regions.forEach { region ->
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
                this.close(player)
                RegionInventory(region).open(player)
            }

            this.addItem(guiItem)
        }
    }

}