package br.com.thiaago.tregion.model.region

data class Region(var regionName: String, val playerUUID: String, val whiteListedPlayers: MutableList<String>, val cuboid: RegionCuboid)