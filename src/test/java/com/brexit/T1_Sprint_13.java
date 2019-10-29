package com.brexit;

import com.apiutils.APIUtils;
import com.backendutils.Env;
import com.backendutils.MongoUtils;
import com.configuration.LoggerInitialization;
import com.configuration.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.response.Response;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import groovy.json.internal.Charsets;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class T1_Sprint_13 extends Configuration {

    Logger logger = LoggerInitialization.setupLogger("T1_Sprint_13");
    MongoUtils mongoUtils = new MongoUtils();
    APIUtils apiUtils = new APIUtils();

    private String baseUserToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJodHRwOi8vaWRlbnRpdHktZGF0YS1zZXJ2aWNlOjgwODAvdjIvdXNlcnMvNWRiNzE4ZmU2YzllZGIwMDAxMGNiNjhmIiwic2NvcGUiOlsidHJ1c3QiXSwiYXRpIjoiNDE3NmM2MTktYWM5YS00ZTBiLWE0MWItNzFlNGI4YzFhMDZiIiwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5maXRjaGNvbm5lY3QtcWEuY29tIiwiZXhwIjoxNTc3NTI2NTc4LCJpYXQiOjE1NzIzNDI1NzgsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJiOTA1NTIwMi01Yzk3LTQyM2ItOTNmZi0xMzlkNTllNmY5NmQiLCJjbGllbnRfaWQiOiJscXI3bzE0NWhmZ2FyNTNsa242b3FlOGRmYSJ9.9o8KlHNa7du2JoSkwO-D66s7dAjdSplH8N2hoDvF7PA";

    private Response postToDataAggregatorBaseUser(String resourceFileName, String xappClientIdValue, String dataPostUrl) throws IOException {

        URL file = Resources.getResource(resourceFileName);
        myjson = Resources.toString(file, Charsets.UTF_8);

        Response response = given()
                .header("Authorization", baseUserToken)
                .header("X-App-Client-Id", xappClientIdValue)
                .contentType("application/vnd.api+json").body(myjson).with()
                .when().post(dataPostUrl).then().assertThat().statusCode(206)
                .extract()
                .response();
        return response;
    }

    @DataProvider(name = "agentIds")
    public Object[][] getAgentIds(){
        return new Object[][] {
                {"1708"},
                {"12624"},
                {"102289"},
                {"113891"},
                {"141822"},
                {"1021561"}
        };
    }

    @Test(dataProvider = "agentIds")
    public void Fisc7821_EntitiesResource_AdditionalDisclosuresForUkRegulatoryAgency(String agentId) {
        try {
            MongoCollection<Document> collection = mongoUtils
                    .connectToMongoDatabase(Env.Mongo.CAL_QA2)
                    .getDatabase("esp-9")
                    .getCollection("fitch_entity");

            Document query = new Document();
            query.append("agentID", Long.parseLong(agentId));
            query.append("disclosureVOs", new Document().append("$exists", true));

            Document projection = new Document();
            projection.append("agentID", 1.0);
            projection.append("disclosureVOs", 1.0);

            Block<Document> processBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    try {
                        JSONParser jsonParser = new JSONParser();
                        Object object = null;
                        object = jsonParser.parse(document.toJson());

                        JSONObject mongoResponse = (JSONObject) object;
                        JSONArray mongoDisclosureVOs = (JSONArray) mongoResponse.get("disclosureVOs");

                        int mongoDisclosureVOsSize = mongoDisclosureVOs.size();

                        String uri = baseURI + "/v1/entities/" + agentId;
                        Response res = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
                        String apiResponse = res.asString();
                        String apiDisclosure = res.path("data.attributes.disclosure");
                        String apiDisclosures = res.path("data.attributes.disclosures").toString();

                        for (int i=0; i<mongoDisclosureVOsSize; i++){
                            JSONObject mongoDisclosures = (JSONObject) mongoDisclosureVOs.get(i);
                            String regulatoryAgency = (String) mongoDisclosures.get("regulatoryAgency");
                            String status = (String) mongoDisclosures.get("status");
                            Assert.assertTrue(apiDisclosure.contains(regulatoryAgency));
                            Assert.assertTrue(apiDisclosure.contains(status));
                            logger.info("FISC 7821 PASSED AGENT ID " + agentId + " REGULATORY AGENCY " + regulatoryAgency + " STATUS " + status);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                        logger.error("FISC 7821 FAILED PARSEEXCEPTION " + e);
                    } catch (AssertionError err) {
                        logger.error("FISC 7821 FAILED ASSERTION ERROR " + err);
                        Assert.fail();
                    }
                }
            };

            collection.find(query).projection(projection).forEach(processBlock);
        } catch (MongoException ex) {
            Assert.fail();
            logger.error("FISC 7821 FAILED MONGOEXCEPTION " + ex);
        }
    }

    @Test
    public void Fisc7822_IssuesResource_AdditionalDisclosuresForUkRegulatoryAgency() {
        MongoCollection<Document> collection = mongoUtils
                .connectToMongoDatabase(Env.Mongo.CAL_QA2)
                .getDatabase("esp-9")
                .getCollection("fitch_ratable");

        Document query = new Document();
        query.append("disclosureList", new Document().append("$exists", true));

        Document sort = new Document();
        sort.append("_id", -1.0);

        int limit = 1;

        Block<Document> processBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                try {
                    System.out.println(document.toJson());
                    JSONParser jsonParser = new JSONParser();
                    Object object = null;
                    object = jsonParser.parse(document.toJson());

                    JSONObject mongoResponse = (JSONObject) object;
                    Long ratableID = (Long) mongoResponse.get("ratableID");
                    JSONArray disclosureList = (JSONArray) mongoResponse.get("disclosureList");
                    int disclosureListSize = disclosureList.size();

                    String uri = baseURI + "/v1/issues/" + ratableID;
                    Response res = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
                    String apiResponse = res.asString();
                    String apiDisclosure = res.path("data.attributes.disclosure");

                    for (int i = 0; i < disclosureListSize; i++) {
                        JSONObject mongoDisclosures = (JSONObject) disclosureList.get(i);
                        String regulatoryAgency = (String) mongoDisclosures.get("regulatoryAgency");
                        String status = (String) mongoDisclosures.get("status");
                        System.out.println(regulatoryAgency);
                        System.out.println(status);
                       // String expectedDisclosure=regulatoryAgency+ ", " + status;

                       // System.out.println("\"disclosure\":\"" + expectedDisclosure);

                       // Assert.assertTrue(apiResponse.contains("\"disclosure\": \""+ expectedDisclosure));
                        Assert.assertTrue(apiResponse.contains("\"id\":\"" + ratableID + "\""));
                        Assert.assertTrue(apiResponse.contains("\"regulatoryAgency\":\"" + regulatoryAgency + "\""));
                        Assert.assertTrue(apiResponse.contains("\"status\":\"" + status + "\""));

                        logger.info("FISC 7822 PASSED REGULATORY AGENCY " + regulatoryAgency + " STATUS " + status);

                    }

                } catch (ParseException e) {
                    logger.error("FISC 7822 FAILED PARSE EXCEPTION " + e);
                    Assert.fail();
                } catch (AssertionError e){
                    logger.error("FISC 7822 FAILED ASSERTION ERROR " + e);
                    Assert.fail();
                }

            }
        };
        collection.find(query).sort(sort).limit(limit).forEach(processBlock);
    }

    @Test
    public void Fisc7823_RatingsMetadata_DisclosureFieldChanges() {
        MongoCollection<Document> collection = mongoUtils
                .connectToMongoDatabase(Env.Mongo.META_QA)
                .getDatabase("xrefdb")
                .getCollection("metadataFields");

        try {
            Block<Document> processBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    try {
                        JSONParser jsonParser = new JSONParser();
                        Object object = null;
                        object = jsonParser.parse(document.toJson());
                        JSONObject mongoResponse = (JSONObject) object;

                        JSONObject fields = (JSONObject) mongoResponse.get("fields");
                        String lookupSources = fields.get("lookupSources").toString();

                        String fitchFieldId = (String) fields.get("fitchFieldId");
                        String categoryId = (String) fields.get("categoryId");
                        String dataType = (String) fields.get("dataType");
                        String displayName = (String) fields.get("displayName");
                        String fitchFieldDesc = (String) fields.get("fitchFieldDesc");

                        if (mongoResponse.get("arrayIndex").toString().contains("383")){
                            Assert.assertEquals(fitchFieldId, "FC_ENDORSEMENT_COMPLIANCE");
                            Assert.assertEquals(categoryId, "15");
                            Assert.assertEquals(dataType, "Text");
                            Assert.assertEquals(fitchFieldDesc, "Endorsement Compliance");
                            Assert.assertEquals(displayName, "Endorsement Compliance");
                            Assert.assertTrue(lookupSources.contains("\"name\":\"field\",\"value\":\"disclosureList.status\""));
                            Assert.assertTrue(lookupSources.contains("\"name\":\"disclosureList.regulatoryAgency\",\"value\":\"EU\""));
                            logger.info("FISC 7823 MONGO PASSED FC_ENDORSEMENT_COMPLIANCE");
                        }
                        else if (mongoResponse.get("arrayIndex").toString().contains("392")){
                            Assert.assertEquals(fitchFieldId, "FC_UK_ENDORSEMENT_COMPLIANCE");
                            Assert.assertEquals(categoryId, "15");
                            Assert.assertEquals(dataType, "Text");
                            Assert.assertEquals(fitchFieldDesc, "UK Endorsement Compliance");
                            Assert.assertEquals(displayName, "UK Endorsement Compliance");
                            Assert.assertTrue(lookupSources.contains("\"name\":\"field\",\"value\":\"disclosureList.status\""));
                            Assert.assertTrue(lookupSources.contains("\"name\":\"disclosureList.regulatoryAgency\",\"value\":\"UK\""));
                            logger.info("FISC 7823 MONGO PASSED FC_UK_ENDORSEMENT_COMPLIANCE");
                        }
                        else if (mongoResponse.get("arrayIndex").toString().contains("393")){
                            Assert.assertEquals(fitchFieldId, "FC_EU_ENDORSEMENT_COMPLIANCE");
                            Assert.assertEquals(categoryId, "15");
                            Assert.assertEquals(dataType, "Text");
                            Assert.assertEquals(fitchFieldDesc, "EU Endorsement Compliance");
                            Assert.assertEquals(displayName, "EU Endorsement Compliance");
                            Assert.assertTrue(lookupSources.contains("\"name\":\"field\",\"value\":\"disclosureList.status\""));
                            Assert.assertTrue(lookupSources.contains("\"name\":\"disclosureList.regulatoryAgency\",\"value\":\"EU\""));
                            logger.info("FISC 7823 MONGO PASSED FC_EU_ENDORSEMENT_COMPLIANCE");
                        }
                        else throw new AssertionError();


                        String uri = baseURI + "/v1/metadata/fields/" + fitchFieldId;
                        String apiResponse = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue).asString();
                        Assert.assertTrue(apiResponse.contains("\"id\":\"" + fitchFieldId + "\""));
                        Assert.assertTrue(apiResponse.contains("\"displayName\":\"" + displayName + "\""));
                        Assert.assertTrue(apiResponse.contains("\"type\":\"" + dataType + "\""));
                        Assert.assertTrue(apiResponse.contains("\"fitchFieldDesc\":\"" + fitchFieldDesc + "\""));
                        logger.info("FISC 7823 API PASSED URI " + uri);

                    } catch (ParseException e) {
                        logger.error("FISC 7823 FAILED PARSEEXCEPTION " + e);
                        Assert.fail();
                    }
                }
            };

            List<? extends Bson> pipeline = Arrays.asList(
                    new Document()
                            .append("$unwind", new Document()
                                    .append("path", "$fields")
                                    .append("includeArrayIndex", "arrayIndex")
                                    .append("preserveNullAndEmptyArrays", false)
                            ),
                    new Document()
                            .append("$match", new Document()
                                    .append("$or", Arrays.asList(
                                            new Document()
                                                    .append("fields.fitchFieldId", "FC_ENDORSEMENT_COMPLIANCE"),
                                            new Document()
                                                    .append("fields.fitchFieldId", "FC_EU_ENDORSEMENT_COMPLIANCE"),
                                            new Document()
                                                    .append("fields.fitchFieldId", "FC_UK_ENDORSEMENT_COMPLIANCE")
                                            )
                                    )
                            )
            );

            collection.aggregate(pipeline)
                    .allowDiskUse(false)
                    .forEach(processBlock);

            AggregateIterable<Document> result = collection.aggregate(pipeline).allowDiskUse(false);
            int i = 0;
            for (Document document : result){
                i++;
            }
            if (i<3){
                logger.warn("FISC 7823 WARNING - ONE OR MORE ATTRIBUTES ARE MISSING!!!");
            }
            else if (i==3){
                logger.info("FISC 7823 OK - ALL NECESSARY ATTRIBUTES ARE PRESENT!");
            }
            else if (i>3) {
                logger.warn("FISC 7823 WARNING - THERE ARE MORE ATTRIBUTES BESIDES: " +
                        "\nFC_ENDORSEMENT_COMPLIANCE \nFC_EU_ENDORSEMENT_COMPLIANCE" +
                        "\nFC_UK_ENDORSEMENT_COMPLIANCE");
            }
            else throw new AssertionError();

        } catch (MongoException e) {
            logger.error("FISC 7823 FAILED MONGOEXCEPTION " + e);
            Assert.fail();
        }
    }

    @Test
    public void Fisc7824_DataAggregator_RatingsMetadata_DisclosureFieldChanges() {
        try {
            MongoCollection<Document> collection = mongoUtils
                    .connectToMongoDatabase(Env.Mongo.CAL_QA2)
                    .getDatabase("esp-9")
                    .getCollection("fitch_entity");

            BasicDBObject query = new BasicDBObject();
            query.put("agentID", 102889);
            query.put("_id", new ObjectId("000010288900000090206861"));

            BasicDBObject projection = new BasicDBObject();
            projection.put("disclosureVOs", 1.0);
            projection.put("agentID", 1.0);
            projection.put("groupID", 1.0);

            BasicDBObject sort = new BasicDBObject();
            sort.put("_id", 1.0);

            String json = null;
            MongoCursor<Document> cursor = collection.find(query).projection(projection).sort(sort).iterator();
            while (cursor.hasNext()) {
                json = cursor.next().toJson();
            }

            net.minidev.json.JSONArray disclosureVOs = JsonPath.read(json, "$.disclosureVOs");
            int disclosureVOsSize = disclosureVOs.size();

            Response res = apiUtils.postToDataAggregator("7824.json", AuthrztionValue, XappClintIDvalue, dataPostUrl);
            String apiResponse = res.asString();

            ArrayList<String> values = res.path("data.attributes.entities[0].values");
            int numberOfValues = values.size();
            HashMap<String, String> apiDisclosures = new HashMap<String, String>();

            //MONGO vs API Validation
            for (int i = 0; i < disclosureVOsSize; i++) {
                HashMap<String, String> mongoDisclosures = (HashMap<String, String>) disclosureVOs.get(i);
                String regulatoryAgency = (String) mongoDisclosures.get("regulatoryAgency");
                String status = (String) mongoDisclosures.get("status");

                try {
                    Assert.assertNotNull(regulatoryAgency);
                    Assert.assertNotNull(status);
                    logger.info("FISC 7824 MONGO OK! " + regulatoryAgency + " REGULATORY AGENCY AND STATUS VALUES ARE NOT NULL!");
                } catch (AssertionError err) {
                    logger.error("FISC 7824 MONGO FAILED! " + regulatoryAgency + " REGULATORY AGENCY AND STATUS VALUES ARE NULL!");
                    Assert.fail();
                }

                try {
                    switch (regulatoryAgency) {
                        case "UK":
                            Assert.assertTrue(mongoDisclosures.containsValue("UK"));
                            Assert.assertTrue(mongoDisclosures.containsValue("Issued"));
                            System.out.println(status);
                            Assert.assertTrue(status.equals("Issued"));
                            logger.info("FISC 7824 MONGO UK PASSED");
                            break;

                        case "EU":
                            Assert.assertTrue(mongoDisclosures.containsValue("EU"));
                            Assert.assertTrue(mongoDisclosures.containsValue("Endorsed"));
                            System.out.println(status);
                            Assert.assertTrue(status.equals("Endorsed"));
                            logger.info("FISC 7824 MONGO EU PASSED");
                            break;

                        default:
                            Assert.assertTrue(true);
                            logger.warn("FISC 7824 WARNING! NEW REGULATORY AGENCY " + regulatoryAgency + " EXISTS!");
                            break;
                    }
                } catch (AssertionError err) {
                    logger.error("FISC 7824 MONGO VALUES FAILED! " + err);
                    Assert.fail();
                }

                try {
                    Assert.assertTrue(apiResponse.contains(regulatoryAgency));
                    Assert.assertTrue(apiResponse.contains(status));
                    logger.info("FISC 7824 MONGO vs API PASSED");
                } catch (AssertionError err) {
                    logger.error("FISC 7824 MONGO vs API FAILED! " + err);
                    Assert.fail();
                }
            }

            //API values Validation
            for (int j = 0; j < numberOfValues; j++) {
                String apiFitchFieldId = res.path("data.attributes.entities[0].values[" + j + "].fitchFieldId");
                String apiValue = res.path("data.attributes.entities[0].values[" + j + "].values[0].value[0]");

                System.out.println(apiFitchFieldId + "  " + apiValue);

                try {
                    switch (apiFitchFieldId) {
                        case "FC_ENDORSEMENT_COMPLIANCE":
                            Assert.assertEquals(apiValue, "Endorsed");
                            logger.info("FISC 7824 API PASSED FC_ENDORSEMENT_COMPLIANCE");
                            break;
                        case "FC_EU_ENDORSEMENT_COMPLIANCE":
                            Assert.assertEquals(apiValue, "Endorsed");
                            logger.info("FISC 7824 API PASSED FC_EU_ENDORSEMENT_COMPLIANCE");
                            break;
                        case "FC_UK_ENDORSEMENT_COMPLIANCE":
                            Assert.assertEquals(apiValue, "Issued");
                            logger.info("FISC 7824 PASSED FC_UK_ENDORSEMENT_COMPLIANCE");
                            break;
                        default:
                            Assert.assertTrue(true);
                            logger.warn("FISC 7824 WARNING NEW FITCHFIELDID " + apiFitchFieldId + " EXISTS IN API");
                            break;
                    }
                    logger.info("FISC 7824 API VALUES PASSED");
                } catch (AssertionError err) {
                    logger.error("FISC 7824 API VALUES FAILED! " + err);
                    Assert.fail();
                }
            }


            //API Base User Validation
            Response res_base = postToDataAggregatorBaseUser("7824.json", XappClintIDvalue, dataPostUrl);

            ArrayList<String> values_baseUser = res_base.path("data.attributes.entities[0].values");
            int numberOfValues_baseUser = values_baseUser.size();

            for (int m = 0; m < numberOfValues_baseUser; m++){
                String apiFitchFieldId = res_base.path("data.attributes.entities[0].values[" + m + "].fitchFieldId").toString();
                String type = res_base.path("data.attributes.entities[0].values[" + m + "].type").toString();
                String isRestricted = res_base.path("data.attributes.entities[0].values[" + m + "].isRestricted").toString();

                try {
                    switch (apiFitchFieldId) {
                        case "FC_ENDORSEMENT_COMPLIANCE":
                            Assert.assertEquals(isRestricted, "true");
                            logger.info("FISC 7824 API PASSED BASE USER FITCHFIELDID " + apiFitchFieldId);
                            break;
                        case "FC_EU_ENDORSEMENT_COMPLIANCE":
                            Assert.assertEquals(isRestricted, "true");
                            logger.info("FISC 7824 API PASSED BASE USER FITCHFIELDID " + apiFitchFieldId);
                            break;
                        case "FC_UK_ENDORSEMENT_COMPLIANCE":
                            Assert.assertEquals(isRestricted, "true");
                            logger.info("FISC 7824 API PASSED BASE USER FITCHFIELDID " + apiFitchFieldId);
                            break;
                        default:
                            logger.warn("FISC 7824 API WARNING BASE USER NEW FITCHFIELDID " + apiFitchFieldId + " EXISTS IN API!");
                            break;
                    }
                } catch (AssertionError err){
                    logger.warn("FISC 7824 API FAILED BASE USER ASSERTIONERROR " + err);
                    Assert.fail();
                }
            }

        } catch (IOException e) {
            logger.error("FISC 7824 FAILED IOEXCEPTION " + e);
            Assert.fail();
        } catch (AssertionError err){
            logger.error("FISC 7824 FAILED ASSERTION ERROR " + err);
            Assert.fail();
        }
    }

    @Test
    public void Fisc7827_IssuesMetadata_DisclosureFieldChanges() {

        MongoCollection<Document> collection = mongoUtils
                .connectToMongoDatabase(Env.Mongo.META_QA)
                .getDatabase("xrefdb")
                .getCollection("metadataFields");
        try {
            Block<Document> processBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    try {
                        JSONParser jsonParser = new JSONParser();
                        Object object = null;
                        object = jsonParser.parse(document.toJson());
                        JSONObject mongoResponse = (JSONObject) object;

                        JSONObject fields = (JSONObject) mongoResponse.get("fields");
                        String lookupSources = fields.get("lookupSources").toString();

                        String fitchFieldId = (String) fields.get("fitchFieldId");
                        String categoryId = (String) fields.get("categoryId");
                        String dataType = (String) fields.get("dataType");
                        String displayName = (String) fields.get("displayName");
                        String fitchFieldDesc = (String) fields.get("fitchFieldDesc");

                        if (mongoResponse.get("arrayIndex").toString().contains("9")) {
                            Assert.assertEquals(fitchFieldId, "FC_ISSUE_DISCLOSURE_REG_AGENCY");
                            Assert.assertEquals(categoryId, "15");
                            Assert.assertEquals(dataType, "Text");
                            Assert.assertEquals(fitchFieldDesc, "Issue Disclosure Regulatory Agency");
                            Assert.assertEquals(displayName, "Issue Disclosure Regulatory Agency");
                            Assert.assertTrue(lookupSources.contains("\"name\":\"field\",\"value\":\"disclosureList.regulatoryAgency\""));
                            Assert.assertTrue(lookupSources.contains("\"name\":\"disclosureList.regulatoryAgency\",\"value\":\"EU\""));
                            logger.info("FISC 7827 MONGO PASSED FC_ISSUE_DISCLOSURE_REG_AGENCY");
                        }
                        else if (mongoResponse.get("arrayIndex").toString().contains("10")) {
                            Assert.assertEquals(fitchFieldId, "FC_ISSUE_DISCLOSURE_STATUS");
                            Assert.assertEquals(categoryId, "15");
                            Assert.assertEquals(dataType, "Text");
                            Assert.assertEquals(fitchFieldDesc, "Issue Disclosure Status");
                            Assert.assertEquals(displayName, "Issue Disclosure Status");
                            Assert.assertTrue(lookupSources.contains("\"name\":\"field\",\"value\":\"disclosureList.status\""));
                            Assert.assertTrue(lookupSources.contains("\"name\":\"disclosureList.regulatoryAgency\",\"value\":\"EU\""));
                            logger.info("FISC 7827 MONGO PASSED FC_ISSUE_DISCLOSURE_STATUS");
                        }
                        else if (mongoResponse.get("arrayIndex").toString().contains("21")) {
                            Assert.assertEquals(fitchFieldId, "FC_ISSUE_EU_ENDORSEMENT_COMPLIANCE");
                            Assert.assertEquals(categoryId, "15");
                            Assert.assertEquals(dataType, "Text");
                            Assert.assertEquals(fitchFieldDesc, "Issue EU Endorsement Compliance");
                            Assert.assertEquals(displayName, "Issue EU Endorsement Compliance");
                            Assert.assertTrue(lookupSources.contains("\"name\":\"field\",\"value\":\"disclosureList.status\""));
                            Assert.assertTrue(lookupSources.contains("\"name\":\"disclosureList.regulatoryAgency\",\"value\":\"EU\""));
                            logger.info("FISC 7827 MONGO PASSED FC_ISSUE_EU_ENDORSEMENT_COMPLIANCE");
                        }
                        else if (mongoResponse.get("arrayIndex").toString().contains("22")) {
                            Assert.assertEquals(fitchFieldId, "FC_ISSUE_UK_ENDORSEMENT_COMPLIANCE");
                            Assert.assertEquals(categoryId, "15");
                            Assert.assertEquals(dataType, "Text");
                            Assert.assertEquals(fitchFieldDesc, "Issue UK Endorsement Compliance");
                            Assert.assertEquals(displayName, "Issue UK Endorsement Compliance");
                            Assert.assertTrue(lookupSources.contains("\"name\":\"field\",\"value\":\"disclosureList.status\""));
                            Assert.assertTrue(lookupSources.contains("\"name\":\"disclosureList.regulatoryAgency\",\"value\":\"UK\""));
                            logger.info("FISC 7827 MONGO PASSED FC_ISSUE_UK_ENDORSEMENT_COMPLIANCE");
                        }
                        else throw new AssertionError();

                        String uri = baseURI + "/v1/metadata/fields/" + fitchFieldId;
                        String apiResponse = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue).asString();
                        Assert.assertTrue(apiResponse.contains("\"id\":\"" + fitchFieldId + "\""));
                        Assert.assertTrue(apiResponse.contains("\"displayName\":\"" + displayName + "\""));
                        Assert.assertTrue(apiResponse.contains("\"type\":\"" + dataType + "\""));
                        Assert.assertTrue(apiResponse.contains("\"fitchFieldDesc\":\"" + fitchFieldDesc + "\""));
                        logger.info("FISC 7827 API PASSED URI " + uri);

                    } catch (ParseException e) {
                        e.printStackTrace();
                        logger.error("FISC 7827 FAILED PARSEEXCEPTION " + e);
                    } catch (AssertionError err) {
                        logger.error("FISC 7827 FAILED ASSERTIONERROR " + err);
                        Assert.fail();
                    }
                }
            };

            List<? extends Bson> pipeline = Arrays.asList(
                    new Document()
                            .append("$unwind", new Document()
                                    .append("path", "$fields")
                                    .append("includeArrayIndex", "arrayIndex")
                                    .append("preserveNullAndEmptyArrays", false)
                            ),
                    new Document()
                            .append("$match", new Document()
                                    .append("$or", Arrays.asList(
                                            new Document()
                                                    .append("fields.fitchFieldId", "FC_ISSUE_EU_ENDORSEMENT_COMPLIANCE"),
                                            new Document()
                                                    .append("fields.fitchFieldId", "FC_ISSUE_UK_ENDORSEMENT_COMPLIANCE"),
                                            new Document()
                                                    .append("fields.fitchFieldId", "FC_ISSUE_DISCLOSURE_REG_AGENCY"),
                                            new Document()
                                                    .append("fields.fitchFieldId", "FC_ISSUE_DISCLOSURE_STATUS")
                                            )
                                    )
                            )
            );

            collection.aggregate(pipeline)
                    .allowDiskUse(false)
                    .forEach(processBlock);

            AggregateIterable<Document> result = collection.aggregate(pipeline).allowDiskUse(false);
            int i = 0;
            for (Document document : result){
                i++;
            }
            if (i<4){
                logger.warn("FISC 7827 WARNING - ONE OR MORE ATTRIBUTES ARE MISSING!!!");
            }
            else if (i==4){
                logger.info("FISC 7827 OK - ALL NECESSARY ATTRIBUTES ARE PRESENT!");
            }
            else if (i>4) {
                logger.warn("FISC 7827 WARNING - THERE ARE MORE ATTRIBUTES BESIDES: " +
                        "\nFC_ISSUE_EU_ENDORSEMENT_COMPLIANCE \nFC_ISSUE_UK_ENDORSEMENT_COMPLIANCE" +
                        "\nFC_ISSUE_DISCLOSURE_REG_AGENCY \nFC_ISSUE_DISCLOSURE_STATUS");
            }
            else throw new AssertionError();

        }
        catch (MongoException e) {
            Assert.fail();
            logger.error("FISC 7827 FAILED MONGOEXCEPTION " + e);
        }
        catch (AssertionError err){
            logger.error("FISC 7827 FAILED ASSERTION ERROR " + err);
            Assert.fail();
        }
    }

    @Test
    public void Fisc7828_DataAggregator_IssuesMetadata_DisclosureFieldChanges() {
        try {
            MongoCollection<Document> collection = mongoUtils
                    .connectToMongoDatabase(Env.Mongo.CAL_QA2)
                    .getDatabase("esp-9")
                    .getCollection("fitch_ratable");

            BasicDBObject query = new BasicDBObject();
            query.put("ratableID", 1020009105);

            BasicDBObject projection = new BasicDBObject();
            projection.put("disclosureList", 1.0);
            projection.put("ratableID", 1.0);

            BasicDBObject sort = new BasicDBObject();
            sort.put("_id", 1.0);
            String json = null;

            MongoCursor<Document> cursor = collection.find(query).projection(projection).sort(sort).iterator();
            while (cursor.hasNext()) {
                json = cursor.next().toJson();
            }
            int ratableID = JsonPath.read(json, "$.ratableID");
            net.minidev.json.JSONArray disclosureList = JsonPath.read(json, "$.disclosureList[*]");
            int disclosureListSize = disclosureList.size();

            Response res = apiUtils.postToDataAggregator("7828.json", AuthrztionValue, XappClintIDvalue, dataPostUrl);
            String apiResponse = res.asString();

            Response res_base = apiUtils.postToDataAggregatorBaseUser("7828.json", XappClintIDvalue, dataPostUrl);
            String apiResponse_base = res_base.asString();

            //MONGO VALIDATION
            for (int i = 0; i < disclosureListSize; i++) {
                HashMap<String, String> mongoDisclosures = (HashMap<String, String>) disclosureList.get(i);
                String regulatoryAgency = (String) mongoDisclosures.get("regulatoryAgency");
                String status = (String) mongoDisclosures.get("status");

                try {
                    Assert.assertNotNull(regulatoryAgency);
                    Assert.assertNotNull(status);
                    logger.info("FISC 7828 MONGO OK! " + regulatoryAgency + " REGULATORY AGENCY AND STATUS VALUES ARE NOT NULL!");
                } catch (AssertionError err) {
                    logger.error("FISC 7828 MONGO FAILED! " + regulatoryAgency + " REGULATORY AGENCY AND STATUS VALUES ARE NULL!");
                    Assert.fail();
                }

                try {
                    switch (regulatoryAgency) {
                        case "UK":
                            Assert.assertTrue(mongoDisclosures.containsValue("UK"));
                            Assert.assertTrue(mongoDisclosures.containsValue("Issued"));
                            System.out.println(status);
                            Assert.assertTrue(status.equals("Issued"));
                            logger.info("FISC 7828 MONGO UK PASSED");
                            break;

                        case "EU":
                            Assert.assertTrue(mongoDisclosures.containsValue("EU"));
                            Assert.assertTrue(mongoDisclosures.containsValue("Endorsed"));
                            System.out.println(status);
                            Assert.assertTrue(status.equals("Endorsed"));
                            logger.info("FISC 7828 MONGO EU PASSED");
                            break;

                        default:
                            Assert.assertTrue(true);
                            logger.warn("FISC 7828 WARNING! NEW REGULATORY AGENCY " + regulatoryAgency + " EXISTS!");
                            break;
                    }
                } catch (AssertionError err) {
                    logger.error("FISC 7828 MONGO VALUES FAILED! " + err);
                    Assert.fail();
                }

                try {
                    Assert.assertTrue(apiResponse.contains(regulatoryAgency));
                    Assert.assertTrue(apiResponse.contains(status));
                    logger.info("FISC 7828 MONGO vs API PASSED");
                } catch (AssertionError err) {
                    logger.error("FISC 7828 MONGO vs API FAILED! " + err);
                    Assert.fail();
                }
            }

            ArrayList<String> values = res.path("data.attributes.issues[0].values");
            int numberOfValues = values.size();
            //API Validation
            for (int j = 0; j < numberOfValues; j++){
                String apiFitchFieldId = res.path("data.attributes.issues[0].values[" + j + "].fitchFieldId").toString();
                String apiValue = res.path("data.attributes.issues[0].values[" + j + "].values[0].value[0]").toString();

                try {
                    switch (apiFitchFieldId) {
                        case "FC_ISSUE_DISCLOSURE_REG_AGENCY":
                            Assert.assertEquals(apiValue, "EU");
                            logger.info("FISC 7828 API PASSED FITCHFIELDID " + apiFitchFieldId);
                            break;
                        case "FC_ISSUE_DISCLOSURE_STATUS":
                            Assert.assertEquals(apiValue, "Endorsed");
                            logger.info("FISC 7828 API PASSED FITCHFIELDID " + apiFitchFieldId);
                            break;
                        case "FC_ISSUE_UK_ENDORSEMENT_COMPLIANCE":
                            Assert.assertEquals(apiValue, "Issued");
                            logger.info("FISC 7828 API PASSED FITCHFIELDID " + apiFitchFieldId);
                            break;
                        case "FC_ISSUE_EU_ENDORSEMENT_COMPLIANCE":
                            Assert.assertEquals(apiValue, "Endorsed");
                            logger.info("FISC 7828 API PASSED FITCHFIELDID " + apiFitchFieldId);
                            break;
                        default:
                            logger.warn("FISC 7828 API WARNING NEW FITCHFIELDID " + apiFitchFieldId + " EXISTS IN API!");
                            break;
                    }
                } catch (AssertionError err){
                    logger.warn("FISC 7828 API FAILED ASSERTIONERROR " + err);
                    Assert.fail();
                }
            }

            ArrayList<String> values_baseUser = res_base.path("data.attributes.issues[0].values");
            int numberOfValues_baseUser = values_baseUser.size();

            for (int k = 0; k < numberOfValues_baseUser; k++){
                String apiFitchFieldId = res_base.path("data.attributes.issues[0].values[" + k + "].fitchFieldId").toString();
                String type = res_base.path("data.attributes.issues[0].values[" + k + "].type").toString();
                String isRestricted = res_base.path("data.attributes.issues[0].values[" + k + "].isRestricted").toString();

                try {
                    switch (apiFitchFieldId) {
                        case "FC_ISSUE_DISCLOSURE_REG_AGENCY":
                            Assert.assertEquals(isRestricted, "true");
                            logger.info("FISC 7828 API PASSED BASE USER FITCHFIELDID " + apiFitchFieldId);
                            break;
                        case "FC_ISSUE_DISCLOSURE_STATUS":
                            Assert.assertEquals(isRestricted, "true");
                            logger.info("FISC 7828 API PASSED BASE USER FITCHFIELDID " + apiFitchFieldId);
                            break;
                        case "FC_ISSUE_UK_ENDORSEMENT_COMPLIANCE":
                            Assert.assertEquals(isRestricted, "true");
                            logger.info("FISC 7828 API PASSED BASE USER FITCHFIELDID " + apiFitchFieldId);
                            break;
                        case "FC_ISSUE_EU_ENDORSEMENT_COMPLIANCE":
                            Assert.assertEquals(isRestricted, "true");
                            logger.info("FISC 7828 API PASSED BASE USER FITCHFIELDID " + apiFitchFieldId);
                            break;
                        default:
                            logger.warn("FISC 7828 API WARNING BASE USER NEW FITCHFIELDID " + apiFitchFieldId + " EXISTS IN API!");
                            break;
                    }
                } catch (AssertionError err){
                    logger.warn("FISC 7828 API FAILED BASE USER ASSERTIONERROR " + err);
                    Assert.fail();
                }
            }
        } catch (IOException ex) {
            logger.error("FISC 7828 FAILED IOEXCEPTION " + ex);
            Assert.fail();
        } catch (ClassCastException cce) {
            logger.warn("FISC 7828 WARNING CLASSCASTEXCEPTION " + cce);
        } catch (IllegalArgumentException iae) {
            logger.warn("FISC 7828 WARNING ILLEGALARGUMENTEXCEPTION " + iae);
        }
    }
}
