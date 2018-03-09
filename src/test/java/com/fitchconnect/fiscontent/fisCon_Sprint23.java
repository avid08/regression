package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint23 extends Configuration {
	@Test
	public void FISC_2175_busnesTemplteAttirbute() {

		String stmentURI = baseURI + "/v1/entities/1036505/statements";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(stmentURI).then()
				.assertThat().statusCode(200).body(containsString("International Public Finance"))
				.body(containsString("businessTemplate")).body(containsString("statements")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FISC_2176_busnesTemplteAttirbute() {

		String stmentURI = baseURI + "/v1/nicknames/154684";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(stmentURI).then()
				.assertThat().statusCode(200).body(containsString("International Public Finance"))
				.body(containsString("businessTemplate")).body(containsString("nicknames")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FISC_2105_statementSortByChangeDate() {

		String stmentURI = baseURI + "/v1/statements?sort=changeDate";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(stmentURI).then()
				.assertThat().statusCode(200).body(containsString("statements")).body(containsString("detail"))
				.body(containsString("periodYear")).extract().response();
		List<String> id = res.path("data.id");

		System.out.println(id);

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FISC_2103_IssUerRating_AvailableWithRequired_Filter() {
        //Date Range within 90 days !
		String IssueRatingURI = baseURI
				+ "/v1/fitchIssuerRatings?filter[startDate]=2017-02-01&filter[endDate]=2017-04-30&filter[groupId]=96378583";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(IssueRatingURI).then()
				.assertThat().statusCode(200)
				.body(containsString("fitchIssuerRatings"))
				.body(containsString("Rating Outlook Stable"))
				.body(containsString("2017-04-28"))
				.body(containsString("96378583"))
				.extract().response();			
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		//Date Range more than 90 Days
		String issueRatingURIDateRange = baseURI
				+ "/v1/fitchIssuerRatings?filter[startDate]=2017-01-01&filter[endDate]=2017-04-30&filter[groupId]=96378583";

		Response resX = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(issueRatingURIDateRange).then()
				.assertThat().statusCode(400)
				.body(containsString("Date Range exceeds a 90 day restriction."))		
				.extract().response();			
		Assert.assertTrue(resX.asString().contains("errors"));

	}
	
	@Test
	public void FISC_2103_IssUerRating_ratingTypeFilter() {
        //Date Range within 90 days !
		String stmentURI = baseURI
				+ "/v1/fitchIssuerRatings?filter[startDate]=2017-04-01&filter[endDate]=2017-04-30&filter[ratingType]=FC_LT_IDR";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(stmentURI).then()
				.assertThat().statusCode(200)
				.body(containsString("fitchIssuerRatings"))
				.body(containsString("Rating Outlook Stable"))
				.body(containsString("FC_LT_IDR"))				
				.extract().response();
		
		List <String> ratingType = res.path("data.attributes.ratingType");	

		Assert.assertTrue(ratingType.contains("FC_LT_IDR"));
		
		
		for(int i=0;i<ratingType.size();i++){
			
			String arrayData = (ratingType.get(i));
			
			Assert.assertTrue(arrayData.contains("FC_LT_IDR"));
			
		}
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));		

	}
	
	@Test
	public void FISC_2228_IncludeIssuerWithIssue_otherDebt() {

		String stmentURI = baseURI + "/v1/transactions/96247915/otherDebt";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(stmentURI).then()
				.assertThat()
				.statusCode(200)
				.body(containsString("issuers"))
				.body(containsString("included"))
				.body(containsString("Rutgers State University (NJ)"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	@Test()
	public void newIssuelevelMetaData_Fisc_2001() throws IOException {

		URL file = Resources.getResource("fisc_2001.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response fieldsResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()
				.when().post(dataPostUrl)
				.then().statusCode(200)
				.body(containsString("value"))
				.body(containsString("type"))
				.body(containsString("year"))
				.body(containsString("D"))
				.extract().response();

		Assert.assertFalse(fieldsResponse.asString().contains("isError"));
		Assert.assertFalse(fieldsResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldsResponse.asString().contains("isRestricted"));
	}
	
	@Test
	public void FISC_1999_New_predefineRating_values() {

		String meTaDataURI = baseURI+"/v1/metadata/fields/FC_LT_IR";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(meTaDataURI).then()
				.assertThat()
				.statusCode(200)
				.body(containsString("predefinedValues"))
				.body(containsString("displayName"))
				.body(containsString("relationships"))
				.body(containsString("NR"))
				.body(containsString("AAA"))
				.body(containsString("AA+"))
				.body(containsString("BBB+"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	

}
