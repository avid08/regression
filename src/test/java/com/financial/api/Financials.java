package com.financial.api;

import static com.jayway.restassured.RestAssured.given;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

public class Financials extends Configuration {
    private SimpleDateFormat queryFormat = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss'Z'");

    private static Date endDate = Date.from(LocalDateTime.now(ZoneId.of("UTC")).toInstant(ZoneOffset.UTC).minus(
            Duration.ofDays(ThreadLocalRandom.current().nextInt(1, 3 + 1))));
    private static Date startDate = Date.from(endDate.toInstant().minus(
            Duration.ofDays(ThreadLocalRandom.current().nextInt(15, 30 + 1))));

    @Test
    public void checkIfMetaCountMatchesWithChangeDatesFilter() throws ParseException {
        long countFromDb = FinancialHelper.getCountForChangeDate(dataBaseServer1, startDate, endDate);
        queryFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Response response = given().log().all()
                .headers(new Headers
                        (Arrays.asList(
                                new Header("Authorization", "Bearer " + refresh_token),
                                new Header("X-App-Client-Id", XappClintIDvalue),
                                new Header("accept", acceptValue),
                                new Header("content", contentValue))))
                .queryParam("fields[statements]", "header")
                .queryParam("filter[startChangeDate]", queryFormat.format(startDate))
                .queryParam("filter[endChangeDate]", queryFormat.format(endDate))
                .get(baseURI + "/v1/statements")
                .then()
                .extract()
                .response();
    	Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
        Assert.assertEquals(response.getStatusCode(), 200, "Response code");
        JSONObject responseObject = (JSONObject) new JSONParser().parse(response.asString());
        long countFromAPI = ((long) ((JSONObject) responseObject.get("meta")).get("count"));
        Assert.assertEquals(countFromAPI, countFromDb, "Comparing count from API vs count from DB");
    }

    @Test
    public void checkIfMetaCountMatchesWithAddDatesFilter() throws ParseException {
        long countFromDb = FinancialHelper.getCountForAddDate(dataBaseServer1, startDate, endDate);
        queryFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Response response = given().log().all()
                .headers(new Headers
                        (Arrays.asList(
                                new Header("Authorization", "Bearer " + refresh_token),
                                new Header("X-App-Client-Id", XappClintIDvalue),
                                new Header("accept", acceptValue),
                                new Header("content", contentValue))))
                .queryParam("fields[statements]", "header")
                .queryParam("filter[startAddDate]", queryFormat.format(startDate))
                .queryParam("filter[endAddDate]", queryFormat.format(endDate))
                .get(baseURI + "/v1/statements")
                .then()
                .extract()
                .response();
        Assert.assertEquals(response.getStatusCode(), 200, "Response code");
        JSONObject responseObject = (JSONObject) new JSONParser().parse(response.asString());
        long countFromAPI = ((long) ((JSONObject) responseObject.get("meta")).get("count"));
        Assert.assertEquals(countFromAPI, countFromDb, "Comparing count from API vs count from DB");
        
    	Assert.assertFalse(response.asString().contains("isError"));
		Assert.assertFalse(response.asString().contains("isMissing"));
		Assert.assertFalse(response.asString().contains("isRestricted"));
    }
}
