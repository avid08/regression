package com.fitchconnect.api;



import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint30 {
	
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
			baseURI = "https://api.fitchconnect-qa.com";
			this.AuthrztionValue = ("Basic MU1HUjNXOFJCV0ZJNFlJMzNEV000MDk2WTpGYXp5Y3E4MHd1M0hpSlFzNVVhZDlJa3E1dEIyZ1YzcnA1OVB4UmowV2pJ");
			dataBaseServer1 = "mgo-que1a-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-que1a-ur001.fitchratings.com";
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

public void FCA_1580_SWIFT_CD () throws IOException {
	
	URL file = Resources.getResource("fca_1580.json");

	String myJson = Resources.toString(file, Charsets.UTF_8);

	Response financialData = given().header("Authorization", AuthrztionValue)
			.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myJson).with()

			.when().post(dataPostUrl)

			.then().assertThat().statusCode(200).body(containsString("value"))
			
			.body(containsString("BCLYBRSP")).extract().response();

	Assert.assertFalse(financialData.asString().contains("isError"));
	Assert.assertFalse(financialData.asString().contains("isMissing"));
	Assert.assertFalse(financialData.asString().contains("isRestricted"));
	
 }
public void FCA_1497test1() {

	String shareholderurl1 = baseURI + "/v1/entities/111522/shareholders?include[shareholders]=shareholders.shareholderEntity";

	Response shareholdersresponse1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(shareholderurl1).then().assertThat()
			.statusCode(200).contentType(ContentType.JSON).body(containsString("shareholders"))
			.extract().response();

	Assert.assertFalse(shareholdersresponse1.asString().contains("isError"));
	Assert.assertFalse(shareholdersresponse1.asString().contains("isMissing"));
	Assert.assertFalse(shareholdersresponse1.asString().contains("isRestricted"));

}

@Test

public void FCA_1497test2() {

	String shareholderurl2 = baseURI + "/v1/entities/1002653/shareholders?include[shareholders]=shareholders.shareholderEntity";

	Response shareholdersresponse2 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(shareholderurl2).then().assertThat()
			.statusCode(200).contentType(ContentType.JSON).body(containsString("shareholders"))
			.extract().response();

	Assert.assertFalse(shareholdersresponse2.asString().contains("isError"));
	Assert.assertFalse(shareholdersresponse2.asString().contains("isMissing"));
	Assert.assertFalse(shareholdersresponse2.asString().contains("isRestricted"));

}

@Test

public void FCA_1528() {

	String moodysurl = baseURI + "/v1/moodyIssuerRatings/111670.46497.804225600000/relationships/entity";

	Response moodysresponse = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(moodysurl).then().assertThat()
			.statusCode(200).contentType(ContentType.JSON).body(containsString("111670"))
			.extract().response();

	Assert.assertFalse(moodysresponse.asString().contains("isError"));
	Assert.assertFalse(moodysresponse.asString().contains("isMissing"));
	Assert.assertFalse(moodysresponse.asString().contains("isRestricted"));

}

@Test

public void FCA_1384() {

	String usersurl = baseURI + "/v1/users";

	Response usersresponse = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(usersurl).then().assertThat()
			.statusCode(200).contentType(ContentType.JSON).body(containsString("users"))
			.extract().response();

	Assert.assertFalse(usersresponse.asString().contains("isError"));
	Assert.assertFalse(usersresponse.asString().contains("isMissing"));
	Assert.assertFalse(usersresponse.asString().contains("isRestricted"));

}

@Test 

public void FCA_1272test1() {

	String moodys1 = baseURI + "/v1/entities/111670/moodyIssuerRatings?filter[ratingRank]=12&filter[watchlist]=uncer&filter[rating]=ba&filter[ratingDirection]=rating&filter[watchlistStartDate]=2000-01-30&filter[watchlistEndDate]=2016-04-21&filter[ratingType]=LT&filter[ratingStartDate]=2000-01-30&filter[ratingEndDate]=2016-04-21";

	Response moodysresponse1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(moodys1).then().assertThat()
			.statusCode(200).contentType(ContentType.JSON).body(containsString("moodyIssuerRatings"))
			.extract().response();

	Assert.assertFalse(moodysresponse1.asString().contains("isError"));
	Assert.assertFalse(moodysresponse1.asString().contains("isMissing"));
	Assert.assertFalse(moodysresponse1.asString().contains("isRestricted"));

}

@Test 
public void FCA_1272test2() {

	String moodys2 = baseURI + "/v1/moodyIssuerRatings?filter[ratingRank]=12&filter[watchlist]=uncer&filter[rating]=ba&filter[ratingDirection]=rating&filter[watchlistStartDate]=2000-01-30&filter[watchlistEndDate]=2016-04-21&filter[ratingType]=LT&filter[ratingStartDate]=2000-01-30&filter[ratingEndDate]=2016-04-21";

	Response moodysresponse2 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(moodys2).then().assertThat()
			.statusCode(200).contentType(ContentType.JSON).body(containsString("moodyIssuerRatings"))
			.extract().response();

	Assert.assertFalse(moodysresponse2.asString().contains("isError"));
	Assert.assertFalse(moodysresponse2.asString().contains("isMissing"));
	Assert.assertFalse(moodysresponse2.asString().contains("isRestricted"));

}

@Test
public void FCA_1273() {

	String SnP = baseURI + "/v1/entities/102798/standardAndPoorIssuerRatings?filter[fitchFieldId]=SP&filter[ratingType]=issuer&filter[rating]=A&filter[ratingStartDate]=2000-12-19&filter[ratingEndDate]=2016-12-19&filter[outlook]=Negative&filter[outlookStartDate]=2000-12-19&filter[outlookEndDate]=2016-12-19&filter[creditWatch]=N&filter[creditWatchStartDate]=2000-12-19&filter[creditWatchEndDate]=2016-12-19";

	Response SnPresponse = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
			.header("accept", acceptValue).header("content", contentValue).when().get(SnP).then().assertThat()
			.statusCode(200).contentType(ContentType.JSON).body(containsString("standardAndPoorIssuerRatings"))
			.extract().response();

	Assert.assertFalse(SnPresponse.asString().contains("isError"));
	Assert.assertFalse(SnPresponse.asString().contains("isMissing"));
	Assert.assertFalse(SnPresponse.asString().contains("isRestricted"));

}
}
	