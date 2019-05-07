package com.DmetadataRegression.Finanicals.LookUps;

import java.util.List;

import org.bson.Document;

import com.DmetadataRegression.Helpers.MetaType;
import com.mongodb.client.MongoDatabase;

import javafx.util.Pair;

public abstract class FinancialLookUpType {
    final MongoDatabase database;

    FinancialLookUpType(MongoDatabase database) {
        this.database = database;
    }

    public abstract Pair<Document, String> extractDocument(List<Document> apiFieldMapList, MetaType.FieldMap fieldMap);
}