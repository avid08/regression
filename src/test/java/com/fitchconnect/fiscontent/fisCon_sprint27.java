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
	
@Test()

public void fisc_2314() {
	String ratingTansitionHistoryURI = baseURI
			+ "/v1/issueRatingsTransitionHistory?filter[marketSectorId]=03070401&filter[countryISOCode2]=US&filter[startDate]=2015-10-01&filter[endDate]=2016-12-31&filter[ratingType]=FC_LT_IR "; // Desc
					
	String jsonAsString;	
	System.out.println(ratingTansitionHistoryURI);	
	// order
	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansitionHistoryURI).then()
			.statusCode(200)			
			/*.body(containsString("year"))
			.body(containsString("period"))
			.body(containsString("Annual"))		
			.body(containsString("C"))
			.body(containsString("relationships"))	*/		
			.extract().response();
	


	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
	
	
  }

@Test(enabled=false)
public void fisc_2602_Ratingstransitionhistory() {
String ratingTansitionHistoryURI = baseURI
		+ "/v1/issueRatingsTransitionHistory?filter[marketSectorId]=03070401&filter[countryISOCode2]=US&filter[startDate]=2015-10-01&filter[endDate]=2016-12-31&filter[ratingType]=FC_LT_IR "; // Desc
																									

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
			+ "/v1/issueRatingsTransitionHistory?include[issueRatingsTransitionHistory]=issue&filter[marketSectorId]=03070401&filter[countryISOCode2]=US&filter[startDate]=2015-10-01&filter[endDate]=2016-12-31&filter[ratingType]=FC_LT_IR "; // Desc
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


@Test()
public void fisc_2312_issuer_RatingTransition() {
	String ratingTansition=baseURI
			+ "/v1/issuerRatingsTransitions?filter[marketSectorId]=01010112,01010109,01010219&filter[startDate]=2000-01-01&filter[endDate]"
			+ "=2001-01-01&filter[ratingType]=FC_LT_IDR&filter[issuerId]=80360063&filter[countryISOCode2]=TR,NL"; // Desc
																						// order
	 Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansition).then()
			.statusCode(200)
			.body(containsString("issuerRatingsTransitions"))
			.body(containsString("percentage"))
			.body(containsString("count"))
			.body(containsString("from"))
			.body(containsString("to"))
			.body(containsString("issuerRatingsAnnualTransitionHistory"))			
			.body(containsString("relationships"))	
			.extract().response();
			
	 Assert.assertFalse(res.asString().contains("isError"));
	 Assert.assertFalse(res.asString().contains("isMissing"));
	 Assert.assertFalse(res.asString().contains("isRestricted"));
    }


@Test()
public void fisc_2313_issuer_RatingTransition() {
	String ratingTansition=baseURI
			+ "/v1/issuerRatingsTransitionHistory?filter[issuerId]=90485080&filter[countryISOCode2]=NL&filter[marketSectorId]=01010111&filter[startDate]=2012-01-01&filter[endDate]=2016-12-31&filter[ratingType]=FC_LT_IDR";
																						// order
	 Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansition).then()
			.statusCode(200)
			.body(containsString("issuerRatingsTransitionHistory"))
			.body(containsString("issuerId"))
			.body(containsString("period"))
			.body(containsString("Annual"))
			.body(containsString("year"))
			.body(containsString("rating"))
			.body(containsString("AA-"))
			.body(containsString("totalResourceCount"))			
			.extract().response();

	 Assert.assertFalse(res.asString().contains("isError"));
	 Assert.assertFalse(res.asString().contains("isMissing"));
	 Assert.assertFalse(res.asString().contains("isRestricted"));
    }



   

}

