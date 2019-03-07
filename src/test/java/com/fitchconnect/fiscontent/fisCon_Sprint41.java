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
import com.jayway.restassured.response.Response;

public class fisCon_Sprint41 extends Configuration {
	
	@Test

	public void FISC_3514_TelecomOperatorMetadata() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("Telecom_Operators_DataMnemonics_V1.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		// ExecutorService executor = Executors.newFixedThreadPool(30);

		boolean failure = false;
		for (int i=1; i < rowcount; i++) {			

			String fitchFieldId = mySheet.getRow(i).getCell(0).getStringCellValue();
			String displayName = mySheet.getRow(i).getCell(1).getStringCellValue();
			String fitchfieldDesC = mySheet.getRow(i).getCell(2).getStringCellValue();
			String dataType = mySheet.getRow(i).getCell(3).getStringCellValue();
			//String categoryId = mySheet.getRow(i).getCell(4).getStringCellValue();
			String permissionRequird = mySheet.getRow(i).getCell(6).getStringCellValue();
           			
			String DataTypeUrl = metaUrl + "/" + fitchFieldId;
			
			System.out.println(DataTypeUrl);
			
			String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").header("accept", acceptValue)
					.header("content", contentValue).when().get(DataTypeUrl).then().statusCode(200)
					.body(containsString("Telecommunications")).body(containsString("telecomOperators"))
					.body(containsString("dataService")).extract().response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));
	
			//System.out.println(ApIparentId);

			int index1 = jsonAsString.indexOf(fitchFieldId);
			int index2 = jsonAsString.indexOf(displayName);
			int index3 = jsonAsString.indexOf(fitchfieldDesC);
			int index4 = jsonAsString.indexOf(dataType);
			//int index5 = jsonAsString.indexOf(categoryId);
			int index6 = jsonAsString.indexOf(permissionRequird);
			
			if (index2 !=-1) {
				//System.out.println(i);

			} else {
				failure = true;
				System.err.println("The response has mismtach for displayname: " + fitchFieldId);
			}
			
			if (index3 !=-1) {
				//System.out.println(i);

			} else {
				failure = true;
				System.err.println("The response has mismatch for fitchFielddescription: " + fitchFieldId);
			}
			
			if (index4 !=-1 && index6 !=-1) {
				//System.out.println(i);

			} else {
				failure = true;
				System.err.println("The response has mismatch for DataType and Permission: " + fitchFieldId);
			}		
		

		}
		
		file.close();
		Assert.assertFalse(failure);

	}
	
	
	@Test

	public void FISC_3514_TelecomOperatorCategory() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("Telecom_Operators_DataMnemonics_V1.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		// ExecutorService executor = Executors.newFixedThreadPool(30);

		boolean failure = false;
		for (int i=1; i < rowcount; i++) {			

			String fitchFieldId = mySheet.getRow(i).getCell(0).getStringCellValue();		
			
			double categoryId = mySheet.getRow(i).getCell(4).getNumericCellValue();			
			int  categoryIdint = (int) categoryId ;			
			String xlcategoryIdStr = Integer.toString(categoryIdint);	
			
		
			String CategoryUrl = metaUrl + "/" + fitchFieldId+"/categories";
			
			System.out.println(CategoryUrl);

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(CategoryUrl).then().statusCode(200)
					.body(containsString("categories")).extract().response();			

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));
			
			String categoryIdx = response.path("data.id");		
	
          if ( categoryIdx.equals(xlcategoryIdStr)) {				

			} else {
				failure = true;
				System.err.println("The response has category  ID  mismatch   : " + fitchFieldId);
				
						
			}		

		}
		
		file.close();
		Assert.assertFalse(failure);

	}


}
