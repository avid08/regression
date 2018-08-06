package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint30 extends Configuration {
	
	@Test 
	
	public void FISC_2780_statementResource(){
		
		String stmtendpoint = baseURI + "/v1/entities/140051/statements";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(stmtendpoint).then()
				.body(containsString("FC_GDP_LOCAL_SOV"))
				.body(containsString("FC_GDP_USD_SOV"))
				.body(containsString("USD"))
				.body(containsString("value"))
				.body(containsString("detail"))				
				.extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	
	}

}
