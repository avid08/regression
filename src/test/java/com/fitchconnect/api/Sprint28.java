package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.configuration.api.Configuration;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import groovy.json.internal.Charsets;

public class Sprint28 extends Configuration {

	private static final int[] GROUP_TYPES = new int[] { 4, 6, 20, 22, 24, 26, 27, 30, 31, 32, 33, 28 };
	private static final int[] VISIBLE_INACTIVE_STATUS_IDS = new int[] { 3, 4 };
	

	@Test

	public void addtionalAttributes_1261() {

		String entityUrl = baseURI + "/v1/entities/117522";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityUrl).then().assertThat()
				.statusCode(200).contentType(ContentType.JSON).body(containsString("zipCode"))
				.body(containsString("city")).body(containsString("address1")).body(containsString("countryISOCode"))
				.body(containsString("name")).body(containsString("stateCode")).body(containsString("state"))
				.body(containsString("countryName")).body(containsString("region"))
				

				.extract().response();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

	}

	@Test

	public void additional_Attributes_value_1261() throws IOException {

		URL file = Resources.getResource("additional_attributes.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertFalse(responsedata.asString().contains("isError"));
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));

	}

	@Test()
	public void removal_inactiveEntity_1131() throws FileNotFoundException, UnsupportedEncodingException {

		Set<Long> agentIdSet = new HashSet<>();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));

			DB db = mongoClient.getDB(databaseFitchEnty);

			DBCollection collection = db.getCollection("fitch_entity");
			List<DBObject> pipeline = new ArrayList<DBObject>();

			DBObject project = new BasicDBObject("$project", new BasicDBObject("agentID", 1));
			DBObject match = new BasicDBObject("$match", new BasicDBObject("$or", matchRatedOrNonRatedDBList()));

			pipeline.add(match);
			pipeline.add(project);
			// pipeline.add(new BasicDBObject("$skip", 0));
			pipeline.add(new BasicDBObject("$limit", 200));

			System.out.println("findByAgentIds: " + pipeline);
			AggregationOutput output = collection.aggregate(pipeline);

			List<Long> entityID = new ArrayList<>();
			for (DBObject result : output.results()) {
				entityID.add((Long) result.get("agentID"));
			}
			System.out.println("Number of Agent Id in fitch_entity" + entityID.size());
			
			agentIdSet = findByAgentIds(db, entityID);

			System.out.println("Number of Agent Id fitch_agent" + agentIdSet.size());
			System.out.println("agent Id " + agentIdSet);

		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

		// PrintWriter writer = new PrintWriter("statusCode.txt", "UTF-8");
		
		

		long startTime = System.currentTimeMillis();
		ExecutorService executor = Executors.newFixedThreadPool(50);

		Iterator<Long> iter = agentIdSet.iterator();
		final AtomicInteger idx = new AtomicInteger(0);
		while (iter.hasNext()) {
			final long agentId = iter.next();
			executor.submit(() -> {
				String entityUri = "/v1/entities/";
				String enTityUrl = baseURI + entityUri + agentId;

				// System.out.println(enTityUrl);

				int statuscode = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
						.contentType(ContentType.JSON).header("Content", contentValue).when().get(enTityUrl)
						.statusCode();

				if (statuscode != 200) {

					System.err.println("statusCode " + statuscode + " agentID " + agentId);

				} else {
					System.out.println((idx.incrementAndGet()));
				}
			});
			
		}

		try {
			System.out.println("attempt to shutdown executor");
			executor.shutdown();
			executor.awaitTermination(15, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			System.err.println("tasks interrupted");
		}

		System.out.println("Took: " + (System.currentTimeMillis() - startTime) / 1000);

	}

	// The one against fitch_agent
	public Set<Long> findByAgentIds(DB db, Collection<Long> agentIds) {
		Set<Long> agentIdSet = new HashSet<>();

		List<DBObject> pipeline = buildFindByAgentIdsQuery(agentIds);
		System.out.println("findByAgentIds in fitch_agent: " + pipeline);
		DBCollection collection = db.getCollection("fitch_agent");
		Iterable<DBObject> entitiesRawData = collection.aggregate(pipeline).results();

		if (entitiesRawData == null || !entitiesRawData.iterator().hasNext()) {
			return agentIdSet;
		}

		Iterator<DBObject> iterator = entitiesRawData.iterator();
		while (iterator.hasNext()) {
			BasicDBObject doc = (BasicDBObject) iterator.next();
			long agentId = doc.getLong("agentID");
			agentIdSet.add(agentId);
		}

		return agentIdSet;
	}

	protected BasicDBList matchRatedOrNonRatedDBList() {
		BasicDBList andList = new BasicDBList();
		andList.add(new BasicDBObject("groupID", new BasicDBObject("$exists", true)));

		andList.add(new BasicDBObject("groupType", new BasicDBObject("$in", GROUP_TYPES)));

		BasicDBList orList = new BasicDBList();
		orList.add(new BasicDBObject("$and", andList));
		orList.add(new BasicDBObject("groupID", new BasicDBObject("$exists", false)));

		return orList;
	}

	protected BasicDBList matchActiveOrVisibleInactiveStatusCodesDBList() {
		BasicDBList andList = new BasicDBList();
		andList.add(new BasicDBObject("isActive", false));
		andList.add(new BasicDBObject("inactiveStatId", new BasicDBObject("$in", VISIBLE_INACTIVE_STATUS_IDS)));

		BasicDBList orList = new BasicDBList();
		orList.add(new BasicDBObject("$and", andList));
		orList.add(new BasicDBObject("isActive", true));

		return orList;
	}

	protected List<DBObject> buildFindByAgentIdsQuery(Collection<Long> agentIds) {
		List<DBObject> pipeline = new ArrayList<DBObject>();

		BasicDBObject matchCondition = new BasicDBObject("agentID", new BasicDBObject("$in", agentIds)).append("$or",
				matchActiveOrVisibleInactiveStatusCodesDBList());
		pipeline.add(new BasicDBObject("$match", matchCondition));

		DBObject projectCondition = new BasicDBObject("$project",
				new BasicDBObject("agentID", 1));
		pipeline.add(projectCondition);

		return pipeline;
	}
	
	
}