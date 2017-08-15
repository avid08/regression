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

public class Sprint25 extends Configuration{
	

	private static final String priority = null;

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
				
								
				.body(containsString("A"))				
				.body(containsString("Annual"))
				.body(containsString("Q1"))
				.body(containsString("Q1"))
				.body(containsString("Q2"))
				.body(containsString("Q4"))	
				.body(containsString("Q3"))
				.body(containsString("periodResolution"))
				.body(containsString("dateOptions"))
				.body(containsString("periods"))
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

		System.out.println("id "+id);

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
		
		System.out.println("id1 "+id1);
		 
	 
  }
 
 @Test(enabled=false )
 public void updateViewfield (){
	   
	   String jsonString1 = null;
	   
	
	   String upDteviewFieldUri = "/v1/viewfields/"; 
	   String updateViewFildUrl= baseURI + upDteviewFieldUri +id;
	   
	   System.out.println("udpateViewfieldUrl "+updateViewFildUrl);


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
		System.out.println("viewfieldUpdate Request"+jsonString1);
		
		
		
		 Response res = given()

		.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
		.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
		
		.body(jsonString1).with().when().patch(updateViewFildUrl).then().statusCode(200)
		.body("data.id",equalTo(id))
		.body("data.attributes.displayName", equalTo("Aminul's VF updates"))
		
		
		.extract().response();
		
		 
		 
	   
 }
	
	@Test(enabled=false )
  public void updateViewDef () {
		
	   String upDteviewDefUri = "/v1/viewdefs/"; 
	   String updateViewdefUrl= baseURI + upDteviewDefUri +id1;
	   
	   System.out.println("udpateview Def url "+updateViewdefUrl);
		   
		   
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

		System.out.println("viewDef update request "+jsonString2);

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
 
 public void fca_1191 () {
	 
	 String StatmentLinkURI= baseURI+"/v1/entities/1708/statements";
	 
	 Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
             .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
             .when().get(StatmentLinkURI).then()
             .body(containsString("stmntId"))
             .body(containsString("stmntDate"))
             .body(containsString("periodType"))
             .body(containsString("inflationAdjusted"))
             .body(containsString("periodYear"))
             .body(containsString("detail"))             
             .body(containsString("consolidation"))
             
             .extract().response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));
	 
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
 
 @Test()
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
                     .body(containsString("value"))
                     .body(containsString("timeIntervalDate"))
                     .body(containsString("type"))
                     .body(containsString("year"))                   
                     
              .extract().response();

       Assert.assertFalse(XREF.asString().contains("isError"));
       Assert.assertFalse(XREF.asString().contains("isMissing"));
       Assert.assertFalse(XREF.asString().contains("isRestricted"));
 }

}	 
	 
	 
	 

