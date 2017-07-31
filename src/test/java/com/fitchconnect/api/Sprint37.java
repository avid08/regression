package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint37 extends Configuration {
	

 @Test
	
public void displaynameChange_1884 () {
	 
	 
	String url = baseURI + "/v1/metadata/fields/FC_LT_LC_NIR_SOLST";

	Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
			.get(url).then()
			.body(containsString("Long-Term National LC Rating Solicitation Status"))
			
			
			.statusCode(200).extract().response();

	
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
	
	
   }

 @Test
	public void BMI_MixDatapoints_valueTest() throws IOException {

		URL file = Resources.getResource("BMI_With_OtherDatapoints_DA_value.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body(containsString("United States of America"))
				.body(containsString("value"))
				.body(containsString("Annual"))
				.body(containsString("USD"))			
				
				.extract().response();

		Assert.assertFalse(responsedata.asString().contains("isError"));
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));
		Assert.assertFalse(responsedata.asString().contains("isMissing"));

		

	}
@Test
 public void disPlayNme_Change_1763() {
	 
	 
		String url = baseURI + "/v1/metadata/fields/FC_SRF";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(url).then()
				.body(containsString("Support Rating Floor"))			
				
				.statusCode(200).extract().response();

		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		
	   }
 
  @Test 
  
  public void investmnt_Mangr_raTing_1732 () throws IOException {
	  
	  
		URL file = Resources.getResource("fca_1732.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body(containsString("text"))
				.body(containsString("date"))
				
				.extract().response();

		Assert.assertFalse(responsedata.asString().contains("isError"));
		Assert.assertFalse(responsedata.asString().contains("isMissing"));
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));
		
	  
	  
	  
  }
  
  
@Test 
  
  public void no_financialdata_forA_Date_1665  () throws IOException {
	  
	URL file = Resources.getResource("FCA_1665.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

			.when().post(dataPostUrl)

			.then().assertThat().log().ifError().statusCode(200)
			.body(containsString("currency"))
			
			
			.extract().response();
	Assert.assertFalse(responsedata.asString().contains("USD"));
	
	
	Assert.assertFalse(responsedata.asString().contains("isError"));
	Assert.assertFalse(responsedata.asString().contains("isMissing"));
	Assert.assertFalse(responsedata.asString().contains("isRestricted"));
  
	  
  
   }

  @Test
  
 public void Financialdata_withOutAdate_1665_CombinefitchFields() throws IOException {
	  
	  URL file = Resources.getResource("fca_1665_withNOdate.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body(containsString("JPMorgan Chase & Co."))
				.body(containsString("value"))
				.body(containsString("timeIntervalPeriod"))
				.body(containsString("year"))	
				
				.extract().response();

		Assert.assertFalse(responsedata.asString().contains("isError"));
		Assert.assertFalse(responsedata.asString().contains("isMissing"));
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));
	  
		  
	  
  }
  
}
