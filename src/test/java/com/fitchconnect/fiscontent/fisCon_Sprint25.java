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

public class fisCon_Sprint25 extends Configuration {
		
	@Test
	public void FISC_1944_selfLink() {

		String metaDataURI = baseURI + "/v1/financialImpliedRatings";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(metaDataURI).then()
				.assertThat().statusCode(200)
				.body(containsString("financialImpliedRatings"))
				.body(containsString("countryRiskIndicator"))
				.body(containsString("links"))					
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	@Test
	 
	 public void Fisc_fIR_valueRequestcall() throws IOException {
		 
		 URL file = Resources.getResource("FIRvalueRequest.json");
			String myRequest = Resources.toString(file, Charsets.UTF_8);
	
			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
					.with()
					.when().post(dataPostUrl)
					.then().assertThat().statusCode(200)
					.body(containsString("value"))					
											
					.extract().response();
		  
			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));
			Assert.assertFalse(response.asString().contains("isRestricted"));
		 	 
	   }
	
	@Test
	public void FISC_1945_selfLink() {

		String metaDataURI = baseURI + "/v1/regions/EMGMKT";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(metaDataURI).then()
				.assertThat().statusCode(200)
				.body(containsString("regions"))
			
				.body(containsString("Emerging Markets"))
				.body(containsString("links"))	
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	@Test
	public void Subnatoinal_RegionType() {

		String metaDataURI = baseURI + "/v1/regions/US-WI";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(metaDataURI).then()
				.assertThat().statusCode(200)
				.body(containsString("regions"))
			
				.body(containsString("Sub-National"))
				.body(containsString("State"))	
				.body(containsString("Wisconsin"))	
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	@Test
	public void FISC_1943_selfLink() {

		String metaDataURI = baseURI + "/v1/issues/90522483/relationships/issueRatings";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(metaDataURI).then()
				.assertThat().statusCode(200)
				.body(containsString("solicitation"))
				.body(containsString("issueRatings"))
				.body(containsString("Long-Term Rating"))				
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	@Test
	public void FISC_2308_predefineValues_rating() {

		String metaDataURI = baseURI + "/v1/metadata/fields/FC_IRR";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(metaDataURI).then()
				.assertThat().statusCode(200)
				.body(containsString("F1+"))
				.body(containsString("F1"))
				.body(containsString("D"))
				.body(containsString("F1+/F1+"))				
				.body(containsString("PIF"))					
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}	
	
	
	@Test
	 
	 public void Fisc_2139() throws IOException {
		 
		 URL file = Resources.getResource("fisc_2139.json");
			String myRequest = Resources.toString(file, Charsets.UTF_8);
	
			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
					.with()
					.when().post(dataPostUrl)
					.then().assertThat().statusCode(200)
					.body(containsString("value"))					
					.body(containsString("forecastType"))
					.body(containsString("Annual"))					
					.body(containsString("firstForecastYear"))
					.body(containsString("source"))
					.body(containsString("lastReviewed"))
					.body(containsString("notes"))										
					.extract().response();
		  
			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));
			Assert.assertFalse(response.asString().contains("isRestricted"));
		 	 
	   }
	
	@Test
	public void FISC_2138_predefineValue_BMI() {

		String metaDataURI = baseURI + "/v1/metadata/fields/BMI_EDUCATION_ENROL_EDU_UNIT";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(metaDataURI).then()
				.assertThat().statusCode(200)
				.body(containsString("fieldDefinition"))
				.body(containsString("Tertiary (third level) education, defined as higher education or post-secondary (final) school level education. Represents the number of students enrolled on &#39;education&#39"))
				.body(containsString("bmi"))
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}	
	
	

}
