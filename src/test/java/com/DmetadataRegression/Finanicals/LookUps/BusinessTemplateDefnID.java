package com.DmetadataRegression.Finanicals.LookUps;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.DmetadataRegression.Helpers.MetaType;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import javafx.util.Pair;

public class BusinessTemplateDefnID extends FinancialLookUpType {
    private static final DecimalFormat decimalFormat = makeFormat();

    public BusinessTemplateDefnID(MongoDatabase database) {
        super(database);
    }

    private static DecimalFormat makeFormat(){
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols decimalFormatSymbols= new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setMaximumFractionDigits(4);
        return decimalFormat;
    }

    @Override
    public Pair<Document, String> extractDocument(List<Document> apiFieldMapList, MetaType.FieldMap fieldMap) {
        MongoCollection<Document> financialStatement_collection = database.getCollection("financial_statement");
        List<Bson> aggregateList = new ArrayList<>();
        int tmpltDefnID = 0;
        for (Document apiField :
                apiFieldMapList) {
            if (apiField.getString("_id").equals(fieldMap.fieldID)) {
                tmpltDefnID = ((List<Integer>) ((List<Document>) apiField.get("sourceLookupFields")).get(0).get("value")).get(0);
                break;
            }
        }
        String[] listOfLookUps = fieldMap.lookUpSourceList.get(1).value.split("\\s*(,)\\s*");
        for (String lookUp :
                listOfLookUps) {
            aggregateList.add(Aggregates.match(Filters.eq("tmpltDefnID", tmpltDefnID)));
            aggregateList.add(Aggregates.match(Filters.exists("stmntDate")));
            aggregateList.add(Aggregates.match(Filters.eq("data.fdrID", Integer.parseInt(lookUp))));
            AggregateIterable<Document> aggregateIterable = financialStatement_collection.aggregate(aggregateList);
            //  System.out.println("right before query to mongo");
            Document found = aggregateIterable.first();
            //System.out.println("right after query to mongo");
            if (found == null) continue;
            //System.out.println(found.toJson());
            List<Document> dataList = (List<Document>) found.get("data");
            String testValue = "";
            for (Document dataPoint :
                    dataList) {
                if (dataPoint.getInteger("fdrID") == Integer.parseInt(lookUp)) {
                    BigDecimal d = BigDecimal.valueOf(dataPoint.getDouble("finalValue"));
                    if (fieldMap.dataType.toLowerCase().equals("currency")) {
                        Document scale = (Document) found.get("scale");
                        double multiplier = Math.pow(10,scale.getInteger("scaleNumberZero"));
                            d = d.multiply(BigDecimal.valueOf(multiplier));
                    }
                    testValue = decimalFormat.format(d);
                }
            }
            return new Pair<>(found, testValue);
        }
        return new Pair<>(null, null);
    }
}
