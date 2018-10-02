package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint34 extends Configuration {
	
	@Test

	public void FISC_3340_findINcluded_SndP() {

		String sandPURI = baseURI + "/v1/standardAndPoorIssuerRatings?include[standardAndPoorIssuerRatings]=entity";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(sandPURI).then().statusCode(200)
				.body(containsString("included"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	@Test

	public void FISC_3339_findINcluded_Moodys() {

		String moodysURI = baseURI + "/v1/moodyIssuerRatings?include[moodyIssuerRatings]=entity";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(moodysURI).then().statusCode(200).body(containsString("included")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	
	@Test

	public void FISC_3343_findINcluded_statements() {

		String statementURI = baseURI + "/v1/statements?include[statements]=entities";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(statementURI).then().statusCode(200).body(containsString("included")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	@Test

	public void FISC_3420() {

		String IssuerResourceURi=baseURI+"/v1/issuerRatingsTransitions/5b84566d929a9d0001a17358-114/issuerRatingsTransitionHistory";
		
		System.out.println(IssuerResourceURi);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(IssuerResourceURi).then().statusCode(200).body(containsString("date")).body(containsString("2017-12-31")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	
	
	@Test

	public void FISC_metacountforStatement() {

		String statementURI=baseURI+"/v1/statements?currency=USD&filter[startChangeDate]=2018-08-07T17:19:42Z&filter[endChangeDate]=2018-08-14T17:19:42Z&fields[statements]=header,detail&page[offset]=0&page[limit]=100";
		
		//System.out.println(statementURI);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(statementURI).then().statusCode(200).extract().response();
		
		List <String> statementID = res.path("data.id");
		
		String  nextLink = res.path("links.next");
		
		

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(nextLink).then().statusCode(200).extract().response();
		
		
		List <String> statementIDx = res1.path("data.id");
		String  nextLinkx = res.path("links.next");
		
		//
		
		System.out.println(statementID.size());
		



	}


}
