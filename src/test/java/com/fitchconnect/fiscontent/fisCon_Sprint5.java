package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint5 extends Configuration {
	
	
	@Test
	 
	 public void Fisc_430() throws IOException {
		 
		 URL file = Resources.getResource("fisc_430.json");
			String myRequest = Resources.toString(file, Charsets.UTF_8);
	
			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
					.with()
					.when().post(dataPostUrl)
					.then().assertThat().statusCode(200)
					.body(containsString("value"))
					.body(containsString("periodResolution"))
					.body(containsString("con"))
					.body(containsString("USGAAP"))	
					.body(containsString("Annual"))					
					.extract().response();
		  
			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));
			Assert.assertFalse(response.asString().contains("isRestricted"));
		 	 
	   }


 @Test
 public void Fisc_532() throws IOException {
	 
	 URL file = Resources.getResource("fisc_532.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response response = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("value"))
				.body(containsString("Affirmed"))
				.body(containsString("Rating Outlook Positive"))
				.body(containsString("timeIntervalDate"))						
				.extract().response();
	  
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
	 	 
   }
	
  @Test 
  public void fisc_560() {
 
 String entityUrl = baseURI +"/v1/entities/1421467/fitchIssuerRatings?filter[groupId]=93129690";
		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(entityUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("ratingType"))
				.body(containsString("rating"))
				.body(containsString("description"))
				.body(containsString("fitchIssuerRatings"))
				.body(containsString("relationships"))				
				.body(containsString("links"))
				.body(containsString("entity"))				
				.body(containsString("effectiveDate"))				
						
				.extract().response();
	  
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
  }
  
  @Test
 public void Fisc_563() throws IOException {
	 
	 String entityUrl = baseURI +"/v1/metadata/fields/FC_SPONSOR_SPV/categories";
	 
		Response response = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(entityUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("FC_SPONSOR_SPV"))
				.body(containsString("fields"))			
				.extract().response();
	 	  
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		
		// data aggregator value 
		 
		 URL file = Resources.getResource("fisc_771.json");
			String myRequest = Resources.toString(file, Charsets.UTF_8);

			Response res = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
					.with()
					.when().post(dataPostUrl)
					.then().assertThat().statusCode(200)
					.body(containsString(""))
					.extract().response();
			
			Assert.assertFalse(res.asString().contains("isError"));
			Assert.assertFalse(res.asString().contains("isMissing"));
			Assert.assertFalse(res.asString().contains("isRestricted"));

	 	 
   }
  
  @Test
  
  public void fisc_564_entity_to_issuer() {
	    
	  
	  String entityUrl = baseURI +"/v1/entities/117954";
		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(entityUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("entities"))
				.body(containsString("address1"))
				.body(containsString("countryName"))
				.body(containsString("relationships"))
				.body(containsString("issuers"))
				.body(containsString("included"))				
				.extract().response();
	  
		Assert.assertFalse(res.asString().contains("isError"));
		
		
		String issuerlink = res.path("data.relationships.issuers.links.related");
		
	    System.out.println(issuerlink);
	    
		Response res1 = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(issuerlink)
				.then().assertThat().statusCode(200)
				.body(containsString("issuers"))
				.body(containsString("typeDesc"))
				.body(containsString("typeId"))
				.body(containsString("entity"))
				.body(containsString("relationships"))
							
				.extract().response();
	    
		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));

  }
	
  
  
 @Test // Not working
 
 public void FISC_587() throws IOException{
	 
	 
	 URL file = Resources.getResource("Fisc_587.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("value"))
				.body(containsString("FC_ESTR_ACTN"))
				.body(containsString("FC_ELTR_DT"))
				.body(containsString("text"))
				
				.extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

	 
   }
  
 
 @ Test
  
  public void Fisc_593(){
	  
	  String DirectrUrl = baseURI +"/v1/entities/101691/fitchIssuerRatings?include[fitchIssuerRatings]=issuer";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(DirectrUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("fitchIssuerRatings"))
				.body(containsString("alert"))
				.body(containsString("solicitation"))
				.body(containsString("ratingType"))
				.body(containsString("description"))
				.body(containsString("issuer"))
				.body(containsString("links"))				
				.body(containsString("included"))				
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		String issuerLink = res.path("data[0].relationships.issuer.links.related");
		
		Response res1 = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(issuerLink)
				.then().assertThat().statusCode(200)
				.body(containsString("typeDesc"))
				.body(containsString("name"))
				.body(containsString("typeId"))
				.body(containsString("fitchIssuerRatings"))
				.body(containsString("entities"))
				.extract().response();
		
		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));

	
  }
 
 @Test
  
 public void Fisc_618() throws IOException {
	 
	 URL file = Resources.getResource("fisc_618.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response response = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("value"))
				.body(containsString("Wells Fargo Bank, N.A."))
				.body(containsString("AA-"))
				.body(containsString("CPS1-"))				
				.extract().response();
	  
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
	 	 
   }
 
 @Test
public void FisC_620_ConvrdBondFlag() throws IOException {

	URL file = Resources.getResource("FISC_620_CovrdBondFlag.json");
	String myRequest = Resources.toString(file, Charsets.UTF_8);

	Response fieldResponse = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
			.with()
			.when().post(dataPostUrl)
			.then().assertThat().statusCode(200)
			.body(containsString("value"))
			.body(containsString("FC_CVB_FLAG"))
			.body(containsString("false"))
			.body(containsString("true"))
			.body("data.attributes.entities[0].values[0].values[0].value[0]", equalTo("true"))
			.body("data.attributes.entities[3].values[0].values[0].value[0]", equalTo("false"))
			.extract().response();

	Assert.assertFalse(fieldResponse.asString().contains("isError"));
	Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
	Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

}
 @Test
  
  public void Fisc_760() throws IOException {
	  
	  URL file = Resources.getResource("fisc_760.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response response = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("value"))
				.body(containsString("timeIntervalDate"))
				.body(containsString("UNITED STATES"))
				.body(containsString("text"))
				.extract().response();
	  
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
    }
 
 
 public void Fisc_765() throws IOException {
	  
	  URL file = Resources.getResource("fisc_765.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response response = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("value"))
				.body(containsString("AUSTRALIA"))
				.body(containsString("Heritage Bank Limited"))
				.body(containsString("Australia/Oceania"))
				.body(containsString("CAYMAN ISLANDS"))
				.body(containsString("Caribbean"))				
				.extract().response();
	  
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
    }
 
 @Test
 public void Fisc_771() throws IOException {
	 
	 String entityUrl = baseURI +"/v1/issuers?filter[typeId]=3&r[typeDesc]=RMBS Issuing Entity";
	 
		Response response = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(entityUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("typeDesc"))
				.body(containsString("name"))				
				.body(containsString("typeId"))
				.body(containsString("relationships"))
				.body(containsString("entity"))
				.extract().response();
	 	  
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
	 	 
   }
 
  
}
