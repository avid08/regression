package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint33 extends Configuration {
	
	
@Test

 public void fca_1512_BMI_Categories (){
	
  String cateGorsUri =baseURI+"/v1/metadata/categories";
  
  Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(cateGorsUri).then()
			.body(containsString("BMI"))
			.body(containsString("Telecommunications"))
			.body(containsString("Information Technology"))
			.body(containsString("Petrochemicals"))
			.body(containsString("Insurance Data"))
			.body(containsString("Fitch Ratings"))
			.extract().response();
  
    Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
  
  String BMI_Id = res.path("data[8].links.self");
  System.out.println(BMI_Id);
  
  Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(BMI_Id).then()
			.body(containsString("name"))
			.body(containsString("categories"))
			.body(containsString("relationships"))
			.extract().response();
  
  Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
	
  
  }
  // Following test case has to be configured(right environment) before running it . So , it is now taken off the list
  @Test (enabled=false)
  
  public void fca_1512_dataAggregator() throws IOException{
	  
	  String KubURI ="http://kubernetes.fitchconnect-int.com:8080/api/v1/proxy/namespaces/default/services/metadata-service:8080/v1/metadata/internal/categories";
	  
	  URL file = Resources.getResource("fca_1512.json");
		myjson = Resources.toString(file, Charsets.UTF_8);

		Response res = given()

				.header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).contentType("application/vnd.api+json").body(myjson).with().when()
				.post(KubURI).then().statusCode(201)
				
				.extract().response();
		
		  Assert.assertFalse(res.asString().contains("isError"));
			Assert.assertFalse(res.asString().contains("isMissing"));
		
	  
  }
  
  @Test
  public void fca_1498() throws IOException{
	  
	  String fieldsURI =baseURI+"/v1/metadata/fields/BMI_LABOUR_WAGES_IND&CONS_LCU";
	  
	  Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(fieldsURI).then()
				.statusCode(200)
				
				.body(containsString("Industry and construction sector average annual wages, LOCCUR"))
			
				.extract().response();
		
	    Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
	
	  
  }
	
  
  @Test 
  
  public void fca_1638() {
	  
	  String umParentUri =baseURI+"/v1/entities/1186810/ultimateParent";
	  
	  Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(umParentUri).then()
				.statusCode(200)
				.body("data.relationships.ultimateParent.data.type",equalTo("entities"))
				.extract().response();
	  
	    Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted")); 
  }

  @Test()
	public void displayname_Compare_1639() throws IOException, URISyntaxException {
		String jsonAsString;
		
		String metaFieldsUrl = baseURI+"/v1/metadata/fields";

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaFieldsUrl).then()
				.contentType(ContentType.JSON).extract().response();

		jsonAsString = response.asString();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));

		URL fileUrl = Resources.getResource("displayName.xlsx");

		File src = new File(fileUrl.toURI());

		FileInputStream file = new FileInputStream(src);

		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet mySheet = wb.getSheet("Sheet1");
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
				System.err.println("The Response does not contain the displayName " + fitchData);

			}

		}

		Assert.assertFalse(failure);

		file.close();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		//Assert.assertFalse(response.asString().contains("isRestricted"));

	}
  
  @Test
  public void FCA_1666_default_case() throws IOException {

      

      String userdc = baseURI + "/v1/users";

      Response metaresponse = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
              .header("accept", acceptValue).header("content", contentValue).when().get(userdc).then().assertThat()
              .statusCode(200).contentType(ContentType.JSON).body(containsString("users"))
              .extract().response();
      Assert.assertFalse(metaresponse.asString().contains("webUserId"));
      Assert.assertFalse(metaresponse.asString().contains("isError"));
      Assert.assertFalse(metaresponse.asString().contains("isMissing"));
      Assert.assertFalse(metaresponse.asString().contains("isRestricted"));

  }

  @Test
  public void FCA_1666_true_case() throws IOException {

      

      String usertc = baseURI + "/v1/users?includeWebUserId=true";

      Response metaresponse = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
              .header("accept", acceptValue).header("content", contentValue).when().get(usertc).then().assertThat()
              .statusCode(200).contentType(ContentType.JSON).body(containsString("users"))
              .extract().response();
      Assert.assertTrue(metaresponse.asString().contains("webUserId"));
      Assert.assertFalse(metaresponse.asString().contains("isError"));
      Assert.assertFalse(metaresponse.asString().contains("isMissing"));
      Assert.assertFalse(metaresponse.asString().contains("isRestricted"));

  }

  @Test
  public void FCA_1666_false_case() throws IOException {

      

      String userfc = baseURI + "/v1/users?includeWebUserId=false";

      Response metaresponse = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
              .header("accept", acceptValue).header("content", contentValue).when().get(userfc).then().assertThat()
              .statusCode(200).contentType(ContentType.JSON).body(containsString("users"))
              .extract().response();
      Assert.assertFalse(metaresponse.asString().contains("webUserId"));
      Assert.assertFalse(metaresponse.asString().contains("isError"));
      Assert.assertFalse(metaresponse.asString().contains("isMissing"));
      Assert.assertFalse(metaresponse.asString().contains("isRestricted"));

  }
  
  
}
