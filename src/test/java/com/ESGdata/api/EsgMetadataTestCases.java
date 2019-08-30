package com.ESGdata.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;

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
import com.jayway.restassured.response.Response;

public class EsgMetadataTestCases extends Configuration{
	
	@Test(enabled=false)
	
	public void ESG_MetaData_Verification() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("ESG_Bnk_NvigatorData.xlsx");
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
			String dataType = mySheet.getRow(i).getCell(4).getStringCellValue();
			String permission = mySheet.getRow(i).getCell(5).getStringCellValue();

			String DataTypeUrl = metaUrl + "/" + fitchFieldIds;



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

}
