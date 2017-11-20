package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class fisCon_Sprint14 extends Configuration {

	@Test

	public void BMI_Batch_FISC1263() {

		String BmiCategoryURl = BMIbaseURL + "/v1/ndb/metadata/categories";
		Response res = given().header("content", contentValue).when().get(BmiCategoryURl).then().statusCode(200)
				.extract().response();

		List<String> BMICategoryId = res.path("data.id");
		List<String> BMICategoryIdChild = res.path("data.relationships.children.data.id");
		
		 		 
		 System.out.println("BMI categoryID parent :"+BMICategoryId);
		
		
		List<String> BMIcateGoryName = res.path("data.attributes.name");

		

		String categoryURI = baseURI + "/v1/metadata/categories";

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(categoryURI).then()
				.statusCode(200).extract().response();

		List<String> fitchCategoryName = res1.path("data.attributes.name");
		
		List<String> catergoryid = res1.path("data.id");
		
		List<String> category = res1.path("data.relationships.children.data.id");
		
		boolean failure = false;
		System.out.println("FitchCategor size : " + fitchCategoryName.size());
		
		System.out.println("BMIcateGoryName size : " + BMIcateGoryName.size());
		
		for (String catGryname :BMIcateGoryName ){
			if (fitchCategoryName.contains(catGryname)){
				
				System.out.println(catGryname );	
			} else {
				System.err.println("Does NOT contains :"+catGryname );
				failure = true;
			}
					 
		}	
		Assert.assertFalse(failure);
			

	}

	
	@Test()
	public void BMIcateGories_Compare() throws IOException, URISyntaxException {
	

		URL fileUrl = Resources.getResource("BMI_CateGory_Name.xlsx");

		File src = new File(fileUrl.toURI());

		FileInputStream file = new FileInputStream(src);

		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
		int rowcount = mySheet.getPhysicalNumberOfRows();
		System.out.println(rowcount + " categories are available");

		boolean failure = false;

		for (int i = 1; i < rowcount; i++) {

			String BmiCategoryId = mySheet.getRow(i).getCell(0).getStringCellValue();
			String BmiCategoryName = mySheet.getRow(i).getCell(1).getStringCellValue();
			
			//System.out.println("bmiId  "+BmiCategoryId);
			
			//System.out.println("bmi catgry Name  "+BmiCategoryName);
			
			String jsonAsString;
			
			String baseCategoryURI = baseURI+"/v1/metadata/categories";
			
			String CategoryURI = baseCategoryURI +"/"+BmiCategoryId;

			Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).when().get(CategoryURI).then()
					.contentType(ContentType.JSON).statusCode(200).extract().response();

			jsonAsString = response.asString();

			Assert.assertFalse(response.asString().contains("isError"));
			Assert.assertFalse(response.asString().contains("isMissing"));
			
			int index1 = jsonAsString.indexOf(BmiCategoryId);
			int index2 = jsonAsString.indexOf(BmiCategoryName);

			if (index1 != -1 && index2 != -1) {

				// System.out.println("The Response contains all the fields");

			} else {

				failure = true;
				System.err.println("The Response does not contain the substring " + BmiCategoryName);

			}

		}

		Assert.assertFalse(failure);

		file.close();

	
	}
		
	@Test
	
	public void FISC_1269_NewFIRModel (){
		
		String fIRmodelURl = baseURI+"/v1/firModels?filter[quarterlyModel]=false&filter[runDate]=2017-02-24";
		

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(fIRmodelURl).then()
				.statusCode(200)
				.body(containsString("id"))
				.body(containsString("runDate"))
				.body(containsString("loanQualityCutoffLow"))				
				.body(containsString("numEntitiesNotchDiffDM"))
				.body(containsString("notchDiffCumFreqDM"))
				.body(containsString("totalAssetsModelCoeff"))				
				.body(containsString("criModelCoeff"))
				.body(containsString("notchDiffAllRegions"))
				.body(containsString("notchDiffDM"))
				.body(containsString("profitabilityNormSD"))
				.body(containsString("profitabilityNormMean"))
				.body(containsString("loanQualityNormSD"))
				.body(containsString("loanQualityNormMean"))
				.body(containsString("numIntercepts"))
				.body(containsString("numEntitiesNotchDiffEM"))
				.body(containsString("notchDiffCumFreqEM"))
				.body(containsString("notchDiffCumFreqAllRegions"))
				.body(containsString("profitabilityCutoffLow"))
				.body(containsString("loanQualityCutoffHigh"))
				.body(containsString("totalAssetsNormMean"))
				.body(containsString("profitabilityModelCoeff"))
				.body(containsString("maximumModelScore"))
				.body(containsString("quarterlyModel"))
				.body(containsString("numEntitiesNotchDiffAllRegions"))
				.body(containsString("quarterlyModel"))
				.body(containsString("numEntitiesNotchDiffAllRegions"))
				.body(containsString("profitabilityCutoffHigh"))
				.body(containsString("notchDiffEM"))
				.body(containsString("totalAssetsNormSD"))				
				.body(containsString("loanQualityModelCoeff"))
				.body(containsString("intercepts"))
				.body(containsString("minimalModelScore"))
				.extract().response();
			
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}
	
    @Test
    
    public void  FISC_1376_StatementResource (){
    	
    	String fincialStatementURI = baseURI+"/v1/entities/113332/statements?filter[businessTemplate]=banks";
    	
    	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(fincialStatementURI).then()
				.statusCode(200)
				.body(containsString("businessTemplate"))	
				.body(containsString("Banks"))
				.extract().response();
    	
    	String businesTemplate = res.path("data[0].attributes.header.businessTemplate");
    	
    	System.out.println(businesTemplate);
    	
	
		
		
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
    	
	String NullBTStatementURI = baseURI+"/v1/entities/750748/statements";
    	
    	Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(NullBTStatementURI).then()
				.statusCode(200)
				//.body(containsString("businessTemplate"))				
				//.body(containsString(null))				
				.extract().response();
    	
    	
		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
    	
    }
    
   @Test(enabled=false)
   
   public void FISC_1337_newAttribtes() {
	   
	   String issueURI = baseURI+"/v1/issuers?filter[identifierType]=CUSIP&filter[identifierValue]=123860&filter[id]=1010000146"  ;
	   
	   Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(issueURI).then()
				.statusCode(200)
				.body(containsString("name"))
				.body(containsString("typeId"))
				.body(containsString("countryOfFitchLegalEntity"))
				.body(containsString("cusip"))
				.body(containsString("123860"))
				.body(containsString("1010000146"))
				.extract().response();
	   
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
   }
}			
				
				
								
				
	   
