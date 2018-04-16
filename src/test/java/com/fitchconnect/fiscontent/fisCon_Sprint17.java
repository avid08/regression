package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint17 extends Configuration {
	
	
	@Test
	
	public void fisc_1503_newRegionsAttributes(){
		

		String marktSectorURI = baseURI+"/v1/marketSectors?filter[bmiServiceChannelIds]=C219,C220&filter[level]=3";		
		
		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(marktSectorURI).then().statusCode(200)
				.body(containsString("bmiServiceChannelIds"))
				.body(containsString("marketSectors"))
				.body(containsString("level"))	
				.body(containsString("C219"))	
				.body(containsString("C220"))				
				.body(containsString("name"))
				.body(containsString("active"))						
				.extract().response();		
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));		 
		
	String IdfilterMSURI = baseURI+"/v1/marketSectors?filter[id]=01010300";	
	
	Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(IdfilterMSURI).then().statusCode(200)
			.body(containsString("bmiServiceChannelIds"))
			.body(containsString("marketSectors"))
			.body(containsString("level"))	
			.body(containsString("C219"))	
			.body(containsString("C220"))				
			.body(containsString("name"))
			.body(containsString("active"))	
			.extract().response();
	
	
String IdfilterMSinclusiveURI = baseURI+"/v1/marketSectors?filter[level]=3&filter[id]=01010300";	
	
	Response res2 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(IdfilterMSinclusiveURI).then().statusCode(400)
			.body(containsString("id filter cannot be used with other filters."))			
			.extract().response();
			
	}
	
	@Test
	
	public void fisc_1507_entities_regionRelationship(){
		
		String entitiesRegionURI = baseURI+"/v1/entities/102798/regions";
		
		System.out.println(entitiesRegionURI);
		
		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(entitiesRegionURI).then().statusCode(200)
				.body(containsString("DE"))
				.body(containsString("Germany"))
				.body(containsString("relationships"))
				.body(containsString("socialRegions"))
				.body(containsString("true"))
				.extract().response();
		
		String XentitiesRegionURI = baseURI+"/v1/entities/116690/regions";
		
		
		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(XentitiesRegionURI).then().statusCode(200)
				.body(containsString("US"))
				.body(containsString("United States"))
				.body(containsString("relationships"))
				.body(containsString("socialRegions"))
				.body(containsString("true"))
				.extract().response();		
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		
		
		
	}
	
	@Test
	
	public void FISC_1508_issuer_regionRelations(){
		
		
	String IssuerRegionURI = baseURI+"/v1/issuers/82/regions";
		
		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(IssuerRegionURI).then().statusCode(200)
				.body(containsString("US"))
				.body(containsString("United States"))
				.body(containsString("relationships"))
				.body(containsString("socialRegions"))
				.body(containsString("regionType"))
				.body(containsString("true"))
				.extract().response();	
				
		
		String XissuerRegionURI = baseURI+"/v1/issuers/3155/regions";
		
		
		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(XissuerRegionURI).then().statusCode(200)
				.body(containsString("US"))
				.body(containsString("United States"))
				.body(containsString("relationships"))
				.body(containsString("socialRegions"))
				.body(containsString("regionType"))
				.body(containsString("true"))
				.extract().response();		
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		
		
	}
	
	@Test
	
	public void fisc_1509_issueResource_regionRelation(){
		
  String IssueRegionURI = baseURI+"/v1/issues/93568897/regions";
		
		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(IssueRegionURI).then().statusCode(200)
				.body(containsString("AU"))
				.body(containsString("Australia"))
				.body(containsString("relationships"))
				.body(containsString("socialRegions"))
				.body(containsString("regionType"))
				.body(containsString("true"))
				.extract().response();	
				
		
		String XissueRegionURI = baseURI+"/v1/issues/96198642/regions";		
		
		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(XissueRegionURI).then().statusCode(200)
				.body(containsString("US"))
				.body(containsString("United States"))
				.body(containsString("relationships"))
				.body(containsString("socialRegions"))
				.body(containsString("regionType"))
				.body(containsString("true"))
				.extract().response();		
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		
		
	}

	@Test
	
	public void FISC_1510_trnsction_regionRelation(){
		

		String trnsactionURI = baseURI+"/v1/transactions/96354479/region";		
		
		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(trnsactionURI).then().statusCode(200)
				.body(containsString("US"))
				.body(containsString("United States"))
				.body(containsString("relationships"))
				.body(containsString("socialRegions"))
				.body(containsString("true"))
				.extract().response();		
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));	
		
	}
	
	@Test
	public void fisc_1558_regions_ancestors() {
		
		String regionsURi = baseURI + "/v1/regions";
		
		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(regionsURi).then().statusCode(200)
				.body(containsString("legacyRegionId"))
				.body(containsString("ancestors"))				
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		
		String ancestorRegionURI =  baseURI+"/v1/regions/DEVMKT/ancestors";
		
		
		Response res1 = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(ancestorRegionURI).then().statusCode(200)			
				.body(containsString("WORLD"))				
				.extract().response();
				
	   }
	
	@Test
	public void fisc_1571_regions_newattribute() {
		
		String regionsURi = baseURI + "/v1/regions";
		
		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(regionsURi).then().statusCode(200)
				.body(containsString("legacyRegionId"))
				
				.extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
	   }
	
		
		
}
		
		  	
				
				
		
		
