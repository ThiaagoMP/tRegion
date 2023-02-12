package br.com.thiaago.tregion.spigot.commands

import br.com.thiaago.tregion.RegionPlugin
import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.controller.RegionController
import br.com.thiaago.tregion.model.region.Region
import br.com.thiaago.tregion.model.region.RegionCuboid
import br.com.thiaago.tregion.model.region.RegionPlayer
import br.com.thiaago.tregion.spigot.actions.AddPlayerWhiteListAction
import br.com.thiaago.tregion.spigot.actions.RemovePlayerWhiteListAction
import br.com.thiaago.tregion.spigot.constants.RegionPermissions
import br.com.thiaago.tregion.spigot.constants.RegionPlayerSelected
import br.com.thiaago.tregion.spigot.inventories.RegionInventory
import br.com.thiaago.tregion.spigot.inventories.RegionsInventory
import br.com.thiaago.tregion.spigot.item.RegionExtenderItem
import org.bukkit.ChatColor
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

class RegionCommand(
    private val regionRepository: RegionRepository, private val regionController: RegionController
) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender, command: org.bukkit.command.Command, label: String, args: Array<out String>
    ): Boolean {
        if (sender !is Player) return true

        if (args.isEmpty()) {
            execute(sender, null)
            return true
        }

        when (args[0].toLowerCase()) {
            "create" -> executeCreate(sender, args[0])
            "wand" -> executeWand(sender)
            "add" -> {
                if (args.size != 2) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid command!"))
                    return true
                }
                executeAdd(sender, args[1], args[2])
            }

            "remove" -> {
                if (args.size != 2) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid command!"))
                    return true
                }
                executeRemove(sender, args[1], args[2])
            }

            "select" -> executeSelect(sender, args[1])
            "whitelist" -> executeWhitelist(sender, args[1])
            else -> execute(sender, args[0])
        }


        return false
    }

    private fun execute(player: Player, regionName: String?) {
        if (!hasPermission(player, RegionPermissions.REGION_MENU.text)) return
        if (regionName == null) RegionsInventory(regionController).open(player)
        else {
            val region =
                regionController.players[player.uniqueId.toString()]?.regions?.firstOrNull { it.regionName == regionName }

            if (region == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRegion not found!"))
                return
            }
            RegionInventory(region).open(player)
        }
    }

    private fun executeCreate(player: Player, regionName: String) {
        if (!player.hasPermission(RegionPermissions.REGION_CREATE.text)) return

        val regionPlayer = regionController.players[player.uniqueId.toString()]

        if (regionPlayer != null) {
            if (regionPlayer.regions.any { region: Region -> region.regionName == regionName }) {
                player.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&', "&cA region with that name already exists!"
                    )
                )
                return
            }
        }

        val region = Region(
            regionName, player.uniqueId.toString(), emptyList<String>().toMutableList(), RegionCuboid(
                player.location.add(-3.0, -3.0, -3.0).toBlockLocation(),
                player.location.add(3.0, 3.0, 3.0).toBlockLocation()
            )
        )

        if (regionPlayer == null) regionController.players[player.uniqueId.toString()] =
            RegionPlayer(player.uniqueId.toString(), listOf(region).toMutableList())
        else regionPlayer.regions.add(region)

        player.sendMessage(
            ChatColor.translateAlternateColorCodes(
                '&', "&7A region has been created around you! Use /region wand to expand it!"
            )
        )
        regionRepository.save(region)
    }

    private fun executeWand(player: Player) {
        if (!player.hasPermission(RegionPermissions.REGION_CREATE.text)) return
        player.inventory.addItem(RegionExtenderItem.get())
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Item added to your inventory!"))
    }

    private fun executeAdd(
        player: Player, regionName: String, playerName: String
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

    private fun executeRemove(
        player: Player, regionName: String, playerName: String
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

    private fun executeWhitelist(
        player: Player,
        regionName: String,
    ) {
        if (!player.hasPermission(RegionPermissions.REGION_WHITELIST.text)) return

        val region =
            regionController.players[player.uniqueId.toString()]?.regions?.firstOrNull { it.regionName == regionName }

        if (region == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRegion not found!"))
            return
        }

        var playersMessage = region.whiteListedPlayers.joinToString(", ")
        if (playersMessage.isEmpty()) playersMessage = "Empty"
        
        player.sendMessage(
            ChatColor.translateAlternateColorCodes(
                '&', "&7Whitelist:&a $playersMessage"
            )
        )
    }

    private fun executeSelect(player: Player, regionName: String) {
        if (!player.hasPermission(RegionPermissions.REGION_CREATE.text)) return

        val region =
            regionController.players[player.uniqueId.toString()]?.regions?.firstOrNull { it.regionName == regionName }

        if (region == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cRegion not found!"))
            return
        }

        player.setMetadata(
            RegionPlayerSelected.REGION_PLAYER_SELECTED.value, FixedMetadataValue(RegionPlugin.getInstance(), region)
        )
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Region ${region.regionName} selected!"))
    }

    private fun hasPermission(player: Player, permission: String): Boolean {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWithout permission!"))
            return false
        }
        return true
    }
}
