package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint22 {
	public Response response;
	String myjson;
	String AuthrztionValue;
	String baseURI;
	String databaseFitchEnty;
	String env;
	String dataBaseServer1;
	String dataBaseServer2;
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
			baseURI = "https://new-api.fitchconnect.com";
			this.AuthrztionValue = ("Basic MUc4TTJCUzVIUTdGTVE5RVlNWTdWWVlUWTpoeU51d2lIYUVtOEpaSnF1RzVsRmM0TnRrTXpMMjdqcVFFczVwSDlUdEZJ");
			dataBaseServer1 = "mgo-pue1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("dev")) {
			baseURI = "https://api.fitchconnect-dev.com";
			this.AuthrztionValue = ("Basic MVNCRFI4MzVTQ1lOVU5CSDJSVk1TU0MxOTpHTExaUlR3QUpRdjVTazV1cXRyZWlqZE9SK01yQTZrU2plVmNuZXdlekow");
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
			this.AuthrztionValue = ("Basic NU5COUFRSDVCSTRDUFZTUktJRUpESjQyNTpDYjFxUXQycHd4VGNKZTg1SjkyRVJmL1JMU1haRUlZSjU3NWR5R3RacDVV");
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

	public void regions_mulitple_524() throws IOException {

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then()
				.body("data[0].id", equalTo("1")).body("data[0].attributes.name", equalTo("Africa"))
				.body("data[1].id", equalTo("2")).body("data[1].attributes.name", equalTo("Antartica"))
				.body("data[2].id", equalTo("3")).body("data[2].attributes.name", equalTo("Asia"))
				.body("data[3].id", equalTo("4")).body("data[3].attributes.name", equalTo("Australia/Oceania"))
				.body("data[4].id", equalTo("5")).body("data[4].attributes.name", equalTo("Europe"))
				.body("data[5].id", equalTo("6")).body("data[5].attributes.name", equalTo("North America"))
				.body("data[6].id", equalTo("7")).body("data[6].attributes.name", equalTo("South America"))
				.body("data[7].id", equalTo("8")).body("data[7].attributes.name", equalTo("Latin America"))
				.body("data[8].id", equalTo("9")).body("data[8].attributes.name", equalTo("Central America"))
				.body("data[9].id", equalTo("10")).body("data[9].attributes.name", equalTo("Caribbean"))
				.body("data[10].id", equalTo("11")).body("data[10].attributes.name", equalTo("Mediterranean"))
				.body("data[11].id", equalTo("12")).body("data[11].attributes.name", equalTo("Middle East"))
				.body("data[12].id", equalTo("20")).body("data[12].attributes.name", equalTo("World"))
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void regions_single_524() throws IOException {

		String regionEndPoint = "/v1/regions/11"; // Metadata-EndPoint
		String regionmetaUrl = baseURI + regionEndPoint;

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue)

				.when().get(regionmetaUrl).then().body("data.id", equalTo("11"))
				.body("data.attributes.name", equalTo("Mediterranean")).contentType(ContentType.JSON).extract()
				.response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void Update_XREF_ISOcountryCd_1019() throws IOException {
		URL file = Resources.getResource("1019_ISO code.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response IsoRes = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body("data.attributes.entities[0].id", equalTo("BRA"))
				.body("data.attributes.entities[0].values[0].values[0].value[0]", equalTo("Brazil"))
				.body("data.attributes.entities[0].fitchEntityId", equalTo("140046"))
				.body("data.attributes.entities[2].id", equalTo("BD"))
				.body("data.attributes.entities[2].values[0].values[0].value[0]", equalTo("Bangladesh"))
				.body("data.attributes.entities[2].fitchEntityId", equalTo("1437410"))
				.body("data.attributes.entities[5].id", equalTo("ZZ"))
				.body("data.attributes.entities[5].isMissing", equalTo(true))
				.body("data.attributes.entities[6].id", equalTo("91086Q"))
				.body("data.attributes.entities[6].values[0].values[0].value[0]", equalTo("Mexico"))
				.body("data.attributes.entities[6].fitchEntityId", equalTo("140070")).extract().response();

		Assert.assertNotNull(IsoRes);
		Assert.assertFalse(IsoRes.asString().contains("isError"));
		Assert.assertFalse(IsoRes.asString().contains("isRestricted"));

	}

	@Test

	public void FCA_1022_allmarketsectors() {

		String url = baseURI + "/v1/marketSectors";
		RestAssured.baseURI = url;

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get().then().statusCode(200)
				.body("data[0].id", equalTo("01000000")).body("data[0].type", equalTo("marketSector"))
				.body("data[0].attributes.name", equalTo("Corporate Finance")).contentType(ContentType.JSON).extract()
				.response();

		AssertJUnit.assertFalse(res.asString().contains("isError"));
		AssertJUnit.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void FCA_1022_singlemarketsectors() {

		String url = baseURI + "/v1/marketSectors/02000000";
		RestAssured.baseURI = url;

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get().then().statusCode(200)
				.body("data.id", equalTo("02000000")).body("data.type", equalTo("marketSector"))
				.body("data.attributes.name", equalTo("Structured Credit")).contentType(ContentType.JSON).extract()
				.response();

		AssertJUnit.assertFalse(res.asString().contains("isError"));
		AssertJUnit.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FCA1011_Composite() throws IOException {
		URL file = Resources.getResource("composite.json");
		String myJson = null;

		myJson = Resources.toString(file, Charsets.UTF_8);

		Response output = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)

				.body("data.attributes.entities[0].values[0].type", equalTo("currency"))

				.contentType(ContentType.JSON).extract().response();

		float result = output.path("data.attributes.entities[0].values[0].values[0].value[0].EUR");
		System.out.println("EUR value is " + result);

		if (result == 2.43723424E8) {
			System.out.println("test case passed");
		} else {
			System.out.println("test case failed");
		}

		AssertJUnit.assertNotNull(output);
		AssertJUnit.assertFalse(output.asString().contains("isError"));
		AssertJUnit.assertFalse(output.asString().contains("isMissing"));
		Assert.assertFalse(output.asString().contains("isRestricted"));

	}

	@Test
	public void FCA1011_Life() throws IOException {
		URL file = Resources.getResource("Life.json");
		String myJson = null;

		myJson = Resources.toString(file, Charsets.UTF_8);

		Response output = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)

				.contentType(ContentType.JSON).extract().response();

		float result = output.path("data.attributes.entities[0].values[0].values[0].value[0].EUR");
		System.out.println("EUR value is " + result);

		if (result == 2.44615008E8) {
			System.out.println("test case passed");
		} else {
			System.out.println("test case failed");
		}
		AssertJUnit.assertNotNull(output);
		AssertJUnit.assertFalse(output.asString().contains("isError"));
		AssertJUnit.assertFalse(output.asString().contains("isMissing"));
		Assert.assertFalse(output.asString().contains("isRestricted"));


	}

	@Test
	public void FCA1011_NonLife() throws IOException {
		URL file = Resources.getResource("NonLife.json");
		String myJson = null;

		myJson = Resources.toString(file, Charsets.UTF_8);

		Response output = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().statusCode(200)
				.body("data.attributes.entities[0].values[0].values[0].value[0].EUR", equalTo((float) 1.17946598E9))

				.contentType(ContentType.JSON).extract().response();

		AssertJUnit.assertNotNull(output);
		AssertJUnit.assertFalse(output.asString().contains("isError"));
		AssertJUnit.assertFalse(output.asString().contains("isMissing"));
		Assert.assertFalse(output.asString().contains("isRestricted"));
	}

	@Test(enabled =false)

	public void FCA_1013() {

		String url = baseURI + "/v1/companies/14528/descendants";
		RestAssured.baseURI = url;

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get().then().statusCode(200)
				.body("data[0].attributes.ownershipType", equalTo("Direct"))
				.body("data[0].attributes.country", equalTo("USA"))
				.body("data[0].attributes.ownershipCategory", equalTo("UNKS"))
				.body("data[0].attributes.type", equalTo("Business Organization"))
				.body("data[0].attributes.parentId", equalTo(14528))
				.contentType(ContentType.JSON)
				.extract().response();

		AssertJUnit.assertFalse(res.asString().contains("isError"));
		AssertJUnit.assertFalse(res.asString().contains("isMissing"));

	}

	@Test

	public void FCA_965_positiveTest_FitchIssuerRatings() throws IOException {

		metaEndPoint = "/v1/entities/116980/fitchIssuerRatings";

		String filters = "?Id=116980&filter[startDate]=2010-01-31&filter[endDate]=2015-12-31&filter[ratingType]=FC_LT_IDR&filter[ratingAction]=Affirmed&filter[ratingAlert]=rating";
		String metaUrlFilter = baseURI + metaEndPoint + filters;

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrlFilter).then()
				.body("data[0].type", Matchers.notNullValue()).body("data[0].type", equalTo("fitchIssuerRatings"))
				.body("data[0].id", Matchers.notNullValue()).body("data[0].id", equalTo("107693063"))
				.body("data[0].attributes.alert", equalTo("Rating Outlook Stable"))
				.body("data[0].attributes.ratingType", equalTo("FC_LT_IDR"))
				.body("data[0].attributes.solicitation", equalTo("Solicited - Sell Side"))
				.body("data[0].attributes.rating", equalTo("A+"))
				.body("data[0].attributes.description", equalTo("Long-Term Issuer Default Rating"))
				.body("data[0].relationships.entity.data.id", equalTo("116980")).contentType(ContentType.JSON).extract()
				.response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void FCA_965_dateFormatWrong() throws IOException {

		metaEndPoint = "/v1/entities/116980/fitchIssuerRatings";

		String filters = "?Id=116980&filter[startDate]=2010-01-3&filter[endDate]"
				+ "=2015-12-31&filter[ratingType]=FC_LT_IDR&filter[ratingAction]=Affirmed&filter[ratingAlert]=rating";
		String metaUrlfilter = baseURI + metaEndPoint + filters;

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrlfilter).then()
				.body("errors[0].status", equalTo("400"))
				.body("errors[0].title",
						equalTo("Invalid Date Format. Use 'yyyy-MM-dd' format. No multiple dates allowed. For ex., ?filter[startDate]=2016-01-01"))
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FCA_965_undefinedRatingTypes() throws IOException {

		metaEndPoint = "/v1/entities/116980/fitchIssuerRatings";

		String filters = "?Id=116980&filter[startDate]=2010-01-30&filter[endDate]"
				+ "=2015-12-31&filter[ratingType]=FC_LT_ID&filter[ratingAction]=Affirmed&filter[ratingAlert]=rating";
		String metaUrlfilter = baseURI + metaEndPoint + filters;

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrlfilter).then()
				.body("errors[0].status", equalTo("400"))
				.body("errors[0].title", equalTo("Undefined Rating Type(s) [FC_LT_ID]")).contentType(ContentType.JSON)
				.extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void FCA_965_ratingAction_String() throws IOException {

		metaEndPoint = "/v1/entities/116980/fitchIssuerRatings";

		String filters = "?Id=116980&filter[startDate]=2010-01-30&filter[endDate]"
				+ "=2015-12-31&filter[ratingType]=FC_LT_IDR&filter[ratingAction]=Affirmeddd&filter[ratingAlert]=rating";
		String metaUrlfilter = baseURI + metaEndPoint + filters;

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrlfilter).then()
				.body("data[0]", Matchers.isEmptyOrNullString()).body("included[0]", Matchers.isEmptyOrNullString())
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void FCA_965_invalidCharacterUsed() throws IOException {

		metaEndPoint = "/v1/entities/116980/fitchIssuerRatings";

		String filters = "?Id=116980&filter[startDate]=2010-01-30&filter[endDate]"
				+ "=2015-12-31&filter[ratingType]=FC_LT_IDR&filter[ratingAction]=Affirmed#x&filter[ratingAlert]=rating";
		String metaUrlfilter = baseURI + metaEndPoint + filters;

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrlfilter).then()
				.body("errors[0].status", equalTo("400"))
				.body("errors[0].title", equalTo("Invalid Characters Used: [Affirmed#x]. Valid chars are [A-Za-z -:]*"))
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void FCA_965_ratingAlert_String() throws IOException {

		metaEndPoint = "/v1/entities/116980/fitchIssuerRatings";
		String filters = "?Id=116980&filter[startDate]=2010-01-30&filter[endDate]"
				+ "=2015-12-31&filter[ratingType]=FC_LT_IDR&filter[ratingAction]=Affirmed#x&filter[ratingAlert]=Stabless";
		String metaUrlfilter = baseURI + metaEndPoint + filters;

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrlfilter).then()
				.body("data", Matchers.isEmptyOrNullString()).body("included", Matchers.isEmptyOrNullString())
				.contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test
	public void fillingtype_515() {

		String url = baseURI + "/v1/statements/5581722/filings";
		RestAssured.baseURI = url;

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue)

				.when().get().then().statusCode(200)

				.body("data.type[0]", equalTo("filings")).body("data.attributes.fileName[0]", containsString(".pdf"))
				.body("data.attributes.fileType[0]", equalTo("pdf"))
				.body("data.relationships.statement.data.id[0]", equalTo("5581722"))
				.body("data.links.download[0]", containsString("https:")).contentType(ContentType.JSON).extract()
				.response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

}
