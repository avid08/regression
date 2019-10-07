package com.fitchconnect.fiscontent;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.google.common.collect.Ordering;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint21 extends Configuration {
	@Test(enabled=true)
	public void Fisc_1988_newCategory() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("NewCateGory.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		// ExecutorService executor = Executors.newFixedThreadPool(30);
		String jsonAsString;
		boolean failure = false;

		for (int i = 1; i < rowcount; i++) {

			String categoryName = mySheet.getRow(i).getCell(0).getStringCellValue();
			double categoryID = mySheet.getRow(i).getCell(1).getNumericCellValue();
			int categoryIdx = (int) categoryID;
			String CateGoryURI = baseURI + "/v1/metadata/categories/" + categoryIdx;
			// System.out.println(CateGoryURI);

			Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).when().get(CateGoryURI).then()
					.statusCode(200).body(containsString("categories")).extract().response();

			jsonAsString = res.asString();

			System.out.println(categoryName);

			String resPnsename = res.path("data.attributes.name");

			System.out.println(resPnsename);

			// if (categoryName.replaceAll("\\s+","").equals(resPnsename))
			if (equalToIgnoringWhiteSpace(categoryName).matches(resPnsename)) {

			} else {
				failure = true;
				System.err.println("The response has categoryName  mismatch   : " + categoryIdx);
			}

		}

		file.close();
		Assert.assertFalse(failure);
	}

	@Test(enabled=true)
	public void FCStatementFlagLogic_1898() throws IOException {

		URL file = Resources.getResource("fisc_1898.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response IsoRes = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body(containsString("AED")).body(containsString("exchangeRate"))
				.body(containsString("baseCurrency")).body(containsString("CHF")).body(containsString("auditTrail"))
				//.body(containsString("false"))
				.body(containsString("noncon"))
				.body(containsString("reportingType"))
				.body(containsString("accountingStandard"))
				.body(containsString("consolidation"))
				.body(containsString("accountingStandard"))
				.body(containsString("true")).extract().response();

		Assert.assertFalse(IsoRes.asString().contains("isError"));
		Assert.assertFalse(IsoRes.asString().contains("isRestricted"));
		Assert.assertFalse(IsoRes.asString().contains("isMissing"));

	}

	@Test(enabled=true)
	public void NewfinancialAttributes_1897() throws IOException {

		URL file = Resources.getResource("FISC_1897.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response IsoRes = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body(containsString("noncon")).body(containsString("reportingType"))
				.body(containsString("accountingStandard")).body(containsString("consolidation"))
				.body(containsString("accountingStandard")).body(containsString("true")).body(containsString("IFRS"))
				.body(containsString("Local")).extract().response();

		Assert.assertFalse(IsoRes.asString().contains("isError"));
		Assert.assertFalse(IsoRes.asString().contains("isRestricted"));
		Assert.assertFalse(IsoRes.asString().contains("isMissing"));

	}

	@Test(enabled=true)
	public void Amntment_Date_Range_Option_1986() throws IOException {

		URL file = Resources.getResource("FISC_1986.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200)
				.body("data.attributes.entities[0].values[0].values[0].timeIntervalDate", equalTo("2016-12-31"))
				.body("data.attributes.entities[0].values[0].values[0].timeIntervalPeriod.year", equalTo(2016))
				.body("data.attributes.entities[0].values[0].values[1].timeIntervalDate", equalTo("2015-12-31"))
				.body("data.attributes.entities[0].values[0].values[1].timeIntervalPeriod.year", equalTo(2015))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		Assert.assertFalse(res.asString().contains("isMissing"));

	}

	@Test
	public void fisc_189_Entities_ratinEffctveDateSorting() {

		String ratingSortURI = baseURI
				+ "/v1/entities/116980/fitchIssuerRatings?sort[fitchIssuerRatings][effectiveDate]=desc"; // Desc
																											// order

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(ratingSortURI).then()
				.statusCode(200).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		List<String> effctiveDate = res.path("data.attributes.effectiveDate");

		boolean sorted = Ordering.natural().reverse().isOrdered(effctiveDate);
		Assert.assertTrue(sorted);

		String ratingSortURIx = baseURI
				+ "/v1/entities/116980/fitchIssuerRatings?sort[fitchIssuerRatings][effectiveDate]=asc"; // ASC
																										// order

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(ratingSortURIx).then()
				.statusCode(200).extract().response();

		Assert.assertFalse(res1.asString().contains("isRestricted"));

		List<String> effctiveDatex = res1.path("data.attributes.effectiveDate");

		System.out.println(effctiveDatex);
 

		String ratingSortFilterURIx = baseURI
				+ "/v1/entities/116980/fitchIssuerRatings?filter[ratingType]=FC_LT_IDR,FC_ST_IDR&filter[startDate]=2010-01-01&filter[endDate]=2016-12-31&filter[ratingAction]"
				+ "=No Action,affirmed&filter[ratingAlert]=stable";

		Response res2 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(ratingSortFilterURIx).then()
				.statusCode(200).body(containsString("FC_LT_IDR")).body(containsString("Affirmed")).extract()
				.response();	
		
		Assert.assertNotNull(res2.asString());

	}
	
	
	@Test
	public void fisc_189_issuers_ratinEffctveDateSorting() {

		String ratingSortURI = baseURI
				+ "/v1/issuers/80089181/fitchIssuerRatings?sort[fitchIssuerRatings][effectiveDate]=desc"; // Desc
																											// order

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(ratingSortURI).then()
				.statusCode(200).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));// Desc
																											// order

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(ratingSortURI).then()
				.statusCode(200).extract().response();

		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));

		List<String> effctiveDate = res.path("data.attributes.effectiveDate");

		boolean sorted = Ordering.natural().reverse().isOrdered(effctiveDate);
		Assert.assertTrue(sorted);

		String ratingSortURIx = baseURI
				+ "/v1/issuers/80089181/fitchIssuerRatings?sort[fitchIssuerRatings][effectiveDate]=asc"; // ASC
																										// order

		Response res3 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(ratingSortURIx).then()
				.statusCode(200).extract().response();

		Assert.assertFalse(res3.asString().contains("isRestricted"));

		List<String> effctiveDatex = res3.path("data.attributes.effectiveDate");

		System.out.println("assending"+effctiveDatex);

		boolean sortedx = Ordering.natural().isOrdered(effctiveDatex);

		Assert.assertTrue(sortedx);

		String ratingSortFilterURIx = baseURI
				+ "/v1/issuers/80089181/fitchIssuerRatings?filter[ratingType]=FC_LT_IDR,FC_ST_IDR&filter[startDate]=2010-01-01"
				+ "&filter[endDate]=2016-12-31&filter[ratingAction]=No Action,affirmed&filter[ratingAlert]=stable";

		Response res2 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(ratingSortFilterURIx).then()
				.statusCode(200).body(containsString("FC_LT_IDR")).body(containsString("Affirmed")).extract()
				.response();	
		
		Assert.assertNotNull(res2.asString());

	}

}
