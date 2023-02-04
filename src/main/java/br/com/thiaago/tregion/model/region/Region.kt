package br.com.thiaago.tregion.model.region

data class Region(val regionName: String, val playerUUID: String, val whiteListedPlayers: List<String>, val cuboid: RegionCuboid)