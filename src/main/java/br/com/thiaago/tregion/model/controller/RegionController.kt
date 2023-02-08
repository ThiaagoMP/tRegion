package br.com.thiaago.tregion.model.controller

import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.region.Region
import br.com.thiaago.tregion.model.region.RegionPlayer
import org.bukkit.Location
import org.bukkit.entity.Player

class RegionController(
    private val regionRepository: RegionRepository,
    val players: MutableMap<String, RegionPlayer> = regionRepository.getAllRegionsPlayer(),
) {

    fun playerHasAccessLocation(player: Player, location: Location): Boolean {
        val region = getRegion(location) ?: return true
        if (region.playerUUID == player.uniqueId.toString() || region.whiteListedPlayers.contains(player.uniqueId.toString())) return true
        return false
    }

    fun getRegion(location: Location): Region? {
        players.values.forEach { regionPlayer ->
            regionPlayer.regions.forEach { region ->
                if (region.cuboid.contains(location))
                    return region
            }
        }
        return null
    }

}