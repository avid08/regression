package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import com.configuration.api.Configuration;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class metaData_CompareTestCases extends Configuration {

	@Test()
	public void BMImetaDataResponse_Compare() throws IOException, URISyntaxException {

		URL fileUrl = Resources.getResource("BMI_MetaData.xlsx");

		File src = new File(fileUrl.toURI());

		FileInputStream file = new FileInputStream(src);

		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		//ExecutorService executor = Executors.newFixedThreadPool(100);

		boolean failure = false;

		for (int i=1; i < rowcount; i++) {


				String BMI_fieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
				String BMI_displayNme = mySheet.getRow(i).getCell(1).getStringCellValue();
				String BMI_DataTyp = mySheet.getRow(i).getCell(2).getStringCellValue();
				String BMI_PerMissn = mySheet.getRow(i).getCell(3).getStringCellValue();

				String MetaDataUrl = metaUrl + "/" + BMI_fieldIds;
				String jsonAsString;
				
				//System.out.println(MetaDataUrl);

				Response response = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).contentType("application/vnd.api+json").when().get(MetaDataUrl).then()
						.statusCode(200).extract().response();

				jsonAsString = response.asString();

				Assert.assertFalse(response.asString().contains("isError"));

				int index1 = jsonAsString.indexOf(BMI_fieldIds);
				int index2 = jsonAsString.indexOf(BMI_displayNme);
				int index3 = jsonAsString.indexOf(BMI_DataTyp);
				int index4 = jsonAsString.indexOf(BMI_PerMissn);

				if ( index1 != -1 && index3 != -1 && index4 !=-1){
				
                  System.out.println(i);
				} else {
					//failure = true;
					System.err.println("The Response does not contain BMI field : " + BMI_fieldIds + " DataType "
							+ BMI_DataTyp + " Permission " + BMI_PerMissn);
				}

				if (index2 != -1) {
					

				} else {
					failure = true;
					System.err.println("The Response does not contain field description :" + BMI_displayNme);
				}



			
		}
		
		Assert.assertFalse(failure);

		file.close();
		
	}


	@Test()
	public void Complete_metaData_Compare() throws IOException, URISyntaxException {

		URL fileUrl = Resources.getResource("MetaData.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("FitchRatings");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		//ExecutorService executor = Executors.newFixedThreadPool(30);
		boolean failure = false;
		for (int i =1; i < rowcount; i++) {

				String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();

				String fieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
				String desC = mySheet.getRow(i).getCell(1).getStringCellValue();
				String dispLayName = mySheet.getRow(i).getCell(2).getStringCellValue();
				String dataType = mySheet.getRow(i).getCell(3).getStringCellValue();
				String PermissionType = mySheet.getRow(i).getCell(4).getStringCellValue();
				

		
				String DataTypeUrl = metaUrl + "/" + fitchFieldIds;
				
				
				//System.out.println(DataTypeUrl);

				String jsonAsString;
 
				Response response = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
						.header("content", contentValue).when().get(DataTypeUrl)
						.then().contentType(ContentType.JSON)
						.statusCode(200)
						.extract().response();

				jsonAsString = response.asString();

				Assert.assertFalse(response.asString().contains("isError"));
				Assert.assertFalse(response.asString().contains("isMissing"));

				int index1 = jsonAsString.indexOf(fieldIds);
				int index2 = jsonAsString.indexOf(desC);
				int index3 = jsonAsString.indexOf(dispLayName);
				int index4 = jsonAsString.indexOf(dataType);
				int index5 = jsonAsString.indexOf(dataType);
				
				if (index1 != -1) {
					
					System.out.println(i);

				} else {
					failure = true;
					System.err.println("The Response does not contain fitch field : " + fieldIds);
				}

				if (index4 != -1) {

				} else {
					failure = true;
					System.err.println("The Response does not contain DataType : " + fieldIds  + " dataType "+dataType);
				}
            
				if (index5 != -1) {

				} else {
					failure = true;
					System.err.println("The Response does not contain DataType : " + fieldIds  + " PermissionType "+PermissionType);
				}
				
				if (index2 != -1) {

				} else {
					failure = true;
					System.err.println("The Response does not contain field Description : " + desC);
				}
          
				/*if (index3 != -1) {
					
				  System.out.println(i);

				} else {
					failure = true;
					System.err.println("The Response does not contain display name  : " + dispLayName);
				} */

		}
		
		file.close();
		Assert.assertFalse(failure);

	
	}
	
	
	@Test()
	public void metaData_DataType_Date_Compare() throws IOException, URISyntaxException {

		URL fileUrl = Resources.getResource("DataType_Date.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");
		boolean failure = false;

		for (int i = 1; i < rowcount; i++) {

			String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
			String dataType = mySheet.getRow(i).getCell(1).getStringCellValue();
			// System.out.println(fitchFieldIds);

			String DataTypeUrl = metaUrl + "/" + fitchFieldIds;

			String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(DataTypeUrl).then().contentType(ContentType.JSON)
					.statusCode(200).extract().response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));

			int index1 = jsonAsString.indexOf(dataType);

			if (index1 != -1) {
				// System.out.println("The Response contains :"+fitchFieldIds);

			} else {

				failure = true;

				System.err.println("The Response does not contain DataType : " + dataType + fitchFieldIds);

			}

		}

		Assert.assertFalse(failure);
		file.close();
	}


}
