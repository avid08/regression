package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint4 extends Configuration {

	String SurVillanceId;

	@Test

	public void FISC_589_entityRanking_Resource() {

		String entityRanking = baseURI + "/v1/entityRankings";
		// + "/58acd660e4b0cb81582462a4?include[entityRankings]=entity";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityRanking).then()
				.statusCode(200).body(containsString("entityRankings")).body(containsString("countryISOCode3"))
				.body(containsString("globalRankChange")).body(containsString("relationships"))
				.body(containsString("rankType"))

				.extract().response();

		List<String> id = response.path("data.id");

		System.out.println(id.size());

		for (int i = 0; i < 10; i++) {

			String entityRankingResourceURI = entityRanking + "/" + id.get(i) + "?include[entityRankings]=entity";

			// System.out.println(entityRankingResourceURI);

			Response response2 = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(entityRankingResourceURI).then().statusCode(200)

					.body(containsString("countryISOCode3")).body(containsString("rankType"))
					.body(containsString("globalRankChange")).body(containsString("included"))
					.body(containsString("attributes")).body(containsString("name")).body(containsString("state"))
					.body(containsString("countryName")).body(containsString("city")).extract().response();

			Assert.assertFalse(response2.asString().contains("isError"));
			Assert.assertFalse(response2.asString().contains("isMissing"));

		}
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

	}

	// This test not on API end rather it's on Rating End

	/*
	 * @Test(enabled = false)
	 * 
	 * public void Create_A_SurveillanceDeal () throws IOException {
	 * 
	 * 
	 * String CreatesurViellanceURI = baseURI + "/v1/surveillanceDeals";
	 * 
	 * 
	 * URL file = Resources.getResource("createAsurVillance.json"); String
	 * myJson = Resources.toString(file, Charsets.UTF_8);
	 * 
	 * Response response = given().header("Authorization",
	 * AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
	 * .contentType(contentValue).body(myJson).with()
	 * .when().post(CreatesurViellanceURI) .then().assertThat().statusCode(201)
	 * .body(containsString("")) .body(containsString(""))
	 * .body(containsString(""))
	 * 
	 * .extract().response();
	 * 
	 * this.SurVillanceId=response.path("");
	 * 
	 * 
	 * Assert.assertFalse(response.asString().contains("isError"));
	 * Assert.assertFalse(response.asString().contains("isMissing"));
	 * Assert.assertFalse(response.asString().contains("isRestricted"));
	 * 
	 * }
	 */

	@Test

	public void get_A_SurveillanceDeal() {

		// write a test case with Support Param

		String allsurViellanceURI = baseURI + "/v1/surveillanceDeals";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(allsurViellanceURI).then()
				.statusCode(200).extract().response();

		String singleSurvillanceID = response.path("data[0].id");

		String AsurveillanceDealUri = allsurViellanceURI + "/" + singleSurvillanceID
				+ "?include[surveillanceDeals]=surveillanceReport";

		Response response2 = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(AsurveillanceDealUri).then().statusCode(200)
				.body(containsString("dealId")).body(containsString("fitchGroupId"))
				.body(containsString("surveillanceType")).body(containsString("assetType"))
				.body(containsString("included")).body(containsString("assetType")).body(containsString("included"))
				.body(containsString("surveillanceReports")).body(containsString("fileUploadDateTime"))
				.body(containsString("fileMimeType"))

				.extract().response();

		Assert.assertFalse(response2.asString().contains("isError"));
		Assert.assertFalse(response2.asString().contains("isMissing"));
		Assert.assertFalse(response2.asString().contains("isRestricted"));

	}

	/*
	 * @Test
	 * 
	 * public void Update_A_SurvillanceDeal () throws IOException {
	 * 
	 * 
	 * String UpdatesurViellanceURI = baseURI +
	 * "/v1/surveillanceDeals/"+SurVillanceId;
	 * 
	 * 
	 * URL file = Resources.getResource("createAsurVillance.json"); String
	 * myJson = Resources.toString(file, Charsets.UTF_8);
	 * 
	 * Response response = given().header("Authorization",
	 * AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
	 * .contentType(contentValue).body(myJson).with()
	 * .when().patch(UpdatesurViellanceURI) .then().assertThat().statusCode(201)
	 * .body(containsString("")) .body(containsString(""))
	 * .body(containsString("")) .extract().response();
	 * 
	 * Assert.assertFalse(response.asString().contains("isError"));
	 * Assert.assertFalse(response.asString().contains("isMissing"));
	 * Assert.assertFalse(response.asString().contains("isRestricted"));
	 * 
	 * }
	 */

	@Test

	public void GetA_surVillanceReport() {

		String SurVillnceReportURI = baseURI + "/v1/surveillanceReports";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(SurVillnceReportURI).then()
				.statusCode(200).body(containsString("type")).body(containsString("fileName"))
				.body(containsString("fileMimeType")).body(containsString("download"))
				.body(containsString("surveillanceDeal")).body(containsString("download")).body(containsString("id"))
				.extract().response();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

		String survillnaceReportId = response.path("data[0].id");

		System.out.println(survillnaceReportId);

		String A_SurVillnceReportUri = baseURI + "/v1/surveillanceReports" + "/" + survillnaceReportId;

		Response response2 = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(A_SurVillnceReportUri).then().statusCode(200)
				.body(containsString("type")).body(containsString("fileName")).body(containsString("fileMimeType"))
				.body(containsString("download")).body(containsString("surveillanceDeals"))
				.body(containsString("relationships")).body(containsString("links"))

				.extract().response();

		Assert.assertFalse(response2.asString().contains("isError"));
		Assert.assertFalse(response2.asString().contains("isMissing"));
		Assert.assertFalse(response2.asString().contains("isRestricted"));

	}

	// SURVILLANCE REPORT TEST CASES

	@Test

	public void getAll_SurveillanceDeals() {

		// Include filter params in the test to make sure the filters are
		// working

		String allsurViellanceURI = baseURI + "/v1/surveillanceDeals";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(allsurViellanceURI).then()
				.statusCode(200).body(containsString("name")).body(containsString("surveillanceType"))
				.body(containsString("assetType")).body(containsString("fitchGroupId"))
				.body(containsString("marketSector")).body(containsString("entities")).body(containsString("links"))
				.body(containsString("id"))

				.extract().response();

		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

		String SurVillaneReportLink = response.path("data[0].relationships.surveillanceReport.links.related");

		System.out.println("survillanceReport " + SurVillaneReportLink);

		Response response2 = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(SurVillaneReportLink).then().statusCode(200)
				.body(containsString("fileName")).body(containsString("fileMimeType"))
				.body(containsString("surveillanceDeal")).body(containsString("fileName"))

				.extract().response();

		Assert.assertFalse(response2.asString().contains("isMissing"));
		Assert.assertFalse(response2.asString().contains("isMissing"));
		Assert.assertFalse(response2.asString().contains("isRestricted"));

	}

	@Test

	public void getAll_SurVillnceReports() {

		// write a test case with Filter ( page number and Page Size)

		String getAllSurVillnceReportURI = baseURI +"/v1/surveillanceReports";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(getAllSurVillnceReportURI)
				.then().statusCode(200).body(containsString("type")).body(containsString("fileName"))
				.body(containsString("surveillanceReports")).body(containsString("relationships"))
				.body(containsString("surveillanceDeal")).body(containsString("self")).body(containsString("download"))
				.extract().response();

		System.out.println(response.asString());

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

		List<String> SurVillnceIds = response.path("data.id");

		System.out.println(SurVillnceIds.size());

		for (int i= 8; i <20; i++) {

			String getASurvillanceReport = baseURI + "/v1/surveillanceReports/" + SurVillnceIds.get(i);

			System.out.println(getASurvillanceReport);

			Response response2 = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(getASurvillanceReport).then().statusCode(200)
					.body(containsString("download")).extract().response();

			String downloadLink = response2.path("data.links.download");

			System.out.println(downloadLink);

			String xvlue = "*/*";

			Response response3 = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", xvlue).when().get(downloadLink).then()
					.statusCode(200).extract().response();

			System.out.println(response3);

		}

	}

	/*
	 * @Test
	 * 
	 * public void Create_SurvillanceReport() throws IOException{
	 * 
	 * String CreatesurViellanceURI = baseURI + "/v1/surveillanceReports";
	 * 
	 * 
	 * URL file = Resources.getResource("createAsurVillanceReport.json"); String
	 * myJson = Resources.toString(file, Charsets.UTF_8);
	 * 
	 * Response response = given().header("Authorization",
	 * AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
	 * .contentType(contentValue).body(myJson).with()
	 * .when().post(CreatesurViellanceURI) .then().assertThat().statusCode(201)
	 * .body(containsString("")) .body(containsString(""))
	 * .body(containsString(""))
	 * 
	 * .extract().response();
	 * 
	 * 
	 * }
	 */

	@Test 
  
    public void survillanceToEntityRelationship () { 
	  
	  
	  String allsurViellanceURI = baseURI + "/v1/surveillanceDeals";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(allsurViellanceURI).then()
				.statusCode(200)
				.extract().response();
		
		
	  List	<String> singleSurvillanceLink =response.path("data.relationships.entity.links.related");
		System.out.println(singleSurvillanceLink.size());
	  
	  for (int i=0; i>singleSurvillanceLink.size();i++){
		  
		  
			Response response2 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).when().get(singleSurvillanceLink.get(i)).then()
					.statusCode(200)
					.extract().response();
			

			Assert.assertFalse(response2.asString().contains("isError"));
			Assert.assertFalse(response2.asString().contains("isMissing"));
			Assert.assertFalse(response2.asString().contains("isRestricted"));
		  
	  }



}}
