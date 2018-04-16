package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class fisCon_Sprint11 extends Configuration {

	@Test
	public void FISC_1048_obligors() {

		String IssueResourceURi = baseURI + "/v1/issues/87130571";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(IssueResourceURi).then().statusCode(200).body(containsString("obligors"))
				.body(containsString("transactionSecurities")).body(containsString("relationships"))
				.body(containsString("analysts")).body(containsString("id")).body(containsString("roleId"))
				.body(containsString("groupCode")).body(containsString("name")).body(containsString("description"))
				.body(containsString("email")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		/*
		 * String obligorslink =
		 * res.path("data[0].relationships.obligors.links.related");
		 * System.out.println(obligorslink);
		 */

		/*
		 * Response res1 = given().header("Authorization",
		 * AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
		 * .header("accept", acceptValue).header("content",
		 * contentValue).contentType("application/vnd.api+json")
		 * .when().get(obligorslink).then() .statusCode(200)
		 * .extract().response();
		 * 
		 * 
		 * Assert.assertFalse(res1.asString().contains("isError"));
		 * Assert.assertFalse(res1.asString().contains("isMissing"));
		 * Assert.assertFalse(res1.asString().contains("isRestricted"));
		 */

	}

	@Test

	public void FISC_1052_Issue_Analstattributes() {

		String IssueResourceURi = baseURI + "/v1/issues/87130571";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(IssueResourceURi).then().statusCode(200).body(containsString("obligors"))
				.body(containsString("transactionSecurities")).body(containsString("originalRatingDate"))
				.body(containsString("analysts")).body(containsString("id")).body(containsString("roleId"))
				.body(containsString("groupCode")).body(containsString("name")).body(containsString("description"))
				.body(containsString("email")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void FISC_1126_Issuer_PeerRelation() {

		String IssueresourceURi = baseURI + "/v1/issuers";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(IssueresourceURi).then().statusCode(200).body(containsString("peers")).extract().response();

		System.out.println(res.asString());

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		String peerlink = res.path("data[0].relationships.peers.links.related");

		System.out.println(peerlink);

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(peerlink).then().statusCode(200).extract().response();

	}

	@Test

	public void FISC_1129_TransactionSecurity() {

		String IssueResourceURi = baseURI + "/v1/issues/87130571";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(IssueResourceURi).then().statusCode(200).body(containsString("obligors"))
				.body(containsString("transactionSecurities")).body(containsString("relationships"))
				.body(containsString("analysts")).body(containsString("id")).body(containsString("roleId"))
				.body(containsString("groupCode")).body(containsString("name")).body(containsString("description"))
				.body(containsString("email")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		/*
		 * String transactionSecurityLink =
		 * res.path("data[0].relationships.transactionSecurities.links.related")
		 * ;
		 * 
		 * System.out.println(transactionSecurityLink);
		 */
		/*
		 * Response res1 = given().header("Authorization",
		 * AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
		 * .header("accept", acceptValue).header("content",
		 * contentValue).contentType("application/vnd.api+json")
		 * .when().get(transactionSecurityLink).then() .statusCode(200)
		 * .extract().response();
		 * 
		 * 
		 * Assert.assertFalse(res1.asString().contains("isError"));
		 * Assert.assertFalse(res1.asString().contains("isMissing"));
		 * Assert.assertFalse(res1.asString().contains("isRestricted"));
		 */

	}

	@Test

	public void FISC_1134() throws IOException {

		URL file = Resources.getResource("FISC_1134.json");
		String myRequest = Resources.toString(file, Charsets.UTF_8);

		Response fieldResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType("application/vnd.api+json").body(myRequest)
				.with().when().post(dataPostUrl).then().assertThat().statusCode(200).body(containsString("value"))
				.body(containsString("timeIntervalDate")).body(containsString("FC_PEER_ISSUER_NAMES"))
				.body(containsString("China Minsheng Banking Corp., Ltd.")).body(containsString("Bank of Beijing")).extract()
				.response();

		Assert.assertFalse(fieldResponse.asString().contains("isError"));
		Assert.assertFalse(fieldResponse.asString().contains("isMissing"));
		Assert.assertFalse(fieldResponse.asString().contains("isRestricted"));

	}

	@Test

	public void FISC_952() {

		String transactionSecurityURi = baseURI + "/v1/transactionSecurities";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(transactionSecurityURi).then().statusCode(200).body(containsString("transactionSecurities"))
				.body(containsString("description")).body(containsString("agentStateName")).body(containsString("name"))
				.body(containsString("marketSectors")).body(containsString("transaction"))
				.body(containsString("issues"))

				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		String SingletransactionSecurityURi = baseURI + "/v1/transactionSecurities/96499501";

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(SingletransactionSecurityURi).then().statusCode(200)
				.body(containsString("transactionSecurities")).body(containsString("description"))
				.body(containsString("agentStateName")).body(containsString("name"))
				.body(containsString("marketSectors")).body(containsString("transaction"))
				.body(containsString("issues"))

				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test

	public void FISC_958_TransactionSecurityRating() {

		String transactionSecurityURi = baseURI
				+ "/v1/transactionSecurities/77663/transactionSecurityRatings?filter[action]=Revision Outlook&filter[alert]=Rating Outlook Stable&filter[startDate]=2010-01-01";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(transactionSecurityURi).then().statusCode(200).body(containsString("alert"))
				.body(containsString("rating")).body(containsString("description")).body(containsString("action"))
				.body(containsString("effectiveDate")).body(containsString("transactionSecurity")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

	}

	@Test

	public void FISC_962() {

		String transactionSecurityURi = baseURI + "/v1/transactions";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(transactionSecurityURi).then().statusCode(200).body(containsString("transactions"))
				.body(containsString("transactionSecurities")).body(containsString("transactionRatings"))
				.body(containsString("issuers")).body(containsString("transactionAgent"))
				.body(containsString("keyRatingFactors")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}

	@Test

	public void FISC_965_transactionSecurityRatings_Relation() {

		String transactionSecurityURi = baseURI + "/v1/transactionSecurities";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(transactionSecurityURi).then().statusCode(200)
				.body(containsString("transactionSecurityRatings")).body(containsString("transactionSecurities"))
				.body(containsString("transaction")).body(containsString("issues"))
				.body(containsString("marketSectors")).body(containsString("primary")).extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));

		String transactionSecurityRatingslink = res
				.path("data[0].relationships.transactionSecurityRatings.links.related");

		Response res1 = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(transactionSecurityRatingslink).then().statusCode(200).extract().response();

	}

	@Test

	public void FISC_966_MS_Analyst_DC_attribute() {

		String entitiesURL = baseURI +"/v1/entities/1431752";

		Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(entitiesURL).then().statusCode(200).body(containsString("analysts"))
				.body(containsString("disclosure")).body(containsString("id")).body(containsString("role"))
				.body(containsString("groupCode")).body(containsString("name")).body(containsString("description"))
				.body(containsString("email")).body(containsString("marketSectors")).body(containsString("primary"))
				.extract().response();

		Assert.assertFalse(res.asString().contains("isError"));
		Assert.assertFalse(res.asString().contains("isMissing"));
		Assert.assertFalse(res.asString().contains("isRestricted"));
	}
}