package com.metadata.api;



import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import  com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;


public class BigmetaDataComparison extends Configuration {

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
						.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue).contentType(ContentType.JSON)
						.header("content", contentValue).contentType("application/vnd.api+json").when().get(MetaDataUrl).then()
						.statusCode(200).extract().response();

				jsonAsString = response.asString();

				Assert.assertFalse(response.asString().contains("isError"));

				int index1 = jsonAsString.indexOf(BMI_fieldIds);
				int index2 = jsonAsString.indexOf(BMI_displayNme);
				int index3 = jsonAsString.indexOf(BMI_DataTyp);
				

				if ( index1 != -1 && index3 != -1 ){
				
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
					
					System.out.println("Number of datapoint being Tested "+i);

				} else {
					failure = true;
					System.err.println("The Response does not contain fitch field : " + fieldIds);
				}

				/*if (index4 != -1) {

				} else {
					failure = true;
					System.err.println("The Response does not contain DataType : " + fieldIds  + " dataType "+dataType);
				}
            */
			/*	if (index5 != -1) {

				} else {
					failure = true;
					System.err.println("The Response does not contain DataType : " + fieldIds  + " PermissionType "+PermissionType);
				}*/
				
				if (index2 != -1) {

				} else {
					failure = true;
					System.err.println("The Response does not contain field Description : " + desC +"   fitchfileds " +fieldIds);
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

		for (int i=0; i < rowcount; i++) {

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
	
	@Test()

	public void All_metaData_fields() throws IOException {
		//FileWriter file = new FileWriter("metadata.txt");
	   // String sb =  " ";

		String metaDataURI = baseURI + "/v1/metadata/fields";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaDataURI).then()
				.statusCode(200).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		List<String> MetafildId = res.path("data.id");

		System.out.println(MetafildId.size());		

		for (int i=0; i< MetafildId.size(); i++) {						
			
			String metaDatafieldId = (MetafildId.get(i));	
			
		   //System.out.println(metaDatafieldId);	
			
		    String PermetaDataURI  = baseURI+"/v1/metadata/fields/"+metaDatafieldId;		   
		    
		    int statuscode = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("Accept", acceptValue).header("Content", contentValue).when().get(PermetaDataURI)

					.statusCode();    
		    
		    
		    if (statuscode != 200) {

				System.err.println("statement statusCode " + statuscode + " FitchFieldId  " + metaDatafieldId);
				 System.out.println(PermetaDataURI);		 		
				 				 
                
			} else {
				
				 String PermetaDataURICatgory  = baseURI+"/v1/metadata/fields/"+metaDatafieldId+"/"+"categories";	
				 
				  Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue).contentType(ContentType.JSON)
							.header("accept", acceptValue).header("content", contentValue).when().get(PermetaDataURICatgory).then()
							.extract().response();
				    
				     String  categorId = res1.path("data.id");
				     
				  //System.out.println(metaDatafieldId  + "                        "+    "              " +categorId )  ;
				  
				  System.out.printf("%-60.60s  %-60.60s%n", metaDatafieldId, categorId );				  
				  
				
				
			}	
		   

		}

		
	}
	
	@Test()

	public void All_metaData_fields_fORcATEGORY() throws IOException, URISyntaxException {
		//RestAssured.defaultParser = Parser.JSON;		
		
		URL fileUrl = Resources.getResource("MetaDataList.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		for (int i=1; i<rowcount; i++) {
			RestAssured.defaultParser = Parser.JSON;
			
			String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();		   
		    String PermetaDataURI  = baseURI+"/v1/metadata/fields/"+fitchFieldIds+"/"+"categories";	
		    
		    System.out.println(PermetaDataURI) ;
		   		    
		    Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue).contentType(ContentType.JSON)
					.header("accept", acceptValue).header("content", contentValue).when().get(PermetaDataURI).then()
					.extract().response();
		    
		     String  categorId = res1.path("data.id");
			
		  System.out.println(categorId) ;
		    

		}

		
	}

@Test()
	
	public void testprint() throws IOException {
	FileWriter file = new FileWriter("hello.txt");
    String sb =  " ";
    
    final String alphabet = "abcdefghigklmnopqrstuvwxyz";
    final int N = alphabet.length();
    Random r = new Random();

   for (int i = 0; i < 1; i++) {
        sb += alphabet.charAt(r.nextInt(N));
        System.out.println(sb);
        int length = sb.length();
        file.write(sb);
        file.close();
        if(length == 30){
            sb = " ";
        }
    }
		
	}

	

}
