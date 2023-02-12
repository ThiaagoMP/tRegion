package br.com.thiaago.tregion.spigot.actions

import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.region.Region
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class RemovePlayerWhiteListAction {

    companion object {
        fun execute(playerName: String, region: Region, player: Player, regionRepository: RegionRepository) {
            val textSplit = playerName.split(" ")[0]
            if (textSplit.length < 3 || textSplit.length > 16) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid name!"))
                return
            }
            if (!region.whiteListedPlayers.removeIf { players -> players == textSplit }) {
                player.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&',
                        "&7This player is not whitelisted!"
                    )
                )
                return
            }
            player.sendMessage(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    "&7Player $textSplit removed from whitelist!"
                )
            )
            regionRepository.save(region)
        }
    }

}