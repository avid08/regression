package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class ApIsmokeTestSuite {
	public static Response response;
	public static String jsonAsString;
	public static String myjson;
	public String AuthrztionValue;
	public String baseURI;
	public String env;
	// String baseURI = "https://api-qa.fitchconnect.com"; // URL for QA
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
			this.AuthrztionValue = ("Basic MVNCRFI4MzVTQ1lOVU5CSDJSVk1TU0MxOTpHTExaUlR3QUpRdjVTazV1cXRyZWlqZE9SK01yQTZrU2plVmNuZXdlekow");

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
	public void StatusCodeTest() {

		int statuscode = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("Accept", acceptValue).header("Content", contentValue).when().get(metaUrl)

				.statusCode();

		assertTrue(statuscode == 200);

	}

	// Test Description : Verify that Pulish_Flag added to metadata service and
	// in the Entity Summary
	@Test
	public void metaDataResponse_with_PF_US897() {

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then().assertThat()
				.statusCode(200).contentType(ContentType.JSON)
				.body("data.attributes.fitchFieldDesc", hasItem("Corporate Hierarchy Publish Flag")).extract()
				.response();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertTrue(response.asString().contains("FC_CH_PUBLISH_FLAG"));

		jsonAsString = response.asString();

		List<String> attributes = response.path("data.attributes");
		assertNotNull(attributes);

	}

	// Test Description :verify that PublishFlag field will return the value
	// from corporateHierarchy.PublishFlag
	@Test
	public void FC_CH_Publish_Flag_EntitySummary_US889() {

		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("Accept", acceptValue).header("content", contentValue).when().get(metaUrl).then().log()
				.ifError().assertThat().statusCode(200).body("data.id", hasItem("FC_CH_PUBLISH_FLAG")).and()
				.body("data.attributes.type", hasItem("CURRENCY"));

	}

	// Test Description : Verify that entity with publishflag = false displays
	// value = false information in the response
	@Test
	public void FC_CH_Publish_Flag_US887_False() throws IOException {

		URL file = Resources.getResource("PublishFlag_False_Req.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertFalse(responsedata.asString().contains("isError"));

		List<String> data = responsedata.path("data.attributes.entities");
		assertNotNull(data);

	}

	// Test Description: verify that system display empty response for entity
	// which are not published or Publishflag = NO
	@Test
	public void Shareholder_869_without_Data() {
		String endpoint1 = "/v1/entities/1025444/shareholders";
		String DirectrUrl = baseURI + endpoint1;

		given().header("Authorization", AuthrztionValue).header("Id", "1025444").header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(DirectrUrl)
				.then().assertThat().statusCode(200).body("isEmpty()", Matchers.is(false))
				.body("data.included", Matchers.hasSize(0));

	}

	// Test Description: Test Description: verify that system display response
	// with entities for entity which are published or Publishflag =Yes
	@Test
	public void Officers_869_With_Data() {
		String officerEnd = "/v1/entities/108273/officers";
		String OfficerUrl = baseURI + officerEnd;

		given().header("Authorization", AuthrztionValue)

				.header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(OfficerUrl).then().assertThat().log().ifError()
				.statusCode(200).body("data", Matchers.empty()).body("data.included", Matchers.hasSize(0));

	}

	// Test Description : Additional Mood's fields(174) are now available with
	// the correctType without once permission is being taken.
	@Test(enabled = true)
	public void ModyNewFields_775() throws IOException {

		URL file = Resources.getResource("ModyNewFieldsReq.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("FC_LT_FC_ISSR_DIR_MDY"))
				.body(containsString("FC_LT_FC_ISSR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_MQ_AMR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_NIFSR_FC_WATCHLIST_DT_MDY"))
				.body(containsString("FC_LT_FC_BANK_DEPOSITS_WATCHLIST_DT_MDY"))

				.extract().response();
		jsonAsString = fieldResponse.asString();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertTrue(fieldResponse.asString().contains("FC_ST_DERIVED_ISSR_MDY"));

		List<String> values = fieldResponse.path("data.attributes.entities.values.fitchFieldId");
		assertNotNull(values);

	}

	// Test Description : User wild card search return everything that match
	// with the input data along iwth all the relationships information

	@Test(enabled = true)
	public void Entity_Search_921() {

		String searchStringEnd = "/v1/entities?filter[name]=Brazil";
		String SerchUrl = baseURI + searchStringEnd;

		given().header("Authorization", AuthrztionValue)

				.header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(SerchUrl).then().assertThat().log().ifError()
				.statusCode(200).body(containsString("Brazil")).body(containsString("ultimateParent"))
				.body("data.attributes.name", hasItem("Algorithmics Brazil do Brazil Ltda"))
				.body(containsString("directors"));

	}

	// Test Description : User wild card search does not return everything if
	// it's does not satisfy the search criteria

	@Test(enabled = true)
	public void Entity_Search_921_Negative() {

		String searchStringEnd = "/v1/entities?filter[name]=aAAd";
		String SerchUrl = baseURI + searchStringEnd;

		given().header("Authorization", AuthrztionValue)

				.header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(SerchUrl).then().assertThat().log().ifError()
				.statusCode(200).body("data", Matchers.empty()).body("data.included", Matchers.hasSize(0));

	}

	// Test Description :
	@Test(enabled = true)
	public void Entity_Search_922() {

		String enTityendPoint = "/v1/entities/1047648/"; // PublishFlag = True
		String enTityUrl = baseURI + enTityendPoint;

		given().header("Authorization", AuthrztionValue).header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(enTityUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("directors"))
				.body(containsString("officers")).body(containsString("shareholders"))
				.body("data.attributes.name", equalTo("Banco Santander Totta SA"));

	}

	@Test(enabled = true)
	public void entity_search_976_statement() {

		String enTityendPoint2 = "/v1/entities/111607/statements"; // publishflag = false 
		String enTityUrl2 = baseURI + enTityendPoint2;

		given().header("Authorization", AuthrztionValue).header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(enTityUrl2)

				.then().assertThat().log().ifError().statusCode(200).body("data.type", hasItem("statements"))
				.body("data.attributes.header.filingType.get(0)", equalTo("Original"))
				.body("data.attributes.header.reportedCurrency.get(0)", equalTo("MXN"));

	}

	// ManthanWorks
	// Test Description :
	@Test

	public void ticket_FCA_774_UltimateParent() {

		String url = baseURI + "/v1/entities/107444/ultimateParent";
		RestAssured.baseURI = url;

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get().then().statusCode(200)
				.body("data.id", equalTo("107444")).body("data.attributes.name", equalTo("Banco Bradesco S.A."))
				.body("data.relationships.shareholders.links.self", containsString("http:"))
				.body("data.relationships.officers.links.self", containsString("http:"))
				.body("data.relationships.statements.links.self", containsString("http:"))
				.body("data.relationships.company.links.self", containsString("http:")).contentType(ContentType.JSON)
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertTrue(res.asString().contains("ultimateParent"));
		Assert.assertTrue(res.asString().contains("descendants"));
		Assert.assertFalse(res.asString().contains("Children"));
		Assert.assertTrue(res.asString().contains("name"));
		Assert.assertTrue(res.asString().contains(" Bradesco S.A."));
		Assert.assertFalse(res.asString().contains("asOBancofDate"));
		Assert.assertTrue(res.asString().contains("id"));// is Id the same as
															// FitchId
		Assert.assertFalse(res.asString().contains("ancestors"));
		Assert.assertTrue(res.asString().contains("type"));
		Assert.assertTrue(res.asString().contains("companies"));
		Assert.assertFalse(res.asString().contains("directIndirect"));
		Assert.assertTrue(res.asString().contains("directors"));
		Assert.assertTrue(res.asString().contains("shareholders"));
		Assert.assertTrue(res.asString().contains("officers"));
		Assert.assertFalse(res.asString().contains("ownersType"));

	}

	// Test Description :
	@Test
	public void ticket_FCA_774_Company() {

		String url = baseURI + "/v1/entities/1025444/company";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(url).then().body("data.id", equalTo("1025444")).body("data.type", equalTo("companies"))
				.body("data.attributes.name", equalTo("Wuestenrot & Wuerttembergische AG"))
				.body("data.relationships.entity.links.self", containsString("http:"))
				.body("data.relationships.descendants.links.self", containsString("http:")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));

		Assert.assertFalse(res.asString().contains("ultimateParent"));
		Assert.assertTrue(res.asString().contains("relationships"));
		Assert.assertFalse(res.asString().contains("children"));
		Assert.assertTrue(res.asString().contains("name"));
		Assert.assertTrue(res.asString().contains("id"));
		Assert.assertTrue(res.asString().contains("type"));
		Assert.assertFalse(res.asString().contains("directIndirect"));
		Assert.assertFalse(res.asString().contains("asOfDate"));
		Assert.assertTrue(res.asString().contains("descendants"));
		Assert.assertFalse(res.asString().contains("ownersType"));

	}

	// Test Description :ultimate parent, test checks to see if the entity has
	// an ultimate parent, and if the parents has valid data.
	// Also checks other relationships and data attributes and sees if the data
	// is there and valid
	@Test
	public void ticket_FCA_774_companies() {

		String url = baseURI + "/v1/companies/1025444/descendants";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then()
				.body("data", Matchers.empty()).body("included", Matchers.empty())

				.contentType(ContentType.JSON).extract().response();

		assertNotNull(res);

	}

	// Test Description : Verify that all fields within MetaData Response
	// contains all the fields from different entities.
	@Test
	public void metaDataResponse_Compare() throws IOException {

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then()
				.contentType(ContentType.JSON).extract().response();

		jsonAsString = response.asString();

		List<String> id = response.path("data.id");
		assertNotNull(id);

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));

		File src = new File("C://Users//aislam//Desktop//Month Of June//API Project//Sprint 19//fitchfieldID.xlsx");
		FileInputStream file = new FileInputStream(src);

		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		boolean failure = false;

		for (int i = 1; i < rowcount; i++) {

			String fitchData = mySheet.getRow(i).getCell(0).getStringCellValue();
			int index1 = jsonAsString.indexOf(fitchData);

			if (index1 != -1) {

				// System.out.println("The Response contains all the fields");

			} else {

				failure = true;
				System.err.println("The Response does not contain the substring " + fitchData);

			}

		}

		Assert.assertFalse(failure);

		file.close();

	}

	// Test Description : Verifies System throws right information in the json
	// response about Mody's fields with proper permission in MongoDB
	@Test(enabled = true)
	public void ModyNewFields_775_withPermission() throws IOException {

		URL file = Resources.getResource("ModyNewFieldsReq.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("FC_LT_FC_ISSR_DIR_MDY"))
				.body(containsString("FC_LT_FC_ISSR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_MQ_AMR_WATCHLIST_DIR_MDY"))
				.body(containsString("FC_NIFSR_FC_WATCHLIST_DT_MDY"))
				.body(containsString("FC_LT_FC_BANK_DEPOSITS_WATCHLIST_DT_MDY")).extract().response();

		jsonAsString = fieldResponse.asString();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertTrue(fieldResponse.asString().contains("FC_ST_DERIVED_ISSR_MDY"));

	}

	// Test Description : Test returns the total count of the number directors
	// given for every entity.
	// It also checks the as of date for when it was last updated

	@Test
	public void ticket_778_ID_DirectorsCount() {

		String url = baseURI + "/v1/directors";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(url).then()
				.body("meta.asOfDate", equalTo("n/a")).contentType(ContentType.JSON).extract().response();

		List<String> dataIndexes = res.path("data");
		List<String> role = res.path("data.attributes.role");
		List<String> name = res.path("data.attributes.name");
		List<String> position = res.path("data.attributes.position");

		int dataIndexCount = dataIndexes.size();
		int metaCount = res.path("meta.count");

		Assert.assertNotNull(metaCount);

		for (int i = 0; i < dataIndexCount; i++) {

			Assert.assertNotNull(role.get(i));
			Assert.assertNotNull(name.get(i));
			Assert.assertNotNull(position.get(i));

		}

	}

	// Test Description : Verifies all the financialService fitch fields are
	// available through Data aggregator and response displays their values as
	// well
	@Test
	public void financialServiceData_Verification_807_dataAggregator() throws IOException {

		URL file = Resources.getResource("financial Mnemonics Data fields.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response fieldsResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)

				.extract().response();

		Assert.assertFalse(fieldsResponse.asString().contains("isError"));
		Assert.assertFalse(fieldsResponse.asString().contains("isMissing"));

		List<String> data = fieldsResponse.path("data.attributes.entities");
		assertNotNull(data);

	}

	// Test Description :
	@Test(enabled = true)

	public void Testing_940_FCURL_Rated() {

		String newEndPoint = "/v1/entities/108273";
		String newUrl = baseURI + newEndPoint;

		// Rated Entity should return GRP within FitchconnectURl
		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("Accept", acceptValue).header("content", contentValue).when().get(newUrl).then().log().ifError()
				.assertThat().statusCode(200).body("data.attributes.name", equalTo("Mizuho Bank, Ltd.")).and()
				.body("data.attributes.fitchConnectUrl", containsString("GRP_"));

	}

	@Test(enabled = true)
	public void Testing_940_FCURL_UnRated() {
		// Testing out not Rated Entity and Expected Result a Fitch Agent ID
		String newEndPoint2 = "/v1/entities/1133670";
		String newUrl2 = baseURI + newEndPoint2;

		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("Accept", acceptValue).header("content", contentValue).when().get(newUrl2).then().log()
				.ifError().assertThat().statusCode(200).body("data.attributes.name", equalTo("Citibank N.A."))
				.body("data.attributes.fitchConnectUrl", containsString("AGNT_"));

	}

	@Test
	public void ticket_FCA_768() throws IOException {
		URL file = Resources.getResource("tix_768_body.json");

		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("FITCHID")).extract()
				.response();

		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));

	}

	@Test(enabled = true)

	public void FCURL_928() throws IOException {

		URL file = Resources.getResource("928_Request.json");

		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response dataResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("FC_COMPANY_NAME"))
				.body(containsString("GRP_")).extract().response();

		Assert.assertFalse(dataResponse.asString().contains("isError"));
		Assert.assertFalse(dataResponse.asString().contains("isMissing"));

	}

	@Test(enabled = true)
	public void FitchRating_957_Entity_Resource() {

		String fitchRatingEndpnt = "/v1/entities/110631/fitchIssuerRatings";
		String fRatingURL = baseURI + fitchRatingEndpnt;

		Response fitchData = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
				.header("content", contentValue).when().get(fRatingURL).then().assertThat().statusCode(200).log()
				.ifError().body("data.get(0).type", equalTo("fitchIssuerRatings"))
				// .body(data.get(0). arg1)
				.contentType(ContentType.JSON).extract().response();

		List<String> alert = fitchData.path("data.attributes.alert");
		List<String> ratinType = fitchData.path("data.attributes.ratingType");
		List<String> rating = fitchData.path("data.attributes.rating");
		List<String> action = fitchData.path("data.attributes.action");
		List<String> effectiveDate = fitchData.path("data.attributes.effectiveDate");
		List<String> relationship = fitchData.path("data.relationships");

		int totalattributes = alert.size();

		System.out.println(totalattributes);

		for (int i = 0; i < totalattributes; i++) {

			Assert.assertNotNull(alert.get(i));
			Assert.assertNotNull(ratinType.get(i));
			Assert.assertNotNull(rating.get(i));
			Assert.assertNotNull(action.get(i));
			Assert.assertNotNull(effectiveDate.get(i));
			Assert.assertNotNull(relationship.get(i));

		}

	}

	// Test Description: returns a single currency, depending on the currency
	// option
	@Test
	public void FCA401USD() throws IOException {

		URL file = Resources.getResource("FCA 401.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values.USD", Matchers.notNullValue()).extract().response();
		assertNotNull(res);
	}

	// Test Description: returns a single currency, depending on the currency
	// option
	@Test
	public void FCA401Local() throws IOException {

		URL file = Resources.getResource("FCA 401.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values.VND", Matchers.notNullValue()).extract().response();
		assertNotNull(res);

	}

	// Test Description:
	@Test
	public void FCA401EUR() throws IOException {

		URL file = Resources.getResource("FCA 401.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values.EUR", Matchers.notNullValue()).extract().response();
		assertNotNull(res);

	}

	// Test Description:
	@Test
	public void FCA401GBP() throws IOException {

		URL file = Resources.getResource("FCA 401.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values.GBP", Matchers.notNullValue()).extract().response();

		assertNotNull(res);
	}

	// Test Description:
	@Test
	public void FCA401JPY() throws IOException {

		URL file = Resources.getResource("FCA 401.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values.JPY", Matchers.notNullValue()).extract().response();

		assertNotNull(res);
	}

	@Test

	public void entityLimit() throws IOException {

		URL file = Resources.getResource("Entity Limit.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(413)
				.body("errors.get(0).title", equalTo("Limit Exceeded")).body("errors.get(0).status", equalTo(413))
				.body("errors.get(0).code", equalTo("20001")).body("errors.get(0).detail", equalTo("Payload too large"))
				.extract().response();

		assertNotNull(res);

	}

	@Test

	public void fieldLimit() throws IOException {

		URL file = Resources.getResource("field limit.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(413)
				.body("errors.get(0).title", equalTo("Limit Exceeded")).body("errors.get(0).status", equalTo(413))
				.body("errors.get(0).code", equalTo("20002")).body("errors.get(0).detail", equalTo("Payload too large"))
				.extract().response();

		assertNotNull(res);
	}

	@Test

	public void missingEntityAndMissingField() throws IOException {

		URL file = Resources.getResource("Missing Entity.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)

				.extract().response();

		Assert.assertTrue(res.asString().contains("isMissing"));

		assertNotNull(res);
	}

	@Test
	public void invalidDate() throws IOException {

		URL file = Resources.getResource("Invalid Date.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(400)
				.body("errors.get(0).title", equalTo("Malformed JSON")).body("errors.get(0).status", equalTo(400))
				.body("errors.get(0).code", equalTo("21002"))
				.body("errors.get(0).detail",
						equalTo("Text '2010-13-09' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13"))
				.extract().response();
		assertNotNull(res);

	}
	// Status Code 200 instead of 400 and it runs

	@Test

	public void invalidPeriod() throws IOException {

		URL file = Resources.getResource("Invalid period.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(400)
				.body("errors.get(0).title", equalTo("Validation error")).body("errors.get(0).status", equalTo(400))
				.body("errors.get(0).code", equalTo("21003"))
				.body("errors.get(0).detail",
						equalTo("data.attributes.dateOptions.periods[0].year must be 4 digit value"))
				.extract().response();
		assertNotNull(res);
	}

	@Test

	public void noEntity() throws IOException {

		URL file = Resources.getResource("no entity.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(400)
				.body("errors.get(0).title", equalTo("Validation error")).body("errors.get(0).status", equalTo(400))
				.body("errors.get(0).code", equalTo("21003"))
				.body("errors.get(0).detail", equalTo("data.attributes.entities may not be empty")).extract()
				.response();
		assertNotNull(res);
	}

	@Test

	public void noField() throws IOException {

		URL file = Resources.getResource("no field.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(400)
				.body("errors.get(0).title", equalTo("Validation error")).body("errors.get(0).status", equalTo(400))
				.body("errors.get(0).code", equalTo("21003"))
				.body("errors.get(0).detail", equalTo("data.attributes.fitchFieldIds may not be empty")).extract()
				.response();
		assertNotNull(res);
	}

	@Test

	public void noRequest() throws IOException {

		URL file = Resources.getResource("no request.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(400)
				.body("errors.get(0).title", equalTo("Malformed JSON")).body("errors.get(0).status", equalTo(400))
				.body("errors.get(0).code", equalTo("21002"))
				.body("errors.get(0).detail", equalTo("Request body contains content that is unparsable")).extract()
				.response();
		assertNotNull(res);
	}

	@Test

	public void wrongMethod() throws IOException {

		try {

			given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).when().get(dataPostUrl)

					.then().assertThat().log().ifError().statusCode(405)
					.body("errors.get(0).title", equalTo("Method Not Allowed"))
					.body("errors.get(0).status", equalTo(405)).body("errors.get(0).code", equalTo("21005"))
					.body("errors.get(0).detail", equalTo("A request was made using a request method not supported."))
					.extract().response();

		} catch (IllegalArgumentException e) {
			assertTrue(true);

		}

	}

	@Test

	public void wrongMediaType() throws IOException {

		URL file = Resources.getResource("wrong media.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/json").body(myJson).with().when().post(dataPostUrl).then()

				.assertThat().log().ifError().statusCode(415).extract().response();

		Assert.assertTrue(res.asString().contains("Unsupported Media Type"));
		Assert.assertTrue(res.asString().contains("415"));
		Assert.assertTrue(res.asString().contains("21007"));
		Assert.assertTrue(res.asString().contains("Request contains media type that is not supported"));

	}

	@Test
	public void unauthorization() throws IOException {

		URL file = Resources.getResource("wrong media.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", "").header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(401).extract().response();

		Assert.assertTrue(res.asString().contains("Unauthorized"));
		Assert.assertTrue(res.asString().contains("401"));
		Assert.assertTrue(res.asString().contains("11008"));
		Assert.assertTrue(res.asString().contains(
				"The requesting client does not have authorization to make the request.  The security token is missing or invalid"));
	}

	// Test Description :
	@Test
	public void regression_testing_Single() throws IOException {

		URL file = Resources.getResource("single_rating.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("116980"))
				.body("data.attributes.entities.values.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).type", contains("text"))
				.body("data.attributes.entities.values.get(0).fitchFieldId", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).values.get(0).timeIntervalDate", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).values.get(0).value.get(0)", Matchers.notNullValue())

				.body("data.attributes.entities.fitchEntityId", Matchers.notNullValue())
				.body("data.attributes.entities.fitchEntityId", contains("116980"))
				.body("data.attributes.entities.values.get(0).fitchFieldId", contains("FC_LT_IDR"))
				.body("data.attributes.dateOptions.dates", Matchers.notNullValue()).extract().response();

		List<String> rating = res.path("data.attributes.entities.values.get(0).values.get(0).value.get(0)");
		List<String> fitchFieldID = res.path("data.attributes.entities.values.get(0).fitchFieldId");
		List<String> fitchEntityID = res.path("data.attributes.entities.fitchEntityId");
		List<String> timeIntervalDate = res
				.path("data.attributes.entities.values.get(0).values.get(0).timeIntervalDate");
		List<String> dateOptionsDates = res.path("data.attributes.dateOptions.dates");

		String cdate = ft.format(dNow).toString();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertTrue(res.asString().contains(ft.format(dNow)));
		Assert.assertTrue(rating.contains("A+"));
		Assert.assertTrue(fitchFieldID.contains("FC_LT_IDR"));
		Assert.assertTrue(res.asString().contains("text"));
		Assert.assertTrue(fitchEntityID.contains("116980"));
		Assert.assertTrue(res.asString().contains("fitchFieldId"));
		Assert.assertTrue(res.asString().contains("fitchEntityId"));
		Assert.assertTrue(res.asString().contains("timeIntervalDate"));
		Assert.assertTrue(timeIntervalDate.contains(cdate));
		Assert.assertTrue(dateOptionsDates.contains(cdate));

	}

	// Test Description: test is pulling data and testing ID, FitchFieldID, type
	// and date. Testing on entity 115915.
	@Test

	public void singleEntitySummary() throws IOException {

		URL file = Resources.getResource("Single Entity Summary.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("115915"))
				.body("data.attributes.entities.values.get(0).fitchFieldId", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).fitchFieldId", contains("FC_INT_INC_LOANS_BNK"))
				.body("data.attributes.entities.values.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).type", contains("currency"))

				.extract().response();

		List<String> dateOptionsDates = res.path("data.attributes.dateOptions.dates");

		String cdate = ft.format(dNow).toString();

		Assert.assertTrue(dateOptionsDates.contains(cdate));

	}

	// CEHCK TO ADD IN ACTUAL VALUE
	// Test Description: test for date rating. the test checks foor ID,
	// fitchFieldID, type, timeIntervalDate, actual rating value, fitchEntityID,
	// and date
	@Test
	public void dateMoodys() throws IOException {

		URL file = Resources.getResource("dateMoodys.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("IBM"))
				.body("data.attributes.entities.values.get(0).fitchFieldId", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).fitchFieldId", contains("FC_LT_LC_ISSR_MDY"))
				.body("data.attributes.entities.type", Matchers.notNullValue())
				.body("data.attributes.entities.type", contains("companyTicker"))
				.body("data.attributes.entities.values.get(0).values.get(0).timeIntervalDate", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).values.get(0).timeIntervalDate", contains("2007-09-09"))
				.body("data.attributes.entities.values.get(0).values.get(0).value.get(0)", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).values.get(0).value.get(0)", contains("A1"))
				.body("data.attributes.entities.fitchEntityId", Matchers.notNullValue())
				.body("data.attributes.entities.fitchEntityId", contains("116980"))
				.body("data.attributes.dateOptions.dates", contains("2007-09-09")).extract().response();
		assertNotNull(res);
	}

	@Test(enabled = true)

	public void periodRating() throws IOException {

		URL file = Resources.getResource("periodRating.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		assertNotNull(res);

	}

	// Test Description:
	@Test
	public void dataEntitySummary() throws IOException {

		URL file = Resources.getResource("data entity Summary.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("116980"))
				.body("data.attributes.entities.values.get(0).fitchFieldId", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).fitchFieldId", contains("FC_ADDRESS_1"))
				.body("data.attributes.entities.values.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).type", contains("text"))
				.body("data.attributes.entities.values.get(0).values.get(0).timeIntervalDate", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).values.get(0).timeIntervalDate", contains("2015-08-09"))
				.body("data.attributes.entities.fitchEntityId", Matchers.notNullValue())
				.body("data.attributes.entities.fitchEntityId", contains("116980"))
				.body("data.attributes.dateOptions.dates", contains("2015-08-09")).extract().response();

		assertNotNull(res);
	}

	// Test Description:
	@Test

	public void dateRatings() throws IOException {

		URL file = Resources.getResource("date rating.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("94834890"))
				.body("data.attributes.entities.values.get(0).fitchFieldId", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).fitchFieldId", contains("FC_LT_IDR"))
				.body("data.attributes.entities.type", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).type", contains("text"))
				.body("data.attributes.entities.values.get(0).values.get(0).timeIntervalDate", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).values.get(0).timeIntervalDate", contains("2015-08-09"))
				.body("data.attributes.entities.values.get(0).values.get(0).value.get(0)", Matchers.notNullValue())
				.body("data.attributes.entities.values.get(0).values.get(0).value.get(0)", contains("BBB"))
				.body("data.attributes.entities.fitchEntityId", Matchers.notNullValue())
				.body("data.attributes.entities.fitchEntityId", contains("1451701"))
				.body("data.attributes.dateOptions.dates", contains("2015-08-09")).extract().response();
		assertNotNull(res);
	}

	// Test Description:
	@Test

	public void daterangeDefaultFrequency() throws IOException {

		URL file = Resources.getResource("dateRangeDefaultFrequency.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("116980"))
				.extract().response();

		List<String> actual_values = res.path("data.attributes.entities.values.values.value");
		List<String> dateOptions = res.path("data.attributes.dateOptions.dates");
		assertNotNull(res);

		for (int i = 0; i < dateOptions.size(); i++) {
			Assert.assertNotNull(dateOptions.get(i));

		}

		for (int j = 0; j < actual_values.size(); j++) {
			Assert.assertNotNull(actual_values.get(j));

		}

	}

	// Test Description:
	@Test
	public void dateRangeWeekly() throws IOException {

		URL file = Resources.getResource("dateRangeWeekly.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("116980"))
				.extract().response();

		List<String> actual_values = res.path("data.attributes.entities.values.values.value");
		List<String> dateOptions = res.path("data.attributes.dateOptions.dates");
		assertNotNull(res);

		for (int i = 0; i < dateOptions.size(); i++) {
			Assert.assertNotNull(dateOptions.get(i));

		}

		for (int j = 0; j < actual_values.size(); j++) {
			Assert.assertNotNull(actual_values.get(j));

		}

	}

	// Test Description:
	@Test

	public void dateRangeMonthly() throws IOException {

		URL file = Resources.getResource("dateRangeMonthly.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("116980"))
				.extract().response();

		List<String> actual_values = res.path("data.attributes.entities.values.values.value");
		List<String> dateOptions = res.path("data.attributes.dateOptions.dates");

		for (int i = 0; i < dateOptions.size(); i++) {
			Assert.assertNotNull(dateOptions.get(i));

		}

		for (int j = 0; j < actual_values.size(); j++) {
			Assert.assertNotNull(actual_values.get(j));

		}

	}

	// Test Description
	@Test

	public void dateRanteQTR() throws IOException {

		URL file = Resources.getResource("dateRangeQtr.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("116980"))
				.extract().response();

		List<String> actual_values = res.path("data.attributes.entities.values.values.value");
		List<String> dateOptions = res.path("data.attributes.dateOptions.dates");

		for (int i = 0; i < dateOptions.size(); i++) {
			Assert.assertNotNull(dateOptions.get(i));

		}

		for (int j = 0; j < actual_values.size(); j++) {
			Assert.assertNotNull(actual_values.get(j));

		}

	}

	// Test Description:
	@Test

	public void dateRangeAnnually() throws IOException {

		URL file = Resources.getResource("dateRangeAnnually.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("116980"))
				.extract().response();

		List<String> actual_values = res.path("data.attributes.entities.values.values.value");
		List<String> dateOptions = res.path("data.attributes.dateOptions.dates");

		for (int i = 0; i < dateOptions.size(); i++) {
			Assert.assertNotNull(dateOptions.get(i));

		}

		for (int j = 0; j < actual_values.size(); j++) {
			Assert.assertNotNull(actual_values.get(j));

		}

	}

	// Test Description
	@Test

	public void dateRangeNullendDate() throws IOException {

		URL file = Resources.getResource("dateRangeNullEndDate.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("116980"))
				.extract().response();

		List<String> actual_values = res.path("data.attributes.entities.values.values.value");
		List<String> dateOptions = res.path("data.attributes.dateOptions.dates");

		for (int i = 0; i < dateOptions.size(); i++) {
			Assert.assertNotNull(dateOptions.get(i));

		}

		for (int j = 0; j < actual_values.size(); j++) {
			Assert.assertNotNull(actual_values.get(j));

		}

	}

	// Test Description:
	@Test

	public void multipleEnttiesandFields() throws IOException {

		URL file = Resources.getResource("mulitpleRatingFields.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("116980"))
				.extract().response();

		List<String> actual_values = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < actual_values.size(); i++) {
			Assert.assertNotNull(actual_values.get(i));

		}

	}

	// Test Description:
	@Test

	public void muitpleRatingEntites() throws IOException {

		URL file = Resources.getResource("mulitpleRatingEntities.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		List<String> fitchIds = res.path("data.attributes.entities.id");
		List<String> actual_value = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < fitchIds.size(); i++) {
			Assert.assertNotNull(fitchIds.get(i));

		}

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

	}

	// Test Description:
	@Test

	public void mulitpleEntitySummaryField() throws IOException {

		URL file = Resources.getResource("multipleEntitySummaryField.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

	}

	// Test Description:
	@Test

	public void mulitpleEntitySummaryEntites() throws IOException {

		URL file = Resources.getResource("mulitpleEntitySummaryEntities.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		List<String> fitchEntityId = res.path("data.attributes.entities.fitchEntityId");

		for (int i = 0; i < fitchEntityId.size(); i++) {
			Assert.assertNotNull(fitchEntityId.get(i));

		}

		Assert.assertTrue(fitchEntityId.contains("112082"));
		Assert.assertTrue(fitchEntityId.contains("1315051"));
		Assert.assertTrue(fitchEntityId.contains("116980"));
		Assert.assertTrue(fitchEntityId.contains("1313773"));
		Assert.assertTrue(fitchEntityId.contains("1407617"));

		List<String> actual_value = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

	}

	// test description:
	@Test

	public void mulitpleMoodsField() throws IOException {

		URL file = Resources.getResource("mulitpleMoodysField.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

	}

	// test description:
	@Test

	public void mulitpleMoodsEntities() throws IOException {

		URL file = Resources.getResource("multipleMoodysEntities.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

	}

	// Test Description:
	@Test
	public void conOptionFinancial() throws IOException {

		URL file = Resources.getResource("conOption.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("1012738"))
				.body("data.attributes.entities.type", contains("FitchID")).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value.USD");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

		Assert.assertNotNull(res.path("data.attributes.entities.values.values.value.USD"));

	}

	// Test Description:
	@Test
	public void nonConOptionFinancial() throws IOException {

		URL file = Resources.getResource("nonconOption.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("1012738"))
				.body("data.attributes.entities.type", contains("FitchID"))

				.extract().response();

		// ask which one he wants
		List<String> actual_value = res.path("data.attributes.entities.values.values.value.USD");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

		Assert.assertNotNull(res.path("data.attributes.entities.values.values.value.USD"));

	}

	// test Description:
	@Test
	public void combinedOptionFinancial() throws IOException {

		URL file = Resources.getResource("combinedOption.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("1012738"))
				.body("data.attributes.entities.type", contains("FitchID"))
				.body("data.attributes.entities.values.fitchFieldId", Matchers.notNullValue()).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value.USD");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

		Assert.assertNotNull(res.path("data.attributes.entities.values.values.value.USD"));

	}

	// Test Description:
	@Test
	public void localOptionFinancial() throws IOException {

		URL file = Resources.getResource("localOption.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("1012738"))
				.body("data.attributes.entities.type", contains("FitchID"))
				.body("data.attributes.entities.values.fitchFieldId", Matchers.notNullValue()).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value.USD");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

		Assert.assertNotNull(res.path("data.attributes.entities.values.values.value.USD"));

	}

	// Test Description:
	@Test

	public void IFRSOptionFinancial() throws IOException {

		URL file = Resources.getResource("IFRS option.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("1012738"))
				.body("data.attributes.entities.type", contains("FitchID"))
				.body("data.attributes.entities.values.fitchFieldId", Matchers.notNullValue()).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value.USD");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

		Assert.assertNotNull(res.path("data.attributes.entities.values.values.value.USD"));

	}

	// Test Description:
	@Test
	public void HGBOptionFinancial() throws IOException {

		URL file = Resources.getResource("HBG option.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("1429993"))
				.body("data.attributes.entities.type", contains("FitchID"))
				.body("data.attributes.entities.values.fitchFieldId", Matchers.notNullValue())

				.extract().response();

		Assert.assertNotNull(res.path("data.attributes.entities.values.values.value.USD"));
		Assert.assertTrue(res.asString().contains("FC_CALLED_SHARE_CAPITAL_INS"));

	}

	// Test Description:
	@Test

	public void latestOptionFinancial() throws IOException {

		URL file = Resources.getResource("latestOption.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("1012738"))
				.body("data.attributes.entities.type", contains("FitchID"))
				.body("data.attributes.entities.values.fitchFieldId", Matchers.notNullValue()).extract().response();

		Assert.assertNotNull(res.path("data.attributes.entities.values.values.value.USD"));
		Assert.assertTrue(res.asString().contains("FC_INT_INC_LOANS_BNK"));

	}

	// Test Description:
	@Test

	public void restatedOptionFinancial() throws IOException {

		URL file = Resources.getResource("restatedOption.JSON");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("1012738"))
				.body("data.attributes.entities.type", contains("FitchID"))
				.body("data.attributes.entities.values.fitchFieldId", Matchers.notNullValue()).extract().response();

		Assert.assertNotNull(res.path("data.attributes.entities.values.values.value.USD"));
		Assert.assertTrue(res.asString().contains("FC_INT_INC_LOANS_BNK"));

	}

	// Test Description :FCA 975 Metadata Service with links to Fitch Field IDs
	@Test(enabled = true)
	public void MetaDataWithLinks_975() {

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then().assertThat()
				.statusCode(200).contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));

		jsonAsString = response.asString();
		assertNotNull(jsonAsString);

		List<String> link = response.path("data.links.self");
		List<String> selfCaterogies = response.path("data.relationships.categories.links.self");
		List<String> reltedCaterogies = response.path("data.relationships.categories.links.self");

		int linkcounts = link.size();

		for (int i = 0; i < linkcounts; i++) {

			assertNotNull(link.get(i));
			assertNotNull(selfCaterogies.get(i));
			assertNotNull(reltedCaterogies.get(i));

		}

	}

	@Test(enabled = true)

	public void additional_FC_ConnectURl_934() {

		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then().assertThat()
				.log().ifError().statusCode(200).body("data.id", hasItem("FC_CONNECT_URL"))
				.body("data.attributes.displayName", hasItem("WebURL"));

	}

	@Test(enabled = true)

	public void GroupingsByCatergoryID_802() {

		String catGoryendPoint = "/v1/metadata/categories/2";
		String catGoryURl = baseURI + catGoryendPoint;

		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue)

				.when().get(catGoryURl).then().assertThat().log().ifError().statusCode(200)
				.body("data.type", equalTo("categories")).body("data.attributes.name", equalTo("Financials"))
				.body("data.relationships.children.data.id", hasItem("13"))
				.body("data.relationships.children.data.id", hasItem("12"))
				.body("data.relationships.children.data.id", hasItem("11"));

	}

	@Test(enabled = true)
	public void singleField_974_fromMetaData() {

		String sngleFildEndpoint = "/v1/metadata/fields/FC_ST_LC_FER_SP";
		String fieldUrl = baseURI + sngleFildEndpoint;

		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue)

				.when().get(fieldUrl).then().statusCode(200)
				.body("data.attributes.displayName", equalTo("S&P ST LC Financial Enhancement Rating"))
				.body("data.attributes.fitchFieldDesc",
						equalTo("S&P Short-term Local Currency Financial Enhancement Rating"))
				.body("data.links.self", equalTo("http://meta-service:8080/v1/metadata/fields/FC_ST_LC_FER_SP"));

	}
	
	@Test
   public void directors_788(){
		
		String directorUri = "/v1/directors";
		String dirctorUrl =baseURI+directorUri ;
		
		Response drectorRes = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
		.header("accept", acceptValue).header("content", contentValue)

		.when().get(dirctorUrl).then().statusCode(200)
		.contentType(ContentType.JSON).extract().response();
		
		
	}

}
