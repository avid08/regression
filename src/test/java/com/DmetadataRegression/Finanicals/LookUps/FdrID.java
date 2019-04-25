package com.DmetadataRegression.Finanicals.LookUps;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.DmetadataRegression.Helpers.MetaType;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javafx.util.Pair;

public class FdrID extends FinancialLookUpType {
    public FdrID(MongoDatabase database) {
        super(database);
    }

    @Override
    public Pair<Document, String> extractDocument(List<Document> apiFieldMapList, MetaType.FieldMap fieldMap) {
        List<Bson> filtersList = new ArrayList<>();
        MongoCollection<Document> financialStatement_collection = database.getCollection("financial_statement");
        filtersList.add(Filters.exists("stmntDate"));
        filtersList.add(Filters.eq("data.fdrID", Integer.parseInt(fieldMap.lookUpSourceList.get(0).value)));
        FindIterable<Document> findIterable = financialStatement_collection.find(Filters.and(filtersList)).limit(1);
        // System.out.println("right before query to mongo");
        Document found = findIterable.first();
        // System.out.println("right after query to mongo");
        if (found==null) return new Pair<>(null,null);
        List<Document> dataList = (List<Document>) found.get("data");
        String testValue = "";
        for (Document dataPoint :
                dataList) {
            if (dataPoint.getInteger("fdrID") == Integer.parseInt(fieldMap.lookUpSourceList.get(0).value)) {
                Double d = dataPoint.getDouble("finalValue");
                if (fieldMap.dataType.equals("currency")) d *= 1000000;
                NumberFormat nf = DecimalFormat.getInstance();
                nf.setMaximumFractionDigits(4);
                nf.setGroupingUsed(false);
                testValue = nf.format(d);
                break;
            }
        }
        return new Pair<>(found, testValue);
    }
}
