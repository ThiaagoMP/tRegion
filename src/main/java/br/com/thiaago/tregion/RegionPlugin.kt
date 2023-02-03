package br.com.thiaago.tregion

import org.bukkit.plugin.java.JavaPlugin

class RegionPlugin : JavaPlugin() {

    fun getInstance(): RegionPlugin {
        return getPlugin(RegionPlugin::class.java)
    }

    override fun onEnable() {
    }

    override fun onDisable() {
    }
}