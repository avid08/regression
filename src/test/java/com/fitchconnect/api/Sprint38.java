package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint38 extends Configuration {
	
	
@Test

 public void BMI_with_EntitySummry_1774 () throws IOException {
	
	URL file = Resources.getResource("FCA_1774.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()
			
			.when().post(dataPostUrl)
			.then().statusCode(200)
			.body(containsString("currency"))
			.body(containsString("periodResolution"))
			.body(containsString("value"))
			
			.extract().response();
	Assert.assertFalse(responsedata.asString().contains("isMissing"));
	Assert.assertFalse(responsedata.asString().contains("isError"));
	Assert.assertFalse(responsedata.asString().contains("isRestricted"));
  
	 
  }

@Test
public void FCA_1805_PeriodResulation () throws IOException {
	
	URL file = Resources.getResource("FCA_1805.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()
			
			.when().post(dataPostUrl)
			.then().statusCode(200)
			.body(containsString("currency"))
			.body(containsString("periodResolution"))
			.body(containsString("value"))
			.body(containsString("Q3"))
			.body(containsString("M1"))
			
			.extract().response();
	Assert.assertFalse(responsedata.asString().contains("isMissing"));
	Assert.assertFalse(responsedata.asString().contains("isError"));
	Assert.assertFalse(responsedata.asString().contains("isRestricted"));
 
	 
 }



@Test
public void FCA_1789_DA_greaterthan_2018 () throws IOException {
	
	URL file = Resources.getResource("FCA_1789.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()
			
			.when().post(dataPostUrl)
			.then().statusCode(200)
			.body(containsString("currency"))
			.body(containsString("periodResolution"))
			.body(containsString("value"))
			.body(containsString("2020"))			
			
			.extract().response();

	Assert.assertFalse(responsedata.asString().contains("isError"));
	Assert.assertFalse(responsedata.asString().contains("isRestricted"));
	Assert.assertFalse(responsedata.asString().contains("isMissing"));
 
	 
 }
@Test (enabled=false) 

public void FCA_1853() throws IOException {

      String userresult = baseURI + "/v2/users";
  	  boolean failure = false;

      Response users = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                    .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                    .when().get(userresult).then()
                    .body(containsString("firstName")).statusCode(200)                 
                    .extract()
                    .response();
      
      String groupsSelflink = users.path("data[2].relationships.groups.links.related");
      int metaCount = users.path("meta.totalResourceCount");                
      
      System.out.println(metaCount);
      
      if (metaCount>=1) {
      
    	  System.out.println(groupsSelflink);
     
    	 
    	  Response group = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                  .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                  .when().get(groupsSelflink).then()
                  .body(containsString("name"))
                  .body(containsString("status"))
                  .body(containsString("ENABLED"))
                  .statusCode(200)                 
                  .extract()
                  .response();     	

		} else {
			
			System.out.println((metaCount));
			failure = true;
		}
      
      Assert.assertFalse(users.asString().contains("isError"));
      Assert.assertFalse(users.asString().contains("isMissing"));
   

}
@Test
public void FCA_1773 () throws IOException {
	
	URL periodfile = Resources.getResource("FCA_1773.json");
	String periodJson = Resources.toString(periodfile, Charsets.UTF_8);

	Response periodresponsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json")
			.body(periodJson).with()			
			.when().post(dataPostUrl)
			.then().statusCode(200)
			.extract().response();

	Assert.assertFalse(periodresponsedata.asString().contains("isError"));
	Assert.assertFalse(periodresponsedata.asString().contains("isRestricted"));
	Assert.assertFalse(periodresponsedata.asString().contains("isMissing"));
 
	 
 }

@Test
public void FCA_1883case1 () throws IOException {
	
	URL case1file = Resources.getResource("FCA_1883case1.json");
	String case1Json = Resources.toString(case1file, Charsets.UTF_8);

	Response case1responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(case1Json).with()
			
			.when().post(dataPostUrl)
			.then().statusCode(200)
			.body(containsString("currency"))
			.extract().response();

	Assert.assertFalse(case1responsedata.asString().contains("isError"));
	Assert.assertFalse(case1responsedata.asString().contains("isRestricted"));
	Assert.assertFalse(case1responsedata.asString().contains("isMissing"));
 
	 
 }

@Test
public void FCA_1883case2 () throws IOException {
	
	URL case2file = Resources.getResource("FCA_1883case2.json");
	String case2Json = Resources.toString(case2file, Charsets.UTF_8);

	Response case2responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(case2Json).with()
			
			.when().post(dataPostUrl)
			.then().statusCode(200)
			.body(containsString("USD"))
			.body(containsString("value"))
			.body(containsString("periodResolution"))
			.extract().response();

	Assert.assertFalse(case2responsedata.asString().contains("isError"));
	Assert.assertFalse(case2responsedata.asString().contains("isRestricted"));
	Assert.assertFalse(case2responsedata.asString().contains("isMissing"));
 
	 
 }

@Test 

public void FCA_1883case3  () throws IOException {
	  
	URL case3file = Resources.getResource("FCA_1883case3.json");
	String case3myJson = Resources.toString(case3file, Charsets.UTF_8);

	Response case3responsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(case3myJson).with()

			.when().post(dataPostUrl)

			.then().assertThat().log().ifError().statusCode(200)
			.body(containsString("USD"))
			
			
			.extract().response();
	
	Assert.assertFalse(case3responsedata.asString().contains("isError"));
	Assert.assertFalse(case3responsedata.asString().contains("isRestricted"));
	Assert.assertFalse(case3responsedata.asString().contains("isMissing"));

	  

 }

@Test

public void FCA_1883case4() throws IOException {
	  
	  URL case4file = Resources.getResource("FCA_1883case4.json");
		String case4myJson = Resources.toString(case4file, Charsets.UTF_8);

		Response case4responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(case4myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body(containsString("JPMorgan Chase & Co."))
				.body(containsString("value"))
				.body(containsString("timeIntervalPeriod"))
				.body(containsString("year"))				
				.extract().response();

		Assert.assertFalse(case4responsedata.asString().contains("isError"));
		Assert.assertFalse(case4responsedata.asString().contains("isRestricted"));
		Assert.assertFalse(case4responsedata.asString().contains("isMissing"));	  
}

@Test

public void FCA_1309metadata() throws IOException {

      String metaresult = baseURI + "/v1/metadata/fields/FC_FOREIGN_OWNED_SUB";

      Response metares = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                    .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                    .when().get(metaresult).then()
                    .assertThat().log().ifError().statusCode(200)
                    .extract()
                    .response();
      Assert.assertFalse(metares.asString().contains("isError"));
      Assert.assertFalse(metares.asString().contains("isMissing"));
      Assert.assertFalse(metares.asString().contains("isRestricted"));

}

@Test 

public void FCA_1309  () throws IOException {
	  
	URL subfile = Resources.getResource("FCA_1309.json");
	String submyJson = Resources.toString(subfile, Charsets.UTF_8);

	Response subresponsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(submyJson).with()
			.when().post(dataPostUrl)
			.then().assertThat().log().ifError().statusCode(200)
			.body(containsString("value"))
			.extract().response();	
	Assert.assertFalse(subresponsedata.asString().contains("isError"));
	Assert.assertFalse(subresponsedata.asString().contains("isRestricted"));
	Assert.assertFalse(subresponsedata.asString().contains("isMissing"));

	  

 }

@Test 

public void FCA_1937  () throws IOException {
	  
	URL finfile = Resources.getResource("FCA_1937.json");
	String finmyJson = Resources.toString(finfile, Charsets.UTF_8);

	Response finresponsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(finmyJson).with()
			.when().post(dataPostUrl)
			.then().assertThat().log().ifError().statusCode(200)
			.body(containsString("periodResolution"))
			.extract().response();	
	Assert.assertFalse(finresponsedata.asString().contains("isError"));
	Assert.assertFalse(finresponsedata.asString().contains("isRestricted"));
	Assert.assertFalse(finresponsedata.asString().contains("isMissing"));
 }
@Test 
public void FCA_1948 () throws IOException {
	  
	URL errfile = Resources.getResource("FCA_1948.json");
	String errmyJson = Resources.toString(errfile, Charsets.UTF_8);

	Response errresponsedata = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(errmyJson).with()
			.when().post(dataPostUrl)
			.then().assertThat().log().ifError().statusCode(200)			
			.extract().response();	
	
	Assert.assertFalse(errresponsedata.asString().contains("isError"));
	Assert.assertFalse(errresponsedata.asString().contains("isRestricted"));
	Assert.assertFalse(errresponsedata.asString().contains("isMissing"));
 }

@Test 

public void FCA_1875allwithfilters() throws IOException {

      String allresult = baseURI + "/v1/entityRankings?filter[rankType]=operatingProfitRanks&filter[rankYear]=2000";

      Response allres = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                    .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                    .when().get(allresult).then()
                    .body(containsString("entityRankings"))
                    .body(containsString("operatingProfitRanks"))
                   .statusCode(200) .extract()
                    .response();
      Assert.assertFalse(allres.asString().contains("isError"));
      Assert.assertFalse(allres.asString().contains("isMissing"));
      Assert.assertFalse(allres.asString().contains("isRestricted"));

}
@Test

public void FCA_1874entities() throws IOException {

      String entresult = baseURI + "/v1/entities/1199650";

      Response entres = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                    .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                    .when().get(entresult).then()
                    .body(containsString("entityRankings"))
                    .assertThat().log().ifError().statusCode(200)
                    .extract()
                    .response();
      Assert.assertFalse(entres.asString().contains("isError"));
      Assert.assertFalse(entres.asString().contains("isMissing"));
      Assert.assertFalse(entres.asString().contains("isRestricted"));

}
@Test

public void FCA_1874entitiesrelationship() throws IOException {

      String rentresult = baseURI + "/v1/entities/1199650/relationships/entityRankings";

      Response rentres = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                    .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                    .when().get(rentresult).then()
                    .body(containsString("entityRankings"))
                    .assertThat().log().ifError().statusCode(200)
                    .extract()
                    .response();
      Assert.assertFalse(rentres.asString().contains("isError"));
      Assert.assertFalse(rentres.asString().contains("isMissing"));
      Assert.assertFalse(rentres.asString().contains("isRestricted"));

}

@Test

public void FCA_1874entitiesER() throws IOException {

      String ERresult = baseURI + "/v1/entities/1199650/entityRankings?filter[rankType]=totalWeightedRisksRanks&sort[rankYear]=desc";

      Response ERres = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                    .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                    .when().get(ERresult).then()
                    .body(containsString("entityRankings"))
                    .assertThat().log().ifError().statusCode(200)
                    .extract()
                    .response();
      Assert.assertFalse(ERres.asString().contains("isError"));
      Assert.assertFalse(ERres.asString().contains("isMissing"));
      Assert.assertFalse(ERres.asString().contains("isRestricted"));

}
}
