package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint24 {

	
	String myjson;
	String AuthrztionValue;
	String baseURI;
	String env;
	String id;
	String id1;
	String databaseFitchEnty;
	String dataBaseServer1;
	String dataBaseServer2;
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
			baseURI = "https://api.fitchconnect-int.com";
			this.AuthrztionValue = ("Basic WkRCSkg4WkpPWEg0S0dQNkZaRE9MVUpDWDp3VTlYWHpjakxsMWZYbldwM1lZaXBhU0VUcXZMTmtIY3hCK09ydXdRSHJB");
			// dataBaseServer = "mongoweb-x01";
			dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
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
			dataBaseServer1 ="mgo-pue1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		}

		System.out.println(baseURI);
		metaUrl = baseURI + metaEndPoint;
		dataPostUrl = baseURI + dataEndPoint;

	}
	@Test

	public void EntityStatement_fields_718() {
		String stmenturi = "/v1/statements?filter[fitchId]=111676";
		String fildStmentURI = baseURI + stmenturi;

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(fildStmentURI).then()
				.statusCode(200).extract().response();

		List<String> value = res.path("data.attributes.detail.value.value");



		for (int i = 0; i < value.size(); i++) {

			Assert.assertNotNull(value.get(i));

		}
	}

	@Test(priority = 1)

	public void Create_Viewfields_1063() throws IOException {

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

		System.out.println("json"+jsonString);

		Response resViewDef = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.body(jsonString).with().when().post(CrteViewDefUrl).then().statusCode(201)
				.body("data.relationships.viewfields.data[0].id", equalTo(id)).extract().response();

		this.id1 = resViewDef.path("data.id");
		
		System.out.println("id1"+id1);

	}

	@Test(priority = 2)
	public void retrieve_viewDefs_1062() throws IOException {
		String CrteViewDefUri = "/v1/viewdefs";
		String CrteViewDefUrl = baseURI + CrteViewDefUri;

		Response listofViewDef = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).contentType("application/vnd.api+json").when().get(CrteViewDefUrl)
				.then().assertThat().statusCode(200).body(containsString(id)).extract().response();
		Assert.assertFalse(listofViewDef.asString().contains("isError"));
		Assert.assertFalse(listofViewDef.asString().contains("isMissing"));
		Assert.assertFalse(listofViewDef.asString().contains("isRestricted"));

	}

	@Test(priority = 3)
	public void Specific_viewDef_1076() {

		String CrteViewDefUri = "/v1/viewdefs/";
		String SpecficVewDefURl = baseURI + CrteViewDefUri + id1;
		System.out.println(SpecficVewDefURl);

		Response viewDef = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(SpecficVewDefURl).then().assertThat().statusCode(200).body(containsString(id))
				.body("data.id", equalTo(id1)).extract().response();

		Assert.assertNotNull(viewDef);
		
		Assert.assertFalse(viewDef.asString().contains("isError"));
		Assert.assertFalse(viewDef.asString().contains("isMissing"));
		Assert.assertFalse(viewDef.asString().contains("isRestricted"));
		

	}
	
  	@Test

	public void FCA_1116_chTree_Descendants() throws IOException {

		String chTreeendpoint = baseURI + "/v1/companies/107559/descendants";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(chTreeendpoint).then().body("data.id[0]", equalTo("57035"))
				.body("data.type[0]", equalTo("companies")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test(enabled=false)

	public void FCA_1104_Filter() throws IOException {

		String fitchIdendpoint = baseURI
				+ "/v1/entities/115915/statements?filter[startDate]=2015-12-31&filter[endDate]=2016-12-31&filter[consolidation]=noncon&filter[accountingStandard]=Regulatory&filter[filingType]=Preliminary&filter[periodType]=Annual&filter[fitchId]=115915";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(fitchIdendpoint).then().body("data[0].id", equalTo("115915")).extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test(enabled=false)

	public void FCA_1104_Filterstatments() throws IOException {

		String stmtendpoint = baseURI
				+ "/v1/statements?filter[fitchId]=115915&filter[startDate]=2015-12-31&filter[endDate]=2016-12-31&filter[consolidation]=noncon&filter[accountingStandard]=Regulatory&filter[filingType]=Preliminary&filter[periodType]=Annual&filter[fitchId]=115915";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(stmtendpoint).then().body("data[0].id", equalTo("115915")).extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test

	public void FCA_1050_Metadatasearch() throws IOException {

		String stmtendpoint = baseURI + "/v1/metadata/fields?filter[name]=uir";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(stmtendpoint).then()
				.body("data[0].id", equalTo("FC_REAL_ESTATE_ACQ_SATISFACTION_DEBT_LIFE_US_INS")).extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test

	public void FCA_1050_Metadatasearcherror() throws IOException {

		String stmtendpoint1 = baseURI + "/v1/metadata/fields?filter[name]=ui";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(stmtendpoint1).then().body("errors[0].title", equalTo("Bad Request")).extract()
				.response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

}
