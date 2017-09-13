package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint9 extends Configuration {

	@Test

	public void FISC_924() {

		String issueRatingUrl = baseURI + "/v1/issues/10008981/issueRatings";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(issueRatingUrl).then()
				.statusCode(200).body(containsString("type")).body(containsString("alert"))
				.body(containsString("solicitation")).body(containsString("rating")).body(containsString("description"))
				.body(containsString("action")).body(containsString("description"))
				.body(containsString("effectiveDate")).body(containsString("issue")).body(containsString("issuer"))
				.extract().response();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
		Assert.assertFalse(response.asString().contains("isError"));

		String Issuelink = response.path("data[0].relationships.issue.links.related");
		String IssueRlink = response.path("data[0].relationships.issuer.links.related");

		System.out.println("issueRelated link " + Issuelink);

		Response response1 = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(Issuelink).then().statusCode(200).extract().response();

		System.out.println("issuer related link " + IssueRlink);

		Response response2 = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(IssueRlink).then().extract().response();

		Assert.assertFalse(response2.asString().contains("isError"));
		Assert.assertFalse(response2.asString().contains("isRestricted"));
		Assert.assertFalse(response2.asString().contains("isError"));

	}

	@Test

	public void FISC_941() {

		String issuerUrl = baseURI + "/v1/issues";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(issuerUrl).then()
				.statusCode(200).body(containsString("country"))
				.body(containsString("cusip")).body(containsString("disclosure")).body(containsString("marketSectors"))
				.body(containsString("marketsectorId")).body(containsString("primary"))
				.body(containsString("classTypeDescription")).body(containsString("coupon"))
				.body(containsString("countryCd")).body(containsString("originalAmount"))
				.body(containsString("bondName")).body(containsString("privatePlacementDescription"))
				.body(containsString("originalAmount")).body(containsString("maturityDate"))
				.body(containsString("countryOfAnalyst")).body(containsString("currency")).body(containsString("isin"))
				.body(containsString("issueRatings")).body(containsString("issuer")).extract().response();
		
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
		Assert.assertFalse(response.asString().contains("isError"));

	}
	

	@Test
	
	public void FISC_940 () {
		
		
		String entitiesUrl = baseURI+"/v1/entities";
		
		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entitiesUrl).then()
				.statusCode(200)
				.extract().response();
		
		String issueLink = response.path("data[0].relationships.issues.links.related");
		
		System.out.println(issueLink);
		
		Response response1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(issueLink).then()
				.statusCode(200)
				.extract().response();
		
		
		Assert.assertFalse(response1.asString().contains("isError"));
		Assert.assertFalse(response1.asString().contains("isRestricted"));
		Assert.assertFalse(response1.asString().contains("isError"));
		
	}
	

}