package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;

public class Sprint26 {

	public Response response;
	String myjson;
	String AuthrztionValue;
	String baseURI;
	String env;
	String dataBaseServer1;
	String dataBaseServer2;
	String id;
	String databaseFitchEnty;
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
			baseURI = "https://new-api.fitchconnect.com";
			this.AuthrztionValue = ("Basic M1FEREJQODMyQ1NKTlMwM1ZQT0NSQ0VFQjpENk9PUWtJVW5uaXhVZlZmL3loVnJhbHNDU1dzaGd0L1NJOGFTSFZEVTJR");
			dataBaseServer1 = "mgo-pue1c-cr001.fitchratings.com";
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
			this.AuthrztionValue = ("Basic NU5COUFRSDVCSTRDUFZTUktJRUpESjQyNTpDYjFxUXQycHd4VGNKZTg1SjkyRVJmL1JMU1haRUlZSjU3NWR5R3RacDVV");
			dataBaseServer1 = "mongorisk-int01";
			dataBaseServer2 = "mongorisk-int01";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("prod")) {
			baseURI = "https://api.fitchconnect.com";
			this.AuthrztionValue = ("Basic M1FEREJQODMyQ1NKTlMwM1ZQT0NSQ0VFQjpENk9PUWtJVW5uaXhVZlZmL3loVnJhbHNDU1dzaGd0L1NJOGFTSFZEVTJR");
			dataBaseServer1 ="mgo-pue1c-cr001..fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		}

		System.out.println(baseURI);
		metaUrl = baseURI + metaEndPoint;
		dataPostUrl = baseURI + dataEndPoint;

	}



    @Test

    public void FCA_1158() throws IOException {

           String newattributes = baseURI + "/v1/entities/1064795";

           Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                        .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                         .when().get(newattributes).then().body("data.id", equalTo("1064795"))
                        .body("data.attributes.countryISOCode", equalTo("KR"))
                        .body("data.attributes.name", equalTo("Korea Hydro & Nuclear Power Co., Ltd.")).extract()
                        .response();
           Assert.assertFalse(res.asString().contains("isError"));
           Assert.assertFalse(res.asString().contains("isMissing"));
           Assert.assertFalse(res.asString().contains("isRestricted"));

    }
    
    @Test
    
    public void FCA_1207() throws IOException {

           String metafields = baseURI + "/v1/metadata/fields";

           Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                        .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                         .when().get(metafields).then().body("data[0].links.self", Matchers.anything("https://api-"))
                        .extract()
                        .response();
           Assert.assertFalse(res.asString().contains("isError"));
           Assert.assertFalse(res.asString().contains("isMissing"));
           Assert.assertFalse(res.asString().contains("isRestricted"));

    }
@Test        
    public void FCA_1157() throws IOException {

           String entitiesurl = baseURI + "/v1/entities";

           Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                        .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                         .when().get(entitiesurl).then().body("data[0].type", equalTo("entities"))
                        .body("data[0].id", equalTo("117522"))
                        .extract()
                        .response();
           Assert.assertFalse(res.asString().contains("isError"));
           Assert.assertFalse(res.asString().contains("isMissing"));
           Assert.assertFalse(res.asString().contains("isRestricted"));

    }

@Test

public void FCA_1157paginationtest() throws IOException {

String paginationentityurl = baseURI + "/v1/entities?page[size]=100";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(paginationentityurl).then().body("errors[0].status", equalTo("400"))
           .body("errors[0].title", equalTo("Max page size of 50 exceeded!"))
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

}


@Test

public void FCA_1187case1() throws IOException {

String case1url = baseURI + "/v1/entities?filter[name]=bank";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(case1url).then().body("data[0].attributes.name", equalTo(" Bank ICBC (JSC)"))
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

}

@Test

public void FCA_1187case2() throws IOException {

String case2url = baseURI + "/v1/entities?filter[countryISOCode]=IN";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(case2url).then().body("data[0].attributes.countryISOCode", equalTo("IN"))
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

}

@Test

public void FCA_1187case3() throws IOException {

String case3url = baseURI + "/v1/entities?filter[fitchEntityId]=1455904";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(case3url).then().body("data[0].id", equalTo("1455904"))
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

}

@Test

public void FCA_1187negativecase1() throws IOException {

String negativecase1url = baseURI + "/v1/entities?filter[name]=aa";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(negativecase1url).then().body("errors[0].status", equalTo("400"))
           
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

}
@Test

public void FCA_1244() throws IOException {

String newattributesurl = baseURI + "/v1/statements/5454931?fields[statements]=header";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(newattributesurl).then().body("data.attributes.header.periodDuration", equalTo("12"))
           .body("data.attributes.header.auditorOpinion", equalTo("Audited - Unqualified"))
           .body("data.attributes.header.inflationAdjusted", equalTo(false))
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

     }

}
