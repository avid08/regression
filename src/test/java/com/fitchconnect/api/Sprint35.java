package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class Sprint35 extends Configuration {

	@Test

	public void stement_nickName_Service_1655() {

		String nickNamesURl = baseURI + "/v1/entities/1079592/nicknames";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(nickNamesURl).then()
				.contentType(ContentType.JSON).statusCode(200).body(containsString("lastStatementDate"))
				.body(containsString("accountingStandard")).body(containsString("consolidation"))
				.body(containsString("entity"))

				.extract().response();

		String nickNameIds = res.path("data[0].relationships.statements.links.related");
		String listOfStatemnt = res.path("data[0].relationships.statements.links.self");

		String entityId = res.path("data[0].relationships.entity.links.related");
		String entityIdself = res.path("data[0].relationships.entity.links.self");

		// List of statement
		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(listOfStatemnt).then()
				.contentType(ContentType.JSON).statusCode(200);

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(nickNameIds).then()
				.contentType(ContentType.JSON).statusCode(200).body(containsString("stmntId"))
				.body(containsString("periodType")).body(containsString("periodYear"))
				.body(containsString("auditorOpinion")).body(containsString("reportedCurrency"))
				.body(containsString("fitchFieldId")).body(containsString("fitchFieldType"))
			
				.extract().response();

		Assert.assertFalse(res1.asString().contains("isError"));
		Assert.assertFalse(res1.asString().contains("isMissing"));
		Assert.assertFalse(res1.asString().contains("isRestricted"));

		// entity information
		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityId).then()
				.contentType(ContentType.JSON).statusCode(200)
				.body(containsString("countryISOCode"))
				.body(containsString("countryName")).body(containsString("name")).body(containsString("city"));;

		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(entityIdself).then()
				.contentType(ContentType.JSON).statusCode(200);

	}

	@Test

	public void effectivePersmisison_1765() {

		String nickNamesURl = baseURI + "/v1/effectivePermissions";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(nickNamesURl).then()
				.contentType(ContentType.JSON).statusCode(200).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

}
