package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint37 extends Configuration {
	
	@Test(enabled=false)
	
	public void BMI_Dates_Monthly_quarterly_Annual_fisc2755() throws IOException{
		URL file = Resources.getResource("bmi_dates_mthly_Quatrly.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200)			
				.body(containsString("Q1"))
				.body(containsString("M8"))
				.body(containsString("Annual"))
				.body(containsString("2015"))
				.extract().response();	
		

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
				
	}
		
@Test(enabled=false)
	
	public void BMI_Period_Monthly_quarterly_Annual_fisc2755() throws IOException{
		
	URL file = Resources.getResource("bmi_Period_monthly_quarterly_annual.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);
	
	//System.out.println(myJson);
	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
			.assertThat().statusCode(200)
			.body(containsString("Annual"))
			.body(containsString("M10"))
			.body(containsString("Q4"))
			.body(containsString("2007"))	
			.extract().response();
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
	

		
		
	}


@Test(enabled=false)

public  void Fisc_2755_BMI_DatesPeriod_Monthly_quarterly_Annual() throws IOException{
	
	
	URL file = Resources.getResource("bmi_datesPeriods_mnthly_QurtlyRange.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);
	
	//System.out.println(myJson);
	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
			.assertThat().statusCode(200)		
			.body(containsString("M9"))
			.body(containsString("M10"))
			.body(containsString("M11"))
			.body(containsString("M12"))
			.body(containsString("Q1"))
			.body(containsString("Q2"))
			.body(containsString("Q3"))
			.body(containsString("Q4"))			
			.body(containsString("2004"))
			.body(containsString("2015"))	
			.extract().response();
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
	
	
	
	
	
     }
	
  





}
