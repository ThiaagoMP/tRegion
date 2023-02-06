package br.com.thiaago.tregion

import br.com.thiaago.tregion.dao.mongo.configuration.RegionRepositoryConfiguration
import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.controller.RegionController
import br.com.thiaago.tregion.spigot.SpigotLoader
import br.com.thiaago.tregion.spigot.inventories.RegionInventory
import br.com.thiaago.tregion.spigot.inventories.RegionsInventory
import org.bukkit.plugin.java.JavaPlugin

class RegionPlugin : JavaPlugin() {

    companion object {
        fun getInstance(): RegionPlugin {
            return getPlugin(RegionPlugin::class.java)
        }
    }

    var regionController: RegionController? = null
    var regionRepository: RegionRepository? = null
    var regionsInventory: RegionsInventory? = null

    override fun onEnable() {
        loadDatabase()
        loadControllers()
        SpigotLoader.load(getInstance(), regionController!!, regionRepository!!)
    }

    override fun onDisable() {
        regionRepository?.closeConnection()
    }

    private fun loadDatabase() {
        regionRepository = RegionRepository(RegionRepositoryConfiguration(this))
    }

    private fun loadControllers() {
        regionController = RegionController()
    }

}