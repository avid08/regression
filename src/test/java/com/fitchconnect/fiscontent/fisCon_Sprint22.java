package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint22 extends Configuration {

	@Test(enabled=false)

	public void Fisc_1974_CDS_MetaData__Verification() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("CDS_metaData_v1.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("CDS_fields");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		// ExecutorService executor = Executors.newFixedThreadPool(30);

		boolean failure = false;

		for (int i = 1; i < rowcount; i++) {

			String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
			String fitchFieldDescp = mySheet.getRow(i).getCell(1).getStringCellValue();
			String displayName = mySheet.getRow(i).getCell(2).getStringCellValue();
			String dataType = mySheet.getRow(i).getCell(3).getStringCellValue();
			String permission = mySheet.getRow(i).getCell(5).getStringCellValue();

			String DataTypeUrl = metaUrl + "/" + fitchFieldIds;

			// System.out.println(DataTypeUrl);

			String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(DataTypeUrl).then().statusCode(200).extract()
					.response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			
			Assert.assertFalse(response.asString().contains("isMissing"));

			String resPnsdsplyName = response.path("data.attributes.displayName");
			String resPnsDesc = response.path("data.attributes.fitchFieldDesc");

			// int index1 = jsonAsString.indexOf(fitchFieldIds);
			int index4 = jsonAsString.indexOf(dataType);
			int index5 = jsonAsString.indexOf(permission);

			if (equalToIgnoringWhiteSpace(displayName).matches(resPnsdsplyName)) {

			} else {
				failure = true;
				System.err.println("The response has displayName  mismatch   : " + fitchFieldIds);
			}

			if (equalToIgnoringWhiteSpace(fitchFieldDescp).matches(resPnsDesc)) {

			} else {

				System.err.println(resPnsDesc);
				System.err.println(fitchFieldDescp);

				failure = true;
				System.err.println("The response has Description  mismatch   : " + fitchFieldIds);
			}

			if (index4 != -1) {
				// System.out.println(i);

			} else {

				failure = true;
				System.err.println("Datatype has misamtch  for   : " + fitchFieldIds);

			}

			if (index5 != -1) {

			} else {

				failure = true;
				System.err.println(" permission has misamtch  for   : " + fitchFieldIds);

			}

		}

		file.close();
		Assert.assertFalse(failure);

	}

	@Test(enabled=true)
	public void dataAggregator_1974_CalendarDate_withData() throws IOException {

		URL file = Resources.getResource("fisc_1974.json");

		myjson = Resources.toString(file, Charsets.UTF_8);

		Response leglAgnetresponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myjson).with()
				.when().post(dataPostUrl).then().assertThat().statusCode(200).body(containsString("value"))
				.body(containsString("SEN")).body(containsString("USD")).body(containsString("2009-09-20")).extract()
				.response();

		Assert.assertFalse(leglAgnetresponse.asString().contains("isError"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isMissing"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isRestricted"));
	}

	@Test(enabled=true)
	public void dataAggregator_1974_calendarDate() throws IOException {

		URL file = Resources.getResource("fisc_1974_calendarDate.json");

		myjson = Resources.toString(file, Charsets.UTF_8);

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		String cdate = ft.format(dNow).toString();

		System.out.println(cdate);
		Response leglAgnetresponse = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myjson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body(containsString("value")).body(containsString("FC_RISK_BENCHMARK_BPS_CDS"))
				.body(containsString("172.3524163486361")).extract().response();

		Assert.assertFalse(leglAgnetresponse.asString().contains("isError"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isMissing"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isRestricted"));
	}

	@Test(enabled=true)
	public void dataAggregator_1974_NOcalendarDate() throws IOException {

		URL file = Resources.getResource("fisc_1974_NocalendarDate.json");

		myjson = Resources.toString(file, Charsets.UTF_8);

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		String cdate = ft.format(dNow).toString();

	
		Response leglAgnetresponse = given()
				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myjson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200).body(containsString("value")).extract().response();
		Assert.assertFalse(leglAgnetresponse.asString().contains("isError"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isMissing"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isRestricted"));
	}

	@Test
	public void predfinedValues_FISC_1999() throws IOException {

		String newattributes = baseURI + "/v1/metadata/fields/FC_LT_IR";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(newattributes).then().assertThat().statusCode(200).body(containsString("predefinedValues"))
				.body(containsString("AAA")).body(containsString("BBB")).extract().response();
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

}
