package com.moodysIssuerRatings;

import static com.jayway.restassured.RestAssured.given;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;

public class MoodysIssuerRatings extends Configuration {
    private static Date endDate = new Date();
    private static Date startDate = Date.from(endDate.toInstant().minus(
            Duration.ofDays(ThreadLocalRandom.current().nextInt(30, 60 + 1))));
    private SimpleDateFormat queryFormat = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss'Z'");

    @Test
    public void checkIfMetaCountMatchesWithDatesFilter() throws ParseException {
        long countFromDb = MoodysIssuerHelper.getCount(dataBaseServer1, startDate, endDate);
        queryFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Response response = given()
                .headers(new Headers
                        (Arrays.asList(
                                new Header("Authorization", "Bearer " + refresh_token),
                                new Header("X-App-Client-Id", XappClintIDvalue),
                                new Header("Accept", acceptValue),
                                new Header("Content-Type", contentValue))))
                .queryParam("filter[startChangeDate]", queryFormat.format(startDate))
                .queryParam("filter[endChangeDate]", queryFormat.format(endDate))
                .get(baseURI + "/v1/moodyIssuerRatings")
                .then()
                .extract()
                .response();
        Assert.assertEquals(response.getStatusCode(), 200, "Response code");
        JSONObject responseObject = (JSONObject) new JSONParser().parse(response.asString());
        long countFromAPI = ((long) ((JSONObject) responseObject.get("meta")).get("count"));
        Assert.assertEquals(countFromAPI, countFromDb, "Meta count in response");
    }
}
