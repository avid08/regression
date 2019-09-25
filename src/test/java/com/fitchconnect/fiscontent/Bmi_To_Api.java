package com.fitchconnect.fiscontent;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class Bmi_To_Api extends Configuration {

	/*
	 * public static Response response; public static String jsonAsString;
	 * public static String myjson; String baseURI =
	 * "https://api.fitchconnect-qa.com"; // URL for QA String AuthrztionValue =
	 * "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJodHRwOi8vaWRlbnRpdHktZGF0YS1zZXJ2aWNlOjgwODAvdjIvdXNlcnMvMzRrWjV1dUNEbGE3N1hxWW1Xc0VvIiwidXNlcl9uYW1lIjoiYW1pbnVsLmlzbGFtQGZpdGNoc29sdXRpb25zLmNvbSIsInNjb3BlIjpbImZvbyJdLCJpc3MiOiJodHRwOi8vaWFtLmZpdGNoY29ubmVjdC1xYS5jb20iLCJleHAiOjE1MDI4MzM3MzAsImlhdCI6MTUwMjgzMDEzMCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6ImQxZGE3ZmI2LWVmOTItNDQ2YS04ZTYwLWE4MWI0Y2EyYmNmMyIsImNsaWVudF9pZCI6ImVkZ2UtaWRtLWFkbWluaXN0cmF0b3JAZml0Y2hzb2x1dGlvbnMuY29tIn0.7GTRuxE2AWXbNlCyB6E_9Pxx8C3DEjLdK0093D-rX7U"
	 * ; String metaEndPoint = "/v1/portfolios/"; // Metadata-EndPoint String
	 * metaUrl = baseURI + metaEndPoint; String dataEndPoint =
	 * "/v1/data/valueRequest"; String dataPostUrl = baseURI + dataEndPoint; //
	 * Data Aggregator -EndPoint String XappClintIDvalue =
	 * "3dab0f06-eb00-4bee-8966-268a0ee27ba0"; String acceptValue =
	 * "application/vnd.api+json"; String contentValue =
	 * "application/vnd.api+json";
	 */
	List BMI_Ids;
	List BMI_Names;
	List Children;
	List BMI_Category_ID;

	@Test
	public void getAPIResponse() {
		String apiName;
		List<String> API_Categories_ID = null;
		List API_Children = null;

		for (int i = 0; i < BMI_Ids.size(); i++) {
			API_Categories_ID = null;
			API_Children = null;
			String ApiBmiURL = baseURI + "/v1/metadata/categories/BMI_" + BMI_Ids.get(i);
			//System.out.println(ApiBmiURL);

			Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
					.header("accept", acceptValue).header("content", contentValue).when().get(ApiBmiURL).then()
					.statusCode(200).contentType(ContentType.JSON).extract().response();

			String responseAsString = res.asString();
			apiName = res.path("data.attributes.name");
			//System.out.println(apiName);

			API_Children = res.path("data.relationships.children.data");
		

			for (int j = 0; j < API_Children.size(); j++) {
				if (API_Children.size() >= 1) {
					API_Categories_ID = res.path("data.relationships.children.data.id");

					// System.out.println("category ID :" +API_Categories_ID );
				} else {
					API_Categories_ID = null;
					break;
				}
			}


			if (!apiName.equals(BMI_Names.get(i))) {
				System.out.println("These do NOT match");
				System.out.println("ID: " + BMI_Ids.get(i));
				System.out.println("API Name: " + apiName);
				System.out.println("BMI Name: " + BMI_Names.get(i));

			}else{
				
				//System.out.println("API Name: " + apiName);
				//System.out.println("BMI Name: " + BMI_Names.get(i));

			}
		}

	}

	@BeforeTest
	public void getBMI_IDS() {
		String contentValue = "application/vnd.api+json";
		String url = "https://api-stg1.bmiresearch.com/v1/ndb/metadata/categories";
		System.out.println(url);

		Response res = given().header("content", contentValue).when().get(url).then().statusCode(200)
				.contentType(ContentType.JSON).extract().response();

		String responseAsString = res.asString();
		// System.out.println(responseAsString);

		BMI_Ids = res.path("data.id");
		
	
		
		BMI_Names = res.path("data.attributes.name");
		Children = res.path("data.relationships.children.data.id");
	
		BMI_Category_ID = res.path("data.relationships.children.data.id");

		for (int i = 0; i < Children.size(); i++) {
			if (Children.get(i) == null) {
				BMI_Category_ID.add(i, null);

			} else {
				List tempList = (List) BMI_Category_ID.get(i);
				for (int j = 0; j < tempList.size(); j++) {
					String temp = "BMI_" + (String) tempList.get(j);
					tempList.remove(j);
					tempList.add(j, temp);

				}
			}
		}

	
	}

}
