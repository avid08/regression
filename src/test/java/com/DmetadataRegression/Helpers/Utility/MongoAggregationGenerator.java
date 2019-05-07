package com.DmetadataRegression.Helpers.Utility;

import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class MongoAggregationGenerator {
    public static String getAggregationString(MongoCollection<Document> collection, List<Bson> aggregateList){
        String collectionName = collection.getNamespace().getCollectionName();
        StringBuilder sb = new StringBuilder();
        sb.append("db.getCollection(").append(collectionName).append(").aggregate([");
        aggregateList.forEach(aggregateStage -> {
            sb.append(aggregateStage.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry())).append(",");
        });
        sb.append("]);");
        return new String(sb);
    }
}
