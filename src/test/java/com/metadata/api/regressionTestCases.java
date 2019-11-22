package com.metadata.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class regressionTestCases extends Configuration {

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

	@Test

	public void all_metadata_filter() {

		String rating = metaUrl + "?filter[source]=ratings";
		String financial = metaUrl + "?filter[source]=financial";
		String entitySummary = metaUrl + "?filter[source]=entitySummary";
		String moodysRatingg = metaUrl + "?filter[source]=moodysRatings";
		String SnPrating = metaUrl + "?filter[source]=standardAndPoorRatings";
		String bmi = metaUrl + "?filter[source]=bmi";
		String financilImpldRating = metaUrl + "?filter[source]=financialImpliedRatings";
		String cds = metaUrl + "?filter[source]=cds";
		String issues = metaUrl + "?filter[source]=issues";
		String diseases = metaUrl + "?filter[source]=diseasesAndInjuries";
		String telcomM = metaUrl + "?filter[source]=telecomOperators";
		String ratingNavigator = metaUrl + "?filter[source]=ratingsNavigator";
		String equtyPrice = metaUrl + "?filter[source]=equityPrice";
		String equtybenchMark = metaUrl + "?filter[source]=equityBenchmark";
		String LeverageFinance = metaUrl + "?filter[source]=leveragedFinance";
		String BaseLeverageFinance = metaUrl + "?filter[source]=leveragedFinanceBase";
		String Biri = metaUrl + "?filter[source]=biri";

		String[] filterdMetadata = { rating, financial, entitySummary, moodysRatingg, SnPrating, bmi,
				financilImpldRating, cds, issues, diseases, telcomM, ratingNavigator, equtyPrice, equtybenchMark,
				LeverageFinance, BaseLeverageFinance, Biri };

		System.out.println(filterdMetadata.length);

		for (int i =0; i < filterdMetadata.length; i++) {

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(filterdMetadata[i]).then().statusCode(200)
					.contentType(ContentType.JSON).body(containsString("id")).body(containsString("type"))
					.body(containsString("displayName")).body(containsString("fitchFieldDesc"))
					.body(containsString("permissionsRequired")).body(containsString("links"))
					.body(containsString("relationships")).body(containsString("service"))
					.body(containsString("categories"))					
					.extract().response();
			
			Assert.assertTrue(response.asString().contains("included"));

		}

	}

}
