package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

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

	@BeforeClass
	public void executionSetup() {
		env = System.getProperty("env");
		System.out.println("Test Execution Environment: " + env);
		if (env == null) {
			baseURI = "https://api-int.fitchconnect.com";
			this.AuthrztionValue = ("Basic MVNCRFI4MzVTQ1lOVU5CSDJSVk1TU0MxOTpHTExaUlR3QUpRdjVTazV1cXRyZWlqZE9SK01yQTZrU2plVmNuZXdlekow");
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

}