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

public class fisCon_Sprint16 extends Configuration {
	
	@Test
	public void Fisc_1177_AuditTrial_update_Verification() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("Financial_AuditTrial_Data.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("auditTrial");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");

		boolean failure = false;
		for (int i =2; i < rowcount; i++) {
			
			String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
			String audit = mySheet.getRow(i).getCell(1).getStringCellValue();
			
			String auditTrial = String.valueOf(audit);
			
			//System.out.println(auditTrial);			
			

			String CategoriesUrl = baseURI + "/v1/metadata/fields/" + fitchFieldIds;

     		String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(CategoriesUrl)
					.then()
					.statusCode(200)
					.body(containsString("relationships")).body(containsString("true"))
					.body(containsString("formula")).extract().response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));

			

			int index1 = jsonAsString.indexOf(auditTrial);

			if (index1 != -1) {

				//System.out.println(i);

			} else {
				failure = true;
				System.err.println("The response  has category Name  mismatch for    : " + fitchFieldIds);
			}
		
		}
		
		file.close();
		Assert.assertFalse(failure);

	}
	
	
	@Test

	public void Fisc_1177_financialFormula_update_Verification() throws URISyntaxException, IOException {

		URL fileUrl = Resources.getResource("financial_data_formula.xlsx");
		File src = new File(fileUrl.toURI());
		FileInputStream file = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("formula");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " fields are available");


		boolean failure = false;
		for (int i =2; i < rowcount; i++) {

			String fitchFieldIds = mySheet.getRow(i).getCell(0).getStringCellValue();
			String formula = mySheet.getRow(i).getCell(1).getStringCellValue();			

			String CategoriesUrl = baseURI + "/v1/metadata/fields/" + fitchFieldIds;

			//System.out.println(CategoriesUrl);
			
	/*		int statuscode = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("Accept", acceptValue).header("Content", contentValue).when().get(metaUrl)

					.statusCode();
			
			System.out.println(statuscode);*/

			String jsonAsString;

			Response response = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).when().get(CategoriesUrl).then()
					.statusCode(200)
					.body(containsString("relationships")).body(containsString("true"))
					.body(containsString("formula")).extract().response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));

			

			int index1 = jsonAsString.indexOf(formula);

			if (index1 != -1) {

			//	System.out.println(i);

			} else {
				failure = true;
				System.err.println("The response  has category Name  mismatch for    : " + fitchFieldIds);
			}
		
		}
		
		file.close();
		Assert.assertFalse(failure);

	}

@Test 
 public void fisc_1445_newAttributes_filter(){
	
	String issueURi = baseURI+"/v1/issues?filter[entityId]=1429651&filter[id]=93575704&filter[isin]=XS0982711391&filter[cusip]=Y00371AA5&filter[issuerId]=93528490&filter[loanXId]=LX146783";
	
	Response response = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(issueURi).then().statusCode(200)
			.body(containsString("isin"))
			.body(containsString("cusip"))
			.body(containsString("loanXId"))
			.body(containsString("1429651"))
			.body(containsString("93575704"))
			.body(containsString("XS0982711391"))
			.body(containsString("Y00371AA5"))
			.body(containsString("93528490"))
			.body(containsString("LX146783"))
			.extract().response();
	
	
	Assert.assertFalse(response.asString().contains("isMissing"));
	
	
   }
@Test
public void fisc_1503_regions() {
	
	String regionsURi = baseURI + "/v1/regions";
	
	Response response = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(regionsURi).then().statusCode(200)
			.body(containsString("id"))
			.body(containsString("name"))
			.body(containsString("regionType"))
			.body(containsString("subNationalType"))
			.body(containsString("identifiers"))
			.body(containsString("countryISOCode2"))
			.body(containsString("countryISOCode3"))
			.body(containsString("localCurrency"))
			.body(containsString("active"))
			.body(containsString("inactiveDate"))
			.body(containsString("true"))
			.body(containsString("false"))
			
			.extract().response();
	
   }
@Test

public void fisc_1506_regions_entities() {
	
	String regionsURi = baseURI + "/v1/regions";
	
	Response response = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(regionsURi).then().statusCode(200)
			.body(containsString("geographicRegions"))
			.body(containsString("entities"))
			.body(containsString("socialRegions"))
			.body(containsString("sovereignEntity"))
			.body(containsString("economicPoliticalRegions"))
			.body(containsString("regionType"))
			.extract().response();	
	Assert.assertFalse(response.asString().contains("isMissing"));
		
   String SovereignEntityURI =baseURI+"/v1/regions/GB/sovereignEntity";
   
   
	Response res = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(SovereignEntityURI).then().statusCode(200)
			.body(containsString("140064"))
			.body(containsString("included"))
			.body(containsString("relationships"))
			.body(containsString("analysts"))
			.body(containsString("GB"))			
			.extract().response(); 

    	
}
@Test
public void fisc_1513_regions_relations() {
	
	String regionsURi = baseURI + "/v1/regions";
	
	Response response = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(regionsURi).then().statusCode(200)
			.body(containsString("geographicRegions"))
			.body(containsString("entities"))
			.body(containsString("socialRegions"))
			.body(containsString("sovereignEntity"))
			.body(containsString("economicPoliticalRegions"))
			.body(containsString("regionType"))
			.extract().response();	
		
	String geographicRegions = response.path("data[0].relationships.geographicRegions.links.related");
	String ecoPolitical = response.path("data[0].relationships.economicPoliticalRegions.links.related");
	String socialRegions = response.path("data[0].relationships.socialRegions.links.related");
	
	Response res = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(geographicRegions).then().statusCode(200)
			.extract().response();
	
	Response res1 = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(ecoPolitical).then().statusCode(200)
			.extract().response();
	
	

	Response res2 = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(socialRegions).then().statusCode(200)
			.extract().response();
	
	// Filter 
	
	String filterdregionsURi = baseURI + "/v1/regions?filter[socialRegions.id]=EMGMKT&filter"
			+ "[economicPoliticalRegions.id]=APEC";

	
	Response res3 = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(filterdregionsURi).then().statusCode(200)
			.body(containsString("CL"))
			.body(containsString("TH"))		
			.extract().response();
	
    }

@Test
public void fisc_1570_regions() {
	
	String regionsURi = baseURI + "/v1/issuers/89069061/peers";
	
	Response response = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue).when().get(regionsURi).then().statusCode(200)
			.body(containsString("hasTransactions"))
			.body(containsString("typeDesc"))
			.body(containsString("identifiers"))
			.body(containsString("Corporate"))	
			.body(containsString("name"))	
			.body(containsString("countryOfFitchLegalEntity"))				
			.extract().response();
	Assert.assertFalse(response.asString().contains("isMissing"));
	
   }


  
}