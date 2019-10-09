package com.standardandpoor;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.configuration.api.Configuration;
import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;

public class StandardAndPoorDataAggregator extends Configuration {
    private void abstractTest(String dataBaseServer, StandardAndPoorHelper.Freshness freshness, int ratingStatusIndex, boolean contain) {
        StandardAndPoorHelper.getBodyListForDataAggregator(dataBaseServer, freshness, ratingStatusIndex)
                .forEach(entry -> {
                    Response response = RestAssured.given()
                            .headers(new Headers
                                    (Arrays.asList(
                                            new Header("Authorization", "Bearer " + refresh_token),
                                            new Header("X-App-Client-Id", XappClintIDvalue),
                                            new Header("Accept", acceptValue),
                                            new Header("Content-Type", contentValue))))
                            .body(new Gson().toJson(entry))
                            .post(baseURI + "/v1/data/valueRequest")
                            .then()
                            .extract()
                            .response();
                    Assert.assertEquals(response.statusCode(), 200);
                    if (contain) {
                        try {
                            Assert.assertTrue(response.asString().contains("\"value\""), response.asString() + " contains \"value\"");
                        } catch (AssertionError assertionError) {
                            Assert.assertTrue(response.asString().contains("\"type\":\"fitchId\",\"isMissing\":true"), "If no value - then contains \"isMissing\"");
                        }
                    } else {
                        try {
                            Assert.assertFalse(response.asString().contains("\"value\""), response.asString() + " contains \"value\"");
                        } catch (AssertionError assertionError) {
                            if (StandardAndPoorHelper.checkIfTwoRatingsInOneDay(response.asString(), dataBaseServer1)) {
                                System.out.println("Rating has two ratings in one day");
                                Assert.assertTrue(true);
                            } else {
                                Assert.fail(response.asString());
                            }
                        }
                    }
                });
    }

    @Test
    public void newRatingsWithRtngStatusIndEq1() {
        abstractTest(dataBaseServer1, StandardAndPoorHelper.Freshness.NEW, 1, true);
    }

    @Test
    public void newRatingsWithRtngStatusIndEq2() {
        abstractTest(dataBaseServer1, StandardAndPoorHelper.Freshness.NEW, 2, true);

    }

    @Test
    public void newRatingsWithRtngStatusIndEq3() {
        abstractTest(dataBaseServer1, StandardAndPoorHelper.Freshness.NEW, 3, true);

    }

    @Test
    public void oldRatingsWithRtngStatusIndEq1() {
        abstractTest(dataBaseServer1, StandardAndPoorHelper.Freshness.OLD, 1, true);

    }

    @Test
    public void oldRatingsWithRtngStatusIndEq2() {
        abstractTest(dataBaseServer1, StandardAndPoorHelper.Freshness.OLD, 2, false);

    }

    @Test
    public void oldRatingsWithRtngStatusIndEq3() {
        abstractTest(dataBaseServer1, StandardAndPoorHelper.Freshness.OLD, 3, false);

    }
}
