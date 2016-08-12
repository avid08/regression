package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
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

import groovy.json.internal.Charsets;

public class Sprint23 {

	public Response response;
	String myjson;
	String AuthrztionValue;
	String baseURI;
	String env;
	String metaEndPoint = "/v1/metadata/fields"; // Metadata-EndPoint
	String metaUrl = baseURI + metaEndPoint;
	String dataEndPoint = "/v1/data/valueRequest";
	String dataPostUrl = baseURI + dataEndPoint; // Data Aggregator -EndPoint
	String XappClintIDvalue = "3dab0f06-eb00-4bee-8966-268a0ee27ba0";
	String acceptValue = "application/vnd.api+json";
	String contentValue = "application/vnd.api+json";
    public static int id = 1010077;
    public static boolean publishFlag = true;
    public static ArrayList<String> DBRes = new ArrayList<String>();
    public static ArrayList<String> APIRes = new ArrayList<String>();


	@BeforeClass
	public void executionSetup() {
		env = System.getProperty("env");
		System.out.println("Test Execution Environment: " + env);
		if (env == null) {
			baseURI = "https://api-qa.fitchconnect.com";
			this.AuthrztionValue = ("Basic MUlLVk1SMjlJS1lIMllPSjFUQkdGQ0tKSDpFN1Y2Z1FJY3RPeG5KbG8rSVBHaGY0K0tTSGc3LzFpOFJsbVo1Tmd6NUpB");
		} else if (env.equals("dev")) {
			baseURI = "https://api-dev.fitchconnect.com";
			this.AuthrztionValue = ("Basic NTA4Rk44V1BKTUdGVVI5VFpOREFEV0NCSzpvMVY5bkRCMG8yM3djSHp2eVlHNnZZb01GSkJWdG1KZmEwS20vbUczVWVV");

		} else if (env.equals("int")) {
			baseURI = "https://api-int.fitchconnect.com";
			this.AuthrztionValue = ("Basic MUtQNk1DVVk0WkU1SDFXVlVBWlJUVjNUSjpPM0owV0orUGVhZ3JqMis1bTBTMkdvdnZKRDBrQUd1R3F6Q0M5REIydjRv");

		} else if (env.equals("qa")) {
			baseURI = "https://api-qa.fitchconnect.com";
			this.AuthrztionValue = ("Basic MUlLVk1SMjlJS1lIMllPSjFUQkdGQ0tKSDpFN1Y2Z1FJY3RPeG5KbG8rSVBHaGY0K0tTSGc3LzFpOFJsbVo1Tmd6NUpB");
		} else if (env.equals("stage")) {
			baseURI = "https://api-stage.fitchconnect.com";
			this.AuthrztionValue = ("Basic NU5COUFRSDVCSTRDUFZTUktJRUpESjQyNTpDYjFxUXQycHd4VGNKZTg1SjkyRVJmL1JMU1haRUlZSjU3NWR5R3RacDVV");

		} else if (env.equals("prod")) {
			baseURI = "https://api.fitchconnect.com";
			this.AuthrztionValue = ("Basic M1FEREJQODMyQ1NKTlMwM1ZQT0NSQ0VFQjpENk9PUWtJVW5uaXhVZlZmL3loVnJhbHNDU1dzaGd0L1NJOGFTSFZEVTJR");

		}

		System.out.println(baseURI);
		metaUrl = baseURI + metaEndPoint;
		dataPostUrl = baseURI + dataEndPoint;
	}

	@Test

	public void FCA_1046_FitchConnect_URL_Post() throws IOException {
		URL file = Resources.getResource("FCURL_RiskConnctFlag_1046.json");
		myjson = Resources.toString(file, Charsets.UTF_8);

		Response FcUrlrespnse = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myjson).with()

				.when().post(dataPostUrl)

				.then().assertThat().statusCode(200)
				.body("data.attributes.entities[0].values[0].values[0].value[0]",
						equalTo("Korea Hydro & Nuclear Power Co., Ltd."))
				.body("data.attributes.entities[0].values[1].fitchFieldId", equalTo("FC_CONNECT_URL"))
				.body("data.attributes.entities[0].values[1].values[0].value[0]", Matchers.anything("https"))
				.body("data.attributes.entities[1].values[1].fitchFieldId", equalTo("FC_CONNECT_URL"))
				.body("data.attributes.entities[1].values[1].values", equalTo(null))

				.extract().response();

		Assert.assertFalse(FcUrlrespnse.asString().contains("isError"));
		Assert.assertFalse(FcUrlrespnse.asString().contains("isMissing"));

	}

	public void FCA_1046_Entities_FCURL_Get() {
		String FCuri = "/v1/entities/1064795";
		String FCUrl = baseURI + FCuri;

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(FCUrl).then().statusCode(200)
				.body("data.attributes.name", equalTo("Korea Hydro & Nuclear Power Co., Ltd."))
				.body("data.attributes.fitchConnectUrl",
						equalTo("https://app-uat.fitchconnect.com/entity/GRP_82254463"))
				.extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
	}

	@Test
	public void remveCH_Reference_1047() throws IOException {

		URL file = Resources.getResource("1047 LegalAgent Name.json");

		myjson = Resources.toString(file, Charsets.UTF_8);

		Response leglAgnetresponse = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myjson).with()

				.when().post(dataPostUrl)

				.then().assertThat().statusCode(200).body("data.attributes.entities[0].id", equalTo("1230051"))
				.body("data.attributes.entities[0].values[0].values[0].value[0]", equalTo("Montagu Evans LLP"))
				.body("data.attributes.entities[5].id", equalTo("1003619"))
				.body("data.attributes.entities[5].values[0].values[0].value[0]", equalTo("Ecobank Nigeria Ltd"))

				.extract().response();

		Assert.assertFalse(leglAgnetresponse.asString().contains("isError"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isMissing"));
	}

	@Test(enabled = true)
	public void Entity_Search_931() {

		String searchStringEnd = "/v1/entities?filter[name]=keno";
		String SerchUrl = baseURI + searchStringEnd;

		given().header("Authorization", AuthrztionValue)

				.header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(SerchUrl).then().assertThat().log().ifError()
				.statusCode(200)
			
				.body("data[0].attributes.name",equalTo("Kenosha"))
				.body(containsString("directors"));

	}
	
	@Test
	public void FlagIsTrueValueFromCH_FCA_1037() {
		String url = baseURI + "/v1/entities/1000685/ultimateParent";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then()
				.body("data.type", equalTo("entities"))
				.body("data.id", equalTo("107559"))
				.body("data.attributes.name", equalTo("Barclays plc"))
				.body("included[0].type", equalTo("companies"))
				.body("included[0].id", equalTo("107559"))
				.body("included[0].attributes.name", equalTo("Barclays plc"))
				.contentType(ContentType.JSON).extract().response();


		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
	}

	@Test
	public void FlagIsFalseValueFromEntity_1037() {
		String url = baseURI + "/v1/entities/100042/ultimateParent";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then()
				.body("data.type", equalTo("entities"))
				.body("data.id", equalTo("100042"))
				.body("data.attributes.name", equalTo("Capital One Financial Corporation"))
				.body("included[0].type", equalTo("companies"))
				.body("included[0].id", equalTo("100042"))
				.body("included[0].attributes.name", equalTo("Capital One Financial Corporation"))
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
	
	}

	@Test
	public void FlagIsTrue_1037() {
		String url = baseURI + "/v1/entities/1064795";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then()
				.body("data.type", equalTo("entities"))
				.body("data.id", equalTo("1064795"))
				.body("data.attributes.name", equalTo("Korea Hydro & Nuclear Power Co., Ltd."))
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
	}

	@Test
	public void FlagIsFalse_1037() {
		String url = baseURI + "/v1/entities/110630";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then()
				.body("data.type", equalTo("entities"))
				.body("data.id", equalTo("110630"))
				.body("data.attributes.name", equalTo("Bank of New York Mellon Corporation (The)"))
				.body("included[0].id", equalTo("110630"))
				.body("included[0].attributes.name", equalTo("The Bank of New York Mellon"))
				.body("included[0].attributes.ownershipType", equalTo("Direct"))
				.body("included[0].attributes.country", equalTo("USA"))
				.body("included[0].attributes.type", equalTo("Business Organization"))
				.body("included[0].attributes.parentId", equalTo(110630))



				.contentType(ContentType.JSON).extract().response();


		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
	}

	@Test
	public void test_FCA_1035() {
		
		
		String url = baseURI + "/v1/metadata/fields/FC_ULT_PARENT_ID";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then()
				.body("data.id", equalTo("FC_ULT_PARENT_ID")).body("data.type", equalTo("fields"))
				.body("data.attributes.displayName", equalTo("Ultimate Parent ID"))
				.body("data.attributes.fitchFieldDesc", equalTo("Ultimate Parent ID"))
				.body("data.relationships.service.data.id", equalTo("entitySummary"))
				.body("data.relationships.service.data.type", equalTo("dataService"))
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
	}
	
    @Test
    public void driver_1034() throws IOException {
           
           String FileName = "FCA_1034_FlgTrue.json";

           getDBResponsePubFlag_true_1034();
           getAPIResponsePubFlag_true_1034(FileName);

           System.out.println("Ulimate ParentIDs");
           if (DBRes.get(0).equals(APIRes.get(0))) {
                  System.out.println("DB Response: " + DBRes.get(0));
                  System.out.println("API Response: " + APIRes.get(0));
                  System.out.println("Parent IDs are a match.");
           } else {
                  System.out.println("DB Response: " + DBRes.get(0));
                  System.out.println("API Response: " + APIRes.get(0));
                  System.out.println("Parent IDs are not match.");

               
           }

           System.out.println("Ulimate Parents Names");
           if (DBRes.get(1).equals(APIRes.get(1))) {
                  System.out.println("DB Response: " + DBRes.get(1));
                  System.out.println("API Response: " + APIRes.get(1));
                  System.out.println("Parent Names are a match.");
           } else {
                  System.out.println("DB Response: " + DBRes.get(1));
                  System.out.println("API Response: " + APIRes.get(1));
                  System.out.println("Parent Names are not a match.");
                
           }
           
           id = 100024;
        FileName = "FCA_1034_FlgFalse.json";
           DBRes.clear();
           APIRes.clear();
           getDBResponsePubFlag_true_1034();
           getAPIResponsePubFlag_true_1034(FileName);
           
           System.out.println("Ulimate ParentIDs");
           if (DBRes.get(0).equals(APIRes.get(0))) {
                  System.out.println("DB Response: " + DBRes.get(0));
                  System.out.println("API Response: " + APIRes.get(0));
                  System.out.println("Parent IDs are a match.");
           } else {
                  System.out.println("DB Response: " + DBRes.get(0));
                  System.out.println("API Response: " + APIRes.get(0));
                  System.out.println("Parent IDs are not match.");

                 
           }

           System.out.println("Ulimate Parents Names");
           if (DBRes.get(1).equals(APIRes.get(1))) {
                  System.out.println("DB Response: " + DBRes.get(1));
                  System.out.println("API Response: " + APIRes.get(1));
                  System.out.println("Parent Names are a match.");
           } else {
                  System.out.println("DB Response: " + DBRes.get(1));
                  System.out.println("API Response: " + APIRes.get(1));
                  System.out.println("Parent Names are not a match.");
                 
           }
           

    }



    public void getDBResponsePubFlag_true_1034() {
    


           try {

                  MongoClient mongoClient = new MongoClient("mongoweb-x01", 27017);

                  DB db = mongoClient.getDB("admin");
                  db.authenticate("reporter", "the_call".toCharArray());

                  db = mongoClient.getDB("esp-dev-9");

                  DBCollection collection = db.getCollection("corpHierarchy");

                  DBObject match = new BasicDBObject("$match", new BasicDBObject("agentID", id));

                  DBObject project = new BasicDBObject("$project",
                               new BasicDBObject("agentID", 1).append("uParentAgentID", 1));

                  AggregationOutput output = collection.aggregate(match, project);

                  for (DBObject result : output.results()) {
                        DBRes.add((String) result.get("uParentAgentID").toString());
                  }

                  int parentId = Integer.parseInt(DBRes.get(0));

                  db = mongoClient.getDB("esp-dev-9");

                  collection = db.getCollection("fitch_entity");

                  match = new BasicDBObject("$match", new BasicDBObject("agentID", parentId));

                  project = new BasicDBObject("$project", new BasicDBObject("agentID", 1).append("agentLegalName", 1));

                  output = collection.aggregate(match, project);

                  for (DBObject result : output.results()) {
                        DBRes.add((String) result.get("agentLegalName").toString());
                  }

           } catch (Exception e) {
                  System.err.println(e.getClass().getName() + ": " + e.getMessage());
           }


    }

   
    public void getAPIResponsePubFlag_true_1034(String name) throws IOException {
           
           
        URL file = Resources.getResource(name);
           String myJson = Resources.toString(file,Charsets.UTF_8);
           
           Response res =  given ()
                        .header("Authorization", AuthrztionValue)
                        .header("X-App-Client-Id", XappClintIDvalue)
                        .contentType("application/vnd.api+json")
                        .body(myJson).with()
                  .when()
                        .post(dataPostUrl)
                  .then()

                  .assertThat().log().ifError().statusCode(200)
                  .extract().response();
           Assert.assertNotNull(res);
           int temp = res.path("data.attributes.entities[0].values[1].values[0].value[0]");
           String tmp; 
           tmp = Integer.toString(temp); 
           APIRes.add(tmp);
           tmp = res.path("data.attributes.entities[0].values[0].values[0].value[0]");
           APIRes.add(tmp);

           
    
}
           

	
	
}
	

















	/*@Test
	public void driver_FCA_1034() throws IOException {
		int id = 1010077;
	 boolean publishFlag = true;
	 ArrayList<String> DBRes = new ArrayList<String>();
	 ArrayList<String> APIRes = new ArrayList<String>();
		getPublishFlag();

		getDBResponsePubFlag_true();
		getAPIResponsePubFlag_true();

		System.out.println("Ulimate ParentIDs");
		if (DBRes.get(0).equals(APIRes.get(0))) {
			System.out.println("DB Response: " + DBRes.get(0));
			System.out.println("API Response: " + APIRes.get(0));
			System.out.println("Parent IDs are a match.");
		} else {
			System.out.println("DB Response: " + DBRes.get(0));
			System.out.println("API Response: " + APIRes.get(0));
			System.out.println("Parent IDs are not match.");

	
		}

		System.out.println("Ulimate Parents Names");
		if (DBRes.get(1).equals(APIRes.get(1))) {
			System.out.println("DB Response: " + DBRes.get(1));
			System.out.println("API Response: " + APIRes.get(1));
			System.out.println("Parent Names are a match.");
		} else {
			System.out.println("DB Response: " + DBRes.get(1));
			System.out.println("API Response: " + APIRes.get(1));
			System.out.println("Parent Names are not a match.");
	
		}

		id = 100024;
		DBRes.clear();
		APIRes.clear();
		getPublishFlag();
		getDBResponsePubFlag_true();
		getAPIResponsePubFlag_true();

		System.out.println("Ulimate ParentIDs");
		if (DBRes.get(0).equals(APIRes.get(0))) {
			System.out.println("DB Response: " + DBRes.get(0));
			System.out.println("API Response: " + APIRes.get(0));
			System.out.println("Parent IDs are a match.");
		} else {
			System.out.println("DB Response: " + DBRes.get(0));
			System.out.println("API Response: " + APIRes.get(0));
			System.out.println("Parent IDs are not match.");

		
		}

		System.out.println("Ulimate Parents Names");
		if (DBRes.get(1).equals(APIRes.get(1))) {
			System.out.println("DB Response: " + DBRes.get(1));
			System.out.println("API Response: " + APIRes.get(1));
			System.out.println("Parent Names are a match.");
		} else {
			System.out.println("DB Response: " + DBRes.get(1));
			System.out.println("API Response: " + APIRes.get(1));
			System.out.println("Parent Names are not a match.");

		}


	}

	public void getPublishFlag() {
		try {
			MongoClient mongoClient = new MongoClient("mongoweb-x01", 27017);

			DB db = mongoClient.getDB("admin");
			db.authenticate("reporter", "the_call".toCharArray());

			db = mongoClient.getDB("esp-dev-9");

			DBCollection collection = db.getCollection("corpHierarchy");

			DBObject match = new BasicDBObject("$match", new BasicDBObject("agentID", id));

			DBObject project = new BasicDBObject("$project", new BasicDBObject("agentID", 1).append("publishFlag", 1));

			AggregationOutput output = collection.aggregate(match, project);

			for (DBObject result : output.results()) {
				publishFlag = (Boolean) result.get("publishFlag");
			}

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}


	}

	public void getDBResponsePubFlag_true() {

//		if(publishFlag == true){

		try {

			MongoClient mongoClient = new MongoClient("mongoweb-x01", 27017);

			DB db = mongoClient.getDB("admin");
			db.authenticate("reporter", "the_call".toCharArray());

			db = mongoClient.getDB("esp-dev-9");

			DBCollection collection = db.getCollection("corpHierarchy");

			DBObject match = new BasicDBObject("$match", new BasicDBObject("agentID", id));

			DBObject project = new BasicDBObject("$project",
					new BasicDBObject("agentID", 1).append("uParentAgentID", 1));

			AggregationOutput output = collection.aggregate(match, project);

			for (DBObject result : output.results()) {
				DBRes.add((String) result.get("uParentAgentID").toString());
			}

			int parentId = Integer.parseInt(DBRes.get(0));

			db = mongoClient.getDB("esp-dev-9");

			collection = db.getCollection("fitch_entity");

			match = new BasicDBObject("$match", new BasicDBObject("agentID", parentId));

			project = new BasicDBObject("$project", new BasicDBObject("agentID", 1).append("agentLegalName", 1));

			output = collection.aggregate(match, project);

			for (DBObject result : output.results()) {
				DBRes.add((String) result.get("agentLegalName").toString());
			}

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
//	 }else{
//
//			try {
//
//				MongoClient mongoClient = new MongoClient("mongoweb-x01", 27017);
//
//				DB db = mongoClient.getDB("admin");
//				db.authenticate("reporter", "the_call".toCharArray());
//
//				db = mongoClient.getDB("esp-dev-9");
//
//				DBCollection collection = db.getCollection("fitch_entity");
//
//				DBObject match = new BasicDBObject("$match", new BasicDBObject("agentID", id));
//
//				DBObject project = new BasicDBObject("$project",
//						new BasicDBObject("agentID", 1).append("ultimateParent.name", 1));
//
//				AggregationOutput output = collection.aggregate(match, project);
//
//				for (DBObject result : output.results()) {
//
//					System.out.println(result);
//					DBRes.add((String) result.get("ultimateParent"));
//				}
//
//			} catch (Exception e) {
//				System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			}
//
//	 }

	}

	@SuppressWarnings("unchecked")
	public void getAPIResponsePubFlag_true() throws IOException {
		String res = null;
		int temp;

		String jsonString = null;

		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		obj.put("data", data);

		JSONObject attributes = new JSONObject();

		JSONArray list1 = new JSONArray();
		JSONObject entities = new JSONObject();
		entities.put("id", id);
		entities.put("type", "FitchID");
		list1.add(entities);

		attributes.put("fitchFieldIds", "FC_ULT_PARENT_ID");
		attributes.put("entities", list1);

		data.put("attributes", attributes);
		data.put("type", "entities");
		jsonString = obj.toString();

		Charset.forName("UTF-8").encode(jsonString);

		Response Response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(jsonString).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).extract().response();

		

		temp = Response.path("data.attributes.entities[0].values[0].values[0].value[0]");
		res = Integer.toString(temp);
		APIRes.add(res);



	 obj = new JSONObject();
	 data = new JSONObject();
	obj.put("data", data);

	 attributes = new JSONObject();

	list1 = new JSONArray();
	entities = new JSONObject();
	entities.put("id", id);
	entities.put("type", "FitchID");
	list1.add(entities);

	attributes.put("fitchFieldIds", "FC_ULT_PARENT");
	attributes.put("entities", list1);

	data.put("attributes", attributes);
	data.put("type", "entities");
	jsonString = obj.toString();

	Charset.forName("UTF-8").encode(jsonString);

	 Response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.contentType(contentValue).body(jsonString).with()

			.when().post(dataPostUrl)

			.then().assertThat().log().ifError().statusCode(200).extract().response();

	
		res = Response.path("data.attributes.entities[0].values[0].values[0].value[0]");
		APIRes.add(res);

	}
*/
