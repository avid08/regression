package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint28 extends Configuration {	
	
	@Test
	public void fisc_2373_RatingstransitionResource_supportmodifiers() {
	String ratingTansition=baseURI
			+ "/v1/issueRatingsTransitions?filter[ratingType]=FC_LT_IR&filter[startDate]=2000-01-01&filter[endDate]=2010-12-31&filter[marketSectorId]=03070201"; // Desc
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
			.body(containsString("percentage"))	
			.body(containsString("BBB+"))	
			.body(containsString("ratingCodesTotalCount"))	
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
	public void fisc_2373_modifierOff() {
	String ratingTansition=baseURI
			+ "/v1/issueRatingsTransitions?filter[ratingType]=FC_LT_IR&filter[startDate]=2000-01-01&filter[endDate]=2010-12-31&filter[marketSectorId]=03070201&filter[modifiers]=off"; // Desc
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
			.body(containsString("percentage"))	
			.body(containsString("BBB"))	
			.body(containsString("ratingCodesTotalCount"))	
			.extract().response();
	 
	 Assert.assertFalse(res.asString().contains("BBB+")); 
	 
	 Assert.assertFalse(res.asString().contains("isError"));
	 Assert.assertFalse(res.asString().contains("isMissing"));
	 Assert.assertFalse(res.asString().contains("isRestricted"));

   }
	
	
	@Test
	public void fisc_2371_RatingCodeCount_Percentage() {
	String ratingTansition=baseURI
			+ "/v1/issueRatingsTransitions?filter[ratingType]=FC_LT_IR&filter[startDate]=2000-01-01&filter[endDate]=2010-12-31&filter[marketSectorId]=03070201&filter[modifiers]=off"; // Desc
																						// order
	 Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansition).then()
			.statusCode(200)			
			.body(containsString("count"))
			.body(containsString("ratingCodesTotalCount"))	
			.extract().response();
	 
	    float percentagefromResponse = res.path("data[0].attributes.percentage");
	    
	    System.out.println(percentagefromResponse);
	 
	   int singleRatingCount_B = res.path("data[0].attributes.count");
	   
	   
	   float B_count = (float) singleRatingCount_B;   

	   
	   int TotalRatingCount_B = res.path("meta.ratingCodesTotalCount.B"); 

	 
	   float Calcultedpercentage =B_count/TotalRatingCount_B*100;
	   
	   boolean failure = false;
	   
	   if(percentagefromResponse==Calcultedpercentage){
		   
		   System.out.println("percentage Calculation is working correctly");
		   
	   }else {
		   failure = true;
		   System.err.println("percentage Calculation is NOT working correctly");
		   
	   }
	   
	   Assert.assertFalse(failure);
			
	}
	
	
	@Test
	public void fisc_2506_newattributes() {
	String ratingTansition=baseURI+ "/v1/entities/1316492/fitchIssuerRatings"; 
																						
	 Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansition).then()
			.statusCode(200)				
			.body(containsString("effectiveDateTime"))	
			.body(containsString("effectiveDate"))	
			.extract().response();
	 Assert.assertFalse(res.asString().contains("isError"));
	 Assert.assertFalse(res.asString().contains("isMissing"));
	 Assert.assertFalse(res.asString().contains("isRestricted"));
	 
	 
  }
	
}
