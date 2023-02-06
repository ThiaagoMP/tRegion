package br.com.thiaago.tregion.spigot.commands

import br.com.thiaago.tregion.RegionPlugin
import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.controller.RegionController
import br.com.thiaago.tregion.spigot.constants.RegionPermissions
import br.com.thiaago.tregion.spigot.inventories.RegionInventory
import br.com.thiaago.tregion.spigot.inventories.actions.AddPlayerWhiteListAction
import br.com.thiaago.tregion.spigot.inventories.actions.RemovePlayerWhiteListAction
import br.com.thiaago.tregion.spigot.item.RegionExtenderItem
import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.ArgName
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.SubCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player

@Command("region")
class RegionCommand(
    private val regionPlugin: RegionPlugin,
    private val regionRepository: RegionRepository,
    private val regionController: RegionController
) :
    BaseCommand() {

    @Default
    fun execute(player: Player) {
        if (!player.hasPermission(RegionPermissions.REGION_MENU.text)) return
        regionPlugin.regionsInventory?.open(player) ?: return
    }

    @Default
    fun execute(player: Player, @ArgName("regionName") regionName: String) {
        if (!player.hasPermission(RegionPermissions.REGION_MENU.text)) return
        val region =
            regionController.players[player.uniqueId.toString()]?.regions?.firstOrNull { it.regionName == regionName }

        if (region == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRegion not found!"))
            return
        }
        RegionInventory(region).open(player)
    }

    @SubCommand("create")
    fun executeCreate(player: Player, @ArgName("regionName") regionName: String) {
        if (!player.hasPermission(RegionPermissions.REGION_CREATE.text)) return

    }

    @SubCommand("wand")
    fun executeWand(player: Player) {
        if (!player.hasPermission(RegionPermissions.REGION_CREATE.text)) return
        player.inventory.addItem(RegionExtenderItem.get())
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Item added to your inventory!"))
    }

    @SubCommand("add")
    fun executeAdd(
        player: Player,
        @ArgName("regionName") regionName: String,
        @ArgName("playerName") playerName: String
    ) {
        if (!player.hasPermission(RegionPermissions.REGION_ADD.text)) return
        val region =
            regionController.players[player.uniqueId.toString()]?.regions?.firstOrNull { it.regionName == regionName }

        if (region == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRegion not found!"))
            return
        }

        AddPlayerWhiteListAction.execute(playerName, region, player, regionRepository)
    }

    @SubCommand("remove")
    fun executeRemove(
        player: Player,
        @ArgName("regionName") regionName: String,
        @ArgName("playerName") playerName: String
    ) {
        if (!player.hasPermission(RegionPermissions.REGION_REMOVE.text)) return
        val region =
            regionController.players[player.uniqueId.toString()]?.regions?.firstOrNull { it.regionName == regionName }

        if (region == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRegion not found!"))
            return
        }

        RemovePlayerWhiteListAction.execute(playerName, region, player, regionRepository)
    }

    @SubCommand("whitelist")
    fun executeWhitelist(
        player: Player,
        @ArgName("regionName") regionName: String,
    ) {
        if (!player.hasPermission(RegionPermissions.REGION_WHITELIST.text)) return

        val region =
            regionController.players[player.uniqueId.toString()]?.regions?.firstOrNull { it.regionName == regionName }

        if (region == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRegion not found!"))
            return
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', region.whiteListedPlayers.joinToString(",")))
    }

}