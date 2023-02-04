package br.com.thiaago.tregion.dao.mongo.factory

import br.com.thiaago.tregion.dao.mongo.configuration.RegionRepositoryConfiguration
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document

class ConnectionFactory {

    fun getCollection(regionRepositoryConfiguration: RegionRepositoryConfiguration): MongoCollection<Document> =
        MongoClient(regionRepositoryConfiguration.uri!!).getDatabase(regionRepositoryConfiguration.database!!)
            .getCollection(regionRepositoryConfiguration.collectionName!!)

}