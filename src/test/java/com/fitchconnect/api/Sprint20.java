package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint20 {
	
	public Response response;
	String myjson;
	String AuthrztionValue;
	String baseURI;
	String env;
	String metaEndPoint = "/v1/metadata/fields"; // Metadata-EndPoint
	String metaUrl = baseURI + metaEndPoint;
	String dataEndPoint = "/v1/data/valueRequest";
	String dataPostUrl = baseURI + dataEndPoint; // Data Aggregator -EndPoint
	String XappClintIDvalue = "3dab0f06-eb00-4bee-8966-268a0ee27ba0";
	String acceptValue = "application/vnd.api+json";
	String contentValue = "application/vnd.api+json";


	@BeforeClass
	public void executionSetup() {
		env = System.getProperty("env");
		System.out.println("Test Execution Environment: " + env);
		if (env == null) {
			baseURI = "https://api-int.fitchconnect.com";
			this.AuthrztionValue = ("Basic MUtQNk1DVVk0WkU1SDFXVlVBWlJUVjNUSjpPM0owV0orUGVhZ3JqMis1bTBTMkdvdnZKRDBrQUd1R3F6Q0M5REIydjRv");
		} else if (env.equals("dev")) {
			baseURI = "https://api-dev.fitchconnect.com";
			this.AuthrztionValue = ("Basic NTA4Rk44V1BKTUdGVVI5VFpOREFEV0NCSzpvMVY5bkRCMG8yM3djSHp2eVlHNnZZb01GSkJWdG1KZmEwS20vbUczVWVV");

		} else if (env.equals("int")) {
			baseURI = "https://api-int.fitchconnect.com";
			this.AuthrztionValue = ("Basic MUtQNk1DVVk0WkU1SDFXVlVBWlJUVjNUSjpPM0owV0orUGVhZ3JqMis1bTBTMkdvdnZKRDBrQUd1R3F6Q0M5REIydjRv");

		} else if (env.equals("qa")) {
			baseURI = "https://api-qa.fitchconnect.com";
			this.AuthrztionValue = ("Basic MUlLVk1SMjlJS1lIMllPSjFUQkdGQ0tKSDpFN1Y2Z1FJY3RPeG5KbG8rSVBHaGY0K0tTSGc3LzFpOFJsbVo1Tmd6NUpB");
		} else if (env.equals("stage")) {
			baseURI = "https://api-stage.fitchconnect.com";
			this.AuthrztionValue = ("Basic NU5COUFRSDVCSTRDUFZTUktJRUpESjQyNTpDYjFxUXQycHd4VGNKZTg1SjkyRVJmL1JMU1haRUlZSjU3NWR5R3RacDVV");

		} else if (env.equals("prod")) {
			baseURI = "https://api.fitchconnect.com";
			this.AuthrztionValue = ("Basic M1FEREJQODMyQ1NKTlMwM1ZQT0NSQ0VFQjpENk9PUWtJVW5uaXhVZlZmL3loVnJhbHNDU1dzaGd0L1NJOGFTSFZEVTJR");

		}

		System.out.println(baseURI);
		metaUrl = baseURI + metaEndPoint;
		dataPostUrl = baseURI + dataEndPoint;
	}
	
	@Test(enabled = true)
	public void ModyNewFields_775() throws IOException {

		URL file = Resources.getResource("ModyNewFieldsReq.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("FC_LT_FC_ISSR_DIR_MDY"))
				.body(containsString("FC_LT_FC_ISSR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_MQ_AMR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_NIFSR_FC_WATCHLIST_DT_MDY"))
				.body(containsString("FC_LT_FC_BANK_DEPOSITS_WATCHLIST_DT_MDY"))

				.extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertTrue(fieldResponse.asString().contains("FC_ST_DERIVED_ISSR_MDY"));

		List<String> values = fieldResponse.path("data.attributes.entities.values.fitchFieldId");
		assertNotNull(values);

	}

	@Test(enabled = true)
	public void ModyNewFields_776_withPermission() throws IOException {

		URL file = Resources.getResource("ModyNewFieldsReq.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("FC_LT_FC_ISSR_DIR_MDY"))
				.body(containsString("FC_LT_FC_ISSR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_MQ_AMR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_NIFSR_FC_WATCHLIST_DT_MDY"))
				.body(containsString("FC_LT_FC_BANK_DEPOSITS_WATCHLIST_DT_MDY")).extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertTrue(fieldResponse.asString().contains("FC_ST_DERIVED_ISSR_MDY"));

	}	
	
	@Test(enabled = true)
	public void Entity_Search_921() {

		String searchStringEnd = "/v1/entities?filter[name]=Brazil";
		String SerchUrl = baseURI + searchStringEnd;

		given().header("Authorization", AuthrztionValue)

				.header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(SerchUrl).then().assertThat().log().ifError()
				.statusCode(200).body(containsString("Brazil")).body(containsString("ultimateParent"))
				.body("data[0].attributes.name", equalTo("Algorithmics Brazil do Brazil Ltda"))
				.body(containsString("directors"));

	}

	// Test Description : User wild card search does not return everything if
	// it's does not satisfy the search criteria

	@Test(enabled = true)
	public void Entity_Search_921_Negative() {

		String searchStringEnd = "/v1/entities?filter[name]=aAAd";
		String SerchUrl = baseURI + searchStringEnd;

		given().header("Authorization", AuthrztionValue)

				.header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(SerchUrl).then().assertThat().log().ifError()
				.statusCode(200).body("data", Matchers.empty()).body("data.included", Matchers.hasSize(0));

	}

	// Test Description :
	@Test(enabled = true)
	public void Entity_Search_922() {

		String enTityendPoint = "/v1/entities/1047648/"; // PublishFlag = True
		String enTityUrl = baseURI + enTityendPoint;

		given().header("Authorization", AuthrztionValue).header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(enTityUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("directors"))
				.body(containsString("officers")).body(containsString("shareholders"))
				.body("data.attributes.name", equalTo("Banco Santander Totta SA"));

	}

	
}
