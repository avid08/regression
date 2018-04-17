package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

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
				.body(containsString("AAA"))
				.body(containsString("NR"))
				.body(containsString("CCC+"))
				.body(containsString("AA+"))	
				.body(containsString("CCC+"))	
				.body(containsString("BBB+"))					
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}	
	

}
