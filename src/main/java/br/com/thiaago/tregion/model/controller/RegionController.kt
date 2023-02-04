package br.com.thiaago.tregion.model.controller

import br.com.thiaago.tregion.RegionPlugin
import br.com.thiaago.tregion.dao.mongo.configuration.RegionRepositoryConfiguration
import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.region.RegionPlayer

class RegionController(
    private val plugin: RegionPlugin,
    val players: MutableMap<String, RegionPlayer> = RegionRepository(RegionRepositoryConfiguration(plugin)).getAllRegions()
)