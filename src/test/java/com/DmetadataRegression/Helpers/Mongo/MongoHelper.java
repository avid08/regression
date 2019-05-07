package com.DmetadataRegression.Helpers.Mongo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class MongoHelper {
    public static MongoDatabase getDataBase(String dataBaseServer, String dataBase) {
        MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
                "the_call".toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer, 27017),
                Arrays.asList(credential));
        return mongoClient.getDatabase(dataBase);
    }

    public static String getToStringWithTypeDotKey(Document document, String type, String key) {
        if (key.contains(".")) {
            Document intermediateDoc = null;
            List<String> keySplit = new ArrayList<>(Arrays.asList(key.split("\\.")));
            for (String part :
                    keySplit.subList(0, keySplit.size() - 1)) {
                intermediateDoc = document.get(part) instanceof List ?
                        (Document) ((List) document.get(part)).get(0) :
                        (Document) document.get(part);
            }
            switch (type.toLowerCase()) {
                case "numerical":
                    return String.valueOf(intermediateDoc.getDouble(keySplit.get(keySplit.size() - 1)));
                case "date":
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    return df.format(intermediateDoc.getDate(keySplit.get(keySplit.size() - 1)));
                case "text":
                    return (intermediateDoc.getString(keySplit.get(keySplit.size() - 1)));
            }

        }
        switch (type.toLowerCase()) {
            case "numerical":
                return String.valueOf(document.getInteger(key));
            case "date":
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                return df.format(document.getDate(key));
            case "text":
                return document.getString(key);
        }
        return null;
    }
}
