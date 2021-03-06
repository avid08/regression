package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.configuration.api.Configuration;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint20 extends Configuration {

	@Test(enabled = true)
	public void ModyNewFields_775() throws IOException {

		URL file = Resources.getResource("ModyNewFieldsReq.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("FC_LT_FC_ISSR_DIR_MDY"))
				.body(containsString("FC_LT_FC_ISSR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_MQ_AMR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_NIFSR_FC_WATCHLIST_DT_MDY"))
				.body(containsString("FC_LT_FC_BANK_DEPOSITS_WATCHLIST_DT_MDY"))

				.extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertTrue(fieldResponse.asString().contains("FC_ST_DERIVED_ISSR_MDY"));

		List<String> values = fieldResponse.path("data.attributes.entities.values.fitchFieldId");
		assertNotNull(values);

	}

	@Test(enabled = true)
	public void ModyNewFields_776_withPermission() throws IOException {

		URL file = Resources.getResource("ModyNewFieldsReq.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("FC_LT_FC_ISSR_DIR_MDY"))
				.body(containsString("FC_LT_FC_ISSR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_MQ_AMR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_NIFSR_FC_WATCHLIST_DT_MDY"))
				.body(containsString("FC_LT_FC_BANK_DEPOSITS_WATCHLIST_DT_MDY")).extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertTrue(fieldResponse.asString().contains("FC_ST_DERIVED_ISSR_MDY"));

	}

	@Test(enabled = true)
	public void Entity_Search_921() {

		String searchStringEnd = "/v1/entities?filter[name]=Brazil";
		String SerchUrl = baseURI + searchStringEnd;

		given().header("Authorization", AuthrztionValue)

				.header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(SerchUrl).then().assertThat().log().ifError()
				.statusCode(200).body(containsString("Brazil")).body(containsString("ultimateParent"))
				.body(containsString("countryName")).body(containsString("region"))
				.body(containsString("Latin America"));

	}

	// Test Description : User wild card search does not return everything if
	// it's does not satisfy the search criteria

	@Test(enabled = true)
	public void Entity_Search_921_Negative() {

		String searchStringEnd = "/v1/entities?filter[name]=aAAd";
		String SerchUrl = baseURI + searchStringEnd;

		given().header("Authorization", AuthrztionValue)

				.header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(SerchUrl).then().assertThat().log().ifError()
				.statusCode(200).body("data", Matchers.empty()).body("data.included", Matchers.hasSize(0));

	}

	// Test Description :
	@Test(enabled = true)
	public void Entity_Search_922() {

		String enTityendPoint = "/v1/entities/1047648/"; // PublishFlag = True
		String enTityUrl = baseURI + enTityendPoint;

		given().header("Authorization", AuthrztionValue).header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(enTityUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("directors"))
				.body(containsString("officers")).body(containsString("shareholders"))
				.body("data.attributes.name", equalTo("Banco Santander Totta SA"));

	}

	@Test

	public void MetaDataService_withLinks_975() {
		Response fieldsRes = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(metaUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertFalse(fieldsRes.asString().contains("isError"));
		Assert.assertFalse(fieldsRes.asString().contains("isMissing"));

		List<String> relationship = fieldsRes.path("data.relationships.categories.links.self");
		List<String> Links = fieldsRes.path("data.links.self");

		for (int i = 0; i > Links.size(); i++) {
			Assert.assertNotNull(Links.get(i));
			Assert.assertNotNull(relationship.get(i));

		}

	}

	@Test
	public void categories2_802() {

		String categoryUri = "/v1/metadata/categories/2";
		String cteGorYUrl = baseURI + categoryUri;

		Response cateGories = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(cteGorYUrl)
				.then().assertThat().statusCode(200).body("data.attributes.name", equalTo("Financials"))
				.body("data.links.self", containsString("https:"))
				.body("data.relationships.children.data[0].type", equalTo("categories"))
				.body("data.relationships.children.data[1].id", Matchers.anything("12"))

				.extract().response();

		Assert.assertFalse(cateGories.asString().contains("isError"));
		Assert.assertFalse(cateGories.asString().contains("isMissing"));
	}

	@Test(enabled = true)
	public void FCURL_928() throws IOException {

		URL file = Resources.getResource("928_Request.json");

		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response dataResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("FC_COMPANY_NAME"))
				.body(containsString("GRP_")).extract().response();

		Assert.assertFalse(dataResponse.asString().contains("isError"));
		Assert.assertFalse(dataResponse.asString().contains("isMissing"));

	}

}
