package com.DmetadataRegression.Finanicals.LookUps;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.DmetadataRegression.Helpers.MetaType;
import com.DmetadataRegression.Helpers.Mongo.MongoHelper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javafx.util.Pair;

public class FinancialCollection extends FinancialLookUpType {
    public FinancialCollection(MongoDatabase database) {
        super(database);
    }

    @Override
    public Pair<Document, String> extractDocument(List<Document> apiFieldMapList, MetaType.FieldMap fieldMap) {
        MongoCollection<Document> financialStatement_collection = database.getCollection("financial_statement");
        List<Bson> nicknameFilters = new ArrayList<>();
        List<Bson> financialFilters = new ArrayList<>();
        switch (fieldMap.lookUpSourceList.get(0).value.toLowerCase()) {

            case "nickname":
                Document intermediate = financialStatement_collection.find().first();
                while (intermediate != null) {
                    nicknameFilters.clear();
                    MongoCollection<Document> nickname = database.getCollection("nickname");
                    nicknameFilters.add(Filters.not(Filters.eq(fieldMap.lookUpSourceList.get(1).value, null)));
                    //TODO ask about discrepancies in the lookupSources
                    nicknameFilters.add(Filters.eq("nickname", intermediate.getString("ncknm")));
                    FindIterable<Document> findIterable = nickname.find(Filters.and(nicknameFilters)).limit(1);
                    Document found = findIterable.first();

                    if (found != null) {
                        String testValue = found.getString(fieldMap.lookUpSourceList.get(1).value);
                        return new Pair<>(found, testValue);

                    }
                    financialFilters.add(Filters.not(Filters.eq("ncknm", intermediate.getString("ncknm"))));
                    intermediate = financialStatement_collection.find(Filters.and(financialFilters)).first();
                }
                //return null;
            case "financial_statement":
                nicknameFilters.add(Filters.not(Filters.eq(fieldMap.lookUpSourceList.get(1).value, null)));
                FindIterable<Document> findIterable = financialStatement_collection.find(Filters.and(nicknameFilters)).limit(1);
                Document found = findIterable.first();
                if (found != null) {
                    String testValue = MongoHelper
                            .getToStringWithTypeDotKey(found, fieldMap.dataType, fieldMap.lookUpSourceList.get(1).value);
                    return new Pair<>(found, testValue);
                }
        }
        return new Pair<>(null,null);
    }
}
