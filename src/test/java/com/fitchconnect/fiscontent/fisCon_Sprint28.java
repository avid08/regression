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
	
	
	@Test(enabled=false)
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
	   
	    System.out.println(Calcultedpercentage);
	   
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
	
	@Test
	public void fisc_2372_issuer_modifierOff() {
	String ratingTansition=baseURI
			+ "/v1/issuerRatingsTransitions?filter[marketSectorId]=01010111&filter[startDate]=2000-01-01&filter[endDate]=2001-01-01&filter[ratingType]=FC_LT_IDR&filter[modifiers]=off"; // Desc
																						// order
	 Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansition).then()
			.statusCode(200)			
			.body(containsString("count"))
			.body(containsString("to"))
			.body(containsString("from"))
			.body(containsString("relationships"))
			.body(containsString("issuerRatingsTransitions"))
			.body(containsString("links"))
			.body(containsString("percentage"))	
			.body(containsString("BBB"))	
			.body(containsString("ratingCodesTotalCount"))	
			.extract().response();
	 
	 Assert.assertFalse(res.asString().contains("AA+")); 
	 
	 Assert.assertFalse(res.asString().contains("isError"));
	 Assert.assertFalse(res.asString().contains("isMissing"));
	 Assert.assertFalse(res.asString().contains("isRestricted"));
	 
	/* 
	  float percentagefromResponse = res.path("data[0].attributes.percentage");
	    
	    System.out.println(percentagefromResponse);
	    
	    int singleRatingCount_BB = res.path("data[0].attributes.count");
	    
	    float BB_count = (float) singleRatingCount_BB; 
	    
	    int TotalRatingCount_BB = res.path("meta.ratingCodesTotalCount.BB"); 
	    
	    
	    System.out.println(TotalRatingCount_BB);
	    
	    
	    float Calcultedpercentage =BB_count/TotalRatingCount_BB*100;
	    
	    System.out.println(Calcultedpercentage);
		   
		   boolean failure = false;
		   
		   if(percentagefromResponse==Calcultedpercentage){
			   
			   System.out.println("percentage Calculation is working correctly");
			   
		   }else {
			   failure = true;
			   System.err.println("percentage Calculation is NOT working correctly");
			   
		   }
		   
		   Assert.assertFalse(failure);
				*/
		}
	
	
	@Test
	public void fisc_2368_relationship() {
	String ratingTansition=baseURI+ "/v1/issuerRatingsTransitions?filter[marketSectorId]=01020100&filter[startDate]=2000-01-01&filter[endDate]=2010-01-01&filter[ratingType]=FC_LT_IDR&filter[issuerId]=80464303"; 
																						
	 Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansition).then()
			.statusCode(200)				
			.body(containsString("issuerRatingsAnnualTransitionHistory"))	
			.body(containsString("ratingCodesTotalCount"))	
			.extract().response();
	 
	 String anualTransitionHistory = res.path("data[0].relationships.issuerRatingsAnnualTransitionHistory.links.related");
	
	 Assert.assertFalse(res.asString().contains("isRestricted"));
	 
	 // Relationship link working
	 Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(anualTransitionHistory).then()
				.statusCode(200)				
				.body(containsString("issuerId"))	
				.body(containsString("period"))	
				.body(containsString("ratingType"))	
				.extract().response(); 
	 
	 Assert.assertFalse(res1.asString().contains("isError"));
	 Assert.assertFalse(res1.asString().contains("isMissing"));
	 Assert.assertFalse(res1.asString().contains("isRestricted"));	 
	 
  }
	
	@Test(enabled=false)
	public void fisc_2368_includedIssuer() {
	String ratingTansition=baseURI+ "/v1/issuerRatingsTransitionHistory?include[issuerRatingsTransitionHistory]=issuer&filter[marketSectorId]=01020100&filter[startDate]=2000-01-01&filter[endDate]=2010-01-01&filter[ratingType]=FC_LT_IDR&filter[issuerId]=80088915"; 
																						
	 Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(ratingTansition).then()
			.statusCode(200)				
			.body(containsString("issuerId"))	
			.body(containsString("period"))	
			.body(containsString("ratingType"))	
			.body(containsString("included"))	
			.body(containsString("regions"))	
			.body(containsString("cusip"))	
			.body(containsString("hasTransactions"))				
			.extract().response();
	 
	 Assert.assertFalse(res.asString().contains("isRestricted"));
	 	
   }
	
}
