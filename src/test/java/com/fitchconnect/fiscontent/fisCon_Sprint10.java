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

public class fisCon_Sprint10 extends Configuration {

	@Test

	public void fisc_1006_otherDebtRelation() {

		String transctnUri = baseURI + "/v1/transactions/96247516";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(transctnUri)
				.then().assertThat().statusCode(200).body(containsString("otherDebt")).body(containsString("transactions"))

				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		String otherDebtRelation = res.path("data.relationships.otherDebt.links.related");
		
		System.out.println(otherDebtRelation);

		Response res1 = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(otherDebtRelation)
				.then().assertThat().statusCode(200)
				.body(containsString("issues"))
				.body(containsString("United States"))
				.extract()
				.response();
		
		int totalCount = res1.path("meta.totalResourceCount");
		
	System.out.println(totalCount);
	
	if (totalCount>=9) {
		
	System.out.print("total count of issues working as expected");
	}else{
		
		System.err.print("total count of issues NOT working as expected");
		
	}
	

}

	@Test
	public void FisC_1023_IssuerRating_AllGroupTypes() throws IOException {

		String endpoint1 = baseURI + "/v1/entities/110631/fitchIssuerRatings";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(endpoint1).then()
				.assertThat().statusCode(200).body(containsString("fitchIssuerRatings")).body(containsString("issuers"))
				.body(containsString("entities"))

				.extract().response();

		// Entity with no groupType aattached

		String noGroupTypeentity = baseURI + "/v1/entities/1230070/fitchIssuerRatings";

		Response res1 = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(endpoint1).then()
				.assertThat().statusCode(200).body(containsString("data")).extract().response();

	}

	@Test

	public void fisc_1024() throws IOException {

		URL file = Resources.getResource("fisc_1024.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl).then().assertThat().statusCode(200).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test

	public void fisc_1033_transCtion_relatedResource() {

		String transctnUri = baseURI + "/v1/transactions/96250107";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(transctnUri)
				.then().assertThat().statusCode(200).body(containsString("relatedCredit"))

				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		String relateCreditlink = res.path("data.relationships.relatedCredit.links.related");

		Response res1 = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(relateCreditlink)
				.then().assertThat().statusCode(200).body(containsString("96250128"))
				.body(containsString("transactions")).body(containsString("agentStateName"))

				.extract().response();

		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));

	}

	@Test

	public void fisc_333_XrefcountryCD() throws IOException {

		URL file = Resources.getResource("fisc_333.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body(containsString("1001603")).body(containsString("1474671"))
				.body(containsString("2015")).body(containsString("value")).body(containsString("Annual")).extract()
				.response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	@Test
	
	public void fisc_942_transactionResource(){
		
		
		String getAlltransctnUri = baseURI +"/v1/transactions";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept",acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(getAlltransctnUri)
				.then().assertThat().statusCode(200)
				.body(containsString("id"))
				.body(containsString("type"))
				.body(containsString("transactions"))
				.body(containsString("name"))
				.body(containsString("description"))
				.body(containsString("agentStateName"))
				.body(containsString("analysts"))
				.body(containsString("groupCode"))
				.body(containsString("name"))
				.body(containsString("role"))
				.body(containsString("keyRatingFactors"))
				.body(containsString("marketSectors"))
				.body(containsString("primary"))
				.body(containsString(""))
				.body(containsString("transactionAgent"))
				.body(containsString("entities"))
				.body(containsString("relatedCredit"))
				.body(containsString("issuers"))
				.body(containsString("otherDebt"))
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		
		String singletransctionId = res.path("data[0].id");
		String SingletransctionURl =getAlltransctnUri+"/"+singletransctionId;
		
	System.out.println("single transaction id Url  " +SingletransctionURl);	


	Response res1 = given().header("Authorization", AuthrztionValue).header("content", contentValue)
			.header("'Accept",acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(SingletransctionURl)
			.then().assertThat().statusCode(200)
			.body(containsString("id"))
			.body(containsString("type"))
			.body(containsString("transactions"))
			.body(containsString("name"))
			.body(containsString("description"))
			.body(containsString("agentStateName"))
			.body(containsString("analysts"))
			.body(containsString("groupCode"))
			.body(containsString("name"))
			.body(containsString("role"))
			.body(containsString("keyRatingFactors"))
			.body(containsString("marketSectors"))
			.body(containsString("primary"))
			.body(containsString(""))
			.body(containsString("transactionAgent"))
			.body(containsString("entities"))
			.body(containsString("relatedCredit"))
			.body(containsString("issuers"))
			.body(containsString("otherDebt"))
			.extract().response();
	
	Assert.assertFalse(res1.asString().contains("isError"));
	Assert.assertFalse(res1.asString().contains("isMissing"));
	Assert.assertFalse(res1.asString().contains("isRestricted"));
		
		
	}
		
	@Test 
	
	public void fisc_943_TrasnctionRating(){
		
		String transCtionRatingURl = baseURI +"/v1/transactions/96249968/transactionRatings?filter[action]=New Rating,Downgrade&filter[startDate]=2016-04-25&filter[endDate]=2016-10-08";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept",acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(transCtionRatingURl)
				.then().assertThat().statusCode(200)
				.body(containsString("alert"))
				.body(containsString("rating"))
				.body(containsString("description"))
				.body(containsString("effectiveDate"))				
				.body(containsString("2016-04-25"))
				.body(containsString("Rating Outlook Stable"))				
				.body(containsString("transactionRatings"))
				.body(containsString("Downgrade"))
				.body(containsString("first"))
				.body(containsString("next"))
				.body(containsString("last"))				
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
	    String transctionResource = res.path("data[0].relationships.transaction.links.related");
	
	    
	    System.out.println(transctionResource);
	    
	    
		Response res1 = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept",acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(transctionResource)
				.then().assertThat().statusCode(200)
				.body(containsString("analysts"))
				.body(containsString("keyRatingFactors"))
				.body(containsString("marketSectors"))				
				.extract().response();
		
		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
	}

	
	@Test
	
	public void FISC_991_issuerRelation_to_Transction(){
		
		String transctnUri = baseURI +"/v1/transactions/96250107";
		
		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept",acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(transctnUri)
				.then().assertThat().statusCode(200)
				.extract().response();
		
		String issuerlink = res.path("data.relationships.issuers.links.related");
		
		Response res1 = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept",acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(issuerlink)
				.then().assertThat().statusCode(200)
				.extract().response();
		
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
	}
	
}
