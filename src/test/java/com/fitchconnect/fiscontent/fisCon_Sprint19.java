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

public class fisCon_Sprint19 extends Configuration {
	
   @Test
	public void BMImetaData_databaseID_FISC_1732() {
		String BMIuRI = baseURI+"/v1/metadata/fields/BMI_INDEX_POL_ST_UNIT";
		
		
		//System.out.println(BMIuRI);
				
		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(BMIuRI).then()
				.statusCode(200)
				.body(containsString("displayName"))
				.body(containsString("Macro-Economic"))
				.body(containsString("BMI"))
				.body(containsString("databaseId"))
				.body(containsString("BMI_10012"))				
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	
  }
	
  @Test(enabled=true)
   
   public void Consolidated_AccuntingStndrd_Default_valueChange_fisc_1760() throws IOException{
	   
	   URL myfile = Resources.getResource("fisc_1760.json");

		String myJson = Resources.toString(myfile, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myJson).with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("auditTrail"))
				.body(containsString("value"))
				.body(containsString("JPY"))
				.body(containsString("2012-12-31"))
				.body(containsString("periodResolution"))
				.body(containsString("Annual"))
				.body(containsString("dateOptions"))				
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	   
	   
   }
  
  @Test (enabled=true)
  public void Consolidated_AccuntingStndrd_Default_valueChange_fisc_1760_BnkOfAmerica() throws IOException{
	   
	   URL myfile = Resources.getResource("fisc_1760_bnkOfamerica.json");

		String myJson = Resources.toString(myfile, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myJson).with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("auditTrail"))
				.body(containsString("value"))
				.body(containsString("JPY"))
				.body(containsString("2012-12-31"))
				.body(containsString("periodResolution"))
				.body(containsString("Annual"))
				.body(containsString("dateOptions"))
				.body(containsString("A"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	   
	   
   }
 
}
