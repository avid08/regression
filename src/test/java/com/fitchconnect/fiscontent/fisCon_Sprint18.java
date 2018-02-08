package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint18 extends Configuration {

@Test
 
 public void issueREsource_incoludeIssuer_Fisc_1559(){
	 
	 String issueWithIssuerIncludeURI = baseURI+"/v1/issues/93568897?include[issues]=issuer";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(issueWithIssuerIncludeURI)
				.then().statusCode(200)
				.body(containsString("included"))
				.body(containsString("issues"))	
				.body(containsString("classTypeDescription"))
				.body(containsString("relationships"))
				.body(containsString("hasTransactions"))
				.body(containsString("role"))
				.body(containsString("name"))
				.body(containsString("email"))
				.body(containsString("groupCode"))				
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));		
	 
   }

@Test
 
  public void nickNameNewAttributes_Fisc_1653(){
	 
		String nickNameURI = baseURI+"/v1/nicknames/79454 ";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(nickNameURI)
				.then().statusCode(200)
				.body(containsString("Commercial Bank"))
				.body(containsString("primary"))	
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));		
		
		String nickNameWithFilterURI = baseURI+"/v1/nicknames?filter[businessTemplate]=sovereigns,insurance";

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(nickNameWithFilterURI)
				.then().statusCode(200)
				.body(containsString("sovereigns"))
				.body(containsString("insurance"))	
				.extract().response();
		
		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
     	 
    }

 @Test

public void RegionAncestorinArray_FISC_1715(){
	
	String regionURI = baseURI+"/v1/regions/BW";

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(regionURI)
			.then().statusCode(200)
			.body(containsString("Country"))
			.body(containsString("regionType"))		
			.extract().response();
	
	List <String> anCestorID = res.path("data.relationships.ancestors.data");
	Assert.assertNotNull(anCestorID);
	
    System.out.println("list of ancestor data array  is  "+anCestorID.size());
    
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));

	
 }
 
 @Test

  public void RegionNewAttribute_FISC_1709_1710(){
	
	String regionURI = baseURI+"/v1/regions";

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(regionURI)
			.then().statusCode(200)
			.body(containsString("World"))
			.body(containsString("regionType"))
			.body(containsString("fitchRatingsRegionId"))
			.body(containsString("legacyRegionId2"))			
			.extract().response();

	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));

	
   }
}
