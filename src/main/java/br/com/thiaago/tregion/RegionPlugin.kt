package br.com.thiaago.tregion

import br.com.thiaago.tregion.model.controller.RegionController
import br.com.thiaago.tregion.spigot.SpigotLoader
import org.bukkit.plugin.java.JavaPlugin

class RegionPlugin : JavaPlugin() {

    fun getInstance(): RegionPlugin {
        return getPlugin(RegionPlugin::class.java)
    }

    var regionController: RegionController? = null

    override fun onEnable() {
        SpigotLoader.load(getInstance(), regionController!!)
        loadControllers()
    }

    override fun onDisable() {
    }

    private fun loadControllers() {
        regionController = RegionController(this)
    }

}