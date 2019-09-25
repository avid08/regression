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
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class ApIsmokeTestSuite extends Configuration {

	@Test()
	public void StatusCodeTest() {

		int statuscode = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("Accept", acceptValue).header("Content", contentValue).when().get(metaUrl)

				.statusCode();

		assertTrue(statuscode == 200);

	}

	// Test Description : Verify that Pulish_Flag added to metadata service and
	// in the Entity Summary
	@Test()
	public void metaDataResponse_with_PF_US897() {

		System.out.println(metaUrl);

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then().assertThat()
				.statusCode(200).contentType(ContentType.JSON)
				.body("data.attributes.fitchFieldDesc", hasItem("Corporate Hierarchy Publish Flag")).extract()
				.response();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

		Assert.assertTrue(response.asString().contains("FC_CH_PUBLISH_FLAG"));

		List<String> attributes = response.path("data.attributes");
		assertNotNull(attributes);

	}

	// Test Description :verify that PublishFlag field will return the value
	// from corporateHierarchy.PublishFlag
	@Test()
	public void FC_CH_Publish_Flag_EntitySummary_US889() throws InterruptedException {

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
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));

		List<String> data = responsedata.path("data.attributes.entities");
		assertNotNull(data);

	}

	// Test Description: verify that system display empty response for entity
	// which are not published or Publishflag = NO
	@Test(enabled = true)
	public void Shareholder_869_without_Data() {
		String endpoint1 = "/v1/entities/11275301127530/shareholders";
		String DirectrUrl = baseURI + endpoint1;

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(DirectrUrl)
				.then().assertThat().statusCode(200).body("isEmpty()", Matchers.is(false))
				.body("data.included", Matchers.hasSize(0)).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Test Description: verify that system display response
	// with entities for entity which are published or Publishflag =Yes
	@Test
	public void Officers_869_With_Data() {
		String officerEnd = "/v1/entities/107444/officers";
		String OfficerUrl = baseURI + officerEnd;

		given().header("Authorization", AuthrztionValue)

				.header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(OfficerUrl).then().assertThat().log().ifError()
				.statusCode(200).body(containsString("officers"));

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
				.body(containsString("FC_LT_FC_BANK_DEPOSITS_WATCHLIST_DT_MDY")).body(containsString("text"))
				.body(containsString("value")).extract().response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

		Assert.assertTrue(fieldResponse.asString().contains("FC_ST_DERIVED_ISSR_MDY"));

		List<String> values = fieldResponse.path("data.attributes.entities.values.fitchFieldId");
		assertNotNull(values);

	}

	// Test Description : User wild card search return everything that match
	// with the input data along iwth all the relationships information

	@Test(enabled = true)
	public void entity_search_976_statement() {

		String enTityendPoint2 = "/v1/entities/111607/statements"; // publishflag
																	// = false
		String enTityUrl2 = baseURI + enTityendPoint2;

		given().header("Authorization", AuthrztionValue).header("content", contentValue).header("'Accept", acceptValue)
				.header("X-App-Client-Id", XappClintIDvalue).when().get(enTityUrl2)

				.then().assertThat().log().ifError().statusCode(200).body("data.type", hasItem("statements"))
				// .body("data.attributes.header.filingType.get(0)",
				// equalTo("Original"))
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
				.body("data.relationships.shareholders.links.self", Matchers.anything("https:"))
				.body("data.relationships.officers.links.self", Matchers.anything("https:"))
				.body("data.relationships.statements.links.self", Matchers.anything("https:"))
				.body("data.relationships.company.links.self", Matchers.anything("https:"))
				.contentType(ContentType.JSON).extract().response();

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
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description :
	@Test
	public void ticket_FCA_774_Company() {

		String url = baseURI + "/v1/entities/107444/company";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(url).then().body("data.id", equalTo("107444")).body("data.type", equalTo("companies"))
				.body("data.attributes.name", equalTo("Banco Bradesco S.A."))
				.body("data.relationships.entity.links.self", Matchers.notNullValue())
				.body("data.relationships.descendants.links.self", Matchers.anything("https:")).extract().response();

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
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void ticket_FCA_774_companies() {

		String url = baseURI + "/v1/companies/107444/descendants";

		Response res = given().header("Authorization", (AuthrztionValue)).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType(ContentType.JSON).when()
				.get(url).then().body("data[0].relationships.descendants.links.self", Matchers.anything("https:"))
				.statusCode(200).extract().response();

		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	// Test Description : Verify that all fields within MetaData Response
	// contains all the fields from different entities.
	@Test(enabled = false)
	public void metaDataResponse_Compare() throws IOException, URISyntaxException {
		String jsonAsString;

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then()
				.contentType(ContentType.JSON).statusCode(200).extract().response();

		jsonAsString = response.asString();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));

		URL fileUrl = Resources.getResource("fitchfieldID.xlsx");

		File src = new File(fileUrl.toURI());

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

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

	}

	@Test()
	
	public void all_metaDataType_Validation() throws URISyntaxException, IOException {
		
		URL fileUrl = Resources.getResource("SampleFrom_All_MetaData_Type.xlsx");

		File src = new File(fileUrl.toURI());

		FileInputStream file = new FileInputStream(src);

		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		boolean failure = false;
		
		for (int i = 1; i < rowcount; i++) {

			  String fitchfieldId = mySheet.getRow(i).getCell(0).getStringCellValue(); 			
			  String metaDataURI = baseURI+"/v1/metadata/fields/"+fitchfieldId;
		      System.out.println(metaDataURI);		

				int statuscode = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
						.header("Accept", acceptValue).header("Content", contentValue).when().get(metaDataURI)

						.statusCode();

				assertTrue(statuscode == 200);
				
		}		
		
		
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

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

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

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description : Verifies all the financialService fitch fields are
	// available through Data aggregator and response displays their values as
	// well
	@Test()
	public void financialServiceData_Verification_807_dataAggregator() throws IOException {

		URL file = Resources.getResource("financial_Mnemonics_Data_fields.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response fieldsResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().statusCode(200).body(containsString("value")).body(containsString("type"))
				.body(containsString("year")).extract().response();

		Assert.assertFalse(fieldsResponse.asString().contains("isError"));
		Assert.assertFalse(fieldsResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldsResponse.asString().contains("isRestricted"));

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
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: returns a single currency, depending on the currency
	// option
	@Test
	public void FCA401USD() throws IOException {

		URL file = Resources.getResource("FCA_401.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values.USD", Matchers.notNullValue()).extract().response();
		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	// Test Description: returns a single currency, depending on the currency
	// option
	@Test
	public void FCA401Local() throws IOException {

		URL file = Resources.getResource("FCA_401.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values.VND", Matchers.notNullValue()).extract().response();
		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test
	public void FCA401EUR() throws IOException {

		URL file = Resources.getResource("FCA_401.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().statusCode(200).body(containsString("EUR")).extract().response();
		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test
	public void FCA401GBP() throws IOException {

		URL file = Resources.getResource("FCA_401.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values.GBP", Matchers.notNullValue()).extract().response();

		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	// Test Description:
	@Test
	public void FCA401JPY() throws IOException {

		URL file = Resources.getResource("FCA_401.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values", Matchers.notNullValue())
				.body("data.attributes.entities.values.values.JPY", Matchers.notNullValue()).extract().response();

		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test

	public void entityLimit() throws IOException {

		URL file = Resources.getResource("Entity Limit.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(413)
				.body("errors.get(0).title", equalTo("Limit Exceeded")).body("errors.get(0).status", equalTo(413))
				.body("errors.get(0).code", equalTo("20001")).body("errors.get(0).detail", equalTo("Payload too large"))
				.extract().response();

		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void fieldLimit() throws IOException {

		URL file = Resources.getResource("field limit.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(413)
				.body("errors.get(0).title", equalTo("Limit Exceeded")).body("errors.get(0).status", equalTo(413))
				.body("errors.get(0).code", equalTo("20002")).body("errors.get(0).detail", equalTo("Payload too large"))
				.extract().response();

		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test

	public void missingEntityAndMissingField() throws IOException {

		URL file = Resources.getResource("Missing Entity.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)

				.extract().response();

		Assert.assertTrue(res.asString().contains("isMissing"));

		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void invalidDate() throws IOException {

		URL file = Resources.getResource("Invalid Date.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(400)
				.body("errors.get(0).title", equalTo("Malformed JSON")).body("errors.get(0).status", equalTo(400))
				.body("errors.get(0).code", equalTo("21002"))
				.body("errors.get(0).detail", equalTo(
						"Text '2010-13-09' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13"))
				.extract().response();
		assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	// Status Code 200 instead of 400 and it runs

	@Test

	public void invalidPeriod() throws IOException {

		URL file = Resources.getResource("Invalid period.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test

	public void noEntity() throws IOException {

		URL file = Resources.getResource("no entity.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(400)
				
				.body("errors.get(0).status", equalTo(400))
				
				.extract().response();
		assertNotNull(res);

		Assert.assertFalse(res.asString().contains("isRestricted"));
		Assert.assertFalse(res.asString().contains("isError"));

	}

	@Test

	public void noField() throws IOException {

		URL file = Resources.getResource("no field.json");
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

		Assert.assertFalse(res.asString().contains("isRestricted"));
		Assert.assertFalse(res.asString().contains("isError"));

	}

	@Test

	public void noRequest() throws IOException {

		URL file = Resources.getResource("no request.json");
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
		Assert.assertFalse(res.asString().contains("isRestricted"));
		Assert.assertFalse(res.asString().contains("isError"));
	}

	@Test

	public void wrongMethod() {

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

		URL file = Resources.getResource("wrong media.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/json").body(myJson).with().when().post(dataPostUrl).then()

				.assertThat().log().ifError().statusCode(415).extract().response();

		Assert.assertTrue(res.asString().contains("Unsupported Media Type"));
		Assert.assertTrue(res.asString().contains("415"));
		Assert.assertTrue(res.asString().contains("21007"));
		Assert.assertTrue(res.asString().contains("Request contains media type that is not supported"));
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));

	}

	@Test(enabled = false)
	public void unauthorization() throws IOException {

		URL file = Resources.getResource("wrong media.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", "").header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().extract().response();

		System.out.print("response :" + res.asString());

		Assert.assertTrue(res.asString().contains("Unauthorized"));
		Assert.assertTrue(res.asString().contains("401"));
		Assert.assertTrue(res.asString().contains("11008"));
		Assert.assertTrue(res.asString().contains(
				"The requesting client does not have authorization to make the request.  The security token is missing or invalid"));
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
	}

	// Test Description :
	@Test
	public void regression_testing_SingleRating() throws IOException {

		URL file = Resources.getResource("single_rating.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)
				.body("data.attributes.entities.id", Matchers.notNullValue())
				.body("data.attributes.entities.id", contains("116980"))

				.body("data.attributes.entities.values.get(0).type", contains("text")).body(containsString("value"))
				.body("data.attributes.entities.fitchEntityId", contains("116980"))
				.body("data.attributes.entities.values.get(0).fitchFieldId", contains("FC_LT_IDR")).extract()
				.response();

		Assert.assertTrue(res.asString().contains("fitchFieldId"));
		Assert.assertTrue(res.asString().contains("fitchEntityId"));
		Assert.assertTrue(res.asString().contains("timeIntervalDate"));
		Assert.assertFalse(res.asString().contains("isMissing"));

	}

	// Test Description: test is pulling data and testing ID, FitchFieldID, type
	// and date. Testing on entity 115915.
	@Test

	public void singleEntitySummary() throws IOException {

		URL file = Resources.getResource("Single Entity Summary.json");
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

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// CEHCK TO ADD IN ACTUAL VALUE
	// Test Description: test for date rating. the test checks foor ID,
	// fitchFieldID, type, timeIntervalDate, actual rating value, fitchEntityID,
	// and date
	@Test
	public void dateMoodys() throws IOException {

		URL file = Resources.getResource("dateMoodys.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	// Test Description:
	@Test
	public void dataEntitySummary() throws IOException {

		URL file = Resources.getResource("data entity Summary.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test

	public void dateRatings() throws IOException {

		URL file = Resources.getResource("date rating.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	// Test Description:
	@Test

	public void daterangeDefaultFrequency() throws IOException {

		URL file = Resources.getResource("dateRangeDefaultFrequency.json");
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

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test
	public void dateRangeWeekly() throws IOException {

		URL file = Resources.getResource("dateRangeWeekly.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test

	public void dateRangeMonthly() throws IOException {

		URL file = Resources.getResource("dateRangeMonthly.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description
	@Test

	public void dateRanteQTR() throws IOException {

		URL file = Resources.getResource("dateRangeQtr.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test

	public void dateRangeAnnually() throws IOException {

		URL file = Resources.getResource("dateRangeAnnually.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description
	@Test

	public void dateRangeNullendDate() throws IOException {

		URL file = Resources.getResource("dateRangeNullEndDate.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test

	public void multipleEnttiesandFields() throws IOException {

		URL file = Resources.getResource("mulitpleRatingFields.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body("data.attributes.entities.id", contains("116980"))
				.extract().response();

		List<String> actual_values = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < actual_values.size(); i++) {
			Assert.assertNotNull(actual_values.get(i));

		}
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));

	}

	// Test Description:
	@Test(enabled = false)

	public void multitpleRatingEntites() throws IOException {

		URL file = Resources.getResource("mulitpleRatingEntities.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test

	public void mulitpleEntitySummaryField() throws IOException {

		URL file = Resources.getResource("multipleEntitySummaryField.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test

	public void mulitpleEntitySummaryEntites() throws IOException {

		URL file = Resources.getResource("mulitpleEntitySummaryEntities.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		List<String> fitchEntityId = res.path("data.attributes.entities.fitchEntityId");

		for (int i = 0; i < fitchEntityId.size(); i++) {
			Assert.assertNotNull(fitchEntityId.get(i));

		}

		Assert.assertTrue(fitchEntityId.contains("112082"));		
		Assert.assertTrue(fitchEntityId.contains("116980"));
		Assert.assertTrue(fitchEntityId.contains("1313773"));
		Assert.assertTrue(fitchEntityId.contains("1407617"));

		List<String> actual_value = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// test description:
	@Test

	public void mulitpleMoodsField() throws IOException {

		URL file = Resources.getResource("mulitpleMoodysField.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// test description:
	@Test

	public void mulitpleMoodsEntities() throws IOException {

		URL file = Resources.getResource("multipleMoodysEntities.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value");

		for (int i = 0; i < actual_value.size(); i++) {
			Assert.assertNotNull(actual_value.get(i));

		}

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test
	public void conOptionFinancial() throws IOException {

		URL file = Resources.getResource("conOption.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test
	public void nonConOptionFinancial() throws IOException {

		URL file = Resources.getResource("nonconOption.json");
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

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// test Description:
	@Test
	public void combinedOptionFinancial() throws IOException {

		URL file = Resources.getResource("combinedOption.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test
	public void localOptionFinancial() throws IOException {

		URL file = Resources.getResource("localOption.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test

	public void IFRSOptionFinancial() throws IOException {

		URL file = Resources.getResource("IFRS option.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		;

	}

	// Test Description:
	@Test
	public void HGBOptionFinancial() throws IOException {

		URL file = Resources.getResource("HBG option.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test

	public void latestOptionFinancial() throws IOException {

		URL file = Resources.getResource("latestOption.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description:
	@Test

	public void restatedOptionFinancial() throws IOException {

		URL file = Resources.getResource("restatedOption.json");
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
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test
	public void directors_788() {

		String directorUri = "/v1/directors";
		String dirctorUrl = baseURI + directorUri;

		Response drectorRes = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue)

				.when().get(dirctorUrl).then().statusCode(200).body("data.type", hasItem("directors"))
				.contentType(ContentType.JSON).extract().response();

		List<String> role = drectorRes.path("data.attributes.role");
		List<String> countnamery = drectorRes.path("data.attributes.name");
		List<String> position = drectorRes.path("data.attributes.position");

		Assert.assertFalse(drectorRes.asString().contains("isError"));
		Assert.assertFalse(drectorRes.asString().contains("isMissing"));
		Assert.assertFalse(drectorRes.asString().contains("isRestricted"));

		for (int i = 0; i < role.size(); i++) {

			Assert.assertNotNull(role.get(i));
			Assert.assertNotNull(countnamery.get(i));
			Assert.assertNotNull(position.get(i));

		}
		Assert.assertFalse(drectorRes.asString().contains("isError"));
		Assert.assertFalse(drectorRes.asString().contains("isMissing"));
		Assert.assertFalse(drectorRes.asString().contains("isRestricted"));

	}

	@Test
	public void Shareholders_830() {
		String sharehdlerUri = "/v1/shareholders";
		String sharhlderUrl = baseURI + sharehdlerUri;

		Response shreholder = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue)

				.when().get(sharhlderUrl).then().statusCode(200).body("data.type", hasItem("shareholders"))
				.contentType(ContentType.JSON).extract().response();

		List<String> wonrshipType = shreholder.path("data.attributes.ownershipType");
		List<String> country = shreholder.path("data.attributes.country");
		List<String> name = shreholder.path("data.attributes.name");

		// System.out.println(shreholder.asString());

		Assert.assertFalse(shreholder.asString().contains("isError"));
		Assert.assertFalse(shreholder.asString().contains("isMissing"));
		Assert.assertFalse(shreholder.asString().contains("isRestricted"));

		for (int i = 0; i < wonrshipType.size(); i++) {

			// Assert.assertNotNull(wonrshipType.get(i));

			// Assert.assertNotNull(country.get(i));

			// Assert.assertNotNull(name.get(i));

		}

	}

	@Test

	public void Officers_756() {

		String officrUri = "/v1/officers";
		String offcrsUrl = baseURI + officrUri;

		Response officersdata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue)

				.when().get(offcrsUrl).then().statusCode(200).body("data.type", hasItem("officers"))
				.contentType("application/vnd.api+json").extract().response();

		List<String> name = officersdata.path("data.attributes.name");
		List<String> position = officersdata.path("data.attributes.position");

		Assert.assertFalse(officersdata.asString().contains("isError"));
		Assert.assertFalse(officersdata.asString().contains("isMissing"));

		for (int i = 0; i < name.size(); i++) {

			Assert.assertNotNull(name.get(i));
			Assert.assertNotNull(position.get(i));

		}
		Assert.assertFalse(officersdata.asString().contains("isError"));
		Assert.assertFalse(officersdata.asString().contains("isMissing"));
		Assert.assertFalse(officersdata.asString().contains("isRestricted"));

	}

	@Test()

	public void UpdateFinalcial_microSrvice_595() throws IOException {

		URL file = Resources.getResource("financial_Micro_Servce.json");

		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response financialData = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertTrue(financialData.asString().contains("currency"));

		List<String> id = financialData.path("data.attributes.entities.type");
		assert (id.contains("FitchID"));

		Assert.assertFalse(financialData.asString().contains("isError"));
		Assert.assertFalse(financialData.asString().contains("isMissing"));
		Assert.assertFalse(financialData.asString().contains("isRestricted"));

	}

	@Test()

	public void defaultCurrencyType_401_returnSingleCurrency() throws IOException {

		URL myfile = Resources.getResource("defaultCurreny_Type.json");

		String myJson = Resources.toString(myfile, Charsets.UTF_8);

		Response defaultCurrncyData = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myJson).with().when()
				.post(dataPostUrl).then().assertThat().log().ifError().statusCode(200).body(containsString("2017"))
				.body(containsString("Annual")).body(containsString("TWD")).extract().response();

		Assert.assertFalse(defaultCurrncyData.asString().contains("isError"));
		Assert.assertFalse(defaultCurrncyData.asString().contains("isMissing"));
		Assert.assertFalse(defaultCurrncyData.asString().contains("isRestricted"));

		List<String> id = defaultCurrncyData.path("data.attributes.entities.id");
		assert (id.contains("111631"));

	}

	@Test

	public void defaultLOB_570() throws IOException {

		URL xfile = Resources.getResource("Default_LOB.json");

		String jsonbody = Resources.toString(xfile, Charsets.UTF_8);

		Response dataResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(jsonbody).with()

				.when().post(dataPostUrl)

				.then().assertThat().statusCode(200).body(containsString("FC_SHARE_CAPITAL_INS"))
				.body(containsString("GBP")).extract().response();

		Assert.assertFalse(dataResponse.asString().contains("isError"));
		Assert.assertFalse(dataResponse.asString().contains("isMissing"));
		Assert.assertFalse(dataResponse.asString().contains("isRestricted"));

	}

	@Test
	public void period_resoltion_available_Statement_709() throws IOException {

		boolean failure = false;

		URL xfile = Resources.getResource("Period Resoluton for statemnet.json");

		String myjson = Resources.toString(xfile, Charsets.UTF_8);

		Response responseData = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myjson).with()

				.when().post(dataPostUrl).then().assertThat().statusCode(200).extract().response();

		String quatr1 = responseData.path("data.attributes.dateOptions.periods.get(0).type");
		String quatr2 = responseData.path("data.attributes.dateOptions.periods.get(1).type");
		String Grade1 = responseData.path("data.attributes.entities[0].values[1].values[0].value[0]");
		String Grade2 = responseData.path("data.attributes.entities[0].values[1].values[1].value[0]");

		if (quatr1.equals("Q1") && quatr2.equals("Annual") && (Grade1.equals("A+") && Grade2.equals("A+"))) {
			System.out.println(quatr1 + "," + quatr2);
			System.out.println(Grade1 + "," + Grade2);

		} else {
			failure = true;
			System.err.println(quatr1 + "Not working");
		}

		Assert.assertFalse(failure);
		Assert.assertFalse(responseData.asString().contains("isError"));
		Assert.assertFalse(responseData.asString().contains("isMissing"));
		Assert.assertFalse(responseData.asString().contains("isRestricted"));

	}

	@Test()

	public void defaultAccountstandard_IFRS_708_704() throws IOException {
		boolean faildata = false;

		URL xfile = Resources.getResource("defaultAccountingStandard.json");

		String myjson = Resources.toString(xfile, Charsets.UTF_8);

		Response dataRespnse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myjson).with().when()
				.post(dataPostUrl).then().assertThat().statusCode(200).body(containsString("USD"))
				.body(containsString("value")).extract().response();

		Assert.assertFalse(dataRespnse.asString().contains("isError"));
		Assert.assertFalse(dataRespnse.asString().contains("isMissing"));
		Assert.assertFalse(dataRespnse.asString().contains("isRestricted"));
	}

	@Test
	public void enforce_date_period_limit_714() throws IOException {
		URL xfile = Resources.getResource("enForce Date_Period limit.json");

		String myjson = null;

		myjson = Resources.toString(xfile, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType(contentValue).body(myjson).with().when().post(dataPostUrl).then().assertThat()
				.statusCode(200).body(containsString("A+")).body(containsString("2015-10-09"))
				.body(containsString("2015-12-09")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test()
	public void MetaData_Issue_1026() {

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then()
				.contentType(ContentType.JSON).extract().response();

		System.out.println(metaUrl);

		List<String> Id = response.path("data.id");
		List<String> displayNme = response.path("data.attributes.displayName");

		List<String> fitchFieldDes = response.path("data.attributes.fitchFieldDesc");

		List<String> link = response.path("data.links.self");

		for (int i = 0; i > Id.size(); i++) {
			Assert.assertNotNull(Id.get(i));
			Assert.assertNotNull(displayNme.get(i));
			Assert.assertNotNull(fitchFieldDes.get(i));
			Assert.assertNotNull(link.get(i));

		}

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

	}

	@Test
	public void currencyoption_716() throws IOException {

		URL file = Resources.getResource("currency_716.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response output = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200)

				.body("data.attributes.entities.values.values.value.USD", Matchers.notNullValue())

				.contentType(ContentType.JSON).extract().response();

		Assert.assertNotNull(output);
		Assert.assertFalse(output.asString().contains("isError"));
		Assert.assertFalse(output.asString().contains("isMissing"));
		Assert.assertFalse(output.asString().contains("isRestricted"));

	}

	@Test

	public void InputdateLimit_IndvidulReqest_267() throws IOException {
		URL file = Resources.getResource("inputDate Limit for Individual API Request.json");
		String jsonBody = Resources.toString(file, Charsets.UTF_8);
		Response Rspnse = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(jsonBody).with().when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("Date"))
				.body("data.attributes.entities[0].values[0].values[0].value[0]", equalTo("AA-"))
				.body("data.attributes.entities[0].values[0].values[1].value[0]", equalTo("AA-"))

				.extract().response();

		Assert.assertNotNull(Rspnse);
		Assert.assertFalse(Rspnse.asString().contains("isError"));
		Assert.assertFalse(Rspnse.asString().contains("isMissing"));
		Assert.assertFalse(Rspnse.asString().contains("isRestricted"));

		List<String> datesOption = Rspnse.path("data.attributes.dateOptions.dates");

		for (int i = 0; i < datesOption.size(); i++) {

			Assert.assertNotNull(datesOption.get(i));
		}

	}

	// Test Description: Test period rating options
	@Test
	public void periodRating() throws IOException {

		URL file = Resources.getResource("periodRating.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().log().ifError().statusCode(200).body(containsString("Q1")).body(containsString("Q2"))
				.body(containsString("Q3")).body(containsString("Q4")).body(containsString("2015"))
				.body("data.attributes.entities[0].values[0].values[0].value[0]", equalTo("A+")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	// Test Description: Test period financial options
	@Test

	public void periodFinancial() throws IOException {

		URL file = Resources.getResource("periodFinancial.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("1003961"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID")).assertThat().log().ifError()
				.statusCode(200).extract().response();

		List<String> actual_value = res.path("data.attributes.entities.values.values.value.GBP");
		Assert.assertNotNull(actual_value);

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Test for period summary
	@Test

	public void periodEntitySummary() throws IOException {

		URL file = Resources.getResource("periodEntitySummary.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("116980"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID")).assertThat()
				.body(containsString("Annual")).body(containsString("Q3")).body(containsString("Q1"))
				.body(containsString("Q2")).body(containsString("2014-09-30")).body(containsString("2014-09-30"))
				.statusCode(200).extract().response();

		Assert.assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Test for period moodys option
	@Test

	public void periodMoodys() throws IOException {

		URL file = Resources.getResource("periodMoodys.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("116980"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).values.get(0).values.get(0).value.get(0)", equalTo("Aa3"))
				.body("data.attributes.entities.get(0).values.get(0).values.get(1).value.get(0)", equalTo("Aa3"))
				.body("data.attributes.entities.get(0).values.get(0).values.get(2).value.get(0)", equalTo("Aa3"))
				.body(containsString("Q1")).body(containsString("Q2")).body(containsString("Q3"))
				.body(containsString("Annual")).body(containsString("Q4")).body(containsString("2014-06-30"))
				.body(containsString("2015-12-31")).assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Test for date and period rating options
	@Test

	public void dateAndPeriodRating() throws IOException {

		URL file = Resources.getResource("datePeriodRating.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("80089181"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("fitchGroupId"))
				.body("data.attributes.entities.get(0).values.get(0).values.get(0).value.get(0)", equalTo("A+"))
				.body("data.attributes.entities.get(0).values.get(0).values.get(1).value.get(0)", equalTo("A+"))
				.assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);

		List<String> periodResolutionDate = res.path("data.attributes.entities.periodResolution.get(0).periodDate");
		Assert.assertNotNull(periodResolutionDate);

		List<String> periodResolutiontype = res.path("data.attributes.entities.periodResolution.get(0).type");
		Assert.assertNotNull(periodResolutiontype);

		List<String> periodResolutionyear = res.path("data.attributes.entities.periodResolution.get(0).year");
		Assert.assertNotNull(periodResolutionyear);

		Assert.assertTrue(periodResolutionDate.get(0).contains("2014-03-31"));
		Assert.assertTrue(periodResolutiontype.get(0).contains("Q1"));
		Assert.assertEquals(periodResolutionyear.get(0), 2014);

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Test for date and period financial options
	@Test

	public void dateAndPeriodFinancial() throws IOException {

		URL file = Resources.getResource("dateAndPeriodFinancial.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities[0].id", equalTo("116980"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID")).assertThat().log().ifError()
				.statusCode(200).extract().response();

		Assert.assertNotNull(res);

		List<String> periodResolutionDate = res.path("data.attributes.entities.periodResolution.get(0).periodDate");
		Assert.assertNotNull(periodResolutionDate);
		;

		List<String> periodResolutiontype = res.path("data.attributes.entities.periodResolution.get(0).type");
		Assert.assertNotNull(periodResolutiontype);

		List<String> periodResolutionyear = res.path("data.attributes.entities.periodResolution.get(0).year");
		Assert.assertNotNull(periodResolutionyear);

		Assert.assertTrue(periodResolutionDate.get(0).contains("2014-03-31"));
		Assert.assertTrue(periodResolutiontype.get(0).contains("Q1"));
		Assert.assertEquals(periodResolutionyear.get(0), 2014);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Test for date and period Entity summary options
	@Test

	public void dateAndPeriodEntitySummary() throws IOException {

		URL file = Resources.getResource("dateAndPeriodEntitySummary.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("116980"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID")).assertThat().log().ifError()
				.statusCode(200).extract().response();

		Assert.assertNotNull(res);

		List<String> periodResolutionDate = res.path("data.attributes.entities.periodResolution.get(0).periodDate");
		Assert.assertNotNull(periodResolutionDate);
		;

		List<String> periodResolutiontype = res.path("data.attributes.entities.periodResolution.get(0).type");
		Assert.assertNotNull(periodResolutiontype);

		List<String> periodResolutionyear = res.path("data.attributes.entities.periodResolution.get(0).year");
		Assert.assertNotNull(periodResolutionyear);

		Assert.assertTrue(periodResolutionDate.get(0).contains("2014-03-31"));
		Assert.assertTrue(periodResolutiontype.get(0).contains("Q1"));
		Assert.assertEquals(periodResolutionyear.get(0), 2014);

		List<String> actual_value = res.path("data.attributes.entities.get(0).values.get(0).values.get(0).value");
		Assert.assertNotNull(actual_value);
		Assert.assertTrue(actual_value.contains("1 New Orchard Road"));

		actual_value = res.path("data.attributes.entities.get(0).values.get(0).values.get(1).value");
		Assert.assertNotNull(actual_value);
		Assert.assertTrue(actual_value.contains("1 New Orchard Road"));

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: checks for date and period moodys option
	@Test(enabled=false)

	public void dateAndPeriodMoodys() throws IOException {

		URL file = Resources.getResource("dateAndPeriodMoodys.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("IBM"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("companyTicker")).body(containsString("A1"))
				.body(containsString("Aa3"))

				.extract().response();

		System.out.println(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Checks for period range qtrly.
	@Test

	public void periodRangeQtr() throws IOException {

		URL file = Resources.getResource("periodRangeQRT.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("116980"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).fitchEntityId", equalTo("116980")).body(containsString("Q1"))
				.body(containsString("Q2")).body(containsString("Q3")).body(containsString("Q4"))
				.body("data.attributes.entities.get(0).values.get(0).fitchFieldId", equalTo("FC_INT_INC_LOANS_BNK"))
				.assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Checks for period range annually.
	@Test

	public void periodRangeAnnually() throws IOException {

		URL file = Resources.getResource("periodRangeAnnually.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("110631"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).fitchEntityId", equalTo("110631"))
				.body("data.attributes.entities.get(0).values.get(0).fitchFieldId", equalTo("FC_INT_INC_LOANS_BNK"))
				.assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);

		List<String> periodResolutionDate = res.path("data.attributes.entities.periodResolution.get(0).periodDate");
		Assert.assertNotNull(periodResolutionDate);

		List<String> periodResolutiontype = res.path("data.attributes.entities.periodResolution.get(0).type");
		Assert.assertNotNull(periodResolutiontype);

		List<String> periodResolutionyear = res.path("data.attributes.entities.periodResolution.get(0).year");
		Assert.assertNotNull(periodResolutionyear);

		List<String> dateOptionsType = res.path("data.attributes.dateOptions.periods.type");
		Assert.assertNotNull(dateOptionsType);

		List<Integer> dateOptionsYear = res.path("data.attributes.dateOptions.periods.year");
		Assert.assertNotNull(dateOptionsYear);

		List<String> actual_value = res.path("data.attributes.entities.get(0).values.get(0).values");
		Assert.assertNotNull(actual_value);
		Assert.assertTrue(dateOptionsType.get(0).contains("Annual"));
		Assert.assertTrue(dateOptionsYear.get(0).equals(2010));

		Assert.assertTrue(dateOptionsType.get(1).contains("Annual"));
		Assert.assertTrue(dateOptionsYear.get(1).equals(2011));
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Checks for period range with null or unreached endpoint
	@Test

	public void periodRangeNullEndPoint() throws IOException {

		URL file = Resources.getResource("periodRangeNullEndpoint.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("110631"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).fitchEntityId", equalTo("110631"))
				.body("data.attributes.entities.get(0).values.get(0).fitchFieldId", equalTo("FC_INT_INC_LOANS_BNK"))
				.assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);

		List<String> dateOptionsType = res.path("data.attributes.dateOptions.periods.type");
		Assert.assertNotNull(dateOptionsType);

		List<Integer> dateOptionsYear = res.path("data.attributes.dateOptions.periods.year");
		Assert.assertNotNull(dateOptionsYear);

		List<String> actual_value = res.path("data.attributes.entities.get(0).values.get(0).values.value.USD");
		Assert.assertNotNull(actual_value);
		Assert.assertTrue(dateOptionsType.get(0).contains("Annual"));
		Assert.assertTrue(dateOptionsYear.get(0).equals(2013));

		Assert.assertTrue(dateOptionsType.get(1).contains("Annual"));
		Assert.assertTrue(dateOptionsYear.get(1).equals(2014));

		Assert.assertTrue(dateOptionsType.get(2).contains("Annual"));
		Assert.assertTrue(dateOptionsYear.get(2).equals(2015));

		Assert.assertTrue(dateOptionsType.get(3).contains("Annual"));
		Assert.assertTrue(dateOptionsYear.get(3).equals(2016));
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Checks for period range qtrly Rating.
	@Test

	public void dateDailyAndPeriodQtrRating() throws IOException {

		URL file = Resources.getResource("date daily and period.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("116980"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).fitchEntityId", equalTo("116980")).body(containsString("Q2"))
				.body(containsString("Q3")).body(containsString("Q4"))
				.body("data.attributes.entities.get(0).values.get(0).fitchFieldId", equalTo("FC_LT_IDR")).assertThat()
				.log().ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);

		List<String> actual_values = res.path("data.attributes.entities.get(0).values.get(0).values");
		List<String> dateOptions = res.path("data.attributes.dateOptions.dates");

		for (int i = 0; i < dateOptions.size(); i++) {
			Assert.assertNotNull(dateOptions.get(i));

		}

		for (int j = 0; j < actual_values.size(); j++) {
			Assert.assertNotNull(actual_values.get(j));

		}

		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(0).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(1).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(2).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(3).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(4).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(5).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(6).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(7).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(8).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(9).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(10).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(11).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(12).value.get(0)"),
				("A+"));
		Assert.assertEquals(res.path("data.attributes.entities.get(0).values.get(0).values.get(13).value.get(0)"),
				("A+"));

		/*
		 * Assert.assertTrue(dateOptions.get(0).contains("2011-01-01"));
		 * Assert.assertTrue(dateOptions.get(1).contains("2015-01-01"));
		 * Assert.assertTrue(dateOptions.get(2).contains("2015-01-02"));
		 * Assert.assertTrue(dateOptions.get(3).contains("2015-01-05"));
		 * Assert.assertTrue(dateOptions.get(4).contains("2015-01-06"));
		 * Assert.assertTrue(dateOptions.get(5).contains("2015-01-07"));
		 * Assert.assertTrue(dateOptions.get(6).contains("2015-01-08"));
		 * Assert.assertTrue(dateOptions.get(7).contains("2015-01-09"));
		 * Assert.assertTrue(dateOptions.get(8).contains("2015-01-12"));
		 * Assert.assertTrue(dateOptions.get(9).contains("2015-01-13"));
		 * Assert.assertTrue(dateOptions.get(10).contains("2015-01-14"));
		 * Assert.assertTrue(dateOptions.get(11).contains("2015-01-15"));
		 */

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Checks for period range qtrly financial option.
	@Test

	public void dateDailyAndPeriodQtrFinancial() throws IOException {

		URL file = Resources.getResource("dateDailyPeriodQtrRangeFinancial.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("116980"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).fitchEntityId", equalTo("116980"))
				.body("data.attributes.entities.get(0).values.get(0).fitchFieldId", equalTo("FC_INT_INC_LOANS_BNK"))
				.assertThat().log().ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);

		List<String> dateOptions = res.path("data.attributes.dateOptions.dates");

		for (int i = 0; i < dateOptions.size(); i++) {
			Assert.assertNotNull(dateOptions.get(i));

		}

		List<String> periodResolutionDate = res.path("data.attributes.entities.periodResolution.get(0).periodDate");
		Assert.assertNotNull(periodResolutionDate);

		List<String> periodResolutiontype = res.path("data.attributes.entities.periodResolution.get(0).type");
		Assert.assertNotNull(periodResolutiontype);

		List<String> periodResolutionyear = res.path("data.attributes.entities.periodResolution.get(0).year");
		Assert.assertNotNull(periodResolutionyear);

		List<String> dateOptionsType = res.path("data.attributes.dateOptions.periods.type");
		Assert.assertNotNull(dateOptionsType);

		List<Integer> dateOptionsYear = res.path("data.attributes.dateOptions.periods.year");
		Assert.assertNotNull(dateOptionsYear);
		/*
		 * Assert.assertTrue(dateOptions.get(0).contains("2015-01-01"));
		 * Assert.assertTrue(dateOptions.get(1).contains("2015-01-02"));
		 * Assert.assertTrue(dateOptions.get(3).contains("2015-01-06"));
		 * Assert.assertTrue(dateOptions.get(4).contains("2015-01-07"));
		 * Assert.assertTrue(dateOptions.get(5).contains("2015-01-08"));
		 * Assert.assertTrue(dateOptions.get(6).contains("2015-01-09"));
		 * Assert.assertTrue(dateOptions.get(7).contains("2015-01-12"));
		 * Assert.assertTrue(dateOptions.get(8).contains("2015-01-13"));
		 * Assert.assertTrue(dateOptions.get(9).contains("2015-01-14"));
		 * Assert.assertTrue(dateOptions.get(10).contains("2015-01-15"));
		 */

		Assert.assertTrue(dateOptionsType.get(0).contains("Q2"));
		Assert.assertTrue(dateOptionsYear.get(0).equals(2010));

		Assert.assertTrue(dateOptionsType.get(1).contains("Q3"));
		Assert.assertTrue(dateOptionsYear.get(1).equals(2010));

		Assert.assertTrue(dateOptionsType.get(2).contains("Q4"));
		Assert.assertTrue(dateOptionsYear.get(2).equals(2010));

		Assert.assertTrue(dateOptionsType.get(3).contains("Q1"));
		Assert.assertTrue(dateOptionsYear.get(3).equals(2011));

		Assert.assertTrue(dateOptionsType.get(4).contains("Q2"));
		Assert.assertTrue(dateOptionsYear.get(4).equals(2011));

		Assert.assertTrue(dateOptionsType.get(5).contains("Q3"));
		Assert.assertTrue(dateOptionsYear.get(5).equals(2011));

		Assert.assertTrue(dateOptionsType.get(6).contains("Q4"));
		Assert.assertTrue(dateOptionsYear.get(6).equals(2011));

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: checks for multiple financial field options
	@Test()

	public void multipleFinancialFields() throws IOException {

		URL file = Resources.getResource("multipleFinancialFields.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()

				.body("data.attributes.entities.get(0).id", equalTo("110631")).body(containsString("2010"))
				.body(containsString("2015")).body(containsString("Q1")).body(containsString("USD"))
				.body(containsString("value")).body(containsString("Annual")).body(containsString("periodResolution"))

				.body("data.attributes.entities.get(0).values.get(3).type", equalTo("currency")).assertThat().log()
				.ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: Test multiple financial entities at once
	@Test

	public void multipleFinancialEntities() throws IOException {

		URL file = Resources.getResource("multipleFinancialEntities.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("110631"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).fitchEntityId", equalTo("110631"))
				.body("data.attributes.entities.get(0).values.get(0).fitchFieldId", equalTo("FC_INT_INC_LOANS_BNK"))
				.body("data.attributes.entities.get(0).values.get(0).type", equalTo("currency"))
				.body("data.attributes.entities.get(1).id", equalTo("14528"))
				.body("data.attributes.entities.get(1).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(1).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(1).fitchEntityId", equalTo("14528"))
				.body("data.attributes.entities.get(1).values.get(0).fitchFieldId", equalTo("FC_INT_INC_LOANS_BNK"))
				.body("data.attributes.entities.get(1).values.get(0).type", equalTo("currency")).assertThat().log()
				.ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);

		List<String> actual_values = res.path("data.attributes.entities.get(0).values.get(0).values");
		List<String> dateOptions = res.path("data.attributes.dateOptions.dates");

		for (int i = 0; i < dateOptions.size(); i++) {
			Assert.assertNotNull(dateOptions.get(i));

		}

		for (int j = 0; j < actual_values.size(); j++) {
			Assert.assertNotNull(actual_values.get(j));

		}

		Assert.assertTrue(dateOptions.get(0).contains("2014-12-31"));
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: test Preliminary option
	@Test

	public void perliminaryOptionFinancial() throws IOException {

		URL file = Resources.getResource("perliminaryOptionFinancial.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("1150233"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).fitchEntityId", equalTo("1150233"))
				.body("data.attributes.entities.get(0).values.get(0).fitchFieldId", equalTo("FC_SHARE_CAPITAL_INS"))
				.body("data.attributes.entities.get(0).values.get(0).type", equalTo("currency")).assertThat().log()
				.ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: test life option insurance option
	@Test

	public void lifeOptionInsurance() throws IOException {

		URL file = Resources.getResource("lifeOptionInsurance.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("1346752"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).fitchEntityId", equalTo("1346752"))

				.body("data.attributes.entities.get(0).values.get(0).type", equalTo("currency")).assertThat().log()
				.ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: test nonlife option insurance option
	@Test()

	public void nonLifeOptionInsurance() throws IOException {

		URL file = Resources.getResource("nonLifeOptionInsurance.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body(containsString("USD")).body(containsString("timeIntervalPeriod")).body(containsString("value"))
				.body(containsString("currency")).body(containsString("type")).body(containsString("year"))

				.statusCode(200).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: test composite option insurance option
	@Test

	public void compositeOptionInsurance() throws IOException {

		URL file = Resources.getResource("compositeOptionInsurance.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.statusCode(200).body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("1153930"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).fitchEntityId", equalTo("1153930"))
				.body("data.attributes.entities.get(0).values.get(0).fitchFieldId", equalTo("FC_SHARE_CAPITAL_INS"))
				.body("data.attributes.entities.get(0).values.get(0).type", equalTo("currency")).assertThat().log()
				// .body("data.attributes.entities[0].values[0].values[0].timeIntervalPeriod.year",equalTo((int)2015))
				.ifError().statusCode(200).extract().response();

		Assert.assertNotNull(res);
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	// Test Description: test default option insurance option
	@Test

	public void defaultOptionsInsurance() throws IOException {

		URL file = Resources.getResource("defaultOptionInsurance.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.body("data.attributes.entities.get(0).id", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).id", equalTo("1153930"))
				.body("data.attributes.entities.get(0).type", Matchers.notNullValue())
				.body("data.attributes.entities.get(0).type", equalTo("FitchID"))
				.body("data.attributes.entities.get(0).fitchEntityId", equalTo("1153930"))
				.body("data.attributes.entities.get(0).values.get(0).fitchFieldId", equalTo("FC_SHARE_CAPITAL_INS"))
				.body("data.attributes.entities.get(0).values.get(0).type", equalTo("currency")).assertThat().log()
				.ifError().statusCode(200).extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void allrelationshipOfanEntity() {

		String url = baseURI + "/v1/entities/1708";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").when().get(url).then().statusCode(200).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test
	public void financial_Datawith_ranking_field() throws IOException {

		URL file = Resources.getResource("financial_dataWith_ranking.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.statusCode(200)

				.extract().response();

		Assert.assertTrue(res.asString().contains("values"));

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test(enabled = false)

	public void defaultOptions_FinancialServiceBank() throws IOException {

		URL file = Resources.getResource("defaultOption_financial_bank.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.statusCode(200).extract().response();

		Assert.assertFalse(res.asString().contains("USD"));

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	@Test	
	public void FISC_5441_IssueRatingType (){
			
			String issueRatingTypeURI = baseURI+"/v1/issues/96496679/issueRatings";		

			Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).when().get(issueRatingTypeURI).then()
					.statusCode(200)
					.body(containsString("rating"))
					.body(containsString("Affirmed"))
					.body(containsString("FC_LT_NIR"))
					.extract().response();
	  }

	@Test	
	public void FISC_5441_IssuerRatingType (){
			
			String issueRatingTypeURI = baseURI+"/v1/entities/1000791/fitchIssuerRatings?filter[startDate]=2018-07-27";		

			Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).when().get(issueRatingTypeURI).then()
					.statusCode(200)
					.body(containsString("FC_LT_NIR_ISSR"))
					.extract().response();
	  }
	
	
	
	
	
	//==================================================Some Scripts=========================================================================================
	

	@Test(enabled = false)

	public void All_metaData_fields() {

		String metaDataURI = baseURI + "/v1/metadata/fields";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaDataURI).then()
				.statusCode(200).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		List<String> MetafildId = res.path("data.id");

		System.out.println(MetafildId.size());

		// ExecutorService executor = Executors.newFixedThreadPool(50);

		for (int i = 0; i < MetafildId.size(); i++) {
			/*
			 * final int idx = i; executor.submit(() -> {
			 */

			String PerMetaDataUri = metaDataURI + "/" + MetafildId.get(i);

			int statusCode = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(PerMetaDataUri).statusCode();

			if (statusCode != 200) {
				System.err.println("Status Code " + statusCode + " MetaData Field Name : " + MetafildId.get(i));
				failure = true;

			} else {
				System.out.println(i + " fields are completed");
			}

			// });

		}

		/*
		 * try { System.out.println("attempt to shutdown executor");
		 * executor.shutdown(); executor.awaitTermination(12, TimeUnit.HOURS); } catch
		 * (InterruptedException e) { System.err.println( "tasks interrupted"); }
		 */

	}

	@Test(enabled = false)
	public void numberOfentitiesPerPage() {

		String entityURI = baseURI + "/v1/issuers";

		int numberofPages = 432;

		String newURI = entityURI + "?page[number]=";

		for (int i = 0; i < numberofPages; i++) {

			int num = (i);

			String NewURIx = newURI + num;

			Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
					.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(NewURIx)
					.then().assertThat().statusCode(200).extract().response();
			List<String> Id = res.path("data.id");

			// System.out.println (Id.size());

			Id.forEach(System.out::println);

		}

	}

	@Test(enabled = false)

	public void numberOftransactionPerPage() {

		String transctionURI = baseURI + "/v1/transactions/?page[limit]=50&";

		int offsetPage = 2400;

		String newURI = transctionURI + "page[offset]=";

		ExecutorService executor = Executors.newFixedThreadPool(10);

		for (int i = 0; i < offsetPage; i += 50) {
			final int idx = i;
			executor.submit(() -> {

				System.out.println(idx);

				String NewURIx = newURI + idx;

				Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
						.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(NewURIx)
						.then().assertThat().statusCode(200).extract().response();
				List<String> Id = res.path("data.id");

				// System.out.println(Id.size());

				Id.forEach(System.out::println);

			});

		}

		try {
			System.out.println("attempt to shutdown executor");
			executor.shutdown();
			executor.awaitTermination(12, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			System.err.println("tasks interrupted");
		}

	}

	@Test(enabled = false)
	public void numberof_issues() {

		String transctionURI = baseURI + "/v1/issues?page[limit]=50&";

		int offsetPage = 429100;

		String newURI = transctionURI + "page[offset]=";

		ExecutorService executor = Executors.newFixedThreadPool(2);

		for (int i = 0; i < offsetPage; i += 50) {
			final int idx = i;
			executor.submit(() -> {

				String NewURIx = newURI + idx;
				// System.out.println(NewURIx);

				Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
						.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(NewURIx)
						.then().assertThat().statusCode(200).extract().response();
				List<String> Id = res.path("data.id");

				// System.out.println(Id.size());

				Id.forEach(System.out::println);

			});

		}

		try {
			System.out.println("attempt to shutdown executor");
			executor.shutdown();
			executor.awaitTermination(12, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			System.err.println("tasks interrupted");
		}

	}

	@Test(enabled = false)
	public void numberOfEntitiesPerPage_testing() {

		String entityURI = baseURI + "/v1/entities";

		int numberofPages = 1847;

		String newURI = entityURI + "?page[number]=";

		ExecutorService executor = Executors.newFixedThreadPool(10);

		for (int i = 0; i < numberofPages; i++) {
			final int idx = i;
			executor.submit(() -> {

				String NewURIx = newURI + idx + "&page[size]=50";

				// System.out.println(NewURIx);

				Response res = given().header("Authorization", AuthrztionValue)
						.header("content-type", "application/vnd.api+json").header("'Accept", acceptValue)
						.header("X-App-Client-Id", XappClintIDvalue).when().get(NewURIx).then().assertThat()
						.statusCode(200).extract().response();

				List<String> Id = res.path("data.id");

				// System.out.println(Id.size());

				Id.forEach(System.out::println);

			});

		}

		try {
			System.out.println("attempt to shutdown executor");
			executor.shutdown();
			executor.awaitTermination(12, TimeUnit.HOURS);

		} catch (InterruptedException e) {
			System.err.println("tasks interrupted");
		}

	}

}
