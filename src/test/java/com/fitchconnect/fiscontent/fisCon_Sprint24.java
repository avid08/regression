package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint24 extends Configuration{
	@Test(enabled=false)
	public void fisc_2271_flagsToIdentify() throws IOException, URISyntaxException {
		String jsonAsString;

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then()
				.contentType(ContentType.JSON).statusCode(200).extract().response();

		jsonAsString = response.asString();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

		URL fileUrl = Resources.getResource("");

		File src = new File(fileUrl.toURI());

		FileInputStream file = new FileInputStream(src);

		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("");
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
	
	@Test
	public void FISC_2279_statementResourcefilter() {

		String stmentURI = baseURI + "/v1/entities/113580/statements?fields[statements]=detail";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(stmentURI).then()
				.assertThat().statusCode(200)
				
				.body(containsString("currency"))				
				.body(containsString("FC_CASH_MARKETABLE_CORPS"))
				.body(containsString("USD"))
				.body(containsString("number"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	@Test
	public void FISC_220_categoy1selfLink() {

		String metaDataURI = baseURI + "/v1/metadata/categories/1/fields";

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(metaDataURI).then()
				.assertThat().statusCode(200)
				.body(containsString("FC_ULT_PARENT_ID"))
				.body(containsString("FC_DATE_INCORPORATION"))
				.body(containsString("fields"))
				.body(containsString("fitchFieldDesc"))
				.body(containsString("permissionsRequired"))					
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}
	
	
	


}
