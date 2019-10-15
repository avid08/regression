package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint8 extends Configuration {

	@Test
	public void FISC_606_attributefilter() {

		String FIRfilterUrl = baseURI
				+ "/v1/financialImpliedRatings/?filter[fitchEntityId]=1143152,1003922,1000469,1040027&filter[rating]=c&filter[stmntDate]=2014-12-31&filter[stmntDateRank]=5";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(FIRfilterUrl).then().statusCode(200).body(containsString("stmntDate"))
				.body(containsString("fitchEntityId")).body(containsString("1143152")).body(containsString("3")).extract()
				.response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FISC_607_firRelationship_From_entity() {

		String FIRrelationshiplink = baseURI + "/v1/entities";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(FIRrelationshiplink).then().statusCode(200).extract().response();
		String firrelationshiplink = res.path("data[0].relationships.financialImpliedRatings.links.related");

		assertNotNull(firrelationshiplink);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void FISC_608_FIR_categories() {

		String FIRcategoriesUrl = baseURI + "/v1/metadata/categories/260";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(FIRcategoriesUrl).then().statusCode(200).body(containsString("Financial Implied Rating")).extract()
				.response();

		System.out.println(res.asString());

		String predictions = res.path("data.relationships.children.data[0].id");

		System.out.println(predictions);

		String predictionURl = baseURI + "/v1/metadata/categories/" + predictions;

		System.out.println(predictionURl);

		Response res1 = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(predictionURl).then().statusCode(200).body(containsString("Predictions"))
				.body(containsString("FC_LOAN_QUAL_FIR")).body(containsString("FC_PERIOD_DT_RANK_FIR"))
				.body(containsString("FC_FIR")).body(containsString("FC_COUNTRY_RISK_IND_FIR"))
				.body(containsString("FC_PROFIT_FIR")).body(containsString("FC_PERIOD_DT_FIR"))
				.body(containsString("FC_REGION_FIR")).body(containsString("FC_TOTAL_ASSETS_FIR"))
				.body(containsString("FC_BAND_RANK_FIR")).body(containsString("FC_MODEL_SCORE_FIR"))
				.body(containsString("FC_STATEMENT_ID_FIR")).extract().response();

	}

	@Test

	public void FISC_608_FIR_DataAggregator() throws IOException {

		URL file = Resources.getResource("FISC_608.json");
		myjson = Resources.toString(file, Charsets.UTF_8);

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).contentType("application/vnd.api+json").body(myjson).with().when()
				.post(dataPostUrl).then().statusCode(200).body(containsString("value")).body(containsString("type"))
				.body(containsString("timeIntervalPeriod"))

				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FISC_923_IssueService() {

		String getallissueUrl = baseURI + "/v1/issues";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(getallissueUrl).then().statusCode(200).body(containsString("country"))
				.body(containsString("cusip"))
				.body(containsString("disclosure")).body(containsString("marketSectors"))
				.body(containsString("marketsectorId")).body(containsString("marketSectorDesc"))
				.body(containsString("classTypeDescription")).body(containsString("coupon"))
				.body(containsString("countryCd")).body(containsString("privatePlacementDescription"))
				.body(containsString("bondName")).body(containsString("originalAmount"))
				.body(containsString("debtLevelDescription")).body(containsString("maturityDate"))
				.body(containsString("countryOfAnalyst")).body(containsString("currency")).body(containsString("isin"))
				.body(containsString("relationships")).body(containsString("entity")).body(containsString("issuer"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		String singleIssueId = res.path("data[0].id");

		System.out.println(" issueId " + singleIssueId);

		String singleIssueIdUrl = getallissueUrl + "/" + singleIssueId;
		
		System.out.println(singleIssueIdUrl);

		Response res1 = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(singleIssueIdUrl).then().statusCode(200).body(containsString("country"))
				.body(containsString("cusip"))
				.body(containsString("disclosure")).body(containsString("marketSectors"))
				.body(containsString("marketsectorId")).body(containsString("marketSectorDesc"))
				.body(containsString("classTypeDescription")).body(containsString("coupon"))
				.body(containsString("countryCd")).body(containsString("privatePlacementDescription"))
				.body(containsString("bondName")).body(containsString("originalAmount"))
				.body(containsString("debtLevelDescription")).body(containsString("maturityDate"))
				.body(containsString("countryOfAnalyst")).body(containsString("currency")).body(containsString("isin"))
				.body(containsString("relationships")).body(containsString("entity")).body(containsString("issuer"))

				.extract().response();

	}

}
