package com.issueRatingTransition;


import static com.jayway.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
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
import static com.issueRatingTransition.IssueRatingsTransitionHelper.getDateWithOffset;

public class IssueRatingsTransition extends Configuration {

    @Test
    public void startDateBeforeValidRatingEndDateBeforeWithdrawal() throws ParseException {
        Document testDoc = IssueRatingsTransitionHelper.getTestDoc(dataBaseServer1);
        Response response = getResponse(testDoc, Types.FirstValid1, Types.LastValid1);
        Assert.assertEquals(response.getStatusCode(), 200, "Response code");
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.asString());
        JSONArray data = (JSONArray) jsonObject.get("data");
        Assert.assertNotEquals(data.size(), 0, "Size of data array in response");
        String to = (String) ((JSONObject) ((JSONObject) data.get(0)).get("attributes")).get("to");
        Assert.assertNotEquals(to, "WD", "Rating in response");
    }

    @Test
    public void startDateBeforeValidRatingEndDateAfterValidAfterWithdrawal() throws ParseException {
        Document testDoc = IssueRatingsTransitionHelper.getTestDoc(dataBaseServer1);
        Response response = getResponse(testDoc, Types.FirstValid1, Types.FirstValid2);
        Assert.assertEquals(response.getStatusCode(), 200, "Response code");
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.asString());
        JSONArray data = (JSONArray) jsonObject.get("data");
        Assert.assertNotEquals(data.size(), 0, "Size of data array in response");
        String to = (String) ((JSONObject) ((JSONObject) data.get(0)).get("attributes")).get("to");
        Assert.assertEquals(to, "WD", "Rating in response");
    }

    @Test
    public void startDateAtWithdrawalEndDateAtWithdrawal() throws ParseException {
        Document testDoc = IssueRatingsTransitionHelper.getTestDoc(dataBaseServer1);
        Response response = getResponse(testDoc, Types.WD, Types.WD);
        Assert.assertEquals(response.getStatusCode(), 200, "Response code");
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.asString());
        JSONArray data = (JSONArray) jsonObject.get("data");
        Assert.assertEquals(data.size(), 0, "Size of data array in response");
    }

    @Test
    public void startDateAfterValidAfterWithdrawal() throws ParseException {
        Document testDoc = IssueRatingsTransitionHelper.getTestDoc(dataBaseServer1);
        Response response = getResponse(testDoc, Types.FirstValid2, Types.LastValid2);
        Assert.assertEquals(response.getStatusCode(), 200, "Response code");
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.asString());
        JSONArray data = (JSONArray) jsonObject.get("data");
        Assert.assertNotEquals(data.size(), 0, "Size of data array in response");
        String to = (String) ((JSONObject) ((JSONObject) data.get(0)).get("attributes")).get("from");
        Assert.assertNotEquals(to, "WD", "Rating in response");
    }

    private Response getResponse(Document testDoc, Types startDateType, Types endDateType) {
        return given()
                .headers(new Headers
                        (Arrays.asList(
                                new Header("Authorization", "Bearer " + refresh_token),
                                new Header("X-App-Client-Id", XappClintIDvalue),
                                new Header("Accept", acceptValue),
                                new Header("Content-Type", contentValue))))
                .queryParam("filter[startDate]", getDateWithOffset(testDoc, startDateType))
                .queryParam("filter[endDate]", getDateWithOffset(testDoc, endDateType))
                .queryParam("filter[ratingType]", ((List<Document>) testDoc.get("ratings")).get(0).getString("fcRatingType"))
                .queryParam("filter[marketSectorId]", ((List<Document>) testDoc.get("ratings")).get(0).getString("marketSectorId"))
                .queryParam("filter[issueId]", ((List<Document>) testDoc.get("ratings")).get(0).getLong("ratableId"))
                .get(baseURI + "/v1/issueRatingsTransitions")
                .then()
                .extract()
                .response();
    }
}
