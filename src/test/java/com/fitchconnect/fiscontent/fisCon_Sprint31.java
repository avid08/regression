package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.testng.Assert.assertNull;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint31 extends Configuration {	

	@Test
	public void RegionEntity_FISC_3060() {

		String regionURI = baseURI + "/v1/regions/AO";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(regionURI).then()
				.statusCode(200)
				.body(containsString("regions"))
				.body(containsString("regionType"))
				.extract().response();
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		String regionEntityLink = res.path("data.relationships.regionEntity.links.related");
		
		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(regionEntityLink).then()
				.statusCode(200)
				.body(containsString("entities"))
				.body(containsString("analysts"))
				.body(containsString("included"))
				.body(containsString("companies"))
				.extract().response();

		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));

	}
	
	@Test
	public void No_RegionEntity_FISC_3060() {

		String regionURI = baseURI + "/v1/regions/WEUR";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(regionURI).then()
				.statusCode(200)
				.body(containsString("regions"))
				.body(containsString("regionType"))
				.extract().response();
		String regionentityData = res.path("data.relationships.regionEntity.data");
		assertNull(regionentityData);
		
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		
	}
	
	@Test 
	
	public void fisc_2616_statementRsource_ConvertMonetaryData () {
	
	String regionURI = baseURI + "/v1/entities/111631/statements?currency=USD";

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(regionURI).then()
			.statusCode(200)
			.body(containsString("statements"))
			.body(containsString("accountingStandard"))
			.body(containsString("businessTemplate"))
			.body(containsString("Banks"))
			.body(containsString("FC_OTHER_INT_INC_BNK"))
			.body(containsString("USD"))
			.body(containsString("currency"))
			.body(containsString("value"))
			.body(containsString("number"))			
			.extract().response();		
	
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
	
	}
}
