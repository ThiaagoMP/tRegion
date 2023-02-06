package br.com.thiaago.tregion.spigot.inventories

import br.com.thiaago.tregion.RegionPlugin
import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.region.Region
import br.com.thiaago.tregion.spigot.constants.RegionPlayerModifier
import br.com.thiaago.tregion.spigot.inventories.actions.AddPlayerWhiteListAction
import br.com.thiaago.tregion.spigot.inventories.actions.ChangeLocationAction
import br.com.thiaago.tregion.spigot.inventories.actions.RemovePlayerWhiteListAction
import br.com.thiaago.tregion.spigot.inventories.actions.RenameRegionAction
import br.com.thiaago.tregion.spigot.inventories.consumer.RegionInventoryConsumer
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.components.InteractionModifier
import dev.triumphteam.gui.guis.Gui
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import java.util.*
import java.util.function.BiConsumer

class RegionInventory(
    private val region: Region,
    private val regionRepository: RegionRepository = RegionPlugin.getInstance().regionRepository!!
) : Gui(
    3,
    ChatColor.translateAlternateColorCodes('&', "&7Region: &a${region.regionName}"),
    EnumSet.noneOf(InteractionModifier::class.java)
) {

    init {
        val renameItem = ItemBuilder.from(Material.WRITTEN_BOOK).name(
            Component.text(
                ChatColor.translateAlternateColorCodes(
                    '&', "&7Rename region: &a${region.regionName}"
                )
            )
        ).asGuiItem()
        renameItem.setAction { clickEvent ->
            clickEvent.isCancelled = true
            val player = clickEvent.whoClicked as Player
            createAction(
                player,
                "&7Please enter the name to rename the region!"
            ) { region, text -> RenameRegionAction.execute(region, regionRepository, player, text) }
        }

        val addWhitelistItem = ItemBuilder.from(Material.SNOW).name(
            Component.text(
                ChatColor.translateAlternateColorCodes(
                    '&', "&7Add new whitelisted player in region: &a${region.regionName}"
                )
            )
        ).asGuiItem()
        addWhitelistItem.setAction { clickEvent ->
            clickEvent.isCancelled = true
            val player = clickEvent.whoClicked as Player
            createAction(
                player,
                "&7Please enter the player's name to add to the whitelist!"
            ) { region, text -> AddPlayerWhiteListAction.execute(text, region, player, regionRepository) }
        }

        val removeWhitelistItem = ItemBuilder.from(Material.SNOW).name(
            Component.text(
                ChatColor.translateAlternateColorCodes(
                    '&', "&7Remove player from &a${region.regionName}&7 region whitelist"
                )
            )
        ).asGuiItem()
        removeWhitelistItem.setAction { clickEvent ->
            clickEvent.isCancelled = true
            val player = clickEvent.whoClicked as Player
            createAction(
                player,
                "&7Please enter the player's nickname to remove from the whitelist!"
            ) { region, text -> RemovePlayerWhiteListAction.execute(text, region, player, regionRepository) }
        }

        val changeLocationItem = ItemBuilder.from(Material.DIRT).name(
            Component.text(
                ChatColor.translateAlternateColorCodes(
                    '&', "&7Change region location: &a${region.regionName}"
                )
            )
        ).asGuiItem()
        changeLocationItem.setAction { clickEvent ->
            clickEvent.isCancelled = true
            val player = clickEvent.whoClicked as Player
            ChangeLocationAction.execute(player)
        }

        this.setItem(10, renameItem)
        this.setItem(12, addWhitelistItem)
        this.setItem(14, removeWhitelistItem)
        this.setItem(16, changeLocationItem)
    }

    private fun createAction(player: Player, message: String, consumer: BiConsumer<Region, String>) {
        player.closeInventory()
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
        player.setMetadata(
            RegionPlayerModifier.REGION_PLAYER_MODIFIER.text,
            FixedMetadataValue(RegionPlugin.getInstance(), RegionInventoryConsumer(consumer, region))
        )
    }

}