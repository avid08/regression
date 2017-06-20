package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import groovy.json.internal.Charsets;

public class fisCon_Sprint2 extends Configuration {

	@Test
	public void all_category_ids_FISC_248() {
		
		boolean failure = false;
		String modyCateGoryURI = baseURI + "/v1/metadata/categories";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(modyCateGoryURI).then()
				.statusCode(200).body(containsString("categories")).body(containsString("relationships"))
				.body(containsString("name")).body(containsString("fields"))

				.extract().response();
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

		List<String> parentrelatedLink = response.path("data.relationships.parents.links.related");
		List<String> cateGoryIds = response.path("data.id");
		long APIresponseCount = (cateGoryIds.size());

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());

			System.out.println("database server name - " + dataBaseServer1);
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer2, 27017),
					Arrays.asList(credential));

			DB db = mongoClient.getDB("xrefdb");
			DBCollection collection = db.getCollection("category");

			long databasecount = collection.getCount();

			DBObject doc = collection.findOne();

			if (databasecount == APIresponseCount) {
				System.out.println("collection count - " + databasecount);
			} else {
				failure = true;
				System.out.println("collection count  " + databasecount);
				System.out.println("Api response " + APIresponseCount);
			}
			Assert.assertFalse(failure);

		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

	}

	
	@Test
	
	public void FISC_351_SPV_enTitySummary() {
		
		String SPVMetaURI = baseURI + "/v1/metadata/fields/FC_SPONSOR_SPV";
		
		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(SPVMetaURI).then()
				.statusCode(200)
				.body(containsString("Sponsor of the SPV"))
				.body(containsString("TEXT"))
				.body(containsString("ratings"))
				.body(containsString("dataService"))
				.extract().response();
		
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
		
		
	}
	
@Test
	
	public void FISC_351_SPV_enTitySummary_dataAggregator() throws IOException {
	
	 URL file = Resources.getResource("FISC_351.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);
		
		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200)
				.statusCode(200)
				.body(containsString("value"))
				.body(containsString("COSTA RICA"))
				.body(containsString("text"))
				.extract().response();
						
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		
	}
	
  @Test 
  
  public void FISC_396_NameChange(){
	  
	  String disPlyChagneURI = baseURI + "/v1/metadata/fields/FC_IMQR_DT";
	  
		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(disPlyChagneURI).then()
				.statusCode(200)
				.body(containsString("Investment Management Quality Rating Effective Date"))
				.body(containsString("Date"))
				.body(containsString("ratings"))
				.body(containsString("fitchIssuerRatings"))
				.extract().response();
		
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
	  
  }
	
}
