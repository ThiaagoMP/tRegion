package br.com.thiaago.tregion.spigot.inventories.actions

import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.region.Region
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class AddPlayerWhiteListAction {

    companion object {
        fun execute(playerName: String, region: Region, player: Player, regionRepository: RegionRepository) {
            val textSplit = playerName.split(" ")[0]
            if (region.whiteListedPlayers.contains(textSplit)) {
                player.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&',
                        "&7This player is already whitelisted!"
                    )
                )
                return
            }
            region.whiteListedPlayers.add(textSplit)
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Player added to the whitelist!"))
            player.closeInventory()
            regionRepository.save(region)
        }
    }
}