package com.issueRatingTransition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.DmetadataRegression.Helpers.Mongo.MongoHelper;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

class IssueRatingsTransitionHelper {
    private static Document testDoc = null;
    static Document getTestDoc(String dataBaseServer) {
        if (testDoc == null) {
            MongoDatabase database = MongoHelper.getDataBase(dataBaseServer, "esp-9");
            MongoCollection<Document> collection = database.getCollection("issueRatings_transitions");
            List<Bson> aggregates = new ArrayList<>();
            aggregates.add(Aggregates.sort(new Document("ratingDate", -1)));
            aggregates.add(Aggregates.group("$ratableId", Accumulators.push("ratings", "$$ROOT")));
            aggregates.add(Aggregates.match(Filters.in("ratings.ratingCode", Arrays.asList("WD","D","DD","DDD"))));
            aggregates.add(Aggregates.match(Filters.nin("ratings.0.ratingCode", Arrays.asList("WD","D","DD","DDD"))));
            AggregateIterable<Document> iterable = collection.aggregate(aggregates).allowDiskUse(true);
            testDoc = iterable.first();
        }
        return testDoc;
    }
    static String getDateWithOffset(Document testDoc, Types types) {
        List<Document> listOfRatings = new ArrayList<>((List<Document>) testDoc.get("ratings"));
        Collections.reverse(listOfRatings);
        Document resultDoc = null;
        switch (types) {
            case FirstValid1:
                resultDoc = listOfRatings.get(0);
                break;
            case LastValid2:
                resultDoc = listOfRatings.get(listOfRatings.size() - 1);
                break;
            case LastValid1:
                for (int i = 0; i < listOfRatings.size(); i++) {
                    if (listOfRatings.get(i).getString("ratingCode").equals("WD"))
                        resultDoc = listOfRatings.get(i - 1);
                }
                break;
            case WD:
                for (Document listOfRating : listOfRatings) {
                    if (listOfRating.getString("ratingCode").equals("WD"))
                        resultDoc = listOfRating;
                }
                break;
            case FirstValid2:
                for (int i = 0; i < listOfRatings.size(); i++) {
                    if (listOfRatings.get(i).getString("ratingCode").equals("WD"))
                        resultDoc = listOfRatings.get(i + 1);
                }
                break;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(resultDoc.getDate("ratingDate"));
    }


}
