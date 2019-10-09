package com.marketsector.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.jayway.restassured.response.Response;

public class marketSectorTestCases extends Configuration  {
	
	@Test()
	
	public void MarketSector_all() {
		String marketSctURI = baseURI+"/v1/marketSectors";
		
		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(marketSctURI).then().statusCode(200)
				.body(containsString("marketSectors"))
				.body(containsString("parent"))
				.body(containsString("children"))
				.body(containsString("level"))
				.body(containsString("name"))
				.body(containsString("active"))
				.body(containsString("bmiServiceChannelIds"))	
				.extract().response();
				
		
	}
	
 	@Test()
	
	public void MarketSector_with_Single_filter() {
		String marketSctURI = baseURI+"/v1/marketSectors?filter[level]=1,2";
		
		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(marketSctURI).then().statusCode(200)
				.body(containsString("marketSectors"))
				.body(containsString("parent"))
				.body(containsString("children"))
				.body(containsString("Corporate Finance"))
				.body(containsString("children"))	
				
				.extract().response();
		boolean failure = false;
		
		int levelData = res.path("data[0].attributes.level");
		
		if ( levelData == 1) {
			
			System.out.println("response contains level 1 data ");			
			
		}else { 
			failure = true;
			System.out.println("response Does contains level 1 data ");	
		}
		
		Assert.assertFalse(failure);
	}
     	
@Test()
	
	public void MarketSector_with_allfilter() {
		String marketSctURI = baseURI+"/v1/marketSectors?filter[level]=1,2&filter[bmiServiceChannelIds]=C427";
		
		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(marketSctURI).then().statusCode(200)
				.body(containsString("marketSectors"))
				.body(containsString("parent"))
				.body(containsString("C427"))
				.extract().response();
		
	}
     

}
