package br.com.thiaago.tregion.spigot

import br.com.thiaago.tregion.RegionPlugin
import br.com.thiaago.tregion.dao.mongo.repository.RegionRepository
import br.com.thiaago.tregion.model.controller.RegionController
import br.com.thiaago.tregion.spigot.commands.RegionCommand
import br.com.thiaago.tregion.spigot.inventories.RegionsInventory
import br.com.thiaago.tregion.spigot.listeners.PlayerBlockEvent
import br.com.thiaago.tregion.spigot.listeners.PlayerChatListener
import br.com.thiaago.tregion.spigot.listeners.PlayerJoinListener
import br.com.thiaago.tregion.spigot.listeners.PlayerQuitListener
import dev.triumphteam.cmd.bukkit.BukkitCommandManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class SpigotLoader {

    companion object {
        fun load(plugin: RegionPlugin, regionController: RegionController, regionRepository: RegionRepository) {
            plugin.saveDefaultConfig()
            Bukkit.getPluginManager().registerEvents(PlayerJoinListener(regionController, regionRepository), plugin)
            Bukkit.getPluginManager().registerEvents(PlayerQuitListener(regionController), plugin)
            Bukkit.getPluginManager().registerEvents(PlayerChatListener(), plugin)
            Bukkit.getPluginManager().registerEvents(PlayerBlockEvent(regionController), plugin)
            plugin.regionsInventory = RegionsInventory(regionController)

            val manager: BukkitCommandManager<CommandSender> = BukkitCommandManager.create(plugin)
            manager.registerCommand(RegionCommand(plugin, regionRepository, regionController))
        }
    }

}