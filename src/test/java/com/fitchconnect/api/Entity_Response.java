package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

public class Entity_Response {
	private static final int[] GROUP_TYPES = new int[] { 4, 6, 20, 22, 24, 26, 27, 30, 31, 32, 33, 28 };
	public Response response;

	String myjson;
	String AuthrztionValue;
	String baseURI;
	String env;
	String metaEndPoint = "/v1/metadata/fields"; // Metadata-EndPoint
	String metaUrl;
	String dataEndPoint = "/v1/data/valueRequest";
	String dataPostUrl; //
	String XappClintIDvalue = "3dab0f06-eb00-4bee-8966-268a0ee27ba0";
	String acceptValue = "application/vnd.api+json";
	String contentValue = "application/vnd.api+json";
	String dataBaseServer;
	String databaseFitchEnty;

	@BeforeClass
	public void executionSetup() {
		env = System.getProperty("env");
		System.out.println("Test Execution Environment: " + env);
		if (env == null) {
			baseURI = "http://docker-q01.fitchratings.com:30001";
			this.AuthrztionValue = ("Basic MUlLVk1SMjlJS1lIMllPSjFUQkdGQ0tKSDpFN1Y2Z1FJY3RPeG5KbG8rSVBHaGY0K0tTSGc3LzFpOFJsbVo1Tmd6NUpB");
			dataBaseServer = "mongorisk-q01";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("dev")) {
			baseURI = "https://api-dev.fitchconnect.com";
			this.AuthrztionValue = ("Basic MVNCRFI4MzVTQ1lOVU5CSDJSVk1TU0MxOTpHTExaUlR3QUpRdjVTazV1cXRyZWlqZE9SK01yQTZrU2plVmNuZXdlekow");
			// dataBaseServer = "mongoweb-x01";
			dataBaseServer = "mgo-due1c-cr001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
		} else if (env.equals("int")) {
			baseURI = "https://api-int.fitchconnect.com";
			this.AuthrztionValue = ("Basic MVNCRFI4MzVTQ1lOVU5CSDJSVk1TU0MxOTpHTExaUlR3QUpRdjVTazV1cXRyZWlqZE9SK01yQTZrU2plVmNuZXdlekow");
			// dataBaseServer = "mongoweb-x01";
			dataBaseServer = "mgo-due1c-cr001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
		} else if (env.equals("qa")) {
			baseURI = "http://docker-q01.fitchratings.com:30001";
			this.AuthrztionValue = ("Basic MUlLVk1SMjlJS1lIMllPSjFUQkdGQ0tKSDpFN1Y2Z1FJY3RPeG5KbG8rSVBHaGY0K0tTSGc3LzFpOFJsbVo1Tmd6NUpB");
			dataBaseServer = "mongorisk-q01";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("stage")) {
			baseURI = "https://api-stage.fitchconnect.com";
			this.AuthrztionValue = ("Basic NU5COUFRSDVCSTRDUFZTUktJRUpESjQyNTpDYjFxUXQycHd4VGNKZTg1SjkyRVJmL1JMU1haRUlZSjU3NWR5R3RacDVV");
			dataBaseServer = "mongorisk-int01";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("prod")) {
			baseURI = "https://api.fitchconnect.com";
			this.AuthrztionValue = ("Basic M1FEREJQODMyQ1NKTlMwM1ZQT0NSQ0VFQjpENk9PUWtJVW5uaXhVZlZmL3loVnJhbHNDU1dzaGd0L1NJOGFTSFZEVTJR");

			dataBaseServer = "mongorisk-p01";
			databaseFitchEnty = "esp-9";
		}

		System.out.println(baseURI);
		metaUrl = baseURI + metaEndPoint;
		dataPostUrl = baseURI + dataEndPoint;

	}

	@Test()
	public void StatusCodeTest() throws FileNotFoundException, UnsupportedEncodingException {

		final ArrayList<Long> entityID = new ArrayList();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer, 27017),
					Arrays.asList(credential));
			

			DB db = mongoClient.getDB(databaseFitchEnty);

			DBCollection collection = db.getCollection("fitch_entity");
			List<DBObject> pipeline = new ArrayList<DBObject>();

			DBObject project = new BasicDBObject("$project", new BasicDBObject("agentID", 1));
			DBObject match = new BasicDBObject("$match", new BasicDBObject("$or", matchRatedOrNonRatedDBList()));

			pipeline.add(match);
			pipeline.add(project);
			//pipeline.add(new BasicDBObject("$skip", 0));
			//pipeline.add(new BasicDBObject("$limit", 10000));

			System.out.println("findByAgentIds: " + pipeline);
			AggregationOutput output = collection.aggregate(pipeline);

			for (DBObject result : output.results()) {
				entityID.add((Long) result.get("agentID"));
			}

			System.out.println("Number of Agent Id " + entityID.size());
			System.out.println("agent Id " + entityID);

		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

		// PrintWriter writer = new PrintWriter("statusCode.txt", "UTF-8");
		
		long startTime = System.currentTimeMillis();
		ExecutorService executor = Executors.newFixedThreadPool(150);
		
		for (int i =0; i < entityID.size(); i++) {
			final int idx = i; 
			executor.submit(() -> {
				String entityUri = "/v1/entities/";
				String enTityUrl = baseURI + entityUri + entityID.get(idx);

				// System.out.println(enTityUrl);

				int statuscode = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
						.contentType(ContentType.JSON).header("Content", contentValue).when().get(enTityUrl).statusCode();

				if (statuscode != 200) {

					System.err.println("statusCode " + statuscode + " agentID " + entityID.get(idx));
					

				}else{
					System.out.println((idx));
				}
			});
		}
		
		try {
		    System.out.println("attempt to shutdown executor");
		    executor.shutdown();
		    executor.awaitTermination(12, TimeUnit.HOURS);
		}
		catch (InterruptedException e) {
		    System.err.println("tasks interrupted");
		}
		
		System.out.println("Took: "+(System.currentTimeMillis() - startTime)/1000);
/*
		for (int i =0; i < entityID.size(); i++) {

			String entityUri = "/v1/entities/";
			String enTityUrl = baseURI + entityUri + entityID.get(i);

			// System.out.println(enTityUrl);

			int statuscode = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
					.contentType(ContentType.JSON).header("Content", contentValue).when().get(enTityUrl).statusCode();

			if (statuscode != 200) {

				System.err.println("statusCode " + statuscode + " agentID " + entityID.get(i));
				

			}else{
				System.out.println((i));
			}

		}
*/
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

}
