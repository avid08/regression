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

import org.junit.Assert;
import org.testng.annotations.BeforeClass;
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

public class Sprint28 {

	public Response response;
	private static final int[] GROUP_TYPES = new int[] { 4, 6, 20, 22, 24, 26, 27, 30, 31, 32, 33, 28 };
	private static final int[] VISIBLE_INACTIVE_STATUS_IDS = new int[] { 3, 4 };
	String myjson;
	String AuthrztionValue;
	String baseURI;
	String env;
	String databaseFitchEnty;
	String dataBaseServer1;
	String dataBaseServer2;
	String id;
	String id1;
	String jsonresponse;
	String metaEndPoint = "/v1/metadata/fields"; // Metadata-EndPoint
	String metaUrl = baseURI + metaEndPoint;
	String dataEndPoint = "/v1/data/valueRequest";
	String dataPostUrl = baseURI + dataEndPoint; // Data Aggregator -EndPoint
	String XappClintIDvalue = "3dab0f06-eb00-4bee-8966-268a0ee27ba0";
	String acceptValue = "application/vnd.api+json";
	String contentValue = "application/vnd.api+json";

	@BeforeClass
	public void executionSetup() {
		env = System.getProperty("env");
		System.out.println("Test Execution Environment: " + env);
		if (env == null) {
			baseURI = "https://api.fitchconnect-stg.com";
			this.AuthrztionValue = ("Basic NTZORlhGTkJOOEdPWVk0Uk41UFdBTTdYVDp5c1FxNEtwazJza1UyVXU4TE1lbytLVVltTEhKMG1COFo5ZWczT0JZTStr");
			dataBaseServer1 = "mongorisk-int01";
			dataBaseServer2 = "mongorisk-int01";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("dev")) {
			baseURI = "https://api.fitchconnect-dev.com";  
			this.AuthrztionValue = ("Basic MUc4TTJCUzVIUTdGTVE5RVlNWTdWWVlUWTpoeU51d2lIYUVtOEpaSnF1RzVsRmM0TnRrTXpMMjdqcVFFczVwSDlUdEZJ");
			// dataBaseServer = "mongoweb-x01";
			dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
		} else if (env.equals("int")) {
			baseURI = "https://api.fitchconnect-int.com";
			this.AuthrztionValue = ("Basic WkRCSkg4WkpPWEg0S0dQNkZaRE9MVUpDWDp3VTlYWHpjakxsMWZYbldwM1lZaXBhU0VUcXZMTmtIY3hCK09ydXdRSHJB");
			// dataBaseServer = "mongoweb-x01";
			dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
		} else if (env.equals("qa")) {
			baseURI = "https://api.fitchconnect-qa.com";
			this.AuthrztionValue = ("Basic MU1HUjNXOFJCV0ZJNFlJMzNEV000MDk2WTpGYXp5Y3E4MHd1M0hpSlFzNVVhZDlJa3E1dEIyZ1YzcnA1OVB4UmowV2pJ");
			dataBaseServer1 = "mgo-que1a-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-que1a-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("stage")) {
			baseURI = "https://api.fitchconnect-stg.com";
			this.AuthrztionValue = ("Basic NTZORlhGTkJOOEdPWVk0Uk41UFdBTTdYVDp5c1FxNEtwazJza1UyVXU4TE1lbytLVVltTEhKMG1COFo5ZWczT0JZTStr");
			dataBaseServer1 = "mongorisk-int01";
			dataBaseServer2 = "mongorisk-int01";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("prod")) {
			baseURI = "https://api.fitchconnect.com";
			this.AuthrztionValue = ("Basic M1FEREJQODMyQ1NKTlMwM1ZQT0NSQ0VFQjpENk9PUWtJVW5uaXhVZlZmL3loVnJhbHNDU1dzaGd0L1NJOGFTSFZEVTJR");
			dataBaseServer1 ="mgo-pue1c-cr001..fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		}

		System.out.println(baseURI);
		metaUrl = baseURI + metaEndPoint;
		dataPostUrl = baseURI + dataEndPoint;

	}

	@Test

	public void addtionalAttributes_1261() {

		String entityUrl = baseURI + "/v1/entities/117522";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityUrl).then().assertThat()
				.statusCode(200).contentType(ContentType.JSON).body(containsString("zipCode"))
				.body(containsString("city")).body(containsString("address1")).body(containsString("countryISOCode"))
				.body(containsString("name")).body(containsString("stateCode")).body(containsString("state"))
				.body(containsString("countryName")).body(containsString("region"))
				.body(containsString("fitchConnectUrl"))

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