package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;


public class Sprint31 extends Configuration {
	
	

	
   @Test
   public void caTegories_1542 () {
	
	String categrisUrl = baseURI + "/v1/metadata/categories";

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(categrisUrl).then()
			.body(containsString("Entity Reference"))
			.body(containsString("Financials"))
			.body(containsString("Fitch Ratings"))
			.body(containsString("Moody's Issuer Rating"))
			.body(containsString("Sovereign Data"))
			//.body(containsString("Registered Office Address"))
			//.body(containsString("Head Office Address"))
			//.body(containsString("Identifiers"))
			//.body(containsString("Industry Classification"))
			.extract().response();
	
	
	
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));


	List <String>self = res.path("data.links.self");
	
	for(int i =0;i<self.size();i++){
	
	Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(self.get(i)).then()
			.body(containsString("relationships"))			
			.body(containsString("links"))
			.extract().response();
	
	Assert.assertFalse(res1.asString().contains("isError"));
	Assert.assertFalse(res1.asString().contains("isMissing"));
	Assert.assertFalse(res1.asString().contains("isRestricted"));

	}

	

  }

 @Test
 
 public void Fca_1489 (){
	   
	     String compnyIdUrl = baseURI+"/v1/entities/107477";

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(compnyIdUrl).then()
			    .body("data.relationships.company.data.id",equalTo("107477"))
			    .extract().response();
		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
				
	 
   }
 
 @Test
  
 public void fca_1561() {
 
 String filterUrl = baseURI+"/v1/metadata/fields?filter[name]=country&filter[source]=ratings";

	Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(filterUrl).then()
			.body(containsString("ratings"))			
			.extract().response();
	
	Assert.assertFalse(res1.asString().contains("isError"));
	Assert.assertFalse(res1.asString().contains("isMissing"));
	Assert.assertFalse(res1.asString().contains("isRestricted"));
			
 }
 

}



