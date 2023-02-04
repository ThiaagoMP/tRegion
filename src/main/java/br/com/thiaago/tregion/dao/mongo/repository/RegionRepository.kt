package br.com.thiaago.tregion.dao.mongo.repository

import br.com.thiaago.tregion.dao.mongo.configuration.RegionRepositoryConfiguration
import br.com.thiaago.tregion.dao.mongo.factory.ConnectionFactory
import br.com.thiaago.tregion.model.region.Region
import br.com.thiaago.tregion.model.region.RegionCuboid
import br.com.thiaago.tregion.model.region.RegionPlayer
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.bukkit.Location

private object RegionFields {
    const val PLAYER_UUID = "UUID"
    const val REGION_NAME = "REGION_NAME"
    const val WHITE_LIST = "WHITELIST"
    const val MINIMUM_LOCATION = "MIN_LOC"
    const val MAXIMUM_LOCATION = "MAX_LOC"
}

class RegionRepository(
    private val regionRepositoryConfiguration: RegionRepositoryConfiguration,
    private val collection: MongoCollection<Document> = ConnectionFactory().getCollection(regionRepositoryConfiguration)
) {

    fun save(region: Region) {
        val regionDocument =
            mapOf(
                Pair(RegionFields.REGION_NAME, region.regionName),
                Pair(RegionFields.WHITE_LIST, region.whiteListedPlayers),
                Pair(RegionFields.MINIMUM_LOCATION, region.cuboid.minimumLocation.serialize()),
                Pair(RegionFields.MAXIMUM_LOCATION, region.cuboid.maximumLocation.serialize())
            )
        val playerDocument =
            Document(RegionFields.PLAYER_UUID, region.playerUUID).append(region.regionName, regionDocument)

        val documentFound = collection.find(Document(mapOf(Pair(RegionFields.PLAYER_UUID, region.playerUUID)))).first()

        if (documentFound == null) collection.insertOne(playerDocument)
        else collection.updateOne(documentFound, playerDocument)
    }

    fun getAllRegions(): MutableMap<String, RegionPlayer> {
        val playersRegions = emptyMap<String, RegionPlayer>().toMutableMap()

        val iterator = collection.find().iterator()
        while (collection.find().iterator().hasNext()) {

            val documentPlayer = iterator.next()

            val regions = emptyList<Region>().toMutableList()

            val playerUUID = documentPlayer.getString(RegionFields.PLAYER_UUID)

            documentPlayer.forEach { entry: Map.Entry<String, Any> ->
                if (entry.value is Document) {
                    val documentRegion = entry.value as Document

                    val locationMinimum =
                        Location.deserialize(documentRegion[RegionFields.MINIMUM_LOCATION] as MutableMap<String, Any>)
                    val locationMaximum =
                        Location.deserialize(documentRegion[RegionFields.MAXIMUM_LOCATION] as MutableMap<String, Any>)

                    regions.add(
                        Region(
                            documentRegion.getString(RegionFields.REGION_NAME),
                            playerUUID,
                            documentRegion.getList(RegionFields.WHITE_LIST, String::class.java),
                            RegionCuboid(locationMinimum, locationMaximum)
                        )
                    )
                }
            }
            
            playersRegions[playerUUID] = RegionPlayer(playerUUID, regions)
        }

        return playersRegions
    }

}