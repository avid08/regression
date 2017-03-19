package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint38 extends Configuration {
	
	
@Test

 public void BMI_with_EntitySummry_1774 () throws IOException {
	
	URL file = Resources.getResource("FCA_1774.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()
			
			.when().post(dataPostUrl)
			.then().statusCode(200)
			.body(containsString("currency"))
			.body(containsString("periodResolution"))
			.body(containsString("value"))
			
			.extract().response();

	Assert.assertFalse(responsedata.asString().contains("isError"));
	Assert.assertFalse(responsedata.asString().contains("isRestricted"));
  
	 
  }

@Test
public void FCA_1805_PeriodResulation () throws IOException {
	
	URL file = Resources.getResource("FCA_1805.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()
			
			.when().post(dataPostUrl)
			.then().statusCode(200)
			.body(containsString("currency"))
			.body(containsString("periodResolution"))
			.body(containsString("value"))
			.body(containsString("Q3"))
			.body(containsString("M1"))
			
			.extract().response();

	Assert.assertFalse(responsedata.asString().contains("isError"));
	Assert.assertFalse(responsedata.asString().contains("isRestricted"));
 
	 
 }



@Test
public void FCA_1789_DA_greaterthan_2018 () throws IOException {
	
	URL file = Resources.getResource("FCA_1789.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()
			
			.when().post(dataPostUrl)
			.then().statusCode(200)
			.body(containsString("currency"))
			.body(containsString("periodResolution"))
			.body(containsString("value"))
			.body(containsString("2020"))			
			
			.extract().response();

	Assert.assertFalse(responsedata.asString().contains("isError"));
	Assert.assertFalse(responsedata.asString().contains("isRestricted"));
 
	 
 }


}
