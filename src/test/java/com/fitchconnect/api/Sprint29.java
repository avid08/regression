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

public class Sprint29 extends Configuration {
	


	
	@Test
	public void test_1351() throws IOException {

		URL file1 = Resources.getResource("1351.json");
		String myJson1 = Resources.toString(file1, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson1)
				.with()

				.when().post(dataPostUrl)
				

				.then().assertThat().statusCode(200)
				.body(containsString("01010306"))
				.body(containsString("FC_MRKT_SECTOR_4_DESC"))				
				.body(containsString("Composite Insurers"))
				.body(containsString("Composite"))
				.extract().response();

		Assert.assertFalse(responsedata.asString().contains("isError"));
		Assert.assertFalse(responsedata.asString().contains("isMissing"));
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));

	}
	
	@Test
	
	public void test_1279 () throws IOException{
		URL file1 = Resources.getResource("1279.json");
		String myJson1 = Resources.toString(file1, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson1)
				.with()

				.when().post(dataPostUrl)
				

				.then().assertThat().statusCode(200)
				.body(containsString("statementLink"))
				.extract().response();
		
		Assert.assertFalse(responsedata.asString().contains("isError"));
		Assert.assertFalse(responsedata.asString().contains("isMissing"));
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));
		
	}

}