package com.metadata.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import org.testng.annotations.Test;

public class regressionTestCases extends Configuration{
	
	@Test()
	public void StatusCodeTest() {

		int statuscode = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("Accept", acceptValue).header("Content", contentValue).when().get(metaUrl)

				.statusCode();

		assertTrue(statuscode == 200);

	}
	
	
	@Test()
	public void metaDataResponse_with_PF_US897() {
		
		System.out.println(metaUrl);

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then().assertThat()
				.statusCode(200).contentType(ContentType.JSON)
				.body("data.attributes.fitchFieldDesc", hasItem("Corporate Hierarchy Publish Flag")).extract()
				.response();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

		Assert.assertTrue(response.asString().contains("FC_CH_PUBLISH_FLAG"));

		List<String> attributes = response.path("data.attributes");
		assertNotNull(attributes);

	}

	
	@Test()
	public void MetaData_Issue_1026() {

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then()
				.contentType(ContentType.JSON).extract().response();
   
		System.out.println(metaUrl);
		
		  
		List<String> Id = response.path("data.id");
		List<String> displayNme = response.path("data.attributes.displayName");

		List<String> fitchFieldDes = response.path("data.attributes.fitchFieldDesc");

		List<String> link = response.path("data.links.self");

		for (int i = 0; i > Id.size(); i++) {
			Assert.assertNotNull(Id.get(i));
			Assert.assertNotNull(displayNme.get(i));
			Assert.assertNotNull(fitchFieldDes.get(i));
			Assert.assertNotNull(link.get(i));

		}

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

	}

	
	
	

}
