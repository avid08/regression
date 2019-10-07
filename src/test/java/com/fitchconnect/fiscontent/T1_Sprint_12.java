package com.fitchconnect.fiscontent;

import com.apiutils.APIUtils;
import com.backendutils.MongoUtils;
import com.backendutils.MySqlUtils;
import com.backendutils.PostgresUtils;
import com.fitchconnect.api.Configuration;
import com.configuration.LoggerInitialization;
import com.jayway.restassured.response.Response;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class T1_Sprint_12 extends Configuration {

    Logger logger = LoggerInitialization.setupLogger("T1_Sprint_12");
    APIUtils apiUtils = new APIUtils();
    PostgresUtils postgresUtils = new PostgresUtils();
    MongoUtils mongoUtils = new MongoUtils();
    MySqlUtils mySqlUtils = new MySqlUtils();

    private Integer timeoutBetweenTests = 2000;


    @Test
    public void Fisc7308_BankScoreCardIntoConfigSchema(){

    }

    @Test
    public void Fisc7309_BankScoreCardIntoMasterSchema(){

    }

    @DataProvider(name="Fisc7311")
    public Object[][] getDataFor7311() throws IOException {
        String dataAggregatorResponse = apiUtils.postToDataAggregator("7311.json", AuthrztionValue, XappClintIDvalue, dataPostUrl).asString();
        return new Object[][]{
                {"FC_ASST_QUAL_VIAB_RTNG_BNK_NAV", dataAggregatorResponse},
                {"FC_ASST_QUAL_VIAB_BNK_NAV", dataAggregatorResponse},
                {"FC_ASST_QUAL_VIAB_ALERT_BNK_NAV", dataAggregatorResponse},
                {"FC_EARN_PROF_VIAB_RTNG_BNK_NAV", dataAggregatorResponse},
                {"FC_EARN_PROF_VIAB_BNK_NAV", dataAggregatorResponse},
                {"FC_EARN_PROF_VIAB_ALERT_BNK_NAV", dataAggregatorResponse},
                {"FC_CAP_LEV_BNK_NAV", dataAggregatorResponse},
                {"FC_CAP_LEV_VIAB_BNK_NAV", dataAggregatorResponse},
                {"FC_CAP_LEV_VIAB_ALERT_BNK_NAV", dataAggregatorResponse},
                {"FC_FUND_LIQ_BNK_NAV", dataAggregatorResponse},
                {"FC_FUND_LIQ_VIAB_BNK_NAV", dataAggregatorResponse},
                {"FC_FUND_LIQ_VIAB_ALERT_BNK_NAV", dataAggregatorResponse}
        };
    }

    @Test(dataProvider = "Fisc7311")
    public void Fisc7311_BankScoreCardDataAggregator(String attribute, String apiResponse) throws IOException {
        logger.log(Level.INFO, "FISC 7311 CHECKING ATTRIBUTE: " + attribute);
        String expectedStringInApiResponse = "\"fitchFieldId\":\"" + attribute + "\",\"type\":\"numerical\"";
        try {
            Assert.assertTrue(apiResponse.contains(expectedStringInApiResponse));
            logger.log(Level.INFO, "FISC 7311 PASSED ATTRIBUTE " + attribute);
        }
        catch (AssertionError err){
            logger.log(Level.WARN, "FISC 7311 FAILED ATTRIBUTE " + attribute + " ERROR " + err);
            Assert.fail();
        }
    }

    @DataProvider(name="Fisc7313")
    public Object[][] getDataFor7313(){
        String uri = baseURI + "/v1/metadata/fields?filter[source]=ratingsNavigator";
        String listOfAttributesFromApi = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue).path("data.id").toString();
        return new Object[][]{
                {"FC_ASST_QUAL_VIAB_RTNG_BNK_NAV", listOfAttributesFromApi},
                {"FC_ASST_QUAL_VIAB_BNK_NAV", listOfAttributesFromApi},
                {"FC_ASST_QUAL_VIAB_ALERT_BNK_NAV", listOfAttributesFromApi},
                {"FC_EARN_PROF_VIAB_RTNG_BNK_NAV", listOfAttributesFromApi},
                {"FC_EARN_PROF_VIAB_BNK_NAV", listOfAttributesFromApi},
                {"FC_EARN_PROF_VIAB_ALERT_BNK_NAV", listOfAttributesFromApi},
                {"FC_CAP_LEV_BNK_NAV", listOfAttributesFromApi},
                {"FC_CAP_LEV_VIAB_BNK_NAV", listOfAttributesFromApi},
                {"FC_CAP_LEV_VIAB_ALERT_BNK_NAV", listOfAttributesFromApi},
                {"FC_FUND_LIQ_BNK_NAV", listOfAttributesFromApi},
                {"FC_FUND_LIQ_VIAB_BNK_NAV", listOfAttributesFromApi},
                {"FC_FUND_LIQ_VIAB_ALERT_BNK_NAV", listOfAttributesFromApi}
        };
    }

    @Test(dataProvider = "Fisc7313")
    public void Fisc7313_BankScoreCardEnhacementMetadata(String attribute, String listOfLfiAttributesFromApi){
        logger.log(Level.INFO, "FISC 7313 CHECKING ATTRIBUTE: " + attribute);
        try {
            Assert.assertTrue(listOfLfiAttributesFromApi.contains(attribute));
            logger.log(Level.INFO, "FISC 7313 PASSED! ATTRIBUTE " + attribute);
        }
        catch (AssertionError err){
            logger.log(Level.WARN, "FISC 7313 FAILED! ATTRIBUTE " + attribute + " ERROR " + err);
            Assert.fail();
        }
    }

    @Test(dataProvider = "Fisc7313")
    public void Fisc7313_BankScoreCardEnhacementMetadata_CheckCategoriesRelationshipLink(String attribute, String listOfLfiAttributesFromApi) throws InterruptedException {
        Thread.sleep(timeoutBetweenTests);
        logger.log(Level.INFO, "FISC 7313 CHECKING RELATIONSHIP LINK: " + attribute);
        String uri = baseURI + "/v1/metadata/fields/" + attribute + "/categories";
        Response res = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        try {
            Assert.assertEquals(res.statusCode(),200);
            logger.log(Level.WARN, "FISC 7313 PASSED! RELATIONSHIP LINK OF ATTRIBUTE " + attribute + " IS WORKING");
        }
        catch(AssertionError err){
            logger.log(Level.WARN, "FISC 7313 FAILED! RELATIONSHIP LINK OF ATTRIBUTE " + attribute + " IS NOT WORKING ERROR " + err);
            Assert.fail();
        }
    }
}
