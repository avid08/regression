package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import groovy.json.internal.Charsets;

public class Sprint27 extends Configuration {

	
	@Test

	public void FCA_1188() {

		List<DBObject> pipeline = new ArrayList<>();
		DBObject match = new BasicDBObject("$match", new BasicDBObject("agentID", new BasicDBObject("$exists", true))
				.append("ratings", new BasicDBObject("$exists", true)));
		pipeline.add(match);

		ArrayList<Long> myArray = new ArrayList();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));

			DB db = mongoClient.getDB(databaseFitchEnty);

			DBCollection collection = db.getCollection("moodys");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("agentID", 1));

			pipeline.add(project);
			AggregationOutput output = collection.aggregate(pipeline);

			for (DBObject result : output.results()) {

				myArray.add((Long) result.get("agentID"));

			}

			System.out.println(myArray.size());

			// System.out.println(Url);

			for (int i = 0; i < 1; i++) {
				String endpoint1 = "/v1/entities/";
				String Url = baseURI + endpoint1 + myArray.get(i);
				System.out.println(Url);

				Response entityres = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).when().get(Url).then().assertThat().statusCode(200)
						.contentType(ContentType.JSON).extract().response();

				// System.out.println(entityres.asString());

				String SpUrl = baseURI + endpoint1 + myArray.get(i) + "/standardAndPoorIssuerRatings";

				Response SPresonse = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).when().get(SpUrl).then().assertThat().statusCode(200)
						.contentType(ContentType.JSON).extract().response();

				System.out.println(SpUrl);

				String link = SPresonse.path("data[0].relationships.entity.links.related");
				System.out.println(link);

				Response modysresponse = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).when().get(link).then().assertThat().statusCode(200)
						.body(containsString("moodyIssuerRatings")).contentType(ContentType.JSON).extract().response();

				System.out.println(modysresponse.asString());

			}

		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

	}

	@Test(enabled = true)

	public void FCA_1284() {

		String UPend = "/v1/entities/100042/ultimateParent";
		String UPComplete = baseURI + UPend;

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(UPComplete).then().body("data.id", equalTo("100042")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FCA_1121datarequest() throws IOException {

		URL file = Resources.getResource("1121.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertFalse(responsedata.asString().contains("isError"));
		Assert.assertFalse(responsedata.asString().contains("isMissing"));
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));

	}

	@Test
	public void FCA_1119datarequest() throws IOException {

		URL file1 = Resources.getResource("1119.json");
		String myJson1 = Resources.toString(file1, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson1)
				.with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertFalse(responsedata.asString().contains("isError"));
		Assert.assertFalse(responsedata.asString().contains("isMissing"));
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));
		

	}

	@Test(enabled = true)

	public void FCA_1127nickname() {

		List<DBObject> pipeline = new ArrayList<>();
		DBObject match = new BasicDBObject("$match", new BasicDBObject("_id", new BasicDBObject("$exists", true)));
		/* .append("ratings", new BasicDBObject("$exists", true)) */
		pipeline.add(match);

		ArrayList<Long> myArray = new ArrayList();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));

			DB db = mongoClient.getDB("financial-1");
			DBCollection collection = db.getCollection("nickname");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("_id", 1));
			pipeline.add(project);
			AggregationOutput output = collection.aggregate(pipeline);

			for (DBObject result : output.results()) {

				myArray.add((Long) result.get("_id"));

			}

			for (int i = 0; i < 1; i++) {
				String NN = "/v1/nicknames/" + myArray.get(i);
				String NNComplete = baseURI + NN;
				System.out.println(NNComplete);

				Response res = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when().get(NNComplete)
						.then().body("data.type", equalTo("nicknames")).extract().response();

				Assert.assertFalse(res.asString().contains("isError"));
				Assert.assertFalse(res.asString().contains("isMissing"));

			}
		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}
	}

	@Test(enabled = false)
	public void FCA_1127_nicknameentities() {
		List<DBObject> pipeline = new ArrayList<>();
		DBObject match = new BasicDBObject("$match", new BasicDBObject("_id", new BasicDBObject("$exists", true)));
		/* .append("ratings", new BasicDBObject("$exists", true)) */
		pipeline.add(match);

		ArrayList<Long> myArray = new ArrayList();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));

			DB db = mongoClient.getDB("financial-1");
			DBCollection collection = db.getCollection("nickname");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("_id", 1));
			pipeline.add(project);
			AggregationOutput output = collection.aggregate(pipeline);

			for (DBObject result : output.results()) {

				myArray.add((Long) result.get("_id"));

			}

			for (int i = 0; i < 1; i++) {
				String NE = "/v1/entities/" + myArray.get(i) + "/nicknames";
				String NEComplete = baseURI + NE;
				System.out.println(NEComplete);

				Response res = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when().get(NEComplete)
						.then().statusCode(200).body("data[0].type", equalTo("nicknames")).extract().response();

				Assert.assertFalse(res.asString().contains("isError"));
				Assert.assertFalse(res.asString().contains("isMissing"));
			}
		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

	}

	@Test
	public void FCA_1127entitynickname() {
		List<DBObject> pipeline = new ArrayList<>();
		DBObject match = new BasicDBObject("$match", new BasicDBObject("identifierVOList.name","LEI"));
		/*.append("identifierVOList.name","LEI", new BasicDBObject("$exists", true)));*/
		pipeline.add(match);

		ArrayList<Long> myArray = new ArrayList();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));

			DB db = mongoClient.getDB(databaseFitchEnty);
			DBCollection collection = db.getCollection("fitch_entity");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("agentID", 1));
			pipeline.add(project);
			AggregationOutput output = collection.aggregate(pipeline);

			for (DBObject result : output.results()) {

				myArray.add((Long) result.get("agentID"));

			}
			  
			  
			for (int i= 1; i < 2; i++) {
				String EN = "/v1/entities/" + myArray.get(i);
				String ENComplete = baseURI + EN;
				System.out.println(ENComplete);

				Response res = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when().get(ENComplete)
						.then().statusCode(200)
						.body("data.relationships.nicknames.links.self", Matchers.anything("https://api-")).extract()
						.response();

				Assert.assertFalse(res.asString().contains("isError"));
				Assert.assertFalse(res.asString().contains("isMissing"));
			}
		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

	}

}
