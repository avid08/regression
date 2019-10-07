package com.issues;

import static com.jayway.restassured.RestAssured.given;

import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;

public class includeTransactionSecurities extends Configuration {
    @Test
    public void issueHasSecurities() throws ParseException {
        for (int count = 0; count <= 1; count++) {
            int id = IssuesHelper.getIssueWithSecurities(dataBaseServerNewESP9, count);
            Response response = given()
                    .headers(new Headers
                            (Arrays.asList(
                                    new Header("Authorization", "Bearer " + refresh_token),
                                    new Header("X-App-Client-Id", XappClintIDvalue),
                                    new Header("Accept", acceptValue),
                                    new Header("Content-Type", contentValue))))
                    .queryParam("include[issues]", "transactionSecurities")
                    .get(baseURI + "/v1/issues/" + id)
                    .then()
                    .extract()
                    .response();
            Assert.assertEquals(response.statusCode(), 200);
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.asString());
            JSONArray included = (JSONArray) jsonObject.get("included");
            Assert.assertEquals(included.size(), count + 1, "Size of \"included\" array in response");
        }
    }

    @Test
    public void issueHasNoSecurities() throws ParseException {
        int id = IssuesHelper.getIssueWithoutSecurities(dataBaseServerNewESP9);
        Response response = given()
                .headers(new Headers
                        (Arrays.asList(
                                new Header("Authorization", "Bearer " + refresh_token),
                                new Header("X-App-Client-Id", XappClintIDvalue),
                                new Header("accept", acceptValue),
                                new Header("content", contentValue))))
                .queryParam("include[issues]", "transactionSecurities")
                .get(baseURI + "/v1/issues/" + id)
                .then()
                .extract()
                .response();
        Assert.assertEquals(response.statusCode(), 200);
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.asString());
        JSONArray included = (JSONArray) jsonObject.get("included");
        Assert.assertNull(included, "\"included\" array in response");
    }
}
