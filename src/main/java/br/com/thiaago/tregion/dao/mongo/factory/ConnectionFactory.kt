package br.com.thiaago.tregion.dao.mongo.factory

import br.com.thiaago.tregion.dao.mongo.configuration.RegionRepositoryConfiguration
import com.mongodb.MongoClient

class ConnectionFactory(
    private val regionRepositoryConfiguration: RegionRepositoryConfiguration,
    val mongoClient: MongoClient = MongoClient(regionRepositoryConfiguration.uri!!)
) {

    fun getCollection() = mongoClient.getDatabase(regionRepositoryConfiguration.database!!)
        .getCollection(regionRepositoryConfiguration.collectionName!!)

}