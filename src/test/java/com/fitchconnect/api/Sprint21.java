package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint21 extends Configuration {
	


	@Test()
	public void MetaDataWithLinks_975() {

		Response response = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then().assertThat()
				.statusCode(200).contentType(ContentType.JSON).extract().response();

		Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));

		List<String> link = response.path("data.links.self");
		List<String> selfCaterogies = response.path("data.relationships.categories.links.self");
		List<String> reltedCaterogies = response.path("data.relationships.categories.links.self");

		int linkcounts = link.size();

		for (int i = 0; i < linkcounts; i++) {

			assertNotNull(link.get(i));
			//assertNotNull(selfCaterogies.get(i));
			//assertNotNull(reltedCaterogies.get(i));

		}

	}

	@Test()

	public void additional_FC_ConnectURl_934() {

		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).when().get(metaUrl).then().assertThat()
				.log().ifError().statusCode(200).body("data.id", hasItem("FC_CONNECT_URL"))
				.body("data.attributes.displayName", hasItem("WebURL"));

	}

	@Test(enabled = true)

	public void GroupingsByCatergoryID_802() {

		String catGoryendPoint = "/v1/metadata/categories/2";
		String catGoryURl = baseURI + catGoryendPoint;

		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue)

				.when().get(catGoryURl).then().assertThat().log().ifError().statusCode(200)
				.body("data.type", equalTo("categories")).body("data.attributes.name", equalTo("Financials"))
				.body("data.relationships.children.data.id", hasItem("13"))
				.body("data.relationships.children.data.id", hasItem("12"))
				.body("data.relationships.children.data.id", hasItem("11"));

	}

	@Test(enabled = true)
	public void singleField_974_fromMetaData() {

		String sngleFildEndpoint = "/v1/metadata/fields/FC_ST_LC_FER_SP";
		String fieldUrl = baseURI + sngleFildEndpoint;

		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue)

				.when().get(fieldUrl).then().statusCode(200)
				.body("data.attributes.displayName", equalTo("S&P ST LC Financial Enhancement Rating"))
				.body("data.attributes.fitchFieldDesc",
						equalTo("S&P Short-term Local Currency Financial Enhancement Rating"));
				

	}

	@Test

	public void unRated_928() throws IOException {

		URL file = Resources.getResource("928_Request_UnRated.json");

		String myJson = Resources.toString(file, Charsets.UTF_8);

		Response dataResponse = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).contentType(contentValue).body(myJson).with()

				.when().post(dataPostUrl)

				.then().assertThat().log().ifError().statusCode(200).body(containsString("FC_COMPANY_NAME"))
				.body(containsString("AGNT_")).extract().response();

		Assert.assertFalse(dataResponse.asString().contains("isError"));
		Assert.assertFalse(dataResponse.asString().contains("isMissing"));
		Assert.assertFalse(dataResponse.asString().contains("isRestricted"));

	}

	@Test(enabled = true)
	public void FitchRating_957_Entity_Resource() {

		String fitchRatingEndpnt = "/v1/entities/110631/fitchIssuerRatings";
		String fRatingURL = baseURI + fitchRatingEndpnt;

		Response fitchData = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("Accept", acceptValue)
				.header("content", contentValue).when().get(fRatingURL).then().assertThat().statusCode(200)
				.body("data.get(0).type", equalTo("fitchIssuerRatings"))				
				.contentType(ContentType.JSON).extract().response();

		List<String> alert = fitchData.path("data.attributes.alert");
		List<String> ratinType = fitchData.path("data.attributes.ratingType");
		List<String> rating = fitchData.path("data.attributes.rating");
		List<String> action = fitchData.path("data.attributes.action");
		List<String> effectiveDate = fitchData.path("data.attributes.effectiveDate");
		List<String> relationship = fitchData.path("data.relationships");

		int totalattributes = alert.size();

		System.out.println(totalattributes);

		for (int i = 0; i < totalattributes; i++) {

			//Assert.assertNotNull(alert.get(i));
			Assert.assertNotNull(ratinType.get(i));
			Assert.assertNotNull(rating.get(i));
			Assert.assertNotNull(action.get(i));
			Assert.assertNotNull(effectiveDate.get(i));
			Assert.assertNotNull(relationship.get(i));

		}
		
		Assert.assertFalse(fitchData.asString().contains("isError"));
		Assert.assertFalse(fitchData.asString().contains("isMissing"));
		Assert.assertFalse(fitchData.asString().contains("isRestricted"));

	}

	// Test Description :
	@Test(enabled = true)

	public void Testing_940_FCURL_Rated() {

		String newEndPoint = "/v1/entities/108273";
		String newUrl = baseURI + newEndPoint;

		// Rated Entity should return GRP within FitchconnectURl
		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("Accept", acceptValue).header("content", contentValue).when().get(newUrl).then().log().ifError()
				.assertThat().statusCode(200).body("data.attributes.name", equalTo("Mizuho Bank, Ltd.")).and()
				.body("data.attributes.fitchConnectUrl", containsString("GRP_"));

	}

	@Test(enabled = true)
	public void Testing_940_FCURL_UnRated() {
		// Testing out not Rated Entity and Expected Result a Fitch Agent ID
		String newEndPoint2 = "/v1/entities/1133670";
		String newUrl2 = baseURI + newEndPoint2;

		given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("Accept", acceptValue).header("content", contentValue).when().get(newUrl2).then().log()
				.ifError().assertThat().statusCode(200).body("data.attributes.name", equalTo("Citibank N.A."))
				.body("data.attributes.fitchConnectUrl", containsString("AGNT_"));

	}

}
