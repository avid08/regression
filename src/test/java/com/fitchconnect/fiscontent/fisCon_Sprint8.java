package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint8 extends Configuration {

	@Test
	public void FISC_606_attributefilter() {

		String FIRfilterUrl = baseURI
				+ "/v1/financialImpliedRatings/?filter[fitchEntityId]=1143152,1003922,1000469,1040027&filter[rating]=c&filter[stmntDate]=2014-12-31&filter[stmntDateRank]=2";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(FIRfilterUrl).then().statusCode(200).body(containsString("1003922"))
				.body(containsString("1000469")).body(containsString("2014-12-31")).body(containsString("2")).extract()
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
	public void FISC_923_IssueService() {

		String getallissueUrl = baseURI + "/v1/issues";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(getallissueUrl).then().statusCode(200).body(containsString("country"))
				.body(containsString("transactionSecurityID")).body(containsString("cusip"))
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

		Response res1 = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(getallissueUrl).then().statusCode(200).body(containsString("country"))
				.body(containsString("transactionSecurityID")).body(containsString("cusip"))
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
	
	@Test
	
	public void FISC_608_FIR_DataAggregator ()throws IOException {
		      
		    URL file = Resources.getResource("FISC_608.json");
			myjson = Resources.toString(file, Charsets.UTF_8);

			Response res = given()

					.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).contentType("application/vnd.api+json").body(myjson).with().when()
					.post(dataPostUrl).then().statusCode(201)
					.body(containsString("value"))
					.body(containsString("type"))
					.body(containsString("8638461"))
					.body(containsString("timeIntervalPeriod"))
					.body(containsString("FC_NOTCH_DIFF_CF_DM_FIR"))							
					.extract().response();
			
			Assert.assertFalse(res.asString().contains("isError"));
			Assert.assertFalse(res.asString().contains("isMissing"));
			Assert.assertFalse(res.asString().contains("isRestricted"));
			
		
	}
	
	
 @Test 
 
 public void FISC_608_FIR_categories () {
	 
	 String FIRcategoriesUrl = baseURI+"/v1/metadata/categories/260";
	 

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(FIRcategoriesUrl).then().statusCode(200)
				.body(containsString("Financial Implied Rating"))
				.extract().response();
		
		String predictions = res.path("data.relationship.children.data[0].id");
				
		String model = res.path("data.relationship.children.data[1].id");
				
		
		Response res1 = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(predictions).then().statusCode(200)
				.body(containsString("Predictions"))
				.body(containsString("FC_LOAN_QUAL_FIR"))
				.body(containsString("FC_PERIOD_DT_RANK_FIR"))
				.body(containsString("FC_FIR"))
				.body(containsString("FC_COUNTRY_RISK_IND_FIR"))
				.body(containsString("FC_PROFIT_FIR"))
				.body(containsString("FC_PERIOD_DT_FIR"))
				.body(containsString("FC_REGION_FIR"))
				.body(containsString("FC_TOTAL_ASSETS_FIR"))
				.body(containsString("FC_BAND_RANK_FIR"))
				.body(containsString("FC_MODEL_SCORE_FIR"))
				.body(containsString("FC_STATEMENT_ID_FIR"))			
				.extract().response();
		
		
		
		Response res2 = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(model).then().statusCode(200)
				.body(containsString("Model"))
				.body(containsString("FC_PROFIT_COEFF_FIR"))
				.body(containsString("FC_LOAN_QUAL_CUTOFF_LOW_FIR"))
				.body(containsString("FC_NOTCH_DIFF_EM_FIR"))
				.body(containsString("FC_INTERCEPTS_NO_FIR"))
				.body(containsString("FC_TOTAL_ASSETS_NORM_MEAN_FIR"))
				.body(containsString("FC_NOTCH_DIFF_ALL_FIR"))
				.body(containsString("FC_PROFIT_CUTOFF_HIGH_FIR"))
				.body(containsString("FC_ENTITIES_NOTCH_DIFF_EM_FIR"))
				.extract().response();
				
				                   
		Assert.assertFalse(res2.asString().contains("isError"));
		Assert.assertFalse(res2.asString().contains("isMissing"));
		Assert.assertFalse(res2.asString().contains("isRestricted"));
	 
  }
	

}
