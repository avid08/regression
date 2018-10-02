package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint35 extends Configuration {
  
	
	@Test

	public void FISC_3300_newattribute() {

		String entitiesURI = baseURI + "/v1/entities/1283";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(entitiesURI).then().statusCode(200)
				.body(containsString("countryName"))
				.body(containsString("United States"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}
	
	
	@Test

	public void FISC_3290_newattribute() {

		String IssueURI = baseURI + "/v1/issues/90557481";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(IssueURI).then().statusCode(200)
				.body(containsString("solicitationStatus"))
				.body(containsString("paperSize"))
				.body(containsString("ratableClass"))
				.body(containsString("originalAmount"))
				.body(containsString("commercialPaperType"))
				.body(containsString("commercialPaperProgramCode"))
				.body(containsString("commercialPaperMarketCode"))
				.body(containsString("issueDescription"))
				.body(containsString("privatePlacement"))
				
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}
	
	
	
	@Test

	public void FISC_3290_newattribute_withSubGroupFilter() {

		String IssueURI = baseURI + "/v1/issues?filter[subgroupId]=25840,1012";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(IssueURI).then().statusCode(200)
				.body(containsString("solicitationStatus"))
				.body(containsString("paperSize"))
				.body(containsString("ratableClass"))
				.body(containsString("originalAmount"))
				.body(containsString("commercialPaperType"))
				.body(containsString("commercialPaperProgramCode"))
				.body(containsString("commercialPaperMarketCode"))
				.body(containsString("issueDescription"))
				.body(containsString("privatePlacement"))
				
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}
	
	
	
	
}
