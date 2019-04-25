package com.DmetadataRegression.FitchRatings;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.DmetadataRegression.FitchRatings.LookUps.Issue;
import com.DmetadataRegression.FitchRatings.LookUps.Issuer;
import com.DmetadataRegression.FitchRatings.LookUps.RatingsCollection;
import com.DmetadataRegression.FitchRatings.LookUps.RatingsLookUpType;
import com.DmetadataRegression.Helpers.MetaType;
import com.DmetadataRegression.Helpers.Mongo.MongoFileBuffer;
import com.DmetadataRegression.Helpers.Mongo.MongoHelper;
import com.DmetadataRegression.Helpers.Request.RequestBody;
import com.DmetadataRegression.Helpers.Request.RequestBodyBuilder;
import com.DmetadataRegression.Helpers.TestRecord.TestRecord;
import com.google.gson.Gson;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import com.mongodb.client.MongoDatabase;

import javafx.util.Pair;

public class RatingsMeta implements MetaType {

    private MongoDatabase database;

    public RatingsMeta(String dataBaseServer) {
        database = MongoHelper.getDataBase(dataBaseServer, "esp-9");
    }

    public RatingsMeta() {
    }

    @Override
    public TestRecord convertDocToRecord(Document document, String testValue) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        TestRecord testRecord = new TestRecord();
        if (document.get("ID") instanceof Integer)
            testRecord.setTestID(new Long((Integer) document.get("ID")));
        else
            testRecord.setTestID((Long) document.get("ID"));
        testRecord.setDate(df.format(document.getDate("ratingDate")!=null?document.getDate("ratingDate"):new Date()));
        testRecord.setTestValue(testValue);
        return testRecord;
    }

    @Override
    public TestRecord getDataFromMongo(FieldMap fieldMap, String metaName) throws IOException {
        MongoFileBuffer mongoFileBuffer = MongoFileBuffer.getInstance(metaName);
        RatingsLookUpType finder = null;
        if (mongoFileBuffer.hasField(fieldMap)) {
            return mongoFileBuffer.getRecord(fieldMap);
        } else {
            switch (fieldMap.lookUpSourceList.get(0).name.toLowerCase()) {
                case "ratingtypeid":
                    switch (fieldMap.permissionsRequired.toLowerCase()) {
                        case "fitchissueratings":
                            finder = new Issue("fitch_ratable", database);
                            break;
                        case "fitchissuerratings":
                            finder = new Issuer("fitch_entity", database);
                            break;
                    }
                    break;
                case "collection":
                    finder = new RatingsCollection(fieldMap.lookUpSourceList.get(0).value, database);
                    break;
            }
            Pair<Document, String> foundPair = finder.extractDocument(fieldMap);
            if (foundPair.getKey() == null) return mongoFileBuffer.insertRecord(fieldMap, new TestRecord().setNoData());
            return mongoFileBuffer.insertRecord(fieldMap, convertDocToRecord(foundPair.getKey(), foundPair.getValue()));
        }
    }

    @Override
    public Pair<String, Response> getDataFromAPI(TestRecord testRecord, FieldMap fieldMap, List<Header> headerList, String url) {
        RequestBodyBuilder requestBodyBuilder = new RequestBodyBuilder();
        switch (fieldMap.permissionsRequired.toLowerCase()) {
            case "fitchissueratings":
                requestBodyBuilder.setIssues(testRecord.getTestID());
                break;
            case "fitchissuerratings":
                requestBodyBuilder.setEntities(testRecord.getTestID());
                break;
        }
        requestBodyBuilder
                .setDate((testRecord.getDate()))
                .setFields(fieldMap.fieldID);
        RequestBody requestBody = requestBodyBuilder.build();
        Gson gson = new Gson();
        String bodyString = gson.toJson(requestBody);
        Response response = given()
                .headers(new Headers(headerList))
                .contentType("application/vnd.api+json")
                .body(bodyString)
                .when()
                .post(url)
                .then()
                .extract()
                .response();
        return new Pair<>(bodyString, response);
    }
}
