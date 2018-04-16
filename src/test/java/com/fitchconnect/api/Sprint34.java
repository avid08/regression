package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint34 extends Configuration {

	@Test

	public void fca_1677() {

		String sandPratingsUrl = baseURI + "/v1/entities/1011510/standardAndPoorIssuerRatings";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue)

				.when().get(sandPratingsUrl).then().statusCode(200).body(containsString("standardAndPoorIssuerRatings"))
				.body(containsString("Issuer Credit Rating")).body(containsString("ratingDate"))
				.body(containsString("A")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void fca_1677_dataAggregtor() throws IOException {

		URL file = Resources.getResource("fca_1677.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).extract().response();

		Assert.assertTrue(res.asString().contains("2016-01-11"));
		Assert.assertTrue(res.asString().contains("2015-01-11"));

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test(enabled=false)
	public void fca_1678() {

		String userUrl = baseURI + "/v1/users";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(userUrl).then()
				.statusCode(200).body(containsString("firstName")).body(containsString("lastName"))
				.body(containsString("username")).extract().response();

		List<String> ids = res.path("data.id");

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void BMI_metaData_field() {

		String Bmi_meTadataURi = baseURI + "/v1/metadata/fields/BMI_FS_SHIP_LIMON_TEU_PCTCH";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue)

				.when().get(Bmi_meTadataURi).then().statusCode(200)
				.body(containsString("Port Limon container throughput, TEU, % chg y-o-y"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void BMI_Integration_1620_450_marcrofields() throws IOException {

		URL file = Resources.getResource("Bmi_macroeco.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		URL file2 = Resources.getResource("Bmi_macroeco_450fields.json");
		String myJson2 = Resources.toString(file2, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body(containsString("Annual")).body(containsString("value"))
				.body(containsString("currency")).body(containsString("USD")).body(containsString("2015"))
				.body(containsString("RON")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));		
		Assert.assertFalse(res.asString().contains("isRestricted"));

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson2).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body(containsString("Annual")).body(containsString("value"))
				.body(containsString("currency")).body(containsString("USD")).body(containsString("2015"))
				.body(containsString("BDT")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		//Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void BMI_Data_1620_Ratingfields_with_Xref_entity() throws IOException {

		URL file = Resources.getResource("bmi_fields_ratingFields.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body(containsString("BMI_FISCAL_BALANCE_PCTGDP"))
				.body(containsString("FC_LT_IDR")).body(containsString("BMI_GDP_NOM_LCU"))
				.body(containsString("Annual"))
				.body(containsString("value"))
				.body(containsString("currency"))
				.body(containsString("Bangladesh"))
				.body(containsString("BDT"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FCA_1535() throws IOException {
		URL file = Resources.getResource("MrketSector.json");
		String MSJson = null;
		MSJson = Resources.toString(file, Charsets.UTF_8);
		Response MSoutput = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(MSJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities[0].values[0].values[0].value[0]", equalTo("Sovereigns"))
				.body("data.attributes.entities[0].values[1].values[0].value[0]", equalTo("05"))
				.contentType(ContentType.JSON).extract().response();
		
		Assert.assertNotNull(MSoutput);
		
		Assert.assertFalse(MSoutput.asString().contains("isError"));
		Assert.assertFalse(MSoutput.asString().contains("isMissing"));
		Assert.assertFalse(MSoutput.asString().contains("isRestricted"));

	}

	@Test
	public void FCA_1640_valuerequest() throws IOException {
		URL file = Resources.getResource("1640vr.json");
		String vrJSON = null;
		vrJSON = Resources.toString(file, Charsets.UTF_8);
		Response vroutput = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(vrJSON).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body(containsString("fitchid")).extract().response();
		Assert.assertNotNull(vroutput);
		Assert.assertFalse(vroutput.asString().contains("isError"));
		Assert.assertFalse(vroutput.asString().contains("isMissing"));
		Assert.assertFalse(vroutput.asString().contains("isRestricted"));

	}

}
