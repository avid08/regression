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

import groovy.json.internal.Charsets;

public class fisCon_Sprint20 extends Configuration{
	
	@Test	
	public void FISC_1835() throws IOException{
		URL file = Resources.getResource("fisc_1835.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);
	
		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.contentType("application/vnd.api+json").body(myJson).with().when().post(dataPostUrl).then()
				.assertThat().statusCode(200)			
				.body(containsString("Solicited by or on behalf of the issuer (sell side)"))
				.body(containsString("UNITED KINGDOM"))
				.body(containsString("Universal Commercial Banks"))
				.extract().response();	
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}	
	
	
	@Test

	public void Fisc_1835_Rating_MetaData_update_Verification() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("rating_data_1.31.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Fitch Ratings");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");
		
		// ExecutorService executor = Executors.newFixedThreadPool(30);

		boolean failure = false;
		
		for (int i=6; i < rowcount; i+=10) {

			String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
			String fitchFieldDescp = mySheet.getRow(i).getCell(1).getStringCellValue();
			String displayName = mySheet.getRow(i).getCell(2).getStringCellValue();
			String dataType = mySheet.getRow(i).getCell(3).getStringCellValue();			
     		String categoryName = mySheet.getRow(i).getCell(4).getStringCellValue();
			String permission = mySheet.getRow(i).getCell(5).getStringCellValue();			
			
			String DataTypeUrl = metaUrl + "/" + fitchFieldIds;
			
		//System.out.println(DataTypeUrl);

			String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(DataTypeUrl).then()
					.statusCode(200)
			      	.body(containsString("ratings"))
				   // .body(containsString("fitchIssuerRatings"))
					.extract().response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));

			//int index1 = jsonAsString.indexOf(fitchFieldIds);
			int index2 = jsonAsString.indexOf(fitchFieldDescp);
            int index3 = jsonAsString.indexOf(displayName);
		    int index4 = jsonAsString.indexOf(dataType);
			int index5 = jsonAsString.indexOf(permission);			

			if ( index2 != -1  ) {

	             // System.out.println(i);

			} else {
				failure = true;
				System.err.println("Description  has mismatch for    : " + fitchFieldIds);
			}
			
		/*if (  index3 != -1 ) {

				System.out.println(i);

			} else {
				failure = true;
				System.err.println("Display  has mismatch for    : " + fitchFieldIds);
			}*/
			
			if ( index4 != -1 ) {

			} else {
				failure = true;
				System.err.println("Response has Datatype mismatch  for   : " + fitchFieldIds);
			}
			
			if (  index5 != -1) {

			} else {

				failure = true;
				System.err.println("Response has permission  mismatch  for   : " + fitchFieldIds);
			}

			//Category Name validation
			String categories = response.path("data.relationships.categories.links.related");
			
			System.out.println(categories);
		       

			Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).when().get(categories).then()
					.statusCode(200).body(containsString("categories")).extract().response();

			jsonAsString = res.asString();
			
			String categoryIdx = res.path("data.id");

			int index6 = jsonAsString.indexOf(categoryName);			

			if (index6 != -1) {

				//System.out.println(i);

			} else {
				failure = true;
				System.err.println("The response has categoryName  mismatch   : " + fitchFieldIds);
			}
		
		}

		file.close();
		Assert.assertFalse(failure);
	}
	
	
	@Test 
	
	public void FISC_189(){
		
     String statementURI = baseURI+"/v1/entities/1258790/statements?filter[periodType]=Annual&filter[consolidation]=con"
     		+ "&filter[accountingStandard]=IFRS&fields[statements]=header";
		

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(statementURI).then()
				.statusCode(200)
				.body(containsString("stmntId"))
				.body(containsString("stmntDate"))
				.body(containsString("businessTemplate"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}
@Test 

public void issuer_Attributes_Fisc_1838(){
	
	String issuerURI = baseURI + "/v1/issuers/6061";

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(issuerURI).then()
			.statusCode(200)
			.body(containsString("analysts"))
			.body(containsString("id"))
			.body(containsString("description"))
			.body(containsString("primary"))
			.body(containsString("email"))
			.body(containsString("description"))
			.body(containsString("name"))
			.body(containsString("groupCode"))				
			.extract().response();
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
	
}

}
