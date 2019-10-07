package com.PublicFinanc.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.jayway.restassured.response.Response;

public class TransactionSecurityTestCases extends Configuration {
	
	
@Test 
	
	public void all_transactionSecurities() {
		
		String trnsctionSecurityUri = baseURI+"/v1/transactionSecurities";
		System.out.println(trnsctionSecurityUri);
		
				Response cateGories = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(trnsctionSecurityUri)
				.then().assertThat().statusCode(200)
				.body(containsString("transactionSecurities"))
				.body(containsString("transactionSecurityRatings"))
				.body(containsString("issues"))
				.body(containsString("transaction"))	
				.extract().response();

		Assert.assertFalse(cateGories.asString().contains("isError"));
		Assert.assertFalse(cateGories.asString().contains("isMissing"));
		
	}

@Test 

public void Single_transactionSecurities() {
	
	String trnsctionSecurityUri = baseURI+"/v1/transactionSecurities?filter[id]=96721186";
	System.out.println(trnsctionSecurityUri);
	
			Response cateGories = given().header("Authorization", AuthrztionValue).header("content", contentValue)
			.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(trnsctionSecurityUri)
			.then().assertThat().statusCode(200)
			.body(containsString("transactionSecurities"))
			.body(containsString("transactionSecurityRatings"))
			.body(containsString("issues"))
			.body(containsString("transaction"))	
			.extract().response();

	Assert.assertFalse(cateGories.asString().contains("isError"));
	Assert.assertFalse(cateGories.asString().contains("isMissing"));
	
}

}
