package com.standardandpoor;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fitchconnect.api.Configuration;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;

public class StandardAndPoorEndpoint extends Configuration {
    private void abstractTest(String dataBaseServer, StandardAndPoorHelper.Freshness freshness, int ratingStatusIndex, boolean contain) {
        StandardAndPoorHelper.getIdListForEndpoint(dataBaseServer, freshness, ratingStatusIndex)
                .forEach(idOfRating -> {
                    Response response = RestAssured.given()
                            .headers(new Headers
                                    (Arrays.asList(
                                            new Header("Authorization", "Bearer " + refresh_token),
                                            new Header("X-App-Client-Id", XappClintIDvalue),
                                            new Header("Accept", acceptValue),
                                            new Header("Content-Type", contentValue))))
                            .pathParam("id", idOfRating)
                            .get(baseURI + "/v1/standardAndPoorIssuerRatings/{id}")
                            .then()
                            .extract()
                            .response();
                    if (contain) {
                        Assert.assertEquals(response.statusCode(), 200, "Status code");
                    } else {
                        Assert.assertEquals(response.statusCode(), 404, "Status code");
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
