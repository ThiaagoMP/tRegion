package br.com.thiaago.tregion.spigot.listeners

import br.com.thiaago.tregion.spigot.constants.RegionPlayerModifier
import br.com.thiaago.tregion.spigot.inventories.consumer.RegionInventoryConsumer
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerChatListener(private val plainSerializer: PlainTextComponentSerializer = PlainTextComponentSerializer.plainText()) :
    Listener {

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val player = event.player
        if (!player.hasMetadata(RegionPlayerModifier.REGION_PLAYER_MODIFIER.text)) return

        val regionInventoryConsumer =
            player.getMetadata(RegionPlayerModifier.REGION_PLAYER_MODIFIER.text)[0].value() as RegionInventoryConsumer

        regionInventoryConsumer.execute(plainSerializer.serialize(event.message()))
    }

}