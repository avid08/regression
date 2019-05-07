package com.financial.api;

import java.util.Date;

import org.bson.Document;

import com.DmetadataRegression.Helpers.Mongo.MongoHelper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class FinancialHelper {
    static long getCountForChangeDate(String dataBaseServer, Date startDate, Date endDate) {
        MongoDatabase database = MongoHelper.getDataBase(dataBaseServer, "financial-1");
        MongoCollection<Document> collection = database.getCollection("financial_statement");
        return collection.count(Filters.and(
                Filters.gte("addedToMongoDate", startDate),
                Filters.lte("addedToMongoDate", endDate))
        );
    }
    static long getCountForAddDate(String dataBaseServer, Date startDate, Date endDate) {
        MongoDatabase database = MongoHelper.getDataBase(dataBaseServer, "financial-1");
        MongoCollection<Document> collection = database.getCollection("financial_statement");
        return collection.count(Filters.and(
                Filters.gte("addDate", startDate),
                Filters.lte("addDate", endDate))
        );
    }
}
