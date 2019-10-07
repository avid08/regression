package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint6 extends Configuration {

	@Test

	public void FISC_599_FIRservice() {

		String getAllurl = baseURI + "/v1/financialImpliedRatings";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(getAllurl).then().statusCode(200).body(containsString("financialImpliedRatings"))
				.body(containsString("countryRiskIndicator")).body(containsString("region"))
				.body(containsString("rating")).body(containsString("fitchEntityId"))
				.body(containsString("totalAssets")).body(containsString("stmntDate"))
				.body(containsString("profitability")).body(containsString("loanQuality"))
				.body(containsString("bandRanking")).body(containsString("modelScore"))
				.body(containsString("batchDate")).extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		String FIR_Id = res.path("data[0].id");
		String getAurl = getAllurl + "/" + FIR_Id;
		System.out.println(getAurl);

		Response res1 = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(getAurl).then().statusCode(200).body(containsString("financialImpliedRatings"))
				.body(containsString("countryRiskIndicator")).body(containsString("region"))
				.body(containsString("rating")).body(containsString("fitchEntityId"))
				.body(containsString("totalAssets")).body(containsString("stmntDate")).extract().response();

		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));

	}

	@Test

	public void Fisc_736() {

		String surVillnceReportURl = baseURI + "/v1/surveillanceDeals/96287534-CDO/surveillanceReport";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(surVillnceReportURl).then().statusCode(200).body(containsString("1828 CLO Ltd..xlsx"))
				.body(containsString("surveillanceDeals")).body(containsString("download")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void fisc_785() throws IOException {
		URL file = Resources.getResource("fisc_785.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myRequest).with().when().post(dataPostUrl).then()
				.statusCode(200).body(containsString("value")).body(containsString("Healthcare"))
				.body(containsString("U.S. Public Finance")).body(containsString("UNITED STATES"))
				.body(containsString("04")).extract().response();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

	}

	@Test

	public void fisc_860() throws IOException {

		URL file = Resources.getResource("fisc_860.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myRequest).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body(containsString("value"))
				.body(containsString("Annual")).body(containsString("timeIntervalDate")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
}