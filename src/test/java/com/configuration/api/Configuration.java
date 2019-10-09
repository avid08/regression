
package com.configuration.api;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Configuration {

    public Response response;
    protected String myjson;
    protected String AuthrztionValue;
    protected String baseURI;
    protected String refresh_token;
    public String env;
    protected String databaseFitchEnty;
    protected String dataBaseServer1;
    protected String dataBaseServerNewESP9;
    protected String dataBaseServer2;
    protected String id;
    protected String id1;
    public static Logger logger;
    public String metaEndPoint = "/v1/metadata/fields"; // Metadata-EndPoint
    public String metaUrl = baseURI + metaEndPoint;
    public String dataEndPoint = "/v1/data/valueRequest";
    protected String dataPostUrl = baseURI + dataEndPoint; // Data Aggregator
    protected String BMIbaseURL;                                                        // -EndPoint
    protected String XappClintIDvalue = "3dab0f06-eb00-4bee-8966-268a0ee27ba0";
    protected String acceptValue = "application/vnd.api+json";
    protected String contentValue = "application/vnd.api+json";
    public static boolean failure = false;
    public static boolean publishFlag = true;
    public static ArrayList<String> DBRes = new ArrayList<String>();
    public static ArrayList<String> APIRes = new ArrayList<String>();

    enum Environment {
        DEV, INT, QA, STAGE, PROD, NOT_DEFINED
    }

    @BeforeClass
    public void executionSetup() throws IOException {
        env = System.getProperty("env");
        System.setProperty("log4j.configurationFile", "src/log4j2.xml");
        logger = LogManager.getLogger(Configuration.class);
        System.out.println("Test Execution Environment: " + env);
        Environment environment;
        try {
            environment = Environment.valueOf(env.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            environment = Environment.NOT_DEFINED;
        }
        switch (environment) {
            case DEV:
                baseURI = "https://api.fitchconnect-dev.com";
                dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
                dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
                databaseFitchEnty = "esp-dev-9";
                BMIbaseURL = "https://api-stg1.bmiresearch.com";
                break;
            case INT:
                baseURI = "https://api.fitchconnect-int.com";
                dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
                dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
                databaseFitchEnty = "esp-dev-9";
                BMIbaseURL = "https://api-stg1.bmiresearch.com";
                break;
            case QA:
                baseURI = "https://api.fitchconnect-qa.com";
                dataBaseServer1 = "mgo-que1a-cr001.fitchratings.com";
                dataBaseServer2 = "mgo-que1a-ur001.fitchratings.com";
                dataBaseServerNewESP9 = "mgo-que1a-sn001";
                databaseFitchEnty = "esp-9";
                BMIbaseURL = "https://api-stg1.bmiresearch.com";
                break;
            case STAGE:
                baseURI = "https://api.fitchconnect-stg.com";
                dataBaseServer1 = "mgo-uue1a-cr001.fitchratings.com";
                dataBaseServer2 = "mgo-uue1a-ur001.fitchratings.com";
                dataBaseServerNewESP9 = "mgo-uue1a-sn001";
                databaseFitchEnty = "esp-9";
                BMIbaseURL = "https://api-stg1.bmiresearch.com";
                break;
            case PROD:
                baseURI = "https://api.fitchconnect.com";
                dataBaseServerNewESP9 = "mgo-uue1a-sn001";
                dataBaseServer1 = "mgo-pue1c-cr001.fitchratings.com";
                dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
                databaseFitchEnty = "esp-9";
                BMIbaseURL = "https://api.bmiresearch.com";
                break;
            default:
                baseURI = "https://api.fitchconnect-qa.com";
                dataBaseServer1 = "mgo-que1a-cr001.fitchratings.com";
                dataBaseServer2 = "mgo-que1a-ur001.fitchratings.com";
                dataBaseServerNewESP9 = "mgo-que1a-sn001";
                databaseFitchEnty = "esp-9";
                BMIbaseURL = "https://api-stg1.bmiresearch.com";
                break;
        }
        bearerToken(environment);
        AuthrztionValue = "Bearer " + refresh_token;
        System.out.println(environment.toString() + " Bearer Token :" + AuthrztionValue);
        System.out.println(baseURI);
        metaUrl = baseURI + metaEndPoint;
        dataPostUrl = baseURI + dataEndPoint;

    }

    private void bearerToken(Environment environment) throws IOException {
        String url = baseURI + "/v1/oauth/token";
        System.out.println(url);
        String filename = null;
        switch (environment) {
        case NOT_DEFINED:
        	filename = "QA_granType.json";
            break;
            case DEV:
                filename = "Dev_granType.json";
                break;
            case INT:
                filename = "Int_granType.json";
                break;
            case QA:
                filename = "QA_granType.json";
                break;
            case STAGE:
                filename = "STG_granType.json";
                break;
           
            case PROD:
                filename = "PROD_granType.json";
                break;
        }
        URL file = Resources.getResource(filename);
        String myjson = Resources.toString(file, Charsets.UTF_8);

        Response response = given().contentType("application/x-www-form-urlencoded").body(myjson).with().when()
                .post(url).then().extract().response();

        Matcher matcher = Pattern.compile("\"refresh_token\":\"(.*?)\"").matcher(response.asString());
        if (matcher.find()) refresh_token = matcher.group(1);
    }

}
