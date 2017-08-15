package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint7 extends Configuration {
	
@Test

public void fisc_870 (){
	
	String url = baseURI + "/v1/entities/14528"; //entity with multiple groupIds resolving for GroupType 4
	String url1 = baseURI + "/v1/entities/1092290";

	Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
			.get(url).then()
			.statusCode(200)
			.body(containsString("fitchConnectUrl"))
			.body(containsString("GRP_80089022"))
			.body(containsString("relationships"))
			.body(containsString("included"))
			.extract().response();
	
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
	
	Response res1 = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
			.get(url1).then()
			.statusCode(200)			
			.body(containsString("relationships"))
			.body(containsString("included"))
			.extract().response();
	Assert.assertFalse(res1.asString().contains("fitchConnectUrl"));
	Assert.assertFalse(res1.asString().contains("isError"));
	Assert.assertFalse(res1.asString().contains("isMissing"));
	Assert.assertFalse(res1.asString().contains("isRestricted"));
	
  }

@Test
public void Fisc_871_NoFConnectURL() throws IOException {

	URL file = Resources.getResource("fisc_nofcConnectURl.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

			.when().post(dataPostUrl)
			.then().assertThat().statusCode(200).extract().response();
	
	Assert.assertFalse(responsedata.asString().contains("fitchconnect.com/entity/GRP_80089022"));
	Assert.assertFalse(responsedata.asString().contains("isError"));
	Assert.assertFalse(responsedata.asString().contains("isRestricted"));
	Assert.assertFalse(responsedata.asString().contains("isMissing"));
	
  }

@Test
public void Fisc_871_FConnectURL() throws IOException {

	URL file = Resources.getResource("fisc_fcConnectURl.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

			.when().post(dataPostUrl)
			.then().assertThat().statusCode(200).extract().response();	
	
	Assert.assertTrue(responsedata.asString().contains("fitchconnect.com/entity/GRP_"));
	Assert.assertTrue(responsedata.asString().contains("https://"));
	Assert.assertFalse(responsedata.asString().contains("isError"));
	Assert.assertFalse(responsedata.asString().contains("isRestricted"));
	Assert.assertFalse(responsedata.asString().contains("isMissing"));

 }

@Test(enabled=false)
public void FISC_869_FIR_Amendments() {

	String getAllurl = baseURI + "/v1/financialImpliedRatings";

	Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
			.get(getAllurl).then().statusCode(200).body(containsString("financialImpliedRatings"))
			.body(containsString("countryRiskIndicator")).body(containsString("region"))
			.body(containsString("rating")).body(containsString("fitchEntityId"))
			.body(containsString("totalAssets")).body(containsString("stmntDate"))
			.body(containsString("profitability")).body(containsString("loanQuality"))
			.body(containsString("bandRanking")).body(containsString("modelScore"))
			.body(containsString("stmntDateRank"))
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

   @Test (enabled=false)
   
   public void fisc_604_FIR_entityRelationship () {
	   
	 String FIRgetAll = baseURI + "/v1/financialImpliedRatings";
	   
			 Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(FIRgetAll).then().statusCode(200)
				.body(containsString("statement"))
				.body(containsString("profitability"))
				.body(containsString("fitchEntityId"))
				.body(containsString("bandRanking"))
				.body(containsString("loanQuality"))
				.body(containsString("totalAssets"))
				.body(containsString("stmntDate"))
				.body(containsString("rating"))
				.body(containsString("batchDate"))
				.body(containsString("stmntDateRank"))
				.body(containsString("region"))
				.body(containsString("countryRiskIndicator"))
				.body(containsString("modelScore"))
				
				
				.extract().response();
			 		 
				Assert.assertFalse(res.asString().contains("isError"));
				Assert.assertFalse(res.asString().contains("isMissing"));
				Assert.assertFalse(res.asString().contains("isRestricted"));
				
			 String entiyRelationshiplink = res.path("data[1].relationships.entity.links.related");
			
			 Response res1 = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
						.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
						.get(entiyRelationshiplink).then().statusCode(200)
						.body(containsString("financialImpliedRatings"))
						.body(containsString("surveillanceDeals"))
						.body(containsString("officers"))
						.body(containsString("statements"))
						.extract().response();
			 
				Assert.assertFalse(res1.asString().contains("isError"));
				Assert.assertFalse(res1.asString().contains("isMissing"));
				Assert.assertFalse(res1.asString().contains("isRestricted"));
		
		System.out.println(entiyRelationshiplink);
	   
   }
   
  @Test (enabled=false)
   
   public void fisc_605_FIR_statementRelation () {
	   
	 String FIRgetAll = baseURI + "/v1/financialImpliedRatings";
	   
			 Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(FIRgetAll).then().statusCode(200)
				.body(containsString("statement"))
				.extract().response();
				Assert.assertFalse(res.asString().contains("isError"));
				Assert.assertFalse(res.asString().contains("isMissing"));
				Assert.assertFalse(res.asString().contains("isRestricted"));
				
				String statementrelation = res.path("data[1].relationships.statement.links.related");	
				
				System.out.println("Statement link" + statementrelation );
	 
				Response res1 = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
						.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
						.get(FIRgetAll).then().statusCode(200)
						.body(containsString("statements"))
						.body(containsString("accountingStandard"))
						.body(containsString("filingType"))
						
						.body(containsString("detail"))
						.body(containsString("fitchFieldType"))
						
						.body(containsString("periodType"))					
						
						.extract().response();
					
   
  }
   
  @Test(enabled=false)
  
  public void FISC_869 () {
	  
	  
	  String FIRgetAll = baseURI + "/v1/financialImpliedRatings";
	   
		 Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
			.get(FIRgetAll).then().statusCode(200)
			.body(containsString("profitability"))
			.body(containsString("stmntDateRank"))
			.body(containsString("loanQuality"))
			.extract().response();
	  
    }
 
}
