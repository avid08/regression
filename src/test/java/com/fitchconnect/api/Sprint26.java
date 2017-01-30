package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;

public class Sprint26 extends Configuration {


    @Test

    public void FCA_1158() throws IOException {

           String newattributes = baseURI + "/v1/entities/1064795";

           Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                        .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                         .when().get(newattributes).then().body("data.id", equalTo("1064795"))
                        .body("data.attributes.countryISOCode", equalTo("KR"))
                        .body("data.attributes.name", equalTo("Korea Hydro & Nuclear Power Co., Ltd.")).extract()
                        .response();
           Assert.assertFalse(res.asString().contains("isError"));
           Assert.assertFalse(res.asString().contains("isMissing"));
           Assert.assertFalse(res.asString().contains("isRestricted"));

    }
    
    @Test
    
    public void FCA_1207() throws IOException {

           String metafields = baseURI + "/v1/metadata/fields";

           Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                        .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                         .when().get(metafields).then().body("data[0].links.self", Matchers.anything("https://api-"))
                        .extract()
                        .response();
           Assert.assertFalse(res.asString().contains("isError"));
           Assert.assertFalse(res.asString().contains("isMissing"));
           Assert.assertFalse(res.asString().contains("isRestricted"));

    }
@Test        
    public void FCA_1157() throws IOException {

           String entitiesurl = baseURI + "/v1/entities";

           Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                        .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
                         .when().get(entitiesurl).then().body("data[0].type", equalTo("entities"))
                        .body("data[0].id", equalTo("117522"))
                        .extract()
                        .response();
           Assert.assertFalse(res.asString().contains("isError"));
           Assert.assertFalse(res.asString().contains("isMissing"));
           Assert.assertFalse(res.asString().contains("isRestricted"));

    }

@Test

public void FCA_1157paginationtest() throws IOException {

String paginationentityurl = baseURI + "/v1/entities?page[size]=100";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(paginationentityurl).then().body("errors[0].status", equalTo("400"))
           .body("errors[0].title", equalTo("Max page size of 50 exceeded!"))
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

}


@Test

public void FCA_1187case1() throws IOException {

String case1url = baseURI + "/v1/entities?filter[name]=bank";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(case1url).then().body("data[0].attributes.name", equalTo(" Bank ICBC (JSC)"))
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

}

@Test

public void FCA_1187case2() throws IOException {

String case2url = baseURI + "/v1/entities?filter[countryISOCode]=IN";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(case2url).then().body("data[0].attributes.countryISOCode", equalTo("IN"))
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

}

@Test

public void FCA_1187case3() throws IOException {

String case3url = baseURI + "/v1/entities?filter[fitchEntityId]=1455904";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(case3url).then().body("data[0].id", equalTo("1455904"))
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

}

@Test

public void FCA_1187negativecase1() throws IOException {

String negativecase1url = baseURI + "/v1/entities?filter[name]=aa";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(negativecase1url).then().body("errors[0].status", equalTo("400"))
           
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

}
@Test

public void FCA_1244() throws IOException {

String newattributesurl = baseURI + "/v1/statements/5454931?fields[statements]=header";

Response res = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
           .header("accept", acceptValue).header("content", contentValue).contentType("application/vnd.api+json")
           .when().get(newattributesurl).then().body("data.attributes.header.periodDuration", equalTo("12"))
           .body("data.attributes.header.auditorOpinion", equalTo("Audited - Unqualified"))
           .body("data.attributes.header.inflationAdjusted", equalTo(false))
           .extract()
           .response();
Assert.assertFalse(res.asString().contains("isError"));
Assert.assertFalse(res.asString().contains("isMissing"));
Assert.assertFalse(res.asString().contains("isRestricted"));

     }

}
