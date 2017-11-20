package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Assert;
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

public class Sprint23 extends Configuration {


	public static int id = 1010077;
	public static boolean publishFlag = true;
	public static ArrayList<String> DBRes = new ArrayList<String>();
	public static ArrayList<String> APIRes = new ArrayList<String>();


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
		Assert.assertFalse(FcUrlrespnse.asString().contains("isRestricted"));

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
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test
	public void remveCH_Reference_1047() throws IOException {

		URL file = Resources.getResource("1047_LegalAgent_name.json");

		myjson = Resources.toString(file, Charsets.UTF_8);

		Response leglAgnetresponse = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myjson).with()

				.when().post(dataPostUrl)

				.then().assertThat().statusCode(200)
				.body(containsString("The Standard Bank of South Africa Limited"))
				.body(containsString("Republic Financial Holdings Limited"))
				.body(containsString("JSC Citibank Kazakhstan"))
				

				.extract().response();

		Assert.assertFalse(leglAgnetresponse.asString().contains("isError"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isMissing"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isRestricted"));
	}

	@Test(enabled = true)
	public void Entity_Search_931() {

		String searchStringEnd = "/v1/entities?filter[name]=keno";
		String SerchUrl = baseURI + searchStringEnd;

		given().header("Authorization", AuthrztionValue)

				.header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(SerchUrl).then().assertThat().log().ifError()
				.statusCode(200)

				.body("data[0].attributes.name", equalTo("Kenosha")).body(containsString("directors"));

	}

	@Test
	public void FlagIsTrueValueFromCH_FCA_1037() {
		String url = baseURI + "/v1/entities/107559/ultimateParent";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then().statusCode(200)
				.body("data.type", equalTo("entities")).body("data.id", equalTo("1418754"))
				//.body("data.attributes.name", equalTo("Barclays plc"))
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test
	public void FlagIsFalseValueFromEntity_1037() {
		String url = baseURI + "/v1/entities/100042/ultimateParent";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then()
				.body("data.type", equalTo("entities")).body("data.id", equalTo("100042"))
				.body("data.attributes.name", equalTo("Capital One Financial Corporation")).statusCode(200)

				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FlagIsTrue_1037() {
		String url = baseURI + "/v1/entities/1064795";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then()
				.body("data.type", equalTo("entities")).body("data.id", equalTo("1064795")).statusCode(200)
				.body("data.attributes.name", equalTo("Korea Hydro & Nuclear Power Co., Ltd."))
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test
	public void FlagIsFalse_1037() {
		String url = baseURI + "/v1/entities/1181314";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then()
				.body("data.type", equalTo("entities")).body("data.id", equalTo("1181314")).statusCode(200)
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
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
				.body("data.relationships.service.data.type", equalTo("dataService")).statusCode(200)
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test(enabled=false)
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
			System.out.println("Parent IDs are a matching.");
		} else {
			System.out.println("DB Response: " + DBRes.get(0));
			System.out.println("API Response: " + APIRes.get(0));
			System.err.println("Parent IDs are not matching.");

		}

		System.out.println("Ulimate Parents Names");
		if (DBRes.get(1).equals(APIRes.get(1))) {
			System.out.println("DB Response: " + DBRes.get(1));
			System.out.println("API Response: " + APIRes.get(1));
			System.out.println("Parent Names are a matching.");
		} else {
			System.out.println("DB Response: " + DBRes.get(1));
			System.out.println("API Response: " + APIRes.get(1));
			System.err.println("Parent Names are not a matching.");

		}

	}

	public void getDBResponsePubFlag_true_1034() {

		try {
			MongoCredential credential = MongoCredential.createCredential("reporter", "admin", "the_call".toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseServer1, 27017), Arrays.asList(credential));

			/*MongoClient mongoClient = new MongoClient("mongoweb-x01", 27017);

			DB db = mongoClient.getDB("admin");
			db.authenticate("reporter", "the_call".toCharArray());*/

			DB db = mongoClient.getDB("esp-dev-9");

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
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()

				.assertThat().log().ifError().statusCode(200).extract().response();
		Assert.assertNotNull(res);
		int temp = res.path("data.attributes.entities[0].values[1].values[0].value[0]");
		System.out.println("data is " + temp);
		String tmp;
		tmp = Integer.toString(temp);
		APIRes.add(tmp);
		tmp = res.path("data.attributes.entities[0].values[0].values[0].value[0]");
		APIRes.add(tmp);

	}

}

// some JDBC
// work======================================================================================!!

/*
 * @Test public void driver_FCA_1034() throws IOException { int id = 1010077;
 * boolean publishFlag = true; ArrayList<String> DBRes = new
 * ArrayList<String>(); ArrayList<String> APIRes = new ArrayList<String>();
 * getPublishFlag();
 * 
 * getDBResponsePubFlag_true(); getAPIResponsePubFlag_true();
 * 
 * System.out.println("Ulimate ParentIDs"); if
 * (DBRes.get(0).equals(APIRes.get(0))) { System.out.println("DB Response: " +
 * DBRes.get(0)); System.out.println("API Response: " + APIRes.get(0));
 * System.out.println("Parent IDs are a match."); } else { System.out.println(
 * "DB Response: " + DBRes.get(0)); System.out.println("API Response: " +
 * APIRes.get(0)); System.out.println("Parent IDs are not match.");
 * 
 * 
 * }
 * 
 * System.out.println("Ulimate Parents Names"); if
 * (DBRes.get(1).equals(APIRes.get(1))) { System.out.println("DB Response: " +
 * DBRes.get(1)); System.out.println("API Response: " + APIRes.get(1));
 * System.out.println("Parent Names are a match."); } else { System.out.println(
 * "DB Response: " + DBRes.get(1)); System.out.println("API Response: " +
 * APIRes.get(1)); System.out.println("Parent Names are not a match.");
 * 
 * }
 * 
 * id = 100024; DBRes.clear(); APIRes.clear(); getPublishFlag();
 * getDBResponsePubFlag_true(); getAPIResponsePubFlag_true();
 * 
 * System.out.println("Ulimate ParentIDs"); if
 * (DBRes.get(0).equals(APIRes.get(0))) { System.out.println("DB Response: " +
 * DBRes.get(0)); System.out.println("API Response: " + APIRes.get(0));
 * System.out.println("Parent IDs are a match."); } else { System.out.println(
 * "DB Response: " + DBRes.get(0)); System.out.println("API Response: " +
 * APIRes.get(0)); System.out.println("Parent IDs are not match.");
 * 
 * 
 * }
 * 
 * System.out.println("Ulimate Parents Names"); if
 * (DBRes.get(1).equals(APIRes.get(1))) { System.out.println("DB Response: " +
 * DBRes.get(1)); System.out.println("API Response: " + APIRes.get(1));
 * System.out.println("Parent Names are a match."); } else { System.out.println(
 * "DB Response: " + DBRes.get(1)); System.out.println("API Response: " +
 * APIRes.get(1)); System.out.println("Parent Names are not a match.");
 * 
 * }
 * 
 * 
 * }
 * 
 * public void getPublishFlag() { try { MongoClient mongoClient = new
 * MongoClient("mongoweb-x01", 27017);
 * 
 * DB db = mongoClient.getDB("admin"); db.authenticate("reporter",
 * "the_call".toCharArray());
 * 
 * db = mongoClient.getDB("esp-dev-9");
 * 
 * DBCollection collection = db.getCollection("corpHierarchy");
 * 
 * DBObject match = new BasicDBObject("$match", new BasicDBObject("agentID",
 * id));
 * 
 * DBObject project = new BasicDBObject("$project", new BasicDBObject("agentID",
 * 1).append("publishFlag", 1));
 * 
 * AggregationOutput output = collection.aggregate(match, project);
 * 
 * for (DBObject result : output.results()) { publishFlag = (Boolean)
 * result.get("publishFlag"); }
 * 
 * } catch (Exception e) { System.err.println(e.getClass().getName() + ": " +
 * e.getMessage()); }
 * 
 * 
 * }
 * 
 * public void getDBResponsePubFlag_true() {
 * 
 * // if(publishFlag == true){
 * 
 * try {
 * 
 * MongoClient mongoClient = new MongoClient("mongoweb-x01", 27017);
 * 
 * DB db = mongoClient.getDB("admin"); db.authenticate("reporter",
 * "the_call".toCharArray());
 * 
 * db = mongoClient.getDB("esp-dev-9");
 * 
 * DBCollection collection = db.getCollection("corpHierarchy");
 * 
 * DBObject match = new BasicDBObject("$match", new BasicDBObject("agentID",
 * id));
 * 
 * DBObject project = new BasicDBObject("$project", new BasicDBObject("agentID",
 * 1).append("uParentAgentID", 1));
 * 
 * AggregationOutput output = collection.aggregate(match, project);
 * 
 * for (DBObject result : output.results()) { DBRes.add((String)
 * result.get("uParentAgentID").toString()); }
 * 
 * int parentId = Integer.parseInt(DBRes.get(0));
 * 
 * db = mongoClient.getDB("esp-dev-9");
 * 
 * collection = db.getCollection("fitch_entity");
 * 
 * match = new BasicDBObject("$match", new BasicDBObject("agentID", parentId));
 * 
 * project = new BasicDBObject("$project", new BasicDBObject("agentID",
 * 1).append("agentLegalName", 1));
 * 
 * output = collection.aggregate(match, project);
 * 
 * for (DBObject result : output.results()) { DBRes.add((String)
 * result.get("agentLegalName").toString()); }
 * 
 * } catch (Exception e) { System.err.println(e.getClass().getName() + ": " +
 * e.getMessage()); } // }else{ // // try { // // MongoClient mongoClient = new
 * MongoClient("mongoweb-x01", 27017); // // DB db = mongoClient.getDB("admin");
 * // db.authenticate("reporter", "the_call".toCharArray()); // // db =
 * mongoClient.getDB("esp-dev-9"); // // DBCollection collection =
 * db.getCollection("fitch_entity"); // // DBObject match = new
 * BasicDBObject("$match", new BasicDBObject("agentID", id)); // // DBObject
 * project = new BasicDBObject("$project", // new BasicDBObject("agentID",
 * 1).append("ultimateParent.name", 1)); // // AggregationOutput output =
 * collection.aggregate(match, project); // // for (DBObject result :
 * output.results()) { // // System.out.println(result); // DBRes.add((String)
 * result.get("ultimateParent")); // } // // } catch (Exception e) { //
 * System.err.println(e.getClass().getName() + ": " + e.getMessage()); // } //
 * // }
 * 
 * }
 * 
 * @SuppressWarnings("unchecked") public void getAPIResponsePubFlag_true()
 * throws IOException { String res = null; int temp;
 * 
 * String jsonString = null;
 * 
 * JSONObject obj = new JSONObject(); JSONObject data = new JSONObject();
 * obj.put("data", data);
 * 
 * JSONObject attributes = new JSONObject();
 * 
 * JSONArray list1 = new JSONArray(); JSONObject entities = new JSONObject();
 * entities.put("id", id); entities.put("type", "FitchID"); list1.add(entities);
 * 
 * attributes.put("fitchFieldIds", "FC_ULT_PARENT_ID");
 * attributes.put("entities", list1);
 * 
 * data.put("attributes", attributes); data.put("type", "entities"); jsonString
 * = obj.toString();
 * 
 * Charset.forName("UTF-8").encode(jsonString);
 * 
 * Response Response = given().header("Authorization",
 * AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
 * .contentType(contentValue).body(jsonString).with()
 * 
 * .when().post(dataPostUrl)
 * 
 * .then().assertThat().log().ifError().statusCode(200).extract().response();
 * 
 * 
 * 
 * temp =
 * Response.path("data.attributes.entities[0].values[0].values[0].value[0]");
 * res = Integer.toString(temp); APIRes.add(res);
 * 
 * 
 * 
 * obj = new JSONObject(); data = new JSONObject(); obj.put("data", data);
 * 
 * attributes = new JSONObject();
 * 
 * list1 = new JSONArray(); entities = new JSONObject(); entities.put("id", id);
 * entities.put("type", "FitchID"); list1.add(entities);
 * 
 * attributes.put("fitchFieldIds", "FC_ULT_PARENT"); attributes.put("entities",
 * list1);
 * 
 * data.put("attributes", attributes); data.put("type", "entities"); jsonString
 * = obj.toString();
 * 
 * Charset.forName("UTF-8").encode(jsonString);
 * 
 * Response = given().header("Authorization",
 * AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
 * .contentType(contentValue).body(jsonString).with()
 * 
 * .when().post(dataPostUrl)
 * 
 * .then().assertThat().log().ifError().statusCode(200).extract().response();
 * 
 * 
 * res =
 * Response.path("data.attributes.entities[0].values[0].values[0].value[0]");
 * APIRes.add(res);
 * 
 * }
 */
