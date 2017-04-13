package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint39 extends Configuration {

	@Test
	public void FCA_2025() throws IOException {

		URL file = Resources.getResource("FCA_2025.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().statusCode(200).body(containsString("value")).body(containsString("year"))
				.body(containsString("text")).body(containsString("CUSIP9"))

				.extract().response();
		assertNotNull(res);
		// Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FCA_1938() throws IOException {

		URL file = Resources.getResource("FCA_1938.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().statusCode(200).body(containsString("currency")).body(containsString("numerical"))
				.body(containsString("FC_PROFIT_BEFORE_TAX_SHARE_BROKER"))

				.extract().response();
		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test(enabled=false)
	public void FCA_1925() throws IOException {

		URL file = Resources.getResource("FCA_1925.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().statusCode(200).body(containsString("date")).body(containsString("text"))
				.body(containsString("FC_NIMQR_ACTN"))

				.extract().response();
		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void FCA_2031_entityRanking() {

		String entiYrankings = "/v1/entityRankings?filter[rankType]=operatingProfitRanks&filter[countryISOCode3]=JPN&sort[globalRank]=desc";
		String entiTyRnkinURI = baseURI + entiYrankings;

		Response response = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(entiTyRnkinURI).then().statusCode(200)
				.contentType("application/vnd.api+json").body(containsString("countryRankChange"))
				.body(containsString("operatingProfitRanks")).body(containsString("countryRank"))
				.body(containsString("JPN"))
				.body(containsString("countryISOCode3"))				
				.body(containsString("JAPAN")).body(containsString("count")).body(containsString("self"))
				.body(containsString("next")).body(containsString("last"))

				.extract().response();

		List<String> entityData = response.path("data.relationships.entity.links.related");

		System.out.println(entityData.size());

		for (int i = 0; i < entityData.size(); i++) {
		

			Response response1 = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(entityData.get(i)).then().statusCode(200)
					.body(containsString("officers")).body(containsString("name")).body(containsString("countryName"))
					.body(containsString("officers"))
				

					.body(containsString("relationships")).body(containsString("entityRankings"))
					.body(containsString("standardAndPoorIssuerRatings")).body(containsString("moodyIssuerRatings"))
					.body(containsString("fitchIssuerRatings")).body(containsString("ultimateParent"))
					.body(containsString("company")).body(containsString("officers")).extract().response();

			Assert.assertFalse(response1.asString().contains("isError"));
		}

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
	}
	

	@Test

	public void FCA_2031_ISOCountryCode() {

	
		String entityIDRankingURi = baseURI +"/v1/entities/100188/entityRankings";

		Response response = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(entityIDRankingURi).then().statusCode(200)
				.contentType("application/vnd.api+json").body(containsString("countryRankChange"))
				.body(containsString("operatingProfitRanks")).body(containsString("countryRank"))
				.body(containsString("countryISOCode3"))
				.body(containsString("globalRank"))
				.body(containsString("globalRankChange"))				
				
				.extract().response();
				
				
	}
	

@Test

public void notAvailable_MetaData_status_1723(){
	
	String NotAvilbeMetaDtaURi = metaUrl+"FC_HTM_OTHER_DEBT_SEC_BNK";
	
	Response response = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(NotAvilbeMetaDtaURi).then().statusCode(404)
			.extract().response();
	
	
  }

@Test

public void FCA_1800_categories() {


	String categoryCHildUri = baseURI +"/v1/metadata/categories/1/relationships/children";

	Response response = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(categoryCHildUri).then().statusCode(200)			
			.body(containsString("categories"))	
			.body(containsString("id"))
			.body(containsString("self"))
			.body(containsString("related"))
			
			.extract().response();
			
			
   }







}
