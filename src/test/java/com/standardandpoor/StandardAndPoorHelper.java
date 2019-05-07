package com.standardandpoor;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.DmetadataRegression.Helpers.Mongo.MongoHelper;
import com.DmetadataRegression.Helpers.Request.RequestBody;
import com.DmetadataRegression.Helpers.Request.RequestBodyBuilder;
import com.DmetadataRegression.Helpers.Request.RequestBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

public class StandardAndPoorHelper {

    static private SimpleDateFormat queryFormat = new SimpleDateFormat("yyyy-MM-dd");

    static {
        queryFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    static boolean checkIfTwoRatingsInOneDay(String response, String dataBaseServer) {
        MongoDatabase database = MongoHelper.getDataBase(dataBaseServer, "esp-9");
        MongoCollection<Document> collection = database.getCollection("snp_ratings");
        JSONObject responseObject = null;
        try {
            responseObject = (JSONObject) new JSONParser().parse(response);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(
                    ((String) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) responseObject.get("data")).get("attributes")).get("dateOptions")).get("dates")).get(0)));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Date endDate = Date.from(date.toInstant().plus(Duration.ofDays(1)));
        long count = collection.count(Filters.and(
                Filters.eq("rtngTypTxt", "Foreign Currency LT"),
                Filters.eq("rtngGrpTxt", "Issuer Credit Rating"),
                Filters.eq("agentID", Long.valueOf((String) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) responseObject.get("data")).get("attributes")).get("entities")).get(0)).get("id"))),
                Filters.gte("rtngDt", date),
                Filters.lt("rtngDt", endDate)
                )
        );
        return count != 1;
    }

    enum Freshness {
        NEW, OLD
    }

    public static List<RequestBody> getBodyListForDataAggregator(String dataBaseServer, Freshness freshness, int ratingStatusIndex) {
        MongoDatabase database = MongoHelper.getDataBase(dataBaseServer, "esp-9");
        MongoCollection<Document> collection = database.getCollection("snp_ratings");
        List<Bson> aggregateList = getMainAggregation(freshness, ratingStatusIndex);
        boolean isShown = false;
        switch (freshness) {
            case OLD:
                if (ratingStatusIndex == 1)
                    isShown = true;
                break;
            case NEW:
                isShown = true;
                break;
        }
        aggregateList.add(Aggregates.project(Projections.fields(
                Projections.computed("agentId", "$agentID"),
                Projections.computed("date", "$rtngDt"),
                Projections.computed("isShown", Document.parse("{ \"$literal\" : " + (isShown ? "true" : "false") + " }"))
        )));
        List<RequestBody> bodyList = new ArrayList<>();
        String[] fieldList = {"FC_LT_FC_ISSR_SP",
                "FC_LT_FC_ISSR_DT_SP",
                "FC_LT_FC_ISSR_OUTLOOK_SP",
                "FC_LT_FC_ISSR_OUTLOOK_DT_SP",
                "FC_LT_FC_ISSR_CW_SP",
                "FC_LT_FC_ISSR_CW_DT_SP"};
        collection.aggregate(aggregateList).forEach((Consumer<? super Document>) document -> {
            RequestBuilder requestBuilder = new RequestBodyBuilder();
            requestBuilder.setEntities(document.getLong("agentId")).setFields(fieldList).setDate(queryFormat.format(document.getDate("date")));
            bodyList.add(requestBuilder.build());
        });
        return bodyList;
    }

    public static List<String> getIdListForEndpoint(String dataBaseServer, Freshness freshness, int ratingStatusIndex) {
        MongoDatabase database = MongoHelper.getDataBase(dataBaseServer, "esp-9");
        MongoCollection<Document> collection = database.getCollection("snp_ratings");
        List<Bson> aggregateList = getMainAggregation(freshness, ratingStatusIndex);
        boolean isShown = false;
        switch (freshness) {
            case OLD:
                if (ratingStatusIndex == 1)
                    isShown = true;
                break;
            case NEW:
                isShown = true;
                break;
        }
        aggregateList.add(Aggregates.project(Projections.fields(
                Projections.computed("agentId", "$agentID"),
                Projections.computed("rtngDataID", "$rtngDataID"),
                Projections.computed("ratingDateMillis", Document.parse("{ $ifNull: [ {$subtract: [ \"$rtngDt\" , {$date : 0} ] }, NumberLong(0) ] }")),
                Projections.computed("outlookDateMillis", Document.parse("{ $ifNull: [ {$subtract: [ \"$outlookDt\" , {$date : 0} ]}, NumberLong(0) ] }")),
                Projections.computed("creditWatchDateMillis", Document.parse("{ $ifNull: [ { $subtract: [ \"$creditWatchDt\" , {$date : 0} ] }, NumberLong(0) ] }")),
                Projections.computed("isShown", Document.parse("{ \"$literal\" : " + (isShown ? "true" : "false") + " }"))
        )));
        List<String> idList = new ArrayList<>();
        collection.aggregate(aggregateList).forEach((Consumer<? super Document>) document -> {
            StringBuilder sb = new StringBuilder();
            sb
                    .append(document.getLong("agentId")).append(".")
                    .append(document.getLong("rtngDataID")).append(".")
                    .append(document.getLong("ratingDateMillis")).append(".")
                    .append(document.getLong("outlookDateMillis")).append(".")
                    .append(document.getLong("creditWatchDateMillis"));

            idList.add(new String(sb));
        });
        return idList;
    }

    private static List<Bson> getMainAggregation(Freshness freshness, int ratingStatusIndex) {
        List<Bson> aggregateList = new ArrayList<>();
        aggregateList.add(Aggregates.match(
                Document.parse("{ \"$or\" : [ \n" +
                        "{ \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Brazil National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Foreign Currency ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Local Currency LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Nigeria National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Nordic Regional Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Program\"} , { \"rtngTypTxt\" : \"Local Currency ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Taiwan National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Turkey National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Russia National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Local Currency Preliminary LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Asset Manager Practices Classification\"} , { \"rtngTypTxt\" : \"Foreign Currency LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Ukraine National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Strength Rating\"} , { \"rtngTypTxt\" : \"Local Currency ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Brazil National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Program\"} , { \"rtngTypTxt\" : \"Foreign Currency LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Gulf Cooperative Council Regional Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Turkey National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Foreign Currency LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Greater China National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"South Africa National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Enhancement Rating\"} , { \"rtngTypTxt\" : \"Foreign Currency LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Strength Rating\"} , { \"rtngTypTxt\" : \"Local Currency LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Program\"} , { \"rtngTypTxt\" : \"Local Currency LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"ASEAN Regional Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"S&P Maalot (Israel) National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Gulf Cooperative Council Regional Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Program\"} , { \"rtngTypTxt\" : \"Foreign Currency ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Argentina National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Uruguay National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Strength Rating\"} , { \"rtngTypTxt\" : \"Foreign Currency ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Kazakhstan National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"South Africa National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Enhancement Rating\"} , { \"rtngTypTxt\" : \"Local Currency ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Foreign Currency Preliminary ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Local Currency ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Nigeria National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"S&P Maalot (Israel) National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Asset Manager Practices Classification\"} , { \"rtngTypTxt\" : \"Local Currency ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"CaVal (Mexico) National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Asset Manager Practices Classification\"} , { \"rtngTypTxt\" : \"Local Currency LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Strength Rating\"} , { \"rtngTypTxt\" : \"Foreign Currency LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Argentina National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Foreign Currency Preliminary LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"ASEAN Regional Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Enhancement Rating\"} , { \"rtngTypTxt\" : \"Local Currency LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Taiwan National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Financial Enhancement Rating\"} , { \"rtngTypTxt\" : \"Foreign Currency ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Uruguay National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Local Currency Preliminary ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Kazakhstan National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"CaVal (Mexico) National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Ukraine National Scale LT\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Greater China National Scale ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Asset Manager Practices Classification\"} , { \"rtngTypTxt\" : \"Foreign Currency ST\"}]} , { \"$and\" : [ { \"rtngGrpTxt\" : \"Issuer Credit Rating\"} , { \"rtngTypTxt\" : \"Russia National Scale LT\"}]}]}")
        ));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -5);
        Date fiveYearsAgo = cal.getTime();
        switch (freshness) {
            case NEW:
                aggregateList.add(Aggregates.match(
                        Filters.and(
                                Filters.eq("rtngStatusInd", ratingStatusIndex),
                                Filters.or(
                                        Filters.gte("rtngDt", fiveYearsAgo),
                                        Filters.gte("outlookDt", fiveYearsAgo),
                                        Filters.gte("creditWatchDt", fiveYearsAgo)
                                )
                        )
                ));
                break;
            case OLD:
                aggregateList.add(Aggregates.match(
                        Filters.and(
                                Filters.eq("rtngStatusInd", ratingStatusIndex),
                                Filters.lt("rtngDt", fiveYearsAgo),
                                Filters.lt("outlookDt", fiveYearsAgo),
                                Filters.lt("creditWatchDt", fiveYearsAgo)
                        )
                ));
                break;
        }
        aggregateList.add(Aggregates.match(Filters.and(
                Filters.eq("rtngTypTxt", "Foreign Currency LT"),
                Filters.eq("rtngGrpTxt", "Issuer Credit Rating"))
        ));
        aggregateList.add(Aggregates.limit(50));
        return aggregateList;
    }
}
