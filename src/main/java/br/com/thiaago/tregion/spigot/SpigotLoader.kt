package br.com.thiaago.tregion.spigot

import br.com.thiaago.tregion.RegionPlugin
import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.controller.RegionController
import br.com.thiaago.tregion.spigot.commands.RegionCommand
import br.com.thiaago.tregion.spigot.commands.RegionCommandTabCompleter
import br.com.thiaago.tregion.spigot.listeners.PlayerBlockListener
import br.com.thiaago.tregion.spigot.listeners.PlayerChatListener
import br.com.thiaago.tregion.spigot.listeners.PlayerInteractListener
import org.bukkit.Bukkit

class SpigotLoader {

    companion object {
        fun load(plugin: RegionPlugin, regionController: RegionController, regionRepository: RegionRepository) {
            Bukkit.getPluginManager().registerEvents(PlayerInteractListener(regionRepository), plugin)
            Bukkit.getPluginManager().registerEvents(PlayerChatListener(), plugin)
            Bukkit.getPluginManager().registerEvents(PlayerBlockListener(regionController), plugin)

            plugin.getCommand("region")?.setExecutor(RegionCommand(regionRepository, regionController))
            plugin.getCommand("region")?.tabCompleter = RegionCommandTabCompleter()
        }
    }

}