package com.moodysIssuerRatings;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.DmetadataRegression.Helpers.Mongo.MongoHelper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;

class MoodysIssuerHelper {
    static long getCount(String dataBaseServer, Date startDate, Date endDate) {
        MongoDatabase database = MongoHelper.getDataBase(dataBaseServer, "esp-9");
        MongoCollection<Document> collection = database.getCollection("moodys_ratings");
        List<Bson> aggregateList = new ArrayList<>();
        aggregateList.add(Aggregates.match(Filters.in(
                "rtngClassNbr",
                Arrays.asList(-1, -2, -200, -3, -4, -5, -6, 117053, 131206, 131211, 131280, 44478, 44479, 44480, 44716, 45055, 45072, 45077, 45138, 45148, 45164, 46491, 46492, 46494, 46495, 46497, 46498, 46499, 46505, 46506, 46507, 46508, 46509, 46622, 46623, 46624, 46644, 46645, 46646, 46664, 46665, 46671, 46672, 46675, 46676, 46677, 46678, 46679, 46702, 5166707, 5166708, 5166709, 5168217, 5168218)))
        );
        aggregateList.add(Aggregates.match(Filters.and(
                Filters.gte("chngDateInMillis", startDate.getTime()),
                Filters.lte("chngDateInMillis", endDate.getTime())))
        );
        aggregateList.add(Aggregates.group(JSON.parse("{ _id: null}"), Accumulators.sum("count", JSON.parse("{ $sum: 1 } "))));
        Object count = collection.aggregate(aggregateList).first().get("count");
        if (count instanceof Long) {
            return (long) count;
        } else {
            return new Long((Integer) count);
        }
    }
}
