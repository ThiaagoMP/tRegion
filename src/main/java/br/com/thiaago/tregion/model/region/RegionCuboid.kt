package br.com.thiaago.tregion.model.region

import org.bukkit.Bukkit
import org.bukkit.Location
import kotlin.math.max
import kotlin.math.min

class RegionCuboid(var minimumLocation: Location, var maximumLocation: Location) {

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

    fun getCenter(): Location {
        val x: Int = this.maximumX + 1
        val y: Int = this.maximumY + 1
        val z: Int = this.maximumZ + 1
        return Location(
            Bukkit.getWorld(worldName),
            minimumX + (x - minimumX) / 2.0,
            minimumY + (y - minimumY) / 2.0,
            minimumZ + (z - minimumZ) / 2.0
        )
    }

}