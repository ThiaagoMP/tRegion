package br.com.thiaago.tregion.model.region

import org.bukkit.Location
import kotlin.math.max
import kotlin.math.min

class RegionCuboid(val minimumLocation: Location, val maximumLocation: Location) {

    private val minimumX: Int = min(minimumLocation.blockX, maximumLocation.blockX)
    private val maximumX: Int = max(minimumLocation.blockX, maximumLocation.blockX)
    private val minimumY: Int = min(minimumLocation.blockY, maximumLocation.blockY)
    private val maximumY: Int = max(minimumLocation.blockY, maximumLocation.blockY)
    private val minimumZ: Int = min(minimumLocation.blockZ, maximumLocation.blockZ)
    private val maximumZ: Int = max(minimumLocation.blockZ, maximumLocation.blockZ)
    private val worldName: String = minimumLocation.world.name

    fun contains(location: Location): Boolean {
        return if (worldName != location.world.name) false
        else location.x.toInt() in minimumX..maximumX && location.y.toInt() in minimumY..maximumY && location.z.toInt() in minimumZ..maximumZ
    }

}