package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;


public class fisCon_sprint27 extends Configuration {

	
	@Test
	public void fisc_2367_RatingstransitionResource() {
	String ratingTansition=baseURI
			+ "/v1/issueRatingsTransitions?filter[ratingType]=FC_LT_LC_IR&filter[startDate]=2009-01-01&filter[endDate]=2018-01-01&filter[marketSectorId]=03071100"; // Desc
																						// order
	 Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansition).then()
			.statusCode(200)
			
			.body(containsString("count"))
			.body(containsString("to"))
			.body(containsString("from"))
			.body(containsString("relationships"))
			.body(containsString("issueRatingsAnnualTransitionHistory"))
			.body(containsString("links"))		
			.extract().response();
	 Assert.assertFalse(res.asString().contains("isError"));
	 Assert.assertFalse(res.asString().contains("isMissing"));
	 Assert.assertFalse(res.asString().contains("isRestricted"));
	 
	 String historylink = res.path("data[0].relationships.issueRatingsAnnualTransitionHistory.links.related");
	 
	 Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(historylink).then()
				.statusCode(200)
				.body(containsString("Annual"))
				.body(containsString("issueId"))
				.extract().response();	 
	 
	 Assert.assertFalse(res1.asString().contains("isError"));
	 Assert.assertFalse(res1.asString().contains("isMissing"));
	 Assert.assertFalse(res1.asString().contains("isRestricted"));

	    }
	
@Test

public void fisc_2314() {
	String ratingTansitionHistoryURI = baseURI
			+ "/v1/issueRatingsTransitionHistory?filter[issueId]=90359864&filter[startDate]=2011-01-03&filter[endDate]=2017-12-30"; // Desc
																										// order

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansitionHistoryURI).then()
			.statusCode(200)
			.body(containsString("rating"))
			.body(containsString("issueId"))
			.body(containsString("period"))
			.body(containsString("Annual"))
			.body(containsString("2015"))
			.body(containsString("BBB+"))
			.body(containsString("relationships"))
			.body(containsString("7"))
			.extract().response();

	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
	
	
  }

@Test
public void fisc_2602_Ratingstransitionhistory() {
String ratingTansitionHistoryURI = baseURI
		+ "/v1/issueRatingsTransitionHistory?filter[issueId]=83022727&filter[startDate]=2009-01-03&filter[endDate]=2010-01-01"; // Desc
																									

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
		.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansitionHistoryURI).then()
		.statusCode(200)
		.body(containsString("issueRatingsTransitionHistory"))
		.body(containsString("issueId"))
		.body(containsString("period"))
		.body(containsString("Annual"))
		.body(containsString("2009"))
		.body(containsString("relationships"))
		.body(containsString("[CCC]"))
		.body(containsString("CCC"))
		.extract().response();

Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));


}


@Test(enabled=false)
public void fisc_2369_RatingstransitionHistory() {
	String ratingTansition=baseURI
			+ "v1/issueRatingsTransitionHistory?include[issueRatingsTransitionHistory]=issues"; // Desc
																						// order
	 Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansition).then()
			.statusCode(200)
			.body(containsString("issueRatingsTransitionHistory"))
			.body(containsString("AA"))
			.body(containsString("Annual"))
			.body(containsString("relationships"))
			.body(containsString("issueId"))
			.body(containsString("period"))
			// inclued issue Section
			.body(containsString("included"))
			.body(containsString("country"))
			.body(containsString("marketSectors"))
			.extract().response();
	 Assert.assertFalse(res.asString().contains("isError"));
	 Assert.assertFalse(res.asString().contains("isMissing"));
	 Assert.assertFalse(res.asString().contains("isRestricted"));
	
    }


}

