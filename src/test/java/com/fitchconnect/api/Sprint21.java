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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint21 {
	public Response response;
	String myjson;
	String AuthrztionValue;
	String baseURI;
	String env;
	String databaseFitchEnty;
	String dataBaseServer1;
	String dataBaseServer2;
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
			this.AuthrztionValue = ("Basic MUc4TTJCUzVIUTdGTVE5RVlNWTdWWVlUWTpoeU51d2lIYUVtOEpaSnF1RzVsRmM0TnRrTXpMMjdqcVFFczVwSDlUdEZJ");
			dataBaseServer1 = "mgo-pue1c-cr001.fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		} else if (env.equals("dev")) {
			baseURI = "https://api.fitchconnect-dev.com";
			this.AuthrztionValue = ("Basic MVNCRFI4MzVTQ1lOVU5CSDJSVk1TU0MxOTpHTExaUlR3QUpRdjVTazV1cXRyZWlqZE9SK01yQTZrU2plVmNuZXdlekow");
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
			dataBaseServer1 ="mgo-pue1c-cr001..fitchratings.com";
			dataBaseServer2 = "mgo-pue1c-ur001.fitchratings.com";
			databaseFitchEnty = "esp-9";
		}

		System.out.println(baseURI);
		metaUrl = baseURI + metaEndPoint;
		dataPostUrl = baseURI + dataEndPoint;

	}

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
			assertNotNull(selfCaterogies.get(i));
			assertNotNull(reltedCaterogies.get(i));

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
				.header("content", contentValue).when().get(fRatingURL).then().assertThat().statusCode(200).log()
				.ifError().body("data.get(0).type", equalTo("fitchIssuerRatings"))
				// .body(data.get(0). arg1)
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

			Assert.assertNotNull(alert.get(i));
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
