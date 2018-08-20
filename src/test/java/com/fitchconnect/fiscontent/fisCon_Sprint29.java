package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint29 extends Configuration {
	
	@Test

	public void fisc_2779_IssueName() throws IOException {

		String issueRatingTrainsition = baseURI + "/v1/issueRatingsTransitionHistory?filter[marketSectorId]=03070401,03031500&filter[countryISOCode2]=US&filter[startDate]=2012-12-01&filter[endDate]=2013-12-31&filter[ratingType]=FC_LT_IR&filter[issueId]=96606484,20303865&filter[periodType]=asOf,semiannual";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(issueRatingTrainsition).then()
				.body(containsString("bondName"))
				.body(containsString("FNT Mortgage ser 2000-1"))
				.body(containsString("asOf"))
				
				
				.extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}
	
	@Test

	public void fisc_2778_IssueRName() throws IOException {

		String issueRatingTrainsition = baseURI + "/v1/issuerRatingsTransitionHistory?filter[marketSectorId]=01030500&filter[countryISOCode2]=US&filter[startDate]=2012-12-01&filter[endDate]=2013-12-31&filter[ratingType]=FC_LT_IDR&filter[periodType]=asOf,semiannual&filter[issuerId]=80091181";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(issueRatingTrainsition).then()
				.body(containsString("issuerName"))
				.body(containsString("Texas Instruments Incorporated"))
				.body(containsString("asOf"))
				.body(containsString("H2"))
				
				.extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}
	
	@Test

	public void fisc_2715_IssueRatingTransition_history_modifierOff() throws IOException {

		String issueRatingTrainsition = baseURI + "/v1/issueRatingsTransitionHistory?filter[marketSectorId]=03070401&filter[countryISOCode2]=US&filter[issueId]=80002686&filter[startDate]=2000-01-01&filter[endDate]=2012-06-30&filter[ratingType]=FC_LT_IR&include[issueRatingsTransitionHistory]=issue&filter[modifiers]=off";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(issueRatingTrainsition).then()
				.body(containsString("B"))
				.body(containsString("Annual"))					
				.extract().response();
		Assert.assertFalse(res.asString().contains("B-"));
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test

	public void fisc_2714_issuer_RatingTransition_modifiers() throws IOException {

		String issueRatingTrainsition = baseURI + "/v1/issuerRatingsTransitionHistory?filter[marketSectorId]=01030500&filter[countryISOCode2]=US&filter[startDate]=2012-12-01&filter[endDate]=2013-12-31&filter[ratingType]=FC_LT_IDR&filter[periodType]=asOf,semiannual&filter[issuerId]=80091181&filter[modifiers]=off";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(issueRatingTrainsition).then()
				.body(containsString("A"))
				.body(containsString("issuerRatingsTransitionHistory"))				
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("A+"));
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}
		
	@Test

	public void Fisc_2692_issueRatingtansition_addtionalFrequencies() throws IOException {

		String issueRatingTrainsition = baseURI + "/v1/issueRatingsTransitionHistory?filter[marketSectorId]=03070401&filter[countryISOCode2]=US&filter[issueId]=80002686&filter[periodType]"
				+ "=quarterly ,semiannual,monthly&filter[startDate]=2012-01-01&filter[endDate]=2012-06-30&filter[ratingType]=FC_LT_IR";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(issueRatingTrainsition).then()
				.body(containsString("M1"))
				.body(containsString("Q2"))
				.body(containsString("H1"))	
				.extract().response();		
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}
	
}
