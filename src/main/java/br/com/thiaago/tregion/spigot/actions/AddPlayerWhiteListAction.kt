package br.com.thiaago.tregion.spigot.actions

import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.region.Region
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class AddPlayerWhiteListAction {

    companion object {
        fun execute(playerName: String, region: Region, player: Player, regionRepository: RegionRepository) {
            val textSplit = playerName.split(" ")[0]
            if (textSplit.length < 3 || textSplit.length > 16) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid name!"))
                return
            }
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
            regionRepository.save(region)
        }
    }
}