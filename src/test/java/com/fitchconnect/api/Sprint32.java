package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint32 extends Configuration{


	@Test
	
	public void Fca_1590 () {
		
		String entitiesUrl = baseURI + "/v1/entities/107477";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(entitiesUrl).then()
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		String ModyReltdlink = res.path("data.relationships.moodyIssuerRatings.links.related");
		String SndPReldLink = res.path("data.relationships.standardAndPoorIssuerRatings.links.related");
		
		System.out.println(ModyReltdlink);
		
		
		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(ModyReltdlink).then()
				.body(containsString("fitchFieldDescription"))
				.extract().response();
		
		
		Response res3 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(SndPReldLink).then()
				.body(containsString("fitchFieldDescription"))
				.extract().response();
		
		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
		
	}
	
	@Test
	
	public void fca_1569() {
		
		String IPFurl = baseURI + "/v1/entities/110631";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(IPFurl).then()
				//.body(containsString("Banco Popular Espanol S.A."))
				
				
				.extract().response();
		
		String fitchiUsserRting = res.path("data.relationships.fitchIssuerRatings.links.related");
		
	
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		
		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(fitchiUsserRting).then()
				.body(containsString("rating"))
				.body(containsString("ratingType"))
				.extract().response();	
		
		
		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
		
	}

   @Test
   
   public void fca_1569_dataAggregator () throws IOException{
	   
	   URL file = Resources.getResource("fca_1569.json");
		myjson = Resources.toString(file, Charsets.UTF_8);

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).contentType("application/vnd.api+json").body(myjson).with().when()
				.post(dataPostUrl).then().statusCode(200)
				.body(containsString("value"))
				.body(containsString("FC_LT_IDR"))
				.extract().response();
		
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	   
   }
   
   @Test
   
   public void fca_1571() {
	   
	   
	   String fitchIssuerUrl = baseURI + "/v1/entities/110631/fitchIssuerRatings?filter[ratingType]=FC_LT_IDR";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(fitchIssuerUrl).then()
				.body(containsString("description"))
				.body(containsString("Long-Term Issuer Default Rating"))
				.body(containsString("fitchIssuerRatings"))					
				.extract().response();
		
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	   
   }
   
 @Test
   
   public void fca_1571_dataAggregator() throws IOException{
	 
	 
	   URL file = Resources.getResource("fca_1571.json");
			myjson = Resources.toString(file, Charsets.UTF_8);

			Response res = given()

					.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).contentType("application/vnd.api+json").body(myjson).with().when()
					.post(dataPostUrl).then().statusCode(200)
					.body(containsString("FC_ST_LC_IDR"))
					.body(containsString("value"))					
					.extract().response();
    
  }
 
}
