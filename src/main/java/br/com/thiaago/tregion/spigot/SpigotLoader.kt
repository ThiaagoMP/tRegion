package br.com.thiaago.tregion.spigot

import br.com.thiaago.tregion.RegionPlugin
import br.com.thiaago.tregion.model.controller.RegionController

class SpigotLoader {

    companion object {
        fun load(plugin: RegionPlugin, regionController: RegionController) {
            plugin.saveDefaultConfig()
        }
    }

}