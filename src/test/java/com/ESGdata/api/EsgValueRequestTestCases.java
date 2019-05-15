package com.ESGdata.api;

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

public class EsgValueRequestTestCases extends Configuration{
	
	
	@Test()
	public void FISC_5173_ESGData_allbank() throws IOException {

		URL file = Resources.getResource("FISC_5173_ESG_allBanks.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body(containsString("Universal Commercial"))
				.body(containsString("bb+"))
				.body(containsString("ScaleRatingInfluence"))
				.body(containsString("Higher"))
				.body(containsString("Moderate"))		
			
				.body(containsString("value")).extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

	

	}
	
	@Test()
	public void FISC_5173_ESGData_AllCorp() throws IOException {

		URL file = Resources.getResource("FISC_5173_ESG_allcorp.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body(containsString("3"))	
				.body(containsString("Boston Scientific Corporation"))	
				.body(containsString("value")).extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

	

	}

}
