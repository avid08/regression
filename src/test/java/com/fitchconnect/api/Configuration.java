package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.testng.annotations.BeforeClass;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Configuration {

	public Response response;
	protected String myjson;
	protected String AuthrztionValue;
	protected String baseURI;
	protected String accessToken;
	String env;
	String databaseFitchEnty;
	protected String dataBaseServer1;
	protected String dataBaseServer2;
	protected String id;
	protected String id1;
	String jsonresponse;
	String metaEndPoint = "/v1/metadata/fields"; // Metadata-EndPoint
	String metaUrl = baseURI + metaEndPoint;
	String dataEndPoint = "/v1/data/valueRequest";
	protected String dataPostUrl = baseURI + dataEndPoint; // Data Aggregator
															// -EndPoint
	protected String XappClintIDvalue = "3dab0f06-eb00-4bee-8966-268a0ee27ba0";
	protected String acceptValue = "application/vnd.api+json";
	protected String contentValue = "application/vnd.api+json";
	public static boolean failure = false;
	public static boolean publishFlag = true;
	public static ArrayList<String> DBRes = new ArrayList<String>();
	public static ArrayList<String> APIRes = new ArrayList<String>();

	@BeforeClass
	public void executionSetup() throws IOException {
		env = System.getProperty("env");
		System.out.println("Test Execution Environment: " + env);
		if (env == null) {
			baseURI = "https://api.fitchconnect-qa.com";
			bearerToken_QA();
			this.AuthrztionValue = "Bearer " + accessToken;
			System.out.println("QA Bearer Token " + AuthrztionValue);
			dataBaseServer1 = "mgo-que1a-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-que1a-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("dev")) {
			baseURI = "https://api.fitchconnect-dev.com";
			this.AuthrztionValue = ("Basic MUc4TTJCUzVIUTdGTVE5RVlNWTdWWVlUWTpoeU51d2lIYUVtOEpaSnF1RzVsRmM0TnRrTXpMMjdqcVFFczVwSDlUdEZJ");
			// dataBaseServer = "mongoweb-x01";
			dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
		} else if (env.equals("int")) {
			baseURI = "https://api.fitchconnect-int.com";
			bearerToken_INT();
			this.AuthrztionValue = "Bearer " + accessToken;
			System.out.println("INT Bearer Token " + AuthrztionValue);
			dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
		} else if (env.equals("qa")) {
			baseURI = "https://api.fitchconnect-qa.com";
			bearerToken_QA();
			this.AuthrztionValue = "Bearer " + accessToken;
			System.out.println("QA Bearer Token " + AuthrztionValue);
			dataBaseServer1 = "mgo-que1a-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-que1a-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("stage")) {
			baseURI = "https://api.fitchconnect-stg.com";
			stage_bearerToken();
			this.AuthrztionValue = "Bearer " + accessToken;
			dataBaseServer1 = "mgo-uue1a-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-uue1a-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("prod")) {
			baseURI = "https://api.fitchconnect.com";
			PROD_bearerToken();
			this.AuthrztionValue = "Bearer " + accessToken;
			dataBaseServer1 = "mgo-pue1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		}

		System.out.println(baseURI);
		metaUrl = baseURI + metaEndPoint;
		dataPostUrl = baseURI + dataEndPoint;

	}

	public void bearerToken_INT() throws IOException {

		// given().config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/zip",
		// ContentType.TEXT))

		String url = baseURI + "/v1/oauth/token";
		System.out.println(url);
		URL file = Resources.getResource("Int_granType.json");
		String myjson = Resources.toString(file, Charsets.UTF_8);

		Response response = given().contentType("application/x-www-form-urlencoded").body(myjson).with().when()
				.post(url).then().statusCode(200).extract().response();

		String responseString = response.asString();

		accessToken = "";

		StringTokenizer tokenizer = new StringTokenizer(responseString, "{}\"");
		if (tokenizer.hasMoreTokens()) {
			tokenizer.nextToken();
			tokenizer.nextToken();
			accessToken = tokenizer.nextToken();
		}
		System.out.println("access token " + accessToken);

	}

	public void bearerToken_QA() throws IOException {

		String url = baseURI + "/v1/oauth/token";
		System.out.println(url);
		URL file = Resources.getResource("QA_granType.json");
		String myjson = Resources.toString(file, Charsets.UTF_8);

		Response response = given().contentType("application/x-www-form-urlencoded").body(myjson).with().when()
				.post(url).then().statusCode(200).extract().response();

		String responseString = response.asString();

		accessToken = "";

		StringTokenizer tokenizer = new StringTokenizer(responseString, "{}\"");
		if (tokenizer.hasMoreTokens()) {
			tokenizer.nextToken();
			tokenizer.nextToken();
			accessToken = tokenizer.nextToken();
		}

		System.out.println("access token " + accessToken);

	}

	public void stage_bearerToken() throws IOException {

		String url = baseURI + "/v1/oauth/token";
		System.out.println(url);
		URL file = Resources.getResource("STG_granType.json");
		String myjson = Resources.toString(file, Charsets.UTF_8);

		Response response = given().contentType("application/x-www-form-urlencoded").body(myjson).with().when()
				.post(url).then().statusCode(200).extract().response();

		String responseString = response.asString();

		accessToken = "";

		StringTokenizer tokenizer = new StringTokenizer(responseString, "{}\"");
		if (tokenizer.hasMoreTokens()) {
			tokenizer.nextToken();
			tokenizer.nextToken();
			accessToken = tokenizer.nextToken();
		}

		System.out.println("access token " + accessToken);

	}

	public void PROD_bearerToken() throws IOException {

		String url = baseURI + "/v1/oauth/token";
		System.out.println(url);
		URL file = Resources.getResource("PROD_granType.json");
		String myjson = Resources.toString(file, Charsets.UTF_8);

		Response response = given().contentType("application/x-www-form-urlencoded").body(myjson).with().when()
				.post(url).then().statusCode(200).extract().response();

		String responseString = response.asString();

		accessToken = "";

		StringTokenizer tokenizer = new StringTokenizer(responseString, "{}\"");
		if (tokenizer.hasMoreTokens()) {
			tokenizer.nextToken();
			tokenizer.nextToken();
			accessToken = tokenizer.nextToken();
		}

		System.out.println("access token " + accessToken);

	}

}
