package com.region.api;

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

public class regions_datavalueRquest extends Configuration{
	
	@Test()
	public void FISC_5173_ESGData_allbank() throws IOException {

		URL file = Resources.getResource("bmi_with_region.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()
				.when().post(dataPostUrl)
				.then().assertThat().log().ifError().statusCode(200)			
				.body(containsString("value"))
				.body(containsString("Annual"))
				.body(containsString("AFN"))
				.body(containsString("forecastType"))
				.body(containsString("USD"))
				.body(containsString("BDT"))
				.body(containsString("firstForecastYear"))
				.body(containsString("source"))
				.body(containsString("lastReviewed"))
				.body(containsString("US GDP By State Series"))	
				.body(containsString("notes"))				
				.extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

	

	}
	
	@Test
	
	public void telecomOperatorsData_forRegionIds() throws IOException {
		

		URL file = Resources.getResource("telecomData_withRegion.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()
				.when().post(dataPostUrl)
				.then().assertThat().log().ifError().statusCode(200)			
				.body(containsString("value"))
				.body(containsString("Q4"))
				.body(containsString("actual"))
				.body(containsString("source"))
				.body(containsString("Vodafone"))
				.body(containsString("Telenor"))
				.body(containsString("Magyar Telekom"))
				.body(containsString("HUF"))
				.body(containsString("8455"))
				.body(containsString("2011-12-31"))	 							
				.extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

		
		
	}
	
@Test
	
	public void diseasesData_with_regionIds() throws IOException {
		

		URL file = Resources.getResource("diseasesData_withRegion.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()
				.when().post(dataPostUrl)
				.then().assertThat().log().ifError().statusCode(200)			
				.body(containsString("value"))
				.body(containsString("Annual"))
				.body(containsString("actual"))
				.body(containsString("source"))
				.body(containsString("gender"))
				.body(containsString("units"))
				.body(containsString("World Health Organization (WHO), Fitch Solutions"))									
				.extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

		
		
	}

}
