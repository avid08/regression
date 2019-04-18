package com.issues;

import org.bson.Document;

import com.DmetadataRegression.Helpers.Mongo.MongoHelper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


public class IssuesHelper {
    public static Integer getIssueWithSecurities(String dataBaseServer, int count) {
        MongoDatabase database = MongoHelper.getDataBase(dataBaseServer, "esp-9");
        MongoCollection<Document> collection = database.getCollection("fitch_ratable");
        Document result = collection.find(Filters.exists("transactionSecurityIDList." + count)).first();
        return result.getInteger("ratableID");
    }

    public static Integer getIssueWithoutSecurities(String dataBaseServer) {
        MongoDatabase database = MongoHelper.getDataBase(dataBaseServer, "esp-9");
        MongoCollection<Document> collection = database.getCollection("fitch_ratable");
        Document result = collection.find(Filters.not(Filters.exists("transactionSecurityIDList"))).first();
        return result.getInteger("ratableID");
    }
}
