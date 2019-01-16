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

public class fisCon_Sprint15 extends Configuration {

	@Test
	public void fisc_182_SpellingMistke_Bug() throws IOException {

		URL file = Resources.getResource("fisc_182.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200)
				.body(containsString("FC_LT_CAVAL_NSR_SP"))
				.body(containsString("value"))
				.extract().response();	
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
			
	}

	@Test
	public void FISC_1294() {

		String entityResrceURI = baseURI + "/v1/issuers?filter[entityId]=116938&filter[typeDesc]=Corporate";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityResrceURI).then()
				.statusCode(200).body(containsString("Corporate")).body(containsString("hasTransactions"))
				.body(containsString("countryOfFitchLegalEntity")).body(containsString("typeDesc")).extract()
				.response();

		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FISC_1337_IssUeAttributes() {

		String entityResrceURI = baseURI + "/v1/issuers?filter[cusip]=57163R,45921E&filter[id]=4&filter[entityId]=116980";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityResrceURI).then()
				.statusCode(200).body(containsString("countryOfFitchLegalEntity"))
				.body(containsString("identifiers"))
				.body(containsString("countryOfFitchLegalEntity"))
				.body(containsString("cusip"))
				.body(containsString("57163R"))
				.body(containsString("45921E"))
				.body(containsString("116980"))				
				.extract().response();

		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FISC_1386_newIsseurIdfilter() {

		String entityResrceURI = baseURI + "/v1/transactions?filter[issuers.id]=2847,2789";
		boolean failure = false;

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityResrceURI).then()
				.statusCode(200).body(containsString("96250128")).body(containsString("transactions"))
				.body(containsString("analysts")).body(containsString("groupCode"))
				.body(containsString("relationships")).body(containsString("keyRatingFactors"))
				.body(containsString("marketSectors")).extract().response();

		int totalCount = res.path("meta.totalResourceCount");

		if (totalCount == 4) {

		} else {
			System.err.println("count does not match" + totalCount);
			failure = true;

		}

		Assert.assertFalse(failure);

		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void FISC_1387() {

		String entityResrceURI = baseURI + "/v1/issuers";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityResrceURI).then()
				.statusCode(200).body(containsString("hasTransactions")).body(containsString("false"))
				.body(containsString("true")).body(containsString("identifiers")).extract().response();

		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
		
	@Test

	public void FISC_1442_newAttributes() throws IOException {

		String entityResrceURI = baseURI + "/v1/entities/116980";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityResrceURI).then()
				.statusCode(200).body(containsString("lei")).body(containsString("swiftCode"))
				.body(containsString("cusip")).body(containsString("dowJonesTicker"))
				.body(containsString("identifiers")).body(containsString("analysts")).extract().response();

		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		
		String OrFilterUri = baseURI +"/v1/entities?filter[lei]=HZSN7FQBPO5IEWYIGC72&filter[cusip]=57163R,516358&filter[dowJonesTicker]=GDLA&filter[swiftCode]=CTYCUS44&filter[fitchEntityId]=116980";
		
		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(OrFilterUri).then()
				.statusCode(200)
				.body(containsString("lei"))
				.body(containsString("swiftCode"))
				.body(containsString("cusip"))
				.body(containsString("dowJonesTicker"))
				.body(containsString("identifiers"))
				.body(containsString("analysts"))
				.body(containsString("HZSN7FQBPO5IEWYIGC72"))
				.body(containsString("57163R"))
				.body(containsString("516358"))
				.body(containsString("GDLA"))
				.body(containsString("CTYCUS44"))
				.body(containsString("116980"))			
				.extract().response();

		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
		
		
		String AndFilterUri = baseURI +"/v1/entities?filter[lei]=HZSN7FQBPO5IEWYIGC72&filter[cusip]=57163R,516358&filter[dowJonesTicker]"
				+ "=GDLA&filter[swiftCode]=CTYCUS44&filter[fitchEntityId]=116980&filter[countryISOCode]=NZ";	
		
		Response res2 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(AndFilterUri).then()
				.statusCode(200)
				.body(containsString("NZ"))
				.extract().response();		
		Assert.assertFalse(res2.asString().contains("United States"));
		Assert.assertFalse(res2.asString().contains("isMissing"));
		Assert.assertFalse(res2.asString().contains("isRestricted"));
		
		// Needs to Automate Filter options
	}
		
	@Test(enabled=false)
	public void FISC_1443_IssUes_otherDebt() {

		String transctionURi = baseURI + "/v1/transactions/96247915/otherDebt";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(transctionURi).then()
				.statusCode(200)
				.body(containsString("96258744"))
				.body(containsString("96258738"))
				.body(containsString("96258740"))
				.body(containsString("96258742"))
				.body(containsString("issues"))
				.body(containsString("GO CP notes ser A (liquidity facility: Wells Fargo Bank, N.A.)"))				
				.extract().response();

		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		
		String NoOtherDebtURi = baseURI + "/v1/transactions/96250107/otherDebt";

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(NoOtherDebtURi).then()
				.statusCode(404)
				.extract().response();

	}

	@Test
	public void FISC_1498() {

		String entityResrceURI = baseURI + "/v1/entities/1202816";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityResrceURI).then()
				.statusCode(200).body(containsString("marketSectors")).body(containsString("description"))
				.body(containsString("id")).body(containsString("primary")).body(containsString("relationships"))
				.body(containsString("Mutual Savings Bank"
						+ "")).body(containsString("included")).extract().response();

		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	   }
	

				
				
		
	}
	
	
	
	

	

