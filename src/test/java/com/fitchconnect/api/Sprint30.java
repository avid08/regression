package com.fitchconnect.api;



import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint30 extends Configuration {
	

	
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
@Test
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
	