package com.Cds.Api;

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

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Cds_TestCases extends Configuration {
	
 @Test
 
  public void cds_dataWithMultidatesNdMultiEntities_FISC_6690() throws IOException{
	 
		URL file = Resources.getResource("Fisc_6690.json");

		myjson = Resources.toString(file, Charsets.UTF_8);

		Response leglAgnetresponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myjson).with()
				.when().post(dataPostUrl).then().assertThat().statusCode(200).body(containsString("value"))
				.body(containsString("JPY")).extract()
				.response();

		Assert.assertFalse(leglAgnetresponse.asString().contains("isError"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isMissing"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isRestricted"));
	 
	 
  }
 
 
	
@Test
 public void cdsValueRequest_NonExistingCdsEntity_FISC_6691() throws IOException{
	 
		URL file = Resources.getResource("Fisc_6691.json");

		myjson = Resources.toString(file, Charsets.UTF_8);

		Response leglAgnetresponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myjson).with()
				.when().post(dataPostUrl).then().assertThat().statusCode(200).body(containsString("value"))
				.body(containsString("JPY")).extract()
				.response();

		Assert.assertFalse(leglAgnetresponse.asString().contains("isError"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isMissing"));
		Assert.assertFalse(leglAgnetresponse.asString().contains("isRestricted"));
	 
	 
}
 
@Test
public void cdsValueRequest_with150Entities_multipleDatapoints() throws IOException{
	 
		URL file = Resources.getResource("Fisc_6690_with150Entities.json");

		myjson = Resources.toString(file, Charsets.UTF_8);

		Response leglAgnetresponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myjson).with()
				.when().post(dataPostUrl).then().assertThat().statusCode(200).body(containsString("value"))
				.body(containsString("JPY"))
				.body(containsString("Sekisui House, Ltd"))
				.body(containsString("auditTrail"))
				.body(containsString("currency"))
				.body(containsString("numerical"))
				
				.extract()
				.response();

		Assert.assertFalse(leglAgnetresponse.asString().contains("isError"));		
		Assert.assertFalse(leglAgnetresponse.asString().contains("isRestricted"));
	 
	 
}

@Test(enabled=true)

public void Fisc_1974_CDS_MetaData__Verification() throws URISyntaxException, IOException {

	URL fileUrl = Resources.getResource("CDS_Data_Mnemonics_v5.1.xlsx");
	File src = new File(fileUrl.toURI());
	FileInputStream file = new FileInputStream(src);
	XSSFWorkbook wb = new XSSFWorkbook(file);
	XSSFSheet mySheet = wb.getSheet("Sheet1");
	int rowcount = mySheet.getPhysicalNumberOfRows();
	System.out.println(rowcount + " fields are available");

	// ExecutorService executor = Executors.newFixedThreadPool(30);

	boolean failure = false;

	for (int i=1;i<rowcount; i++) {

		String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
		String displayName = mySheet.getRow(i).getCell(1).getStringCellValue();
		String fitchFieldDescp = mySheet.getRow(i).getCell(2).getStringCellValue();
		String dataType = mySheet.getRow(i).getCell(3).getStringCellValue();
		String permission = mySheet.getRow(i).getCell(4).getStringCellValue();

		String DataTypeUrl = metaUrl + "/" + fitchFieldIds;
		
		System.out.println(DataTypeUrl );



		String jsonAsString;

		Response response = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).when().get(DataTypeUrl).then().statusCode(200).extract()
				.response();

		jsonAsString = response.asString();
		
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

@Test(enabled=true)
public void cdsDataValueReqWith_multigrpIdsfromSameEntity_FISC_6754() throws IOException {

	URL file = Resources.getResource("multiGroupIds_frm_sameEntity_Cds.json");

	myjson = Resources.toString(file, Charsets.UTF_8);

	Date dNow = new Date();
	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
	String cdate = ft.format(dNow).toString();


	Response leglAgnetresponse = given()
			.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.contentType("application/vnd.api+json").body(myjson).with().when().post(dataPostUrl).then()
			.assertThat().statusCode(200).body(containsString("value")).extract().response();
	
	Assert.assertFalse(leglAgnetresponse.asString().contains("isError"));
	Assert.assertTrue(leglAgnetresponse.asString().contains("isMissing"));
	Assert.assertFalse(leglAgnetresponse.asString().contains("isRestricted"));
}

 
}
