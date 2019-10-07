package com.CreditOpinions.api;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.jayway.restassured.response.Response;

public class IssueCredtOpinoinPaginationValidaton extends Configuration {
	
	@Test()
	   
	   public void IssuerCreditOpinions() {
		
		int totalCount = 0;
	
		   String IssuerCreditOpnionUri = baseURI+"/v1/issueCreditOpinions" ;
		   
		   System.out.println(IssuerCreditOpnionUri);
		   
		   Response res = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
					.header("content", contentValue)
					.when().get(IssuerCreditOpnionUri).then()
					.statusCode(200)					
					.extract().response();
		   
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isError"));
		   
		   int numbrOfIssuCreditOpinion = res.path("meta.totalResourceCount");
		   assertNotNull(numbrOfIssuCreditOpinion);
		   
		  System.out.println("TotalNumber of Moodys "+numbrOfIssuCreditOpinion);
		   
		  List <String>IssrRCreditOpin1stPge  = res.path("data.id");
		  int totlssRCreditOpin1stPge= IssrRCreditOpin1stPge.size();
		  
		  System.out.println("totalnumber Moodys in the first page "+totlssRCreditOpin1stPge);
		  
		  String nextPage = res.path("links.next");		 	  
	  
		
		  while (nextPage != null){	  		
			  
			  Response res1 = given().header("Authorization", AuthrztionValue)
						.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
						.header("content", contentValue)
						.when().get(nextPage).then()
						.statusCode(200)					
						.extract().response();
			  
			   nextPage = res1.path("links.next"); 	
			   
			   System.out.println(nextPage);
			  
			  List <String> resCount = res1.path("data.id");
			  int totalreCount= resCount.size();
			  
			  totalCount +=totalreCount;  	   
			  
			  
		  }			 
		 int totalcountFromresponse = totalCount+totlssRCreditOpin1stPge;
		 
		   System.out.println(totalcountFromresponse);
		 
		 
		if(totalcountFromresponse==numbrOfIssuCreditOpinion) {
			System.out.println("Test Passed");
			System.out.println("totalcountFromResponse :"+totalcountFromresponse);
			
		}else {
			failure = true;
			System.err.println("Test Failed");
			
			System.out.println("The total count from response "+totalcountFromresponse);
			
		}
		Assert.assertFalse(failure);

			
	   }

}
