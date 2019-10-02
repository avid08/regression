package com.apiutils;

import com.configuration.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;
import groovy.json.internal.Charsets;

import java.io.IOException;
import java.net.URL;

import static com.jayway.restassured.RestAssured.given;

public class APIUtils extends Configuration {
    public Response getResponse(String uri){
        return given()
                .header("Authorization", AuthrztionValue)
                .header("X-App-Client-Id", XappClintIDvalue)
                .header("accept", acceptValue)
                .header("Content", contentValue)
                .when().get(uri).then().statusCode(200)
                .extract().response();
    }

    public Response postToDataAggregator(String resourceFileName) throws IOException {

        URL file = Resources.getResource(resourceFileName);
        myjson = Resources.toString(file, Charsets.UTF_8);

        Response response = given()
                .header("Authorization", AuthrztionValue)
                .header("X-App-Client-Id", XappClintIDvalue)
                .contentType("application/vnd.api+json").body(myjson).with()
                .when().post(dataPostUrl).then().assertThat().statusCode(200)
                .extract()
                .response();
        return response;
    }

    public Response postToDataAggregatorBaseUser(String resourceFileName) throws IOException {

        URL file = Resources.getResource(resourceFileName);
        myjson = Resources.toString(file, Charsets.UTF_8);

        Response response = given()
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJodHRwOi8vaWRlbnRpdHktZGF0YS1zZXJ2aWNlOjgwODAvdjIvdXNlcnMvNWNhMjRkMzk2MmNjMzcwMDAxMzJlMjY2Iiwic2NvcGUiOlsidHJ1c3QiXSwiYXRpIjoiOTI3YTcyODEtMzJmNi00ZmYxLTg1N2UtNzAwMTk1MmM2MzkxIiwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5maXRjaGNvbm5lY3QtcWEuY29tIiwiZXhwIjoxNTczODIxOTg4LCJpYXQiOjE1Njg2Mzc5ODgsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI0ZjIxMjQ3ZC1kOTE1LTQ2YTItODZlOC0yZjFlNjk5YWY2ZDYiLCJjbGllbnRfaWQiOiJscXI3bzE0NWhmZ2FyNTNsa242b3FlOGRmYSJ9.iuXBcwghr82ZYhIBq5Br5OJtH0RtswcWgjGnUXfqQ38")
                .header("X-App-Client-Id", XappClintIDvalue)
                .contentType("application/vnd.api+json").body(myjson).with()
                .when().post(dataPostUrl).then().assertThat().statusCode(206)
                .extract()
                .response();
        return response;
    }
}
