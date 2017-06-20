package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint1 extends Configuration {

	@Test

	public void FISC_162_StatementRelationship_EntityRankings() {

		String statemetnRelationshipURi = baseURI + "/v1/statements?filter[fitchId]=100188";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when()
				.get(statemetnRelationshipURi).then().assertThat().statusCode(200).extract().response();

		List<String> entiTiyLinks = res.path("data.relationships.entityRankings.links.related.href");

		System.out.println("number of available entityRanking Links " + entiTiyLinks.size());

		for (int i = 7; i < 30; i++) {

			Response res1 = given().header("Authorization", AuthrztionValue).header("content", contentValue)
					.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when()
					.get(entiTiyLinks.get(i)).then().assertThat().statusCode(200).extract().response();

			int MetaCount = res1.path("meta.count");

			if (MetaCount > 1) {
				System.out.println(MetaCount);

				Assert.assertTrue(res1.asString().contains("countryISOCode3"));
				Assert.assertTrue(res1.asString().contains("countryName"));
				Assert.assertTrue(res1.asString().contains("globalRankChange"));
				Assert.assertTrue(res1.asString().contains("rankType"));
				Assert.assertTrue(res1.asString().contains("rankYear"));
				Assert.assertTrue(res1.asString().contains("countryRank"));
				Assert.assertTrue(res1.asString().contains("relationships"));
				Assert.assertTrue(res1.asString().contains("statement"));

			}

			// System.out.println(res1.asString());

			Assert.assertFalse(res1.asString().contains("isError"));
			Assert.assertFalse(res1.asString().contains("isMissing"));
			Assert.assertFalse(res1.asString().contains("isRestricted"));
		}

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	@Test

	public void FISC_160_entityRanking() {

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
	
	public void FISC_223 (){
		
		String modyCateGoryURI = baseURI + "/v1/metadata/fields/FC_ST_NSR_MDY/categories";
		
		Response response = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(modyCateGoryURI).then().statusCode(200)
				.body(containsString("categories"))
				.body(containsString("relationships"))
				.body(containsString("fields"))
				.body(containsString("FC_ST_NSR_WATCHLIST_DT_MDY"))
				.body(containsString("National"))
				.extract().response();
		
		
		
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
		
	}
	

}
