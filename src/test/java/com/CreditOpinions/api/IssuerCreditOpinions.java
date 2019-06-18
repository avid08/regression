package com.CreditOpinions.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.collect.Ordering;
import com.jayway.restassured.response.Response;

public class IssuerCreditOpinions extends Configuration{
	
	
@Test
 
public void FISC_5890_aLLIssuerCreditOpinions() {
	
	String IssuerCreditOpnionUri = baseURI +"/v1/issuerCreditOpinions";

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(IssuerCreditOpnionUri).then().statusCode(200)
			.body(containsString("issuerCreditOpinions"))
			.body(containsString("secondaryDescription"))
			.body(containsString("effectiveDateTime"))	
			.body(containsString("creditOpinionType"))	
			.body(containsString("description"))
			.body(containsString("action"))	
			.body(containsString("creditOpinion"))	
			.body(containsString("issuer"))	
			.body(containsString("entity"))	
			.body(containsString("links"))				
			.extract().response();		
	
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));

	}
@Test
public void FISC_5842_aLLIssuerCreditOpinions() {
	
	String IssuerCreditOpnionUri = baseURI +"/v1/issuerCreditOpinions";

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(IssuerCreditOpnionUri).then().statusCode(200)
			.body(containsString("issuerCreditOpinions"))
			.body(containsString("secondaryDescription"))
			.body(containsString("effectiveDateTime"))	
			.body(containsString("creditOpinionType"))	
			.body(containsString("description"))
			.body(containsString("action"))	
			.body(containsString("creditOpinion"))	
			.body(containsString("issuer"))	
			.body(containsString("entity"))	
			.body(containsString("links"))				
			.extract().response();		
	// entity relationship
	 String entityRelationShip = res.path("data[0].relationships.entity.links.related");	 
	 System.out.println(entityRelationShip);
	 //issuer relationship
	 String IssuerRelationshipLink = res.path("data[0].relationships.issuer.links.related");	 
	 System.out.println(IssuerRelationshipLink);
	 
	 
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
		
	Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(entityRelationShip).then().statusCode(200)
			.body(containsString("entities"))
			.body(containsString("countryISOCode"))	
			.body(containsString("included"))	
			.extract().response();		
	
	}

@Test

public void FISC_5843_Filter_Issuer() {
	
	String IssuerCreditOpnionUri = baseURI +"/v1/issuerCreditOpinions?filter[FC_ISSUER_ID]=96683626";

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(IssuerCreditOpnionUri).then().statusCode(200)
			.body(containsString("Public CO / Public Data"))
			.body(containsString("1500345"))			
			.extract().response();		
	
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));


 }
@Test

public void FISC_5843_Filter_Entity() {
	
	String IssuerCreditOpnionUri = baseURI +"/v1/issuerCreditOpinions?filter[FC_ISSUER_ID]=96683626";

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(IssuerCreditOpnionUri).then().statusCode(200)
			.body(containsString("Public CO / Public Data"))
			.body(containsString("b+*"))			
			.extract().response();		
	
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));


 }
@Test
public void FISC_5843_Sorting_descending() {
	
	String IssuerCreditOpnionUri = baseURI +"/v1/issuerCreditOpinions?sort=-effectiveDateTime";
	
	System.out.println(IssuerCreditOpnionUri);

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(IssuerCreditOpnionUri).then().statusCode(200)
			.body(containsString("effectiveDateTime"))
			.body(containsString("action"))	
			.extract().response();
	
	 List<String> effectDatetimelist = res.path("data.attributes.effectiveDateTime"); 
	 
	 System.out.println(effectDatetimelist);
	//descending 
	boolean sorted = Ordering.natural().reverse().isOrdered(effectDatetimelist);
	 Assert.assertTrue(sorted);	 

	
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));


 }


@Test
public void FISC_5843_Sorting_Ascending() {
	
	String IssuerCreditOpnionUri = baseURI +"/v1/issuerCreditOpinions?sort=effectiveDateTime";
	
	System.out.println(IssuerCreditOpnionUri);

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(IssuerCreditOpnionUri).then().statusCode(200)
			.body(containsString("effectiveDateTime"))
			.body(containsString("action"))	
			.extract().response();
	
	 List<String> effectDatetimelist = res.path("data.attributes.effectiveDateTime"); 
	 
	 System.out.println(effectDatetimelist);
	//acensending
	boolean sorted = Ordering.natural().isOrdered(effectDatetimelist);
	 Assert.assertTrue(sorted);	 

	
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));


 }

}



