package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint24 extends Configuration {

	
	

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
		
		Random rand = new Random();		
		int n =rand.nextInt(100)+1;		
		String randomviewDefname = "CorporateViewdef_QATest"+n;		
		System.out.println("ViewDef Name "+randomviewDefname);

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
		
		System.out.println("viewfield  " +res.asString());

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
		

		attributes.put("name", randomviewDefname);
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
		
		
		System.out.println("viewdef  " +resViewDef.asString());

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
	
  	@Test(enabled=false)

	public void FCA_1116_chTree_Descendants() throws IOException {

		String chTreeendpoint = baseURI + "/v1/companies/107559/descendants";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(chTreeendpoint).then().body(containsString("57035"))
				.body(containsString("companies")).extract().response();

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
