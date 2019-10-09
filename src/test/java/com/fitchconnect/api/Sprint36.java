package com.fitchconnect.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.URL;

import com.configuration.api.Configuration;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import groovy.json.internal.Charsets;

public class Sprint36 extends Configuration {

                @Test                
                public void FCA_1363() {
                    String catchild = baseURI + "/v1/metadata/categories/1/children";
                    Response childres = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                                                    .header("accept", acceptValue).header("content", contentValue).when().get(catchild).then()
                                                    .contentType(ContentType.JSON).statusCode(200).extract().response();
                    Assert.assertFalse(childres.asString().contains("isError"));
                    Assert.assertFalse(childres.asString().contains("isMissing"));
                    Assert.assertFalse(childres.asString().contains("isRestricted"));

    }             
 @Test 
                public void FCA_1363relationship() {
                    String rcatchild = baseURI + "/v1/metadata/categories/1/relationships/children";
                    Response rchildres = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                                                    .header("accept", acceptValue).header("content", contentValue).when().get(rcatchild).then()
                                                    .contentType(ContentType.JSON).statusCode(200).extract().response();
                    Assert.assertFalse(rchildres.asString().contains("isError"));
                    Assert.assertFalse(rchildres.asString().contains("isMissing"));
                    Assert.assertFalse(rchildres.asString().contains("isRestricted"));

    }
@Test
public void FCA_1761() {
     String stmturl = baseURI + "/v1/entities/1258790/statements?filter[startPeriod]=Q3:2015&filter[endPeriod]=Annual:2015&filter[consolidation]=con&filter[accountingStandard]=IFRS&fields[statements]=header";
     Response stmtres = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                                     .header("accept", acceptValue).header("content", contentValue).when().get(stmturl).then()
                                     .contentType(ContentType.JSON).statusCode(200).extract().response();
     Assert.assertFalse(stmtres.asString().contains("isError"));
     Assert.assertFalse(stmtres.asString().contains("isMissing"));
     Assert.assertFalse(stmtres.asString().contains("isRestricted"));
     Assert.assertFalse(stmtres.asString().contains("8285187"));

}             
 @Test 
 public void FCA_1797meta() {
     String snpurl = baseURI + "/v1/entities/140090/standardAndPoorIssuerRatings";
     Response snpres = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                                     .header("accept", acceptValue).header("content", contentValue).when().get(snpurl).then()
     .body("meta.count", equalTo(6))
     .contentType(ContentType.JSON).statusCode(200).extract().response();
     Assert.assertFalse(snpres.asString().contains("isError"));
     Assert.assertFalse(snpres.asString().contains("isMissing"));
     Assert.assertFalse(snpres.asString().contains("isRestricted"));
}

 @Test 

public void FCA_1797_datavaluerequest () throws IOException{

       URL dvfile = Resources.getResource("1797.json");
       String datavJson = Resources.toString(dvfile, Charsets.UTF_8);
     
       Response datavres = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                          .contentType("application/vnd.api+json").body(datavJson).with().when().post(dataPostUrl).then()
                          .assertThat().statusCode(200)
                          .body("data.attributes.entities[0].values[0].values[0].value[0]", equalTo("NR"))
                          .extract().response(); 
           Assert.assertFalse(datavres.asString().contains("isError"));
            Assert.assertFalse(datavres.asString().contains("isMissing"));
            Assert.assertFalse(datavres.asString().contains("isRestricted"));
       
        
   }

 @Test(enabled=false)                
 public void FCA_1383() {
     String userurl = baseURI + "/v2/users";
     Response userres = given().header("Authorization", AuthrztionValue).header("X-App-Client-Id", XappClintIDvalue)
                                     .header("accept", acceptValue).header("content", contentValue).when().get(userurl).then()
                                     .contentType(ContentType.JSON).statusCode(200).extract().response();
     Assert.assertFalse(userres.asString().contains("isError"));
     Assert.assertFalse(userres.asString().contains("isMissing"));
     Assert.assertFalse(userres.asString().contains("isRestricted"));
     
 }
}

