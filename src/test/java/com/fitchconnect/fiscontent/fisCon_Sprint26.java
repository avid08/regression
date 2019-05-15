package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint26 extends Configuration {
	
	@Test
	 
	 public void Fisc_430_region() throws IOException {
		 
		 URL file = Resources.getResource("FISC_2140_Nodate.json");
			String myRequest = Resources.toString(file, Charsets.UTF_8);
	
			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
					.with()
					.when().post(dataPostUrl)
					.then().assertThat().statusCode(200)
					.body(containsString("isRestricted"))
					.body(containsString("true"))													
					.extract().response();
		  
			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));
			Assert.assertFalse(response.asString().contains("isRestricted"));
		 	 
	   }
	
	@Test(enabled=false)
	 
	 public void Fisc_2321_Cds_DateChange() throws IOException {
		 
		 URL file = Resources.getResource("FISC_2321_date.json");
			String myRequest = Resources.toString(file, Charsets.UTF_8);
	
			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
					.with()
					.when().post(dataPostUrl)
					.then().assertThat().statusCode(200)
					.body(containsString("value"))
					.body(containsString("2015-10-30"))
					.body(containsString("S"))					
					.body(containsString("date"))								
					.extract().response();
		  
			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));
			Assert.assertFalse(response.asString().contains("isRestricted"));
		 	 
	   }
}
