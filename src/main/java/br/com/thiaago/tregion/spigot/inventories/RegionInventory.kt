package br.com.thiaago.tregion.spigot.inventories

import dev.triumphteam.gui.components.InteractionModifier
import dev.triumphteam.gui.guis.Gui
import org.bukkit.ChatColor
import java.util.*

class RegionInventory :
    Gui(3, ChatColor.translateAlternateColorCodes('&', "&7Duels"), EnumSet.noneOf(InteractionModifier::class.java)) {

    init {
        this.setDefaultClickAction { event -> event.isCancelled = true }
    }

}