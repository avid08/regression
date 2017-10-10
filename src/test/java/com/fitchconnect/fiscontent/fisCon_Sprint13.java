package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.collect.Ordering;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint13 extends Configuration {
 
 float rate ;
	
 @Test
 
 public void fisc_1214_nickNme_Amendments(){	 
	
		String nickNmeUri = baseURI +"/v1/nicknames";

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue)
				.when().get(nickNmeUri).then()
				.statusCode(200)
				.body(containsString("nicknames"))
				.body(containsString("lastStatementDate"))
				.body(containsString("primary"))
				.body(containsString("specialization"))
				.body(containsString("consolidation"))
				.body(containsString("accountingStandard"))
				.body(containsString("true"))	
				.body(containsString("false"))	
				.extract().response();		

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));		
		
	String nickNmefilterURI=baseURI+"/v1/nicknames?filter[lineOfBusiness]=Broker&filter[primary]"
			+ "=false&filter[accountingStandard]=Local&filter[consolidation]=noncon";
	
	Response res1 = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue)
			.when().get(nickNmefilterURI).then()
			.statusCode(200)
			.body(containsString("lineOfBusiness"))
			.body(containsString("inflationAdjustedFlag"))
			.body(containsString("primary"))
			.body(containsString("specialization"))
			.body(containsString("consolidation"))
			.body(containsString("accountingStandard"))	
			.body(containsString("false"))	
			.body(containsString("Local"))	
			.extract().response();	

	Assert.assertFalse(res1.asString().contains("isError"));
	Assert.assertFalse(res1.asString().contains("isMissing"));
	Assert.assertFalse(res1.asString().contains("isRestricted"));
	 
    }
 
 
 @Test
  
 public void fisc_1260_finncialMetaDataUpdate(){
	 
		String metaDataUri = baseURI+"/v1/metadata/fields/FC_GDP_LOCAL_SOV";

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue)
				.when().get(metaDataUri).then()
				.statusCode(200)
				.body(containsString("databaseId"))
				.extract().response();		
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
	String DatbaseIdfilterURI =baseURI+"/v1/metadata/fields?filter[databaseId]=1010";
	
	Response res1 = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
			.header("content", contentValue)
			.when().get(DatbaseIdfilterURI).then()
			.statusCode(200)
			.body(containsString("databaseId"))
			.body(containsString("financials"))
			.body(containsString("included"))
			.extract().response();		
			
  }
 
 @Test 
 
 public void fisc_1267_ExnGRateSorting (){
		String ExNgRateURI = baseURI+"/v1/exchangeRates?sort=-date";  // Desc order 

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue)
				.when().get(ExNgRateURI).then()
				.statusCode(200)
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
		
		List<String> id = res.path("data.id");	
		
		boolean sorted = Ordering.natural().reverse().isOrdered(id);
		
	     Assert.assertTrue(sorted);
	     
	  
	     String AscExNgRateURI = baseURI+"/v1/exchangeRates?sort=date";  // ASC order 

			Response res1 = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue)
					.when().get(AscExNgRateURI).then()
					.statusCode(200)
					.extract().response();
			
			List<String> idx = res1.path("data.id");	
			
			boolean sortedx = Ordering.natural().isOrdered(idx);
			
		    Assert.assertTrue(sortedx);	     
	 
   }
 
 @Test
	
	public void FISC_1236_newCurrencies() throws IOException{	 
	 
	   URL file = Resources.getResource("FISC_1236.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json")
				.body(myJson).with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("Malaysia"))
				.body(containsString("currency"))
				.body(containsString("MYR"))
				.body(containsString("Malaysia"))
				.body(containsString("0.8556616962"))
				.body(containsString("2015-12-31"))
				.body(containsString("AED"))
				.body(containsString("21562674.7442"))
				.extract().response();
		
		Assert.assertFalse(responsedata.asString().contains("isError"));
		Assert.assertFalse(responsedata.asString().contains("isMissing"));
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));
			
	}
 
@Test
 
 public void FISC_1240_exchangeRate_CurrencYDate () throws IOException{
	
   	 exchngeRateValue();
	
     System.out.println(rate);
   
        URL file = Resources.getResource("FISC_1240_1.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response responsedata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json")
				.body(myJson).with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("rate"))
				.body(containsString("date"))
				.body(containsString("baseCurrency"))
				.body(containsString("MYR"))
				.body(containsString("Annual"))
				.body(containsString("2011"))
				.extract().response();
		
		Assert.assertFalse(responsedata.asString().contains("isError"));
		Assert.assertFalse(responsedata.asString().contains("isMissing"));
		Assert.assertFalse(responsedata.asString().contains("isRestricted"));
	
	 
	String exchangeRate =responsedata.path("data.attributes.entities[0].values[5].values[0].exchangeRate.rate");	
	
	float Exchngerate = Float.parseFloat(exchangeRate) ;	
	
	System.out.println("exchangeRate :"+Exchngerate);
		
	if (rate==Exchngerate){
	    System.out.println("exchangeRates are matching");
	   	    
	}else{	 
		 System.err.println("exchangeRates are NOT matching");
		failure = true;
      }
	
	Assert.assertFalse(failure);
}
 
@Test
public void FISC_1240_exchangeRate_CustomRate () throws IOException{
	
	 exchngeRateValue();
	
	Float expectedvalue = (float) (rate*144.7*1000000*1.2);
	
	System.out.println("expected total Netincome "+expectedvalue);
	
	 URL file = Resources.getResource("FISC_1240_2.json");
	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response res = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json")
			.body(myJson).with()
			.when().post(dataPostUrl)
			.then().assertThat().statusCode(200)
			.body(containsString("rate"))
			.body(containsString("date"))
			.body(containsString("baseCurrency"))
			.body(containsString("INR"))
			.body(containsString("Annual"))
			.body(containsString("2013"))
			.body(containsString("periodResolution"))
			.body(containsString("1.2"))
			.body(containsString("2014-12-31"))
			.extract().response();
		
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
	
	
	float totalActualNetIncome= res.path("data.attributes.entities[0].values[5].values[0].value[0].INR");
	
	
System.out.println("TotalNetCome :"+totalActualNetIncome);
	


if (expectedvalue==totalActualNetIncome){
    System.out.println("exchangeRates are matching");
   	    
}else{	 
	 System.err.println("exchangeRates are NOT matching");
	failure = true;
  }

Assert.assertFalse(failure);

}


 public void exchngeRateValue(){
	 
	 String ExNgRateURI = baseURI+"/v1/exchangeRates?filter[baseCurrency]=MYR&filter[currency]=INR&filter[date]=2014-12-31";	 
	 
	 Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue)
				.when().get(ExNgRateURI).then()
				.statusCode(200)
				.body(containsString("MYR"))
				.body(containsString("currency"))
				.body(containsString("INR"))
				.body(containsString("2014-12-31"))				
				.extract().response();
	 
	this.rate = res.path("data[0].attributes.exchangeRates[0].rate"); 
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));
	 
   }
 
 
 @Test 
 
  public void fisc_1240_GroupIDtest () throws IOException {
	 
	 URL file = Resources.getResource("FISC_1240_GroupID.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json")
				.body(myJson).with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("AED"))
				.body(containsString("baseCurrency"))
				.body(containsString("date"))
				.body(containsString("rate"))
				.body(containsString("value"))
				.extract().response();
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	 
      }
 
  @Test
  
  public void FISC_1240_NocurrencY() throws IOException{
	  
	  URL file = Resources.getResource("FISC_1240_noCurrency.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json")
				.body(myJson).with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("MYR"))
				.body(containsString("Malaysia"))
				.body(containsString("baseCurrency"))
				.body(containsString("date"))
				.body(containsString("rate"))
				.body(containsString("value"))
				.body(containsString("periodResolution"))
				.body(containsString("dateOptions"))				
				.extract().response();	
		
		String rateData = res.path("data.attributes.entities[0].values[5].values[0].exchangeRate.rate");	
		
		Assert.assertTrue(rateData.toString().contains("1"));	
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	 	  
    }
   
 @Test
  
  public void FISC_1373_LocalcurrencY() throws IOException{
	  
	  URL file = Resources.getResource("FISC_1373_localCurrency.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response res = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json")
				.body(myJson).with()
				.when().post(dataPostUrl)
				.then().assertThat().statusCode(200)
				.body(containsString("JPY"))
				.body(containsString("Japan"))
				.body(containsString("baseCurrency"))
				.body(containsString("date"))
				.body(containsString("rate"))
				.body(containsString("value"))
				.body(containsString("periodResolution"))
				.body(containsString("dateOptions"))	
				.body(containsString("Annual"))
				.extract().response();	
		
		
		
	
		
		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	 	  
    }
   
  

}	
	 
	
	
	 
	 
			
	 
	 
	 

