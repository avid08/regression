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

public class fisCon_Sprint11_SNDP_Metadata extends Configuration {

	
	
	@Test

	public void Fisc_1173_S_ND_P_MetaData_update_Verification() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("SandP Field Ids _FISC_v1.26 .xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Fields");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");
		
		// ExecutorService executor = Executors.newFixedThreadPool(30);

		boolean failure = false;
		for (int i =1; i < rowcount; i++) {

			String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
			String fitchFieldDescp = mySheet.getRow(i).getCell(1).getStringCellValue();
			String displayName = mySheet.getRow(i).getCell(2).getStringCellValue();
			String fieldDef = mySheet.getRow(i).getCell(3).getStringCellValue();
			String dataType = mySheet.getRow(i).getCell(4).getStringCellValue();
			double categoryId = mySheet.getRow(i).getCell(5).getNumericCellValue();			
			int  categoryIdint = (int) categoryId ;	
			
			String categoryIdStr = Integer.toString(categoryIdint);
     		String categoryName = mySheet.getRow(i).getCell(13).getStringCellValue();
			String permission = mySheet.getRow(i).getCell(9).getStringCellValue();	
			
			String DataTypeUrl = metaUrl + "/" + fitchFieldIds;
			
			System.out.println(DataTypeUrl);

			String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(DataTypeUrl).then().statusCode(200)
					.body(containsString("sandpRatings")).body(containsString("dataService"))
					.body(containsString("standardAndPoorRatings")).extract().response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));

			int index1 = jsonAsString.indexOf(fitchFieldIds);
			int index2 = jsonAsString.indexOf(fitchFieldDescp);
			int index3 = jsonAsString.indexOf(displayName);
			int index4 = jsonAsString.indexOf(fieldDef);
			int index5 = jsonAsString.indexOf(dataType);
			int index6 = jsonAsString.indexOf(permission);
			int index7 = jsonAsString.indexOf(categoryName);

			if (index1 != -1 && index2 != -1) {

				//System.out.println(i);

			} else {
				failure = true;
				System.err.println("fitchfield Id or Description  has mismatch for    : " + fitchFieldIds);
			}

			if (index3 != -1 && index4 != -1 ) {

			} else {

				failure = true;
				System.err.println("The Response has mismatch for  displayname Or FieldDefination  : " + fitchFieldIds);

			}
			

			if (index6 != -1 && index5!= -1 ) {

			} else {

				failure = true;
				System.err.println("The Response has mismatch for DataType or Permission   : " + fitchFieldIds);

			}
			
			
              
			//Category Name validation
			String categories = response.path("data.relationships.categories.links.related");
			
			System.out.println(categories);
		

			Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).when().get(categories).then()
					.statusCode(200).body(containsString("categories")).extract().response();

			jsonAsString = res.asString();
			
			String categoryIdx = res.path("data.id");
			
			String respnseName = res.path("data.attributes.name");
			
			// Category Name 
			
			if (equalToIgnoringWhiteSpace(categoryName).matches(respnseName)){				

			} else {
				failure = true;
				System.err.println("The response has categoryName  mismatch  : " + fitchFieldIds);
			}


		// Categoy ID 	
			if ( categoryIdx.equals(categoryIdStr)) {
				

			} else {
				failure = true;
				System.err.println("The response has category  ID  mismatch   : " + fitchFieldIds);
				
						
			}
		
			
		}

		file.close();
		Assert.assertFalse(failure);

	}
	
	
	@Test

	public void Fisc_1173_SandP_Category_update_Verification() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("SandP_Field_Ids_v9.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Categories");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		// ExecutorService executor = Executors.newFixedThreadPool(30);

		boolean failure = false;
		for (int i = 2; i < rowcount; i++) {

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

				System.out.println(i);

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
	

}
