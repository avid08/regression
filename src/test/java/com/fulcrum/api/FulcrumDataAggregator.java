package com.fulcrum.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.jayway.restassured.response.Response;

public class FulcrumDataAggregator extends Configuration{	
	 @Test

	  public void Lfiresourcefindall(){
		
		String LFIUri = baseURI+"/v1/levfinloans";
		  System.out.println(LFIUri);
		  

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(LFIUri)
				.then().statusCode(200)
				.body(containsString("levfinloans"))					
				.extract().response();
		

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		  String findOneLfi = res.path("data[0].id");
		  System.out.println(findOneLfi);		  
		  String findone =  baseURI+"/v1/levfinloans/"+findOneLfi;   	  
		  
		  Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
					.when().get(findone)
					.then().statusCode(200)									
					.extract().response();
		  
		  
			Assert.assertFalse(res1.asString().contains("isError"));
			Assert.assertFalse(res1.asString().contains("isMissing"));
			Assert.assertFalse(res1.asString().contains("isRestricted"));
			
		  


		
	   }

}
