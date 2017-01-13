package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;


public class Sprint31 {
	
	public Response response;
	String myjson;
	String AuthrztionValue;
	String baseURI;
	String env;
	String databaseFitchEnty;
	String dataBaseServer1;
	String dataBaseServer2;
	String id;
	String id1;
	String jsonresponse;
	String metaEndPoint = "/v1/metadata/fields"; // Metadata-EndPoint
	String metaUrl = baseURI + metaEndPoint;
	String dataEndPoint = "/v1/data/valueRequest";
	String dataPostUrl = baseURI + dataEndPoint; // Data Aggregator -EndPoint
	String XappClintIDvalue = "3dab0f06-eb00-4bee-8966-268a0ee27ba0";
	String acceptValue = "application/vnd.api+json";
	String contentValue = "application/vnd.api+json";
	@BeforeClass
	public void executionSetup() {
		env = System.getProperty("env");
		System.out.println("Test Execution Environment: " + env);
		if (env == null) {
			baseURI = "https://api.fitchconnect.com";
			this.AuthrztionValue = ("Basic M1FEREJQODMyQ1NKTlMwM1ZQT0NSQ0VFQjpENk9PUWtJVW5uaXhVZlZmL3loVnJhbHNDU1dzaGd0L1NJOGFTSFZEVTJR");
			dataBaseServer1 ="mgo-pue1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("dev")) {
			baseURI = "https://api.fitchconnect-dev.com";
			this.AuthrztionValue = ("Basic MUc4TTJCUzVIUTdGTVE5RVlNWTdWWVlUWTpoeU51d2lIYUVtOEpaSnF1RzVsRmM0TnRrTXpMMjdqcVFFczVwSDlUdEZJ");
			// dataBaseServer = "mongoweb-x01";
			dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
		} else if (env.equals("int")) {
			baseURI = "https://api.fitchconnect-int.com";
			this.AuthrztionValue = ("Basic WkRCSkg4WkpPWEg0S0dQNkZaRE9MVUpDWDp3VTlYWHpjakxsMWZYbldwM1lZaXBhU0VUcXZMTmtIY3hCK09ydXdRSHJB");
			// dataBaseServer = "mongoweb-x01";
			dataBaseServer1 = "mgo-due1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-due1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-dev-9";
		} else if (env.equals("qa")) {
			baseURI = "https://api.fitchconnect-qa.com";
			this.AuthrztionValue = ("Basic MU1HUjNXOFJCV0ZJNFlJMzNEV000MDk2WTpGYXp5Y3E4MHd1M0hpSlFzNVVhZDlJa3E1dEIyZ1YzcnA1OVB4UmowV2pJ");
			dataBaseServer1 = "mgo-que1a-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-que1a-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("stage")) {
			baseURI = "https://api.fitchconnect-stg.com";
			this.AuthrztionValue = ("Basic NTZORlhGTkJOOEdPWVk0Uk41UFdBTTdYVDp5c1FxNEtwazJza1UyVXU4TE1lbytLVVltTEhKMG1COFo5ZWczT0JZTStr");
			dataBaseServer1 = "mongorisk-int01";
			dataBaseServer2 = "mongorisk-int01";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("prod")) {
			baseURI = "https://api.fitchconnect.com";
			this.AuthrztionValue = ("Basic M1FEREJQODMyQ1NKTlMwM1ZQT0NSQ0VFQjpENk9PUWtJVW5uaXhVZlZmL3loVnJhbHNDU1dzaGd0L1NJOGFTSFZEVTJR");
			dataBaseServer1 ="mgo-pue1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		}

		System.out.println(baseURI);
		metaUrl = baseURI + metaEndPoint;
		dataPostUrl = baseURI + dataEndPoint;

	}
	
   @Test
   public void caTegories_1542 () {
	
	String categrisUrl = baseURI + "/v1/metadata/categories";

	Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(categrisUrl).then()
			.body(containsString("Entity Reference"))
			.body(containsString("Financials"))
			.body(containsString("Fitch Ratings"))
			.body(containsString("Moody's Issuer Rating"))
			.body(containsString("Sovereign Data"))
			//.body(containsString("Registered Office Address"))
			//.body(containsString("Head Office Address"))
			//.body(containsString("Identifiers"))
			//.body(containsString("Industry Classification"))
			.extract().response();
	
	
	
	Assert.assertFalse(res.asString().contains("isError"));
	Assert.assertFalse(res.asString().contains("isMissing"));
	Assert.assertFalse(res.asString().contains("isRestricted"));


	List <String>self = res.path("data.links.self");
	
	for(int i =0;i<self.size();i++){
	
	Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(self.get(i)).then()
			.body(containsString("relationships"))			
			.body(containsString("links"))
			.extract().response();
	
	Assert.assertFalse(res1.asString().contains("isError"));
	Assert.assertFalse(res1.asString().contains("isMissing"));
	Assert.assertFalse(res1.asString().contains("isRestricted"));

	}

	

  }

 @Test
 
 public void Fca_1489 (){
	   
	     String compnyIdUrl = baseURI+"/v1/entities/107477";

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(compnyIdUrl).then()
			    .body("data.relationships.company.data.id",equalTo("107477"))
			    .extract().response();
		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));
				
	 
   }
 
 @Test
  
 public void fca_1561() {
 
 String filterUrl = baseURI+"/v1/metadata/fields?filter[name]=country&filter[source]=ratings";

	Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
			.when().get(filterUrl).then()
			.body(containsString("ratings"))			
			.extract().response();
	
	Assert.assertFalse(res1.asString().contains("isError"));
	Assert.assertFalse(res1.asString().contains("isMissing"));
	Assert.assertFalse(res1.asString().contains("isRestricted"));
			
 }
 

}



