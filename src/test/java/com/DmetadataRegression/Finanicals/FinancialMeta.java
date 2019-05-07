package com.DmetadataRegression.Finanicals;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;

import com.DmetadataRegression.Finanicals.LookUps.BusinessTemplateDefnID;
import com.DmetadataRegression.Finanicals.LookUps.FdrID;
import com.DmetadataRegression.Finanicals.LookUps.FinancialCollection;
import com.DmetadataRegression.Finanicals.LookUps.FinancialLookUpType;
import com.DmetadataRegression.Helpers.MetaType;
import com.DmetadataRegression.Helpers.MetaType.FieldMap;
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

public class FinancialMeta implements MetaType {
    private MongoDatabase database;
    private final List<Document> apiFieldMapList = new ArrayList<>();

    public FinancialMeta(String dataBaseServer) {
        database = MongoHelper.getDataBase(dataBaseServer, "financial-1");

        database.getCollection("api_field_map_financials").find().forEach((Consumer<Document>) apiFieldMapList::add);

    }

    public FinancialMeta() {

    }


    @Override
    public TestRecord getDataFromMongo(FieldMap fieldMap, String metaName) throws IOException {
        MongoFileBuffer mongoFileBuffer = MongoFileBuffer.getInstance(metaName);
        FinancialLookUpType finder = null;
        if (mongoFileBuffer.hasField(fieldMap)) {
            return mongoFileBuffer.getRecord(fieldMap);
        } else {
            switch (fieldMap.lookUpSourceList.get(0).name.toLowerCase()) {
                case "collection":
                    finder = new FinancialCollection(database);
                    break;
                case "businesstemplatedefnid":
                    finder = new BusinessTemplateDefnID(database);
                    break;
                case "fdrid":
                    finder = new FdrID(database);
                    break;
            }
            Pair<Document, String> foundPair = finder.extractDocument(apiFieldMapList, fieldMap);
            if (foundPair.getKey() == null) return mongoFileBuffer.insertRecord(fieldMap, new TestRecord().setNoData());
            return mongoFileBuffer.insertRecord(fieldMap, convertDocToRecord(foundPair.getKey(), foundPair.getValue()));
        }
    }

    @Override
    public Pair<String, Response> getDataFromAPI(TestRecord testRecord, FieldMap fieldMap, List<Header> headerList, String url) {
        RequestBodyBuilder requestBodyBuilder = new RequestBodyBuilder();
        requestBodyBuilder
                .setDate((testRecord.getDate()))
                .setEntities(testRecord.getTestID())
                .setFields(fieldMap.fieldID);
        testRecord.getOptions().forEach(option -> requestBodyBuilder.setOptions(option.getKey(), option.getValue()));
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

    @Override
    public TestRecord convertDocToRecord(Document document, String testValue) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        TestRecord testRecord = new TestRecord();
        testRecord.setTestID(new Long(document.getInteger("agentID")));
        testRecord.setDate(df.format(document.getDate("stmntDate")));
        testRecord.setTestValue(testValue);
        Document accountingStandard = (Document) document.get("accntSys");
        String accountingStandardString;
        switch (accountingStandard.getInteger("accntSysID")) {
            case 1:
                accountingStandardString = "USGAAP";
                break;
            case 2:
                accountingStandardString = "Local";
                break;
            case 3:
                accountingStandardString = "IAS";
                break;
            case 4:
                accountingStandardString = "Regulatory";
                break;
            case 5:
                accountingStandardString = "IFRS";
                break;
            case 6:
                accountingStandardString = "HGB";
                break;
            default:
                accountingStandardString = "ALL AVAILABLE";
                break;
        }
        testRecord.setOptions(new Pair<>("accountingStandard", accountingStandardString));
        testRecord.setOptions(new Pair<>("consolidation", document.getBoolean("cnsldtdFlg") ? "con" : "noncon"));
        String filingType;
        switch (document.getInteger("stmntTypID")) {
            case 0:
                filingType = "Original";
                break;
            case 1:
                filingType = "Restated";
                break;
            case 4:
                filingType = "Preliminary";
                break;
            default:
                filingType = "Latest";
                break;
        }
        testRecord.setOptions(new Pair<>("filingType", filingType));
        return testRecord;
    }
}
