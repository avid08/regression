package com.DmetadataRegression.FitchRatings.LookUps;

import org.bson.Document;

import com.DmetadataRegression.Helpers.MetaType;
import com.DmetadataRegression.Helpers.Mongo.MongoHelper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javafx.util.Pair;

public class RatingsCollection extends RatingsLookUpType {
    public RatingsCollection(String collection, MongoDatabase database) {
        super(collection, database);
    }

    @Override
    public Pair<Document, String> extractDocument(MetaType.FieldMap fieldMap) {
        if (fieldMap.fieldID.equals("FC_CVB_FLAG")) {

        } else {
            MongoCollection<Document> dbCollection = database.getCollection(collection);
            Document document = dbCollection.find(Filters.exists(fieldMap.lookUpSourceList.get(1).value)).first();
            Document resultDoc = new Document();
            switch (collection) {
                case "fitch_entity":
                    resultDoc.put("ID", document.getLong("agentID"));
                    break;
                case "fitch_peer":
                    //TODO - nothing here atm
                    //resultDoc.put("ID", document.getLong("agentID"));
                    return new Pair<>(null, null);
                default:
                    //TODO one field has no info in it
                    return new Pair<>(null, null);

            }
            String result = MongoHelper.getToStringWithTypeDotKey(document, fieldMap.dataType, fieldMap.lookUpSourceList.get(1).value);
            return new Pair<>(resultDoc, result);
        }
        return new Pair<>(null, null);
    }
}
