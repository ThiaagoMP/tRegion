package br.com.thiaago.tregion.spigot.inventories.actions

import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.region.Region
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class RenameRegionAction {

    companion object {
        fun execute(region: Region, regionRepository: RegionRepository, player: Player, text: String) {
            region.regionName = text
            regionRepository.save(region)
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Region renamed!"))
        }
    }

}