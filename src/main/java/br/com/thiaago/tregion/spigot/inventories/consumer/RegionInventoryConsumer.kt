package br.com.thiaago.tregion.spigot.inventories.consumer

import br.com.thiaago.tregion.model.region.Region
import java.util.function.BiConsumer

class RegionInventoryConsumer(private val consumer: BiConsumer<Region, String>, private val region: Region) {

    fun execute(text: String) {
        consumer.accept(region, text)
    }

}