package br.com.thiaago.tregion.dao.mongo.repository

import br.com.thiaago.tregion.dao.mongo.configuration.RegionRepositoryConfiguration
import br.com.thiaago.tregion.dao.mongo.factory.ConnectionFactory
import br.com.thiaago.tregion.model.region.Region
import br.com.thiaago.tregion.model.region.RegionCuboid
import br.com.thiaago.tregion.model.region.RegionPlayer
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document
import org.bukkit.Location

private object RegionFields {
    const val PLAYER_UUID = "UUID"
    const val REGION_NAME = "REGION_NAME"
    const val REGION_ID = "REGION_ID"
    const val WHITE_LIST = "WHITELIST"
    const val MINIMUM_LOCATION = "MIN_LOC"
    const val MAXIMUM_LOCATION = "MAX_LOC"
}


class RegionRepository(
    private val regionRepositoryConfiguration: RegionRepositoryConfiguration,
    private val connectionFactory: ConnectionFactory = ConnectionFactory(regionRepositoryConfiguration),
    private val mongoClient: MongoClient = connectionFactory.mongoClient,
    private val collection: MongoCollection<Document> = connectionFactory.getCollection(),
) {

    fun closeConnection() {
        mongoClient.close()
    }

    fun save(region: Region) {
        val regionFields = mapOf(
            Pair(RegionFields.REGION_NAME, region.regionName),
            Pair(RegionFields.REGION_ID, region.id),
            Pair(RegionFields.WHITE_LIST, region.whiteListedPlayers),
            Pair(RegionFields.MINIMUM_LOCATION, region.cuboid.minimumLocation.serialize()),
            Pair(RegionFields.MAXIMUM_LOCATION, region.cuboid.maximumLocation.serialize())
        )

        val documentFound = collection.find(Document(RegionFields.PLAYER_UUID, region.playerUUID)).first()

        val newDocument = Document(RegionFields.PLAYER_UUID, region.playerUUID).append(region.id, regionFields)

        if (documentFound == null) collection.insertOne(newDocument)
        else collection.replaceOne(
            Filters.eq(RegionFields.PLAYER_UUID, region.playerUUID),
            documentFound.append(region.id, regionFields)
        )
    }

    fun getRegionPlayer(playerUUID: String): RegionPlayer? {
        val documentPlayer =
            collection.find(Document(RegionFields.PLAYER_UUID, playerUUID)).firstOrNull() ?: return null

        val regions = emptyList<Region>().toMutableList()

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
                        RegionCuboid(locationMinimum, locationMaximum),
                        documentRegion.getString(RegionFields.REGION_ID)
                    )
                )
            }
        }

        return RegionPlayer(playerUUID, regions)
    }

    fun getAllRegionsPlayer(): MutableMap<String, RegionPlayer> {
        val map = emptyMap<String, RegionPlayer>().toMutableMap()
        collection.find().forEach { document ->
            map[document.getString(RegionFields.PLAYER_UUID)] =
                getRegionPlayer(document.getString(RegionFields.PLAYER_UUID))!!
        }
        return map
    }

}