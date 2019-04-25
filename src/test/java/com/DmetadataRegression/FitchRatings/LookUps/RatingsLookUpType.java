package com.DmetadataRegression.FitchRatings.LookUps;

import org.bson.Document;

import com.DmetadataRegression.Helpers.MetaType;
import com.mongodb.client.MongoDatabase;

import javafx.util.Pair;

public abstract class RatingsLookUpType {
    final String collection;
    final MongoDatabase database;

    RatingsLookUpType(String collection, MongoDatabase database) {
        this.collection = collection;
        this.database = database;
    }

    public abstract Pair<Document, String> extractDocument(MetaType.FieldMap fieldMap);


}
