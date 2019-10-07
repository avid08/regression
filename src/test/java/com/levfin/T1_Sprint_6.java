package com.levfin;

import com.backendutils.Env;
import com.backendutils.ExcelUtils;
import com.backendutils.MongoUtils;
import com.backendutils.PostgresUtils;
import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;
import com.mongodb.Block;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import groovy.json.internal.Charsets;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class T1_Sprint_6 extends Configuration {

    public static void initLogger(String testSuiteName) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
            Date date = new Date();
            String filePath = System.getProperty("user.home") + "\\IdeaProjects\\apiregresssion\\regression\\src\\test\\java\\com\\logs\\" + testSuiteName + "__" + dateFormat.format(date) + "_API_Test_Execution_Log.log";
            // String filePath = System.getProperty("user.home") + "\\IdeaProjects\\APIRegression\\src\\test\\java\\com\\logs\\log.log";
            org.apache.log4j.PatternLayout layout = new PatternLayout("%-5p %d %m%n");
            org.apache.log4j.RollingFileAppender appender = new RollingFileAppender(layout, filePath);
            appender.setName("Test_Execution_Log");
            appender.setMaxFileSize("10MB");
            appender.activateOptions();
            org.apache.log4j.Logger.getRootLogger().addAppender(appender);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static Logger setupLogger() {
        initLogger("T1_Sprint_9");
        Logger logger = Logger.getLogger("Test_Execution_Log_Sprint_9");
        return logger;
    }

    Logger logger = setupLogger();

    private Response getResponse(String uri) {
        return given()
                .header("Authorization", AuthrztionValue)
                .header("X-App-Client-Id", XappClintIDvalue)
                .header("accept", acceptValue)
                .header("Content", contentValue)
                .when().get(uri).then().statusCode(200)
                .extract().response();
    }

    private Response postToDataAggregator(String resourceFileName) throws IOException {

        URL file = Resources.getResource(resourceFileName);
        myjson = Resources.toString(file, Charsets.UTF_8);

        Response response = given()
                .header("Authorization", AuthrztionValue)
                .header("X-App-Client-Id", XappClintIDvalue)
                .contentType("application/vnd.api+json").body(myjson).with()
                .when().post(dataPostUrl).then().assertThat().statusCode(200)
                .extract()
                .response();
        return response;
    }

    private Response postToDataAggregatorBaseUser(String resourceFileName) throws IOException {

        URL file = Resources.getResource(resourceFileName);
        myjson = Resources.toString(file, Charsets.UTF_8);

        Response response = given()
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJodHRwOi8vaWRlbnRpdHktZGF0YS1zZXJ2aWNlOjgwODAvdjIvdXNlcnMvNWNhMjRkMzk2MmNjMzcwMDAxMzJlMjY2Iiwic2NvcGUiOlsidHJ1c3QiXSwiYXRpIjoiOTI3YTcyODEtMzJmNi00ZmYxLTg1N2UtNzAwMTk1MmM2MzkxIiwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5maXRjaGNvbm5lY3QtcWEuY29tIiwiZXhwIjoxNTczODIxOTg4LCJpYXQiOjE1Njg2Mzc5ODgsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI0ZjIxMjQ3ZC1kOTE1LTQ2YTItODZlOC0yZjFlNjk5YWY2ZDYiLCJjbGllbnRfaWQiOiJscXI3bzE0NWhmZ2FyNTNsa242b3FlOGRmYSJ9.iuXBcwghr82ZYhIBq5Br5OJtH0RtswcWgjGnUXfqQ38")
                .header("X-App-Client-Id", XappClintIDvalue)
                .contentType("application/vnd.api+json").body(myjson).with()
                .when().post(dataPostUrl).then().assertThat().statusCode(206)
                .extract()
                .response();
        return response;
    }

    private Integer timeoutBetweenTests = 2000;

    MongoUtils mongoUtils = new MongoUtils();

    @Test
    public void readDataFromExcel() {
        ExcelUtils excelUtils = new ExcelUtils();
        Object[][] excelOutput = excelUtils.getDataFromExcel(Resources.getResource("Lfi_mapping_July_2019.xlsx").getPath(), "MAPPING");
        System.out.println(Arrays.deepToString(excelOutput).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));

    }


    @Test
    public void Fisc5853_addCountiryInformationIntoTheResponse() {
        PostgresUtils pgutils = new PostgresUtils();
        Connection conn = pgutils.connectToPostgreDatabase(Env.Postgres.QA);
        ResultSet rs = pgutils.executePostgreScript(conn, Resources.getResource("5853.sql").getPath());
        Object[][] fisc5853Data = pgutils.resultSetToArray(rs, true);
        Object[][] expectedData = new Object[][]{

        };

        System.out.println(Arrays.deepToString(fisc5853Data).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));

        try {
//            logger.log(Level.INFO, "FISC 6537 PASSED! Tested " + stringsForTest + " STRING: FOUND IN API RESPONSE");
        } catch (AssertionError err) {
//            logger.log(Level.WARN, "FISC 6537 FAILED! Tested " + stringsForTest + " STRING: NOT FOUND IN API RESPONSE        ERROR: " + err);
//            Assert.fail();
        }
    }

    @Test
    public void Fisc6725_addPredefindValues() {

        //Expected Data
        ArrayList<String> expectedValuesList = new ArrayList<String>();
        expectedValuesList.add("NR");
        expectedValuesList.add("AAA");
        expectedValuesList.add("AA+");
        expectedValuesList.add("AA");
        expectedValuesList.add("AA-");
        expectedValuesList.add("A+");
        expectedValuesList.add("A");
        expectedValuesList.add("A-");
        expectedValuesList.add("BBB+");
        expectedValuesList.add("BBB");
        expectedValuesList.add("BBB-");
        expectedValuesList.add("BB+");
        expectedValuesList.add("BB");
        expectedValuesList.add("BB-");
        expectedValuesList.add("B+");
        expectedValuesList.add("B");
        expectedValuesList.add("B-");
        expectedValuesList.add("CCC+");
        expectedValuesList.add("CCC");
        expectedValuesList.add("CCC-");
        expectedValuesList.add("CC");
        expectedValuesList.add("C");
        expectedValuesList.add("DDD");
        expectedValuesList.add("DD");
        expectedValuesList.add("D");
        expectedValuesList.add("E");
        expectedValuesList.add("C+");
        expectedValuesList.add("C-");
        expectedValuesList.add("NR(EXP)");
        expectedValuesList.add("AAA(EXP)");
        expectedValuesList.add("AA+(EXP)");
        expectedValuesList.add("AA(EXP)");
        expectedValuesList.add("AA-(EXP)");
        expectedValuesList.add("A+(EXP)");
        expectedValuesList.add("A(EXP)");
        expectedValuesList.add("A-(EXP)");
        expectedValuesList.add("BBB+(EXP)");
        expectedValuesList.add("BBB(EXP)");
        expectedValuesList.add("BBB-(EXP)");
        expectedValuesList.add("BB+(EXP)");
        expectedValuesList.add("BB(EXP)");
        expectedValuesList.add("BB-(EXP)");
        expectedValuesList.add("B+(EXP)");
        expectedValuesList.add("B(EXP)");
        expectedValuesList.add("B-(EXP)");
        expectedValuesList.add("CCC+(EXP)");
        expectedValuesList.add("CCC(EXP)");
        expectedValuesList.add("CCC-(EXP)");
        expectedValuesList.add("CC(EXP)");
        expectedValuesList.add("C(EXP)");
        expectedValuesList.add("DDD(EXP)");
        expectedValuesList.add("DD(EXP)");
        expectedValuesList.add("D(EXP)");
        expectedValuesList.add("E(EXP)");
        expectedValuesList.add("PIF");
        expectedValuesList.add("WD");
        expectedValuesList.add("C+(EXP)");
        expectedValuesList.add("C-(EXP)");
        expectedValuesList.add("S");
        expectedValuesList.add("AAA (CAT)");
        expectedValuesList.add("AA (CAT)");
        expectedValuesList.add("A (CAT)");
        expectedValuesList.add("BBB (CAT)");
        expectedValuesList.add("BB (CAT)");
        expectedValuesList.add("B (CAT)");
        expectedValuesList.add("CCC (CAT)");
        expectedValuesList.add("CC (CAT)");
        expectedValuesList.add("C (CAT)");
        expectedValuesList.add("RD");
        expectedValuesList.add("AAA(SO)");
        expectedValuesList.add("AA+(SO)");
        expectedValuesList.add("AA(SO)");
        expectedValuesList.add("AA-(SO)");
        expectedValuesList.add("A+(SO)");
        expectedValuesList.add("A(SO)");
        expectedValuesList.add("A-(SO)");
        expectedValuesList.add("BBB+(SO)");
        expectedValuesList.add("BBB(SO)");
        expectedValuesList.add("BBB-(SO)");
        expectedValuesList.add("BB+(SO)");
        expectedValuesList.add("BB(SO)");
        expectedValuesList.add("BB-(SO)");
        expectedValuesList.add("B+(SO)");
        expectedValuesList.add("B(SO)");
        expectedValuesList.add("B-(SO)");
        expectedValuesList.add("C+(SO)");
        expectedValuesList.add("C(SO)");
        expectedValuesList.add("C-(SO)");
        expectedValuesList.add("D(SO)");
        expectedValuesList.add("AAA(SO)(EXP)");
        expectedValuesList.add("AA+(SO)(EXP)");
        expectedValuesList.add("AA(SO)(EXP)");
        expectedValuesList.add("AA-(SO)(EXP)");
        expectedValuesList.add("A+(SO)(EXP)");
        expectedValuesList.add("A(SO)(EXP)");
        expectedValuesList.add("A-(SO)(EXP)");
        expectedValuesList.add("BBB+(SO)(EXP)");
        expectedValuesList.add("BBB(SO)(EXP)");
        expectedValuesList.add("BBB-(SO)(EXP)");
        expectedValuesList.add("BB+(SO)(EXP)");
        expectedValuesList.add("BB(SO)(EXP)");
        expectedValuesList.add("BB-(SO)(EXP)");
        expectedValuesList.add("B+(SO)(EXP)");
        expectedValuesList.add("B(SO)(EXP)");
        expectedValuesList.add("B-(SO)(EXP)");
        expectedValuesList.add("C+(SO)(EXP)");
        expectedValuesList.add("C(SO)(EXP)");
        expectedValuesList.add("C-(SO)(EXP)");
        expectedValuesList.add("D(SO)(EXP)");
        expectedValuesList.add("AAAmfs");
        expectedValuesList.add("AA+mfs");
        expectedValuesList.add("AAmfs");
        expectedValuesList.add("AA-mfs");
        expectedValuesList.add("A+mfs");
        expectedValuesList.add("Amfs");
        expectedValuesList.add("A-mfs");
        expectedValuesList.add("BBB+mfs");
        expectedValuesList.add("BBBmfs");
        expectedValuesList.add("BBB-mfs");
        expectedValuesList.add("BB+mfs");
        expectedValuesList.add("BBmfs");
        expectedValuesList.add("BB-mfs");
        expectedValuesList.add("B+mfs");
        expectedValuesList.add("Bmfs");
        expectedValuesList.add("B-mfs");
        expectedValuesList.add("C+mfs");
        expectedValuesList.add("Cmfs");
        expectedValuesList.add("C-mfs");
        expectedValuesList.add("iAAA");
        expectedValuesList.add("iAA+");
        expectedValuesList.add("iAA");
        expectedValuesList.add("iAA-");
        expectedValuesList.add("iA+");
        expectedValuesList.add("iA");
        expectedValuesList.add("iA-");
        expectedValuesList.add("iBBB+");
        expectedValuesList.add("iBBB");
        expectedValuesList.add("iBBB-");
        expectedValuesList.add("iBB+");
        expectedValuesList.add("iBB");
        expectedValuesList.add("iBB-");
        expectedValuesList.add("iB+");
        expectedValuesList.add("iB");
        expectedValuesList.add("iB-");
        expectedValuesList.add("iCCC");
        expectedValuesList.add("iE");
        expectedValuesList.add("iAAA(EXP)");
        expectedValuesList.add("iAA+(EXP)");
        expectedValuesList.add("iAA(EXP)");
        expectedValuesList.add("iAA-(EXP)");
        expectedValuesList.add("iA+(EXP)");
        expectedValuesList.add("iA(EXP)");
        expectedValuesList.add("iA-(EXP)");
        expectedValuesList.add("iBBB+(EXP)");
        expectedValuesList.add("iBBB(EXP)");
        expectedValuesList.add("iBBB-(EXP)");
        expectedValuesList.add("iBB+(EXP)");
        expectedValuesList.add("iBB(EXP)");
        expectedValuesList.add("iBB-(EXP)");
        expectedValuesList.add("iB+(EXP)");
        expectedValuesList.add("iB(EXP)");
        expectedValuesList.add("iB-(EXP)");
        expectedValuesList.add("iCCC(EXP)");
        expectedValuesList.add("iE(EXP)");
        expectedValuesList.add("RAS");

        try {
            //MATCHING WITH API
            String fieldURI = baseURI + "/v1/metadata/fields/FC_LT_NIR_ISSR";
            Response predefinedValuesResponse = getResponse(fieldURI);
            ArrayList<String> predefinedValuesList = predefinedValuesResponse.path("data.attributes.predefinedValues");
            Assert.assertTrue(predefinedValuesList.equals(expectedValuesList));
            System.out.println("API Response " + Arrays.toString(predefinedValuesList.toArray()));


            //MATCHING WITH MONGO
            MongoCollection<Document> collection = mongoUtils
                    .connectToMongoDatabase(Env.Mongo.META_QA)
                    .getDatabase("xrefdb")
                    .getCollection("metadataFields");

            Block<Document> processBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {
                   Assert.assertTrue(document.toString().contains(Arrays.toString(predefinedValuesList.toArray())));
                }
            };

            List<? extends Bson> pipeline = Arrays.asList(
                    new Document().append("$match", new Document().append("dataSourceName", "Ratings")),
                    new Document().append("$unwind", new Document().append("path", "$fields").append("includeArrayIndex", "arrayIndex").append("preserveNullAndEmptyArrays", false)),
                    new Document().append("$match", new Document().append("fields.fitchFieldId", "FC_LT_NIR_ISSR")),
                    new Document().append("$unwind", new Document().append("path", "$fields"))
            );

            collection.aggregate(pipeline)
                    .allowDiskUse(false)
                    .forEach(processBlock);

        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
}
