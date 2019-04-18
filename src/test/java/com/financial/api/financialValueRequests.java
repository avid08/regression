package com.financial.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class financialValueRequests extends Configuration {
	
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

					.then().assertThat().statusCode(200)
					.body(containsString("EUR"))
					.extract().response();
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
					.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myJson).with()
					.when().post(dataPostUrl)
					.then().assertThat().log().ifError().statusCode(200).body(containsString("2017"))
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
			Assert.assertTrue(dateOptions.get(0).contains("2015-01-01"));
			Assert.assertTrue(dateOptions.get(1).contains("2015-01-02"));
		Assert.assertTrue(dateOptions.get(3).contains("2015-01-06"));
			Assert.assertTrue(dateOptions.get(4).contains("2015-01-07"));
			Assert.assertTrue(dateOptions.get(5).contains("2015-01-08"));
			Assert.assertTrue(dateOptions.get(6).contains("2015-01-09"));
			Assert.assertTrue(dateOptions.get(7).contains("2015-01-12"));
			Assert.assertTrue(dateOptions.get(8).contains("2015-01-13"));
			Assert.assertTrue(dateOptions.get(9).contains("2015-01-14"));
			Assert.assertTrue(dateOptions.get(10).contains("2015-01-15"));*/

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
		
		
		@Test(enabled=false)

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
}
