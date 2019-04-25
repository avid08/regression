package com.metadata.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
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

public class fisCon_Sprint11_Financial_Metadata extends Configuration {
	
	@Test

	public void Fisc_1177_Financial_Category_update_Verification() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("Financials_Data.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Categories");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		// ExecutorService executor = Executors.newFixedThreadPool(30);

		boolean failure = false;
		for (int i =2; i < rowcount; i++) {

			double categoryChildId = mySheet.getRow(i).getCell(0).getNumericCellValue();
			int categoryChildIdint = (int) categoryChildId;
			String categoryChildIdStr = Integer.toString(categoryChildIdint);

			double parentId = mySheet.getRow(i).getCell(1).getNumericCellValue();
			int parentIdint = (int) parentId;
			String parentIdStr = Integer.toString(parentIdint);

			String categoryNme = mySheet.getRow(i).getCell(2).getStringCellValue();

			String CategoriesUrl = baseURI + "/v1/metadata/categories/" + categoryChildIdStr;

			System.out.println(CategoriesUrl);

			String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(CategoriesUrl).then().statusCode(200)
					.body(containsString("relationships")).body(containsString("parents"))
					.body(containsString("categories")).extract().response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));

			String ApIparentId = response.path("data.relationships.parents.data[0].id");			

			int index1 = jsonAsString.indexOf(categoryNme);

			if (index1 != -1) {

				//System.out.println(i);

			} else {				
				
				failure = true;
				System.err.println("The response  has category Name  mismatch for    : " + categoryChildIdStr);			
				
			}

			if (ApIparentId.equals(parentIdStr)) {

			} else { 
					
				failure = true;
		     	System.err.println("The Response has category Id mismatch for   : " + categoryChildIdStr);

			}	
		
		}
		
		file.close();
		Assert.assertFalse(failure);

	}	
	
	@Test()

	public void Financial_MetaData_update_Verification() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("Financials_Data_Mnemonics_V1.39.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet2");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");
		
		// ExecutorService executor = Executors.newFixedThreadPool(30);

		boolean failure = false;
		
		for (int i=2; i < rowcount; i++) {

			String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
			String fitchFieldDescp = mySheet.getRow(i).getCell(1).getStringCellValue();
			String displayName = mySheet.getRow(i).getCell(2).getStringCellValue();
			String dataType = mySheet.getRow(i).getCell(4).getStringCellValue();
			/*double categoryId = mySheet.getRow(i).getCell(4).getNumericCellValue();			
			int  categoryIdint = (int) categoryId ;				
			String categoryIdStr = Integer.toString(categoryIdint);
     		String categoryName = mySheet.getRow(i).getCell(5).getStringCellValue();		
			//String audTrial = mySheet.getRow(i).getCell(6).getStringCellValue();	
			//String formula = mySheet.getRow(i).getCell(7).getStringCellValue();	
*/
			String permission = mySheet.getRow(i).getCell(12).getStringCellValue();


			
			//System.out.println(dataType);
			
			
			String DataTypeUrl = metaUrl + "/" + fitchFieldIds;
			
		System.out.println(DataTypeUrl);

			String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(DataTypeUrl).then()
					.statusCode(200)				
					.extract().response();

			jsonAsString = response.asString();
			
		String resPnsdsplyName = response.path("data.attributes.displayName");
		String resPnsDesc = response.path("data.attributes.fitchFieldDesc");
			

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));

			//int index1 = jsonAsString.indexOf(fitchFieldIds);		
			int index4 = jsonAsString.indexOf(dataType);
			int index5 = jsonAsString.indexOf(permission);
	
			
			if (equalToIgnoringWhiteSpace(displayName).matches(resPnsdsplyName)){				

			} else {
				failure = true;
				System.err.println("The response has displayName  mismatch   : " + fitchFieldIds);
			}

			
			if (equalToIgnoringWhiteSpace(fitchFieldDescp).matches(resPnsDesc)){				

			} else {
				failure = true;
				System.err.println("The response has Description  mismatch   : " + fitchFieldIds);
			}


			if ( index4 != -1 ) {
				System.out.println(i);

			} else {

				failure = true;
				System.err.println("Datatype has misamtch  for   : " + fitchFieldIds);

			}
			
			if (  index5 != -1) {

			} else {

				failure = true;
				System.err.println(" permission has misamtch  for   : " + fitchFieldIds);

			}        
	
					
		}

		file.close();
		Assert.assertFalse(failure);

	}
	
	
	
	@Test

	public void financialCategory_udpate_4795() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("financialCategoryUpdate.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		// ExecutorService executor = Executors.newFixedThreadPool(30);

		boolean failure = false;
		for (int i=580; i < rowcount; i++) {

			double categoryChildId = mySheet.getRow(i).getCell(0).getNumericCellValue();
			int categoryChildIdint = (int) categoryChildId;
			String categoryChildIdStr = Integer.toString(categoryChildIdint);
			
			String categoryNme = mySheet.getRow(i).getCell(1).getStringCellValue();
			
		  double parentId = mySheet.getRow(i).getCell(2).getNumericCellValue();
			int parentIdint = (int) parentId;
			String parentIdStr = Integer.toString(parentIdint);
			//String parentIdStr = mySheet.getRow(i).getCell(2).getStringCellValue();
			
			//System.out.println(parentIdStr);

			String CategoriesUrl = baseURI + "/v1/metadata/categories/" + categoryChildIdStr;

			System.out.println(CategoriesUrl);

			String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(CategoriesUrl).then().statusCode(200)
					.body(containsString("relationships")).body(containsString("parents"))
					.body(containsString("categories")).extract().response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));

			String ApIparentId = response.path("data.relationships.parents.data[0].id");			

			int index1 = jsonAsString.indexOf(categoryNme);

			if (index1!= -1) {

				//System.out.println(i);

			} else {				
				
				failure = true;
				System.err.println("The response  has category Name  mismatch for    : " + categoryChildIdStr);			
				
			}

			if (ApIparentId.equals(parentIdStr)) {

			} else { 
					
				failure = true;
		     	System.err.println("The Response has category Id mismatch for   : " + categoryChildIdStr);

			}	
		
		}
		
		file.close();
		Assert.assertFalse(failure);

	}	
	
	
	@Test

	public void financialCategory_udpate_fisc_3833() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("Financials_Data_Mnemonics_V1.39.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet2");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		// ExecutorService executor = Executors.newFixedThreadPool(30);

		boolean failure = false;
		for (int i=4156; i < rowcount; i++) {

			String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
			
			double categoryChildId = mySheet.getRow(i).getCell(7).getNumericCellValue();
			int categoryChildIdint = (int) categoryChildId;
			String categoryChildIdStr = Integer.toString(categoryChildIdint);

			String categoryNme = mySheet.getRow(i).getCell(2).getStringCellValue();

			String CategoriesUrl = baseURI + "/v1/metadata/fields/" +fitchFieldIds+"/categories";

			System.out.println(CategoriesUrl);

			String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(CategoriesUrl).then().statusCode(200)
					.body(containsString("relationships")).body(containsString("parents"))
					.body(containsString("categories")).extract().response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));
			Assert.assertFalse(response.asString().contains("isRestricted"));

			String ApIparentId = response.path("data.relationships.parents.data[0].id");			

			int index1 = jsonAsString.indexOf(categoryChildIdStr);

			if (index1 != -1) {

				//System.out.println(i);

			} else {				
				
				failure = true;
				System.err.println("The response  has category id  mismatch for    : " + categoryChildIdStr);			
				
			}

			
		}
		
		file.close();
		Assert.assertFalse(failure);

	}	
	
	
}


