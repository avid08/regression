package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
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

public class Sprint25{
	public Response response;
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
	public void entity_includedShareHolders_() {

		String IncludeUri = "/v1/entities/115740/shareholders?include[shareholders]=shareholders.shareholderEntity";
		String IcludedURL = baseURI + IncludeUri;

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(IcludedURL).then()
				.statusCode(200).extract().response();

		List<String> company = res.path("included.relationships.company.links");
		List<String> Ulparent = res.path("included.relationships.ultimateParent.links");
		List<String> shareholdr = res.path("included.relationships.shareholders.links");
		List<String> directors = res.path("included.relationships.directors.links");
		List<String> fitchIssurRting = res.path("included.relationships.fitchIssuerRatings.links");
		List<String> statemnts = res.path("included.relationships.statements.links");

		for (int i = 0; i > company.size(); i++) {

			Assert.assertNotNull(company.get(i));
			Assert.assertNotNull(Ulparent.get(i));
			Assert.assertNotNull(shareholdr.get(i));
			Assert.assertNotNull(directors.get(i));
			Assert.assertNotNull(fitchIssurRting.get(i));
			Assert.assertNotNull(statemnts.get(i));

		}
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void relative_period_ref() throws IOException {

		URL file = Resources.getResource("relative_Period_ref.json");
		myjson = Resources.toString(file, Charsets.UTF_8);

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).contentType("application/vnd.api+json").body(myjson).with().when()
				.post(dataPostUrl).then().statusCode(200)
				.body("data.attributes.entities[0].values[0].values[0].timeIntervalPeriod.type", equalTo("Q3"))
				.body("data.attributes.entities[0].values[0].values[0].timeIntervalPeriod.year", equalTo(2014))					
				.body(containsString("A"))				
				.body(containsString("Annual"))
				.body(containsString("Q1"))
				.body(containsString("Q1"))
				.body(containsString("Q2"))
				.body(containsString("Q4"))	
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
	   Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
 @Test(priority=1)
 
 public void viewfield_and_viewDef_creation() throws IOException  {
	 String CrteviewfildUri = "/v1/viewfields";
		String CrteViewfildUrl = baseURI + CrteviewfildUri;
		String CrteViewDefUri = "/v1/viewdefs";
		String CrteViewDefUrl = baseURI + CrteViewDefUri;

		URL file = Resources.getResource("createViewField.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.body(myJson).with().when().post(CrteViewfildUrl).then().statusCode(201).extract().response();

		this.id = res.path("data.id");

		System.out.println("id"+id);

		String jsonString = null;

		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject type = new JSONObject();

		obj.put("data", data);

		data.put("type", "viewdefs");

		JSONObject attributes = new JSONObject();
		getClass();
		JSONObject field = new JSONObject();
		getClass();

		type.put("attributes", attributes);
		JSONArray list1 = new JSONArray();
		JSONArray list2 = new JSONArray();
		JSONObject relationships = new JSONObject();
		JSONObject data1 = new JSONObject();
		JSONObject data2 = new JSONObject();
		

		attributes.put("name", "CorporateViewdef");
		attributes.put("suppressColumn", "true");
		attributes.put("tags", list1);
		list1.add("Corporate");
		data.put("relationships", relationships);

		data.put("attributes", attributes);
		relationships.put("viewfields", field);
		field.put("data", list2);
		list2.add(data2);
		data2.put("type", "viewfields");
		data2.put("id", id);
		jsonString = obj.toString();

		//System.out.println("json"+jsonString);

		Response resViewDef = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.body(jsonString).with().when().post(CrteViewDefUrl).then().statusCode(201)
				.body("data.relationships.viewfields.data[0].id", equalTo(id)).extract().response();

		this.id1 = resViewDef.path("data.id");
		
		System.out.println("id1"+id1);
		 
	 
  }
 
 @Test(priority=2)
 public void updateViewfield (){
	   
	   String jsonString1 = null;
	   
	
	   String upDteviewFieldUri = "/v1/viewfields/"; 
	   String updateViewFildUrl= baseURI + upDteviewFieldUri +id;


		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject type = new JSONObject();

		obj.put("data", data);

		data.put("type", "viewfields");
		data.put("id", id);

		JSONObject attributes = new JSONObject();
		getClass();
		JSONObject field = new JSONObject();
		getClass();

		type.put("attributes", attributes);
		JSONObject list1 = new JSONObject();
		JSONObject list2 = new JSONObject();
		JSONObject relationships = new JSONObject();
		JSONObject data1 = new JSONObject();
		JSONObject data2 = new JSONObject();
		

		attributes.put("displayName", "Aminul's VF updates");
		attributes.put("suppressColumn", "true");
		attributes.put("dataMask", "12");
		
		data.put("relationships", relationships);

		data.put("attributes", attributes);
		relationships.put("field", field);
		field.put("data", list2);
		
		list2.put("type", "viewfields");
		list2.put("id", "FC_CONNECT_URL");
		jsonString1 = obj.toString();
		//System.out.println("json1"+jsonString1);
		
		
		
		 Response res = given()

		.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
		.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
		
		.body(jsonString1).with().when().patch(updateViewFildUrl).then().statusCode(200)
		.body("data.id",equalTo(id))
		.body("data.attributes.displayName", equalTo("Aminul's VF updates"))
		
		
		.extract().response();
		
		 
		 
	   
 }
	
	@Test(priority=3)
  public void updateViewDef () {
		
	   String upDteviewDefUri = "/v1/viewdefs/"; 
	   String updateViewdefUrl= baseURI + upDteviewDefUri +id1;
		   
		   
		String jsonString2 = null;

		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject type = new JSONObject();

		obj.put("data", data);
		

		data.put("type", "viewdefs");
		data.put("id",id1);

		JSONObject attributes = new JSONObject();
		getClass();
		JSONObject field = new JSONObject();
		getClass();

		type.put("attributes", attributes);
		JSONArray list1 = new JSONArray();
		JSONArray list2 = new JSONArray();
		JSONObject relationships = new JSONObject();
		JSONObject data1 = new JSONObject();
		JSONObject data2 = new JSONObject();
		

		attributes.put("name", "Aminul's VD updates");
		attributes.put("suppressColumn", "true");
		attributes.put("tags", list1);
		list1.add("Test");
		data.put("relationships", relationships);

		data.put("attributes", attributes);
		relationships.put("viewfields", field);
		field.put("data", list2);
		list2.add(data2);
		data2.put("type", "viewfields");
		data2.put("id", id);
		jsonString2 = obj.toString();

		System.out.println("json2"+jsonString2);

		 Response res = given()

					.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
					
					.body(jsonString2).with().when().patch(updateViewdefUrl).then().statusCode(200)
					.body("data.id",equalTo(id1))
					.body("data.attributes.name", equalTo("Aminul's VD updates"))
					
					
					.extract().response();
		
		
	}

 
 
 @Test(enabled=false)

 public void FCA_1191() throws IOException {

		List<DBObject> pipeline = new ArrayList<>();
		DBObject match = new BasicDBObject("$match", new BasicDBObject("accntSys.accntSysDesc","U.S. GAAP"));
		//.append("ratings", new BasicDBObject("$exists", true)) ;
		
		pipeline.add(match);

		ArrayList<Integer> myArray = new ArrayList();
		
		//System.out.println(myArray);

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
			
      for (int i=0;i<1;i++){

       String statementID = baseURI + "/v1/statements/"+myArray.get(i);

       Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                     .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                     .when().get(statementID).then().statusCode(200).body("data.type", equalTo("statements")).extract()
                     .response();
       Assert.assertFalse(res.asString().contains("isError"));
       Assert.assertFalse(res.asString().contains("isMissing"));
       
      }
		} catch (Exception e) {
			System.err.println("try catch error " + e.getClass().getName() + ": " + e.getMessage());
		}

 }
 
 @Test

 public void FCA_1138() throws IOException {

       String descendantsincluded = baseURI + "/v1/companies/107559/descendants?include[descendants]=descendants.entity";

       Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                     .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                     .when().get(descendantsincluded).then().body("included[0].type", equalTo("entities")).extract()
                     .response();
       Assert.assertFalse(res.asString().contains("isError"));
       Assert.assertFalse(res.asString().contains("isMissing"));
       Assert.assertFalse(res.asString().contains("isRestricted"));

 }
 
 @Test
 public void FCA_467() throws IOException {

       URL file = Resources.getResource("FCA_467.json");

       myjson = Resources.toString(file, Charsets.UTF_8);
       String dep = "/v1/data/valueRequest";
       String xrefurl = baseURI + dep;

       Response XREF = given()

                     .header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                     .contentType("application/vnd.api+json").body(myjson).with()

                     .when().post(xrefurl)

                     .then().assertThat().log().ifError().statusCode(200).body("data.attributes.issues[0].id", equalTo("US69348LE547"))
                     .body("data.attributes.issues[0].type", equalTo("ISIN"))
                     .body("data.attributes.issues[0].values[0].fitchFieldId", equalTo("FC_IRR"))
              .extract().response();

       Assert.assertFalse(XREF.asString().contains("isError"));
       Assert.assertFalse(XREF.asString().contains("isMissing"));
       Assert.assertFalse(XREF.asString().contains("isRestricted"));
 }

}	 
	 
	 
	 

