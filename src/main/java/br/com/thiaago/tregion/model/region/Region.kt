package br.com.thiaago.tregion.model.region

import java.util.*

data class Region(
    var regionName: String,
    val playerUUID: String,
    val whiteListedPlayers: MutableList<String>,
    var cuboid: RegionCuboid,
    var id: String = UUID.randomUUID().toString().replace("_", "")
)