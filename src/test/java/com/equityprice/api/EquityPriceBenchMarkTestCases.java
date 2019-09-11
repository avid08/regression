package com.equityprice.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class EquityPriceBenchMarkTestCases extends Configuration {
	
	
	
	@Test()
	public void equitypriceDataAggregator() throws IOException {

		URL file = Resources.getResource("equitypriceData.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()
				.when().post(dataPostUrl)
				.then().assertThat().log().ifError().statusCode(200)	
				.body(containsString("value"))
				.body(containsString("benchmarkId"))
				.body(containsString("4"))
				.extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

	

	}
	
	

}
