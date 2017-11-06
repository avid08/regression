package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint12 extends Configuration {

	@Test
	public void FISC_506_SwiftCodes_LEI_DataAggregator() throws IOException {

		URL file = Resources.getResource("fisc_506.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()
				.when().post(dataPostUrl).then().statusCode(200).body(containsString("value"))
				.body(containsString("type")).body(containsString("year")).body(containsString("OKOYFIHH"))
				.body(containsString("OPSEFIH1")).body(containsString("549300NQ588N7RWKBP98")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test
	
	public void FISC_786_additionofAllMStoEntitySummary() throws IOException{
		
		URL file = Resources.getResource("fisc_786.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()
				.when().post(dataPostUrl).then().statusCode(200).body(containsString("value"))
				.body(containsString("FC_MRKT_SECTOR_ALL_DESC"))
				.body(containsString("City Special Tax"))
				.body(containsString("Tax-Supported"))
				.body(containsString("State Special Tax"))
				.body(containsString("Municipal Structured Finance"))
				.body(containsString("Water & Sewer"))
				.body(containsString("Airports"))
				.body(containsString("City General Obligation"))
				.body(containsString("ABCP - Multi-Seller"))
				.body(containsString("07100100"))
				.body(containsString("04040000"))
				.body(containsString("04010303"))
				.body(containsString("04010301"))
				.body(containsString("04010103"))
				.body(containsString("04010000"))
				.body(containsString("04020100"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
	}
	
 @Test
 
 public void FISC_1139(){
	 
	 String financialmetaData = baseURI +"/v1/metadata/fields/FC_GROSS_INT_DIV_INC_BNK";
	 
		Response response = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(financialmetaData)
				.then().assertThat().statusCode(200)
				.body(containsString("CURRENCY"))
				.body(containsString("financials"))
				.body(containsString("calculationTransparency"))
				.body(containsString("auditTrail"))
				.body(containsString("true"))
				.body(containsString("formula"))	
				.extract().response();
	 	  
		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
	 
 }
 
 @Test 
 
 public void fisc_1172() throws IOException{
	 
	 URL file = Resources.getResource("fisc_1172.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myJson).with()
				.when().post(dataPostUrl).then().statusCode(200).body(containsString("value"))
				.body(containsString("type")).body(containsString("Italy"))
				.body(containsString("Banca Federico Del Vecchio"))
				.body(containsString("EUR"))
				.body(containsString("auditTrail"))
				.body(containsString("true"))
				.body(containsString("numerical"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	 	 
  }
 
 @Test 
 public void fisc_899_bug(){
	
	 String entityUrl = baseURI +"/v1/entities/1466963";	 

		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(entityUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("countryName"))
				.body(containsString("fitchConnectUrl"))				
				.body(containsString("https://app.fitchconnect"))
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	 
   }
 
  @Test 
  
  public void fisc_1056_ExchngeRate(){
	  
	  String exchangeRateURI = baseURI +"/v1/exchangeRates";
	  
		Response res = given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(exchangeRateURI)
				.then().assertThat().statusCode(200)
				.body(containsString("date"))
				.body(containsString("exchangeRates"))				
				.body(containsString("currency"))
				.body(containsString("rate"))
				.body(containsString("USD"))
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		String exchangeRatefilterUrI=baseURI+"/v1/exchangeRates?filter[startDate]=2014-01-31&filter[endDate]=2014-12-31&filter[baseCurrency]=JPY&filter[currency]=AED";
		
		
		Response res1= given().header("Authorization", AuthrztionValue).header("content", contentValue)
				.header("'Accept", acceptValue).header("X-App-Client-Id", XappClintIDvalue).when().get(exchangeRatefilterUrI)
				.then().assertThat().statusCode(200)
				.body(containsString("date"))
				.body(containsString("exchangeRates"))				
				.body(containsString("JPY-2014-12-31"))
				.body(containsString("AED"))
				.body(containsString("JPY"))
				.extract().response();	
				
		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
		
  }


}