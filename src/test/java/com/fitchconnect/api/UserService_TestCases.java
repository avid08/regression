package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;

import com.configuration.api.Configuration;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;

public class UserService_TestCases extends Configuration {

	@Test

	public void v2_users() throws IOException {

		String userresult = baseURI + "/v2/users";
		boolean failure = false;

		Response users = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
				.header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
				.when().get(userresult).then().body(containsString("firstName")).statusCode(200).extract().response();

		String groupsSelflink = users.path("data[2].relationships.groups.links.related");
		int metaCount = users.path("meta.totalResourceCount");

		System.out.println(metaCount);

		if (metaCount >= 1) {

			System.out.println(groupsSelflink);

			Response group = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).contentType("application/vnd.api+json").when().get(groupsSelflink)
					.then().body(containsString("name")).body(containsString("status")).body(containsString("ENABLED"))
					.statusCode(200).extract().response();

		} else {

			System.out.println((metaCount));
			failure = true;
		}
		Assert.assertFalse(failure);
		Assert.assertFalse(users.asString().contains("isError"));
		Assert.assertFalse(users.asString().contains("isMissing"));

	}

	@Test

	public void v2_Groups() {

		String GroupUri = baseURI + "/v2/groups";
		boolean failure = false;

		Response Groupsdata = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).contentType("application/vnd.api+json").when().get(GroupUri).then()
				.statusCode(200).body(containsString("name")).body(containsString("status"))
				.body(containsString("ENABLED")).body(containsString("groups"))
				.body(containsString("effectivePackages")).body(containsString("packages")).extract().response();

		String effectivePackge = Groupsdata.path("data[0].relationships.effectivePackages.links.related");
		int NumbOfGrps = Groupsdata.path("meta.totalResourceCount");

		System.out.println("number of Groups :" + NumbOfGrps);
		if (NumbOfGrps >= 1) {

			System.out.println(effectivePackge);

			Response group = given().header("Authorization", AuthrztionValue)
					.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
					.header("content", contentValue).contentType("application/vnd.api+json").when().get(effectivePackge)
					.then().body(containsString("groupRateLimits")).body(containsString("featurePermissions"))
					.body(containsString("marketSectors")).body(containsString("regions"))
					.body(containsString("fieldPermissions")).body(containsString("deliveryChannels"))
					.body(containsString("dataPermissions")).statusCode(200).extract().response();

		} else {

			System.out.println((NumbOfGrps));
			failure = true;
		}
		Assert.assertFalse(failure);
		Assert.assertFalse(Groupsdata.asString().contains("isError"));
		Assert.assertFalse(Groupsdata.asString().contains("isMissing"));

	}

	@Test
	public void v2_effecTivePackage() {
		
		String effictvePakge = baseURI + "/v2/effectivePackages";
		boolean failure = false;

		Response effectvePackge = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).contentType("application/vnd.api+json").when().get(effictvePakge).then()
				.statusCode(200)
				.body(containsString("groupRateLimits"))
				.body(containsString("featurePermissions"))
				.body(containsString("marketSectors"))
				.body(containsString("regions"))
				.body(containsString("fieldPermissions"))
				.body(containsString("deliveryChannels"))
				.extract().response();
		
   int totalResourceCount= effectvePackge.path("meta.totalResourceCount");
   
   String userRelatedLink = effectvePackge.path("data[0].relationships.user.links.related");
    System.out.println(userRelatedLink);
   
   if (totalResourceCount>=1){

		Response GroupeffectvePagke = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).contentType("application/vnd.api+json").when().get(userRelatedLink).then()
				.statusCode(200)
				.body(containsString("firstName"))
				.body(containsString("lastName"))
				.body(containsString("webUserId"))
				.body(containsString("email"))
				.body(containsString("relationships"))
				.body(containsString("groups"))
				.extract().response();
	   
	   
      }else {
    	  System.out.println((totalResourceCount));
		failure = true;
    	  
      }
   
	Assert.assertFalse(failure);
	Assert.assertFalse(effectvePackge.asString().contains("isError"));
	Assert.assertFalse(effectvePackge.asString().contains("isMissing"));


	}
	
	@Test
	
     public void v2_Packages() {
		String PakgeUri = baseURI + "/v2/packages";
		boolean failure = false;

		Response Packge = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).contentType("application/vnd.api+json").when().get(PakgeUri).then()
				.statusCode(200)
				.body(containsString("groupRateLimits"))
				.body(containsString("featurePermissions"))
				.body(containsString("marketSectors"))
				.body(containsString("regions"))
				.body(containsString("fieldPermissions"))
				.body(containsString("deliveryChannels"))
				.extract().response();
		
		String grouRelatedLink = Packge.path("data[0].relationships.groups.links.related");
		System.out.println(grouRelatedLink);
		
		Response PackgeGroup = given().header("Authorization", AuthrztionValue)
				.header("X-App-Client-Id", XappClintIDvalue).header("accept", acceptValue)
				.header("content", contentValue).contentType("application/vnd.api+json").when().get(grouRelatedLink).then()
				.statusCode(200)
				.body(containsString("name"))
				.body(containsString("description"))
				.body(containsString("status"))
				.body(containsString("name"))
				.body(containsString("users"))
				.body(containsString("type"))
				.body(containsString("id"))
				
				.extract().response();
		
	}

}
