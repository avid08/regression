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

import org.testng.Assert;
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

public class relationship_linksTest {
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
	String dataBaseServer1;
	String dataBaseServer2;
	String databaseFitchEnty;

	@BeforeClass
	public void executionSetup() {
		env = System.getProperty("env");
		System.out.println("Test Execution Environment: " + env);
		if (env == null) {
			baseURI = "https://new-api.fitchconnect.com";
			this.AuthrztionValue = ("Basic M1FEREJQODMyQ1NKTlMwM1ZQT0NSQ0VFQjpENk9PUWtJVW5uaXhVZlZmL3loVnJhbHNDU1dzaGd0L1NJOGFTSFZEVTJR");
			dataBaseServer1 = "mgo-pue1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("dev")) {
			baseURI = "https://api-dev.fitchconnect.com";
			this.AuthrztionValue = ("Basic MVNCRFI4MzVTQ1lOVU5CSDJSVk1TU0MxOTpHTExaUlR3QUpRdjVTazV1cXRyZWlqZE9SK01yQTZrU2plVmNuZXdlekow");
			// dataBaseServer = "mongoweb-x01";
			dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
		} else if (env.equals("int")) {
			baseURI = "https://api.fitchconnect-int.com";
			this.AuthrztionValue = ("Basic MVNCRFI4MzVTQ1lOVU5CSDJSVk1TU0MxOTpHTExaUlR3QUpRdjVTazV1cXRyZWlqZE9SK01yQTZrU2plVmNuZXdlekow");
			// dataBaseServer = "mongoweb-x01";
			dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
		} else if (env.equals("qa")) {
			baseURI = "https://api.fitchconnect-qa.com";
			this.AuthrztionValue = ("Basic MUlLVk1SMjlJS1lIMllPSjFUQkdGQ0tKSDpFN1Y2Z1FJY3RPeG5KbG8rSVBHaGY0K0tTSGc3LzFpOFJsbVo1Tmd6NUpB");
			dataBaseServer1 = "mgo-que1a-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-que1a-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("stage")) {
			baseURI = "https://api.fitchconnect-stg.com";
			this.AuthrztionValue = ("Basic NU5COUFRSDVCSTRDUFZTUktJRUpESjQyNTpDYjFxUXQycHd4VGNKZTg1SjkyRVJmL1JMU1haRUlZSjU3NWR5R3RacDVV");
			dataBaseServer1 = "mongorisk-int01";
			dataBaseServer2 = "mongorisk-int01";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("prod")) {
			baseURI = "https://new-api.fitchconnect.com";
			this.AuthrztionValue = ("Basic M1FEREJQODMyQ1NKTlMwM1ZQT0NSQ0VFQjpENk9PUWtJVW5uaXhVZlZmL3loVnJhbHNDU1dzaGd0L1NJOGFTSFZEVTJR");
			dataBaseServer1 ="mgo-pue1c-cr001..fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		}

		System.out.println(baseURI);
		metaUrl = baseURI + metaEndPoint;
		dataPostUrl = baseURI + dataEndPoint;

	}


	@Test()
	public void entitylinks() throws FileNotFoundException, UnsupportedEncodingException {

		final ArrayList<Long> entityID = new ArrayList();

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
			// pipeline.add(new BasicDBObject("$limit", 10000));

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

		for (int i = 0; i < entityID.size(); i++) {
			final int idx = i;
			executor.submit(() -> {
				String entityUri = "/v1/entities/";
				String enTityUrl = baseURI + entityUri + entityID.get(idx) + "/statements";
				// System.out.println(enTityUrl);

				int statuscode = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
						.contentType(ContentType.JSON).header("Content", contentValue).when().get(enTityUrl)
						.statusCode();

				if (statuscode != 200) {

					System.err.println("statement statusCode " + statuscode + " agentID " + entityID.get(idx));

				} else {
					System.out.println((idx));
				}

				String ultimteUrl = baseURI + entityUri + entityID.get(idx) + "/ultimateParent";
				// System.out.println(ultimteUrl);

				int statuscode1 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
						.contentType(ContentType.JSON).header("Content", contentValue).when().get(ultimteUrl)
						.statusCode();

				if (statuscode1 != 200) {

					System.err.println("UparentStatusCode " + statuscode1 + " agentID " + entityID.get(idx));

				} else {
					System.out.println((idx));
				}

				String fitchissuerURl = baseURI + entityUri + entityID.get(idx) + "/fitchIssuerRatings";
				// System.out.println(fitchissuerURl);

				int statuscode2 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
						.contentType(ContentType.JSON).header("Content", contentValue).when().get(fitchissuerURl)
						.statusCode();

				if (statuscode2 != 200) {

					System.err.println("fitchIssuerRating statusCode " + statuscode2 + "agentID " + entityID.get(idx));

				} else {
					System.out.println((idx));
				}

				String nicknamesUrl = baseURI + entityUri + entityID.get(idx) + "/nicknames";
				// System.out.println(nicknamesUrl);

				int statuscode3 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
						.contentType(ContentType.JSON).header("Content", contentValue).when().get(nicknamesUrl)
						.statusCode();

				if (statuscode3 != 200) {

					System.err.println("nickNames statusCode " + statuscode3 + "agentID " + entityID.get(idx));

				} else {
					System.out.println((idx));
				}

				String modyIsserRatingsUrl = baseURI + entityUri + entityID.get(idx) + "/moodyIssuerRatings";
				// System.out.println(modyIsserRatingsUrl);

				int statuscode4 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
						.contentType(ContentType.JSON).header("Content", contentValue).when().get(modyIsserRatingsUrl)
						.statusCode();

				if (statuscode4 != 200) {

					System.err.println("moodyIssuerRatings statusCode " + statuscode4 + "agentID " + entityID.get(idx));

				} else {
					System.out.println((idx));
				}

				String SndPIssuerRatingUrl = baseURI + entityUri + entityID.get(idx) + "/standardAndPoorIssuerRatings";
				// System.out.println(SndPIssuerRatingUrl);

				int statuscode5 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
						.contentType(ContentType.JSON).header("Content", contentValue).when().get(SndPIssuerRatingUrl)
						.statusCode();

				if (statuscode5 != 200) {

					System.err.println(
							"standardAndPoorIssuerRatings statusCode " + statuscode4 + "agentID " + entityID.get(idx));

				} else {
					System.out.println((idx));
				}

				String fitchissuerRtingURL = baseURI + "fitchIssuerRatings" + entityID.get(idx);
				// System.out.println(SndPIssuerRatingUrl);

				int statuscode6 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
						.contentType(ContentType.JSON).header("Content", contentValue).when().get(fitchissuerRtingURL)
						.statusCode();

				if (statuscode5 != 200) {

					System.err.println(
							"fitchIssuerRatings and entity statuscode " + statuscode6 + "agentID " + entityID.get(idx));

				} else {
					System.out.println((idx));
				}

			});
		}

		System.out.println("Took: " + (System.currentTimeMillis() - startTime) / 1000);

		try {
			System.out.println("attempt to shutdown executor");
			executor.shutdown();
			executor.awaitTermination(12, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			System.err.println("tasks interrupted");
		}

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

	@Test
	public void nicknamesLinks() {

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
			System.out.println(myArray.size());
			for (int i = 0; i < 200; i++) {
				String niknames = "/v1/nicknames/" + myArray.get(i) + "/entity";
				String niknamesUrl = baseURI + niknames;
				String niknamesrelation = "/v1/nicknames/" + myArray.get(i) + "/relationships/entity";
				String niknamesrelationUrl = baseURI + niknamesrelation;

				// System.out.println(NNComplete);

				Response res = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when().get(niknamesUrl)
						.then().extract().response();
				// Assert.assertTrue(res.asString().contains("https://api"));
				Assert.assertFalse(res.asString().contains("meta"));

				int statuscode = 0;
				if (statuscode == 200) {
					System.out.println(myArray.get(i));
				} else {
					System.err.println("entity id " + myArray.get(i));

				}

				int StatusCode1 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(niknamesrelationUrl).getStatusCode();

				if (StatusCode1 == 200) {
					System.out.println(myArray.get(i));
				} else {
					System.err.println("relationship entity id " + myArray.get(i));

				}

			}
		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}
	}

	@Test

	public void filingsLinks() {

		List<DBObject> pipeline = new ArrayList<>();
		DBObject match = new BasicDBObject("$match", new BasicDBObject("sctrID", 7));
		/* .append("ratings", new BasicDBObject("$exists", true)) */
		pipeline.add(match);

		ArrayList<Integer> myArray = new ArrayList();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));

			DB db = mongoClient.getDB("financial-1");
			DBCollection collection = db.getCollection("financial_statement");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("_id", 1));
			pipeline.add(project);
			AggregationOutput output = collection.aggregate(pipeline);

			for (DBObject result : output.results()) {

				myArray.add((Integer) result.get("_id"));

			}
			System.out.println(myArray.size());

			for (int i = 0; i < 20000; i++) {
				String filings = "/v1/statements/" + myArray.get(i) + "/filings";
				String filingsUrl = baseURI + filings;

				int StatusCode1 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when().get(filingsUrl)
						.getStatusCode();

				if (StatusCode1 == 200) {
					System.out.println(myArray.get(i));
				} else {
					System.err.println("relationship entity id " + myArray.get(i));
				}

			}
		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

	}

	@Test
	public void CorporateHirechy() {

		List<DBObject> pipeline = new ArrayList<>();
		DBObject match = new BasicDBObject("$match", new BasicDBObject("agentID", new BasicDBObject("$exists", true)));
		/* .append("ratings", new BasicDBObject("$exists", true)) */
		pipeline.add(match);

		ArrayList<Long> myArray = new ArrayList();

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin",
					"the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017),
					Arrays.asList(credential));

			DB db = mongoClient.getDB(databaseFitchEnty);
			DBCollection collection = db.getCollection("corpHierarchy");

			DBObject project = new BasicDBObject("$project", new BasicDBObject("agentID", 1));
			pipeline.add(project);
			AggregationOutput output = collection.aggregate(pipeline);

			for (DBObject result : output.results()) {

				myArray.add((Long) result.get("agentID"));
			}

		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}
		System.out.println(myArray.size());
		ExecutorService executor = Executors.newFixedThreadPool(50);

		for (int i = 0; i < myArray.size(); i++) {
			final int idx = i;
			executor.submit(() -> {

				// BUG # (FCA-1372)

				String directors = "/v1/directors/" + myArray.get(idx);
				String directorUrl = baseURI + directors;

				int StatusCode1 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when().get(directorUrl)
						.getStatusCode();

				if (StatusCode1 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println("status " + StatusCode1 + " agentID " + myArray.get(idx));
				}

				String directorsrelation = "/v1/directors/" + myArray.get(idx) + "/relationships/entities";
				String directorrelationUrl = baseURI + directorsrelation;

				int StatusCode2 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(directorrelationUrl).getStatusCode();

				if (StatusCode2 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println("status " + StatusCode2 + " agentID with relationship " + myArray.get(idx));
				}

				String directorEntity = "/v1/directors/" + myArray.get(idx) + "/entities";
				String directorEntityUrl = baseURI + directorEntity;

				int StatusCode3 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(directorEntityUrl).getStatusCode();

				if (StatusCode3 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println("status " + StatusCode3 + " director agentID with entities " + myArray.get(idx));
				}

				// (BUG# FCA-1373)
				String shreholderurl = baseURI + "/v1/shareholders/" + myArray.get(idx);

				int StatusCode4 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(shreholderurl).getStatusCode();

				if (StatusCode4 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println("status " + StatusCode4 + "shareholder agentID " + myArray.get(idx));
				}

				String shreholderEntityUrl = shreholderurl + "/shareholderEntity";

				int StatusCode5 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(shreholderEntityUrl).getStatusCode();

				if (StatusCode5 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println("status " + StatusCode5 + "shareholderEntity  agentID " + myArray.get(idx));
				}

				String shreholderWonrship = shreholderurl + "/wonership";
				int StatusCode6 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(shreholderWonrship).getStatusCode();

				if (StatusCode6 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println("status " + StatusCode6 + "shareholder ownership  agentID " + myArray.get(idx));
				}

				String shreholderRelatnship = shreholderurl + "/relationships/wonership";
				int StatusCode7 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(shreholderRelatnship).getStatusCode();

				if (StatusCode7 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println("status " + StatusCode7 + "shareholder relationship ownership  agentID "
							+ myArray.get(idx));
				}

				// FCA- (FCA-1372)
				String officrentityUrl = baseURI + "/v1/officers" + myArray.get(idx) + "/entities";

				int StatusCode8 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(officrentityUrl).getStatusCode();

				if (StatusCode8 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println("status " + StatusCode8 + "officer Entity AgentID " + myArray.get(idx));
				}

				String officrentityRelationshipUrl = baseURI + "/v1/officers" + myArray.get(idx)
						+ "/relationships/entities";

				int StatusCode9 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(officrentityRelationshipUrl).getStatusCode();

				if (StatusCode9 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println(
							"status " + StatusCode9 + "officer Entity Relationship AgentID " + myArray.get(idx));
				}

				String companiesurl = baseURI + "/companies" + myArray.get(idx);
				int StatusCode10 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(companiesurl).getStatusCode();

				if (StatusCode10 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println("status " + StatusCode10 + "companies AgentID " + myArray.get(idx));
				}

				String compniesEntity = companiesurl + "/entities";

				int StatusCode11 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(compniesEntity).getStatusCode();

				if (StatusCode11 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println("status " + StatusCode11 + "companies entity AgentID " + myArray.get(idx));
				}

				String compniesRelationEntity = companiesurl + "relationships/entities";

				int StatusCode12 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when()
						.get(compniesRelationEntity).getStatusCode();

				if (StatusCode12 == 200) {
					System.out.println(myArray.get(idx));
				} else {
					System.err.println(
							"status " + StatusCode12 + "companies relationship entity AgentID " + myArray.get(idx));
				}

			});

		}
		try {
			System.out.println("attempt to shutdown executor");
			executor.shutdown();
			executor.awaitTermination(12, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			System.err.println("tasks interrupted");
		}

	}

	@Test

	public void MetaData_CategoryLinks() {

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("Accept", acceptValue).header("Content", contentValue).when().get(metaUrl).then()
				.statusCode(200).extract().response();

		List<String> selflinks = res.path("data.relationships.categories.links.self");
		List<String> relatedlinks = res.path("data.relationships.categories.links.related");

		System.out.println(relatedlinks);

		for (int i = 0; i <10; i++) {

		String selfCatergorylinksUrl=selflinks.get(i);
	    // System.out.println(selfCatergorylinksUrl);
			int statuscode = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
					.header("Content", contentValue).when().get(selfCatergorylinksUrl)

					.statusCode();
			
			if (statuscode == 200) {
				System.out.println(selflinks.get(i));
			} else {
				System.err.println("status code " + statuscode + " selflink " + selflinks.get(i));
			}
			
			String relatedCategryLinksUrl=relatedlinks.get(i);
			
			//System.out.println(relatedCategryLinksUrl);
			
			int statuscode1 = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
					.header("Content", contentValue).when().get(relatedCategryLinksUrl)

					.statusCode();
			
			if (statuscode1 == 200) {
				System.out.println(relatedlinks.get(i));
			} else {
				System.err.println("status code " + statuscode1 + " relatedLinks " + relatedlinks.get(i));
			}



		}

	}

}
