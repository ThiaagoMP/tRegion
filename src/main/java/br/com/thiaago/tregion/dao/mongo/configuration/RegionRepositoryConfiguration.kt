package br.com.thiaago.tregion.dao.mongo.configuration

import br.com.thiaago.tregion.RegionPlugin

class RegionRepositoryConfiguration(
    private val plugin: RegionPlugin,
    val uri: String? = plugin.config.getString("MongoDB.uri"),
    val database: String? = plugin.config.getString("MongoDB.database"),
    val collectionName: String? = plugin.config.getString("MongoDB.collectionName")
)
