package com.PublicFinanc.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.jayway.restassured.response.Response;

public class TransactionService_Test_cases extends Configuration {
	
	@Test 
	
	public void all_transactions() {
		
		String trnsctionUri = baseURI+"/v1/transactions";
		System.out.println(trnsctionUri);
		
		

		Response cateGories = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(trnsctionUri)
				.then().assertThat().statusCode(200)
				.body(containsString("transactions"))
				.body(containsString("keyRatingFactors"))
				.body(containsString("transactionRatings"))
				.body(containsString("transactionAgent"))
				.body(containsString("transactionSecurities"))	
				.extract().response();

		Assert.assertFalse(cateGories.asString().contains("isError"));
		Assert.assertFalse(cateGories.asString().contains("isMissing"));
		
	}
	
@Test()
public void Single_transactions_with_fitler() {
		
		String trnsctionUri = baseURI+"/v1/transactions?filter[id]=96461415,96350371";
		System.out.println(trnsctionUri);
		

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(trnsctionUri)
				.then().assertThat().statusCode(200)
				.body(containsString("transactions"))
				.body(containsString("keyRatingFactors"))
				.body(containsString("transactionRatings"))
				.body(containsString("transactionAgent"))
				.body(containsString("transactionSecurities"))
				.extract().response();
		
		String sngleTransactionId = res.path("data[0].id");
		
		 String singletransctionIdURI = trnsctionUri+"/v1/"+sngleTransactionId ;
		
		Response res1 = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(singletransctionIdURI)
				.then().assertThat().statusCode(200)
				.body(containsString("transactions"))
				.body(containsString("relationships"))
				.extract().response();				

		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
		
	}


@Test()
public void transactions_allfilters() {
		
		String trnsctionUri = baseURI+"/v1/transactions?filter[issuers.id]=101121";
		
		System.out.println(trnsctionUri);

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(trnsctionUri)
				.then().assertThat().statusCode(200)
				.body(containsString("transactions"))
				.body(containsString("keyRatingFactors"))
				.body(containsString("transactionRatings"))
				.body(containsString("transactionAgent"))
				.body(containsString("transactionSecurities"))		
				.body(containsString("Memphis & Shelby County Sports Authority (TN)"))
				.extract().response();		

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
	}
	

}
