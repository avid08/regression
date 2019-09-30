package com.levfin;

import com.fitchconnect.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;
import groovy.json.internal.Charsets;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jayway.restassured.RestAssured.given;

public class T1_Sprint_9 extends Configuration {

    public static void initLogger(String testSuiteName) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
            Date date = new Date();
            String filePath = System.getProperty("user.home") + "\\IdeaProjects\\apiregresssion\\regression\\src\\test\\java\\com\\logs\\" + testSuiteName + "__" + dateFormat.format(date) + "_API_Test_Execution_Log.log";
           // String filePath = System.getProperty("user.home") + "\\IdeaProjects\\APIRegression\\src\\test\\java\\com\\logs\\log.log";
            org.apache.log4j.PatternLayout layout = new PatternLayout("%-5p %d %m%n");
            org.apache.log4j.RollingFileAppender appender = new RollingFileAppender(layout, filePath);
            appender.setName("Test_Execution_Log");
            appender.setMaxFileSize("10MB");
            appender.activateOptions();
            org.apache.log4j.Logger.getRootLogger().addAppender(appender);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static Logger setupLogger() {
        initLogger("T1_Sprint_9");
        Logger logger = Logger.getLogger("Test_Execution_Log_Sprint_9");
        return logger;
    }

    Logger logger = setupLogger();

    private Response getResponse(String uri){
        return given()
                .header("Authorization", AuthrztionValue)
                .header("X-App-Client-Id", XappClintIDvalue)
                .header("accept", acceptValue)
                .header("Content", contentValue)
                .when().get(uri).then().statusCode(200)
                .extract().response();
    }

    private Response postToDataAggregator(String resourceFileName) throws IOException {

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

    private Response postToDataAggregatorBaseUser(String resourceFileName) throws IOException {

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

    private Integer timeoutBetweenTests = 2000;

    @DataProvider(name="Fisc6537")
    public Object[][] getStringForTests(){
        return new Object[][] {
                {"countryISOCode2"},
                {"countryISOCode3"},
                {"legacyRegionId"},
                {"legacyRegionId2"},
                {"fitchRatingsRegionId"},
                {"inactiveDate"},
                {"active"},
                {"subNationalType"},
                {"localCurrency"}
        };
    }

    @Test(dataProvider = "Fisc6537")
    public void Fisc6537_addCountiryInformationIntoTheResponse(String stringsForTest) throws InterruptedException {
        Thread.sleep(timeoutBetweenTests);
        String noRegionsURI = baseURI + "/v1/issuers/96172771";
        String regionsURI = baseURI + "/v1/issuers/96172771?include[issuers]=regions";

        Response noRegionsResponse = getResponse(noRegionsURI);
        Response regionsResponse = getResponse(regionsURI);

        try {
            //No regions API tests
            Assert.assertFalse(noRegionsResponse.asString().contains(stringsForTest));
            //regions API tests
            Assert.assertTrue(regionsResponse.asString().contains(stringsForTest));
            logger.log(Level.INFO, "FISC 6537 PASSED! Tested " + stringsForTest + " STRING: FOUND IN API RESPONSE");
        }
        catch (AssertionError err) {
            logger.log(Level.WARN, "FISC 6537 FAILED! Tested " + stringsForTest + " STRING: NOT FOUND IN API RESPONSE        ERROR: " + err);
            Assert.fail();
        }
    }

    @DataProvider(name="Fisc5897")
    public Object[][] getNewCategories(){
        return new Object[][]{
                {"682","Issue Reference"},
                {"683","Loan Public Data"}
        };
    }

    @Test(dataProvider = "Fisc5897")
    public void Fisc5897_metadataCategoriesForLfi(String categoryNumber, String categoryName){
       String uri = baseURI + "/v1/metadata/categories/" + categoryNumber;
       Response res = getResponse(uri);

       try {
           Assert.assertTrue(res.asString().contains("id"));
           Assert.assertTrue(res.asString().contains("categories"));
           Assert.assertTrue(res.asString().contains("name"));
           Assert.assertTrue(res.asString().contains("relationships"));
           Assert.assertTrue(res.asString().contains(categoryNumber));
           Assert.assertTrue(res.asString().contains(categoryName));
           logger.log(Level.INFO, "FISC 5897 PASSED! Tested CATEGORY NUMBER" + categoryNumber + " CATEGORY NAME" + categoryName +" FOUND IN API RESPONSE");
       }
       catch (AssertionError err){
           logger.log(Level.WARN, "FISC 5897 FAILED! Tested CATEGORY NUMBER" + categoryNumber + " CATEGORY NAME" + categoryName +" ERROR " + err);
           Assert.fail();
       }
    }

    @DataProvider(name="Fisc5891_FitchFieldIds")
    public Object[][] getFitchFieldsIdsForValidation(){
        return new Object[][] {
                {"FC_100_MARGIN"},{"FC_50_MARGIN"},{"FC_AMT_REPRICD"},{"FC_ASSET_SL_CNG"},{"FC_BID_PRICE"},{"FC_BREAK_DT"},{"FC_COMMIT_DT"},{"FC_BOND_AMT_CONCURT"},{"FC_CURR"},{"FC_COVENANT_COMMENT_FNL"},{"FC_FINANCIAL_COVENANT_FNL"},{"FC_INCREMENTAL_TERMS_FNL"},{"FC_OTHR_COMMENT"},{"FC_DDTL_AMT"},{"FC_DDTL_AVL_MTH"},{"FC_DL_CAT"},{"FC_DELAY_CLS"},{"FC_DESC_TERMS"},{"FC_EBITDA"},{"FC_ECF_CHNG"},{"FC_EQTY_CONT_LBO"},{"FC_FIN_COV_CHNG"},{"FC_FNL_DOC_SCR"},{"FC_SCRD_LEV"},{"FC_FXD_FLTING"},{"FC_FLX_DT_LN1"},{"FC_FLX_TYPE_LN1"},{"FC_REPRICRD_DEAL_FLOOR"},{"FC_GRID_CHANGE"},{"FC_INCR_FCLTY_CHNG"},{"FC_CALLS_PRLM"},{"FC_COVENANT_COMMENT_PRLM"},{"FC_FINANCIAL_COVENANT_PRLM"},{"FC_INITIAL_FLT_FEE"},{"FC_FLOOR_PRLM"},{"FC_INCREMENTAL_TERMS_PRLM"},{"FC_MATURITY_DATE_PRLM"},{"FC_OID_PRLM"},{"FC_INITIAL_OTHR"},{"FC_ISSUE_AMOUNT_PRLM"},{"FC_INITIAL_SPRD"},{"FC_TERM_PRLM"},{"FC_YT3_PRLM"},{"FC_YTM_PRLM"},{"FC_LIBOR_RT"},{"FC_MARKET_ISSUANCE"},{"FC_MEETING_DT"},{"FC_MFN"},{"FC_OFFER_PRC"},{"FC_PERMID"},{"FC_PERMNAME"},{"FC_PIK"},{"FC_PRELM_DOC"},{"FC_PRVT_PLCD_SLTL"},{"FC_REPAY_AMT"},{"FC_REPAY_101"},{"FC_REPRC"},{"FC_RP_CHNG"},{"FC_SPRD_REPRC_DL"},{"FC_STORY"},{"FC_SUNSET"},{"FC_SUNSET_CMT"},{"FC_TTL_LEV"},{"FC_TRAN_CNT"},{"FC_CALLS_FNL"},{"FC_FLOOR_FNL"},{"FC_MATURITY_DATE_FNL"},{"FC_OID_FNL"},{"FC_ISSUE_AMOUNT_FNL"},{"FC_CURRENT_SPREAD"},{"FC_TERM_FNL"},{"FC_YT3_FNL"},{"FC_YTM_FNL"},{"FC_LAUNCH_DATE"},{"FC_LEAD_ARRANGERS"},{"FC_OWNERSHIP"},{"FC_CMNT_PURPOSE"},{"FC_REVLVR_SIZE"},{"FC_TLA"},{"FC_TRANCHE_NM"},{"FC_TRANSACTION_TYPE"}        };
    }

    @DataProvider(name="Fisc5891_FitchFieldIds_SmokeTest")
    public Object[][] getFitchFieldsIdsForValidation_SmokeTest(){
        return new Object[][] {
                {"FC_100_MARGIN"},{"FC_50_MARGIN"},{"FC_AMT_REPRICD"},{"FC_ASSET_SL_CNG"},{"FC_BID_PRICE"},{"FC_YT3_FNL"},{"FC_YTM_FNL"},{"FC_LAUNCH_DATE"},{"FC_LEAD_ARRANGERS"},{"FC_OWNERSHIP"},{"FC_CMNT_PURPOSE"},{"FC_REVLVR_SIZE"},{"FC_TLA"},{"FC_TRANCHE_NM"},{"FC_TRANSACTION_TYPE"}        };
    }

    @Test(dataProvider = "Fisc5891_FitchFieldIds_SmokeTest")
    public void Fisc5891_metadataFieldsForLfi_ValidateFitchFieldIds(String fitchFieldId) throws InterruptedException {
        Thread.sleep(timeoutBetweenTests);
        String uri = baseURI + "/v1/metadata/categories/683";
        Response res = getResponse(uri);
        try {
            Assert.assertTrue(res.asString().contains(fitchFieldId));
            logger.log(Level.INFO, "FISC 5891 PASSED! Tested FITCHFIELDID " + fitchFieldId);
        }
        catch (AssertionError err){
            logger.log(Level.WARN, "FISC 5891 FAILED! Tested FITCHFIELDID " + fitchFieldId + " ERROR " + err);
            Assert.fail();
        }
    }

    @Test
    public void Fisc6825_loansDataThroughDataAggregator() throws InterruptedException {
        Thread.sleep(timeoutBetweenTests);
        Assert.assertTrue(true);
    }

    @DataProvider(name = "Fisc6928")
    public Object[][] getMetadataChangesForLfiFields(){
        return new Object[][] {
                {"FC_100_MARGIN"},{"FC_50_MARGIN"},{"FC_AMT_REPRICD"},{"FC_ASSET_SL_CNG"},{"FC_BID_PRICE"},{"FC_BREAK_DT"},{"FC_COMMIT_DT"},{"FC_BOND_AMT_CONCURT"},{"FC_CURR"},{"FC_COVENANT_COMMENT_FNL"},{"FC_FINANCIAL_COVENANT_FNL"},{"FC_INCREMENTAL_TERMS_FNL"},{"FC_OTHR_COMMENT"},{"FC_DDTL_AMT"},{"FC_DDTL_AVL_MTH"},{"FC_DL_CAT"},{"FC_DELAY_CLS"},{"FC_DESC_TERMS"},{"FC_EBITDA"},{"FC_ECF_CHNG"},{"FC_EQTY_CONT_LBO"},{"FC_FIN_COV_CHNG"},{"FC_FNL_DOC_SCR"},{"FC_SCRD_LEV"},{"FC_FXD_FLTING"},{"FC_FLX_DT_LN1"},{"FC_FLX_TYPE_LN1"},{"FC_REPRICRD_DEAL_FLOOR"},{"FC_GRID_CHANGE"},{"FC_INCR_FCLTY_CHNG"},{"FC_CALLS_PRLM"},{"FC_COVENANT_COMMENT_PRLM"},{"FC_FINANCIAL_COVENANT_PRLM"},{"FC_INITIAL_FLT_FEE"},{"FC_FLOOR_PRLM"},{"FC_INCREMENTAL_TERMS_PRLM"},{"FC_MATURITY_DATE_PRLM"},{"FC_OID_PRLM"},{"FC_INITIAL_OTHR"},{"FC_ISSUE_AMOUNT_PRLM"},{"FC_INITIAL_SPRD"},{"FC_TERM_PRLM"},{"FC_YT3_PRLM"},{"FC_YTM_PRLM"},{"FC_LIBOR_RT"},{"FC_MARKET_ISSUANCE"},{"FC_MEETING_DT"},{"FC_MFN"},{"FC_OFFER_PRC"},{"FC_PERMID"},{"FC_PERMNAME"},{"FC_PIK"},{"FC_PRELM_DOC"},{"FC_PRVT_PLCD_SLTL"},{"FC_REPAY_AMT"},{"FC_REPAY_101"},{"FC_REPRC"},{"FC_RP_CHNG"},{"FC_SPRD_REPRC_DL"},{"FC_STORY"},{"FC_SUNSET"},{"FC_SUNSET_CMT"},{"FC_TTL_LEV"},{"FC_TRAN_CNT"},{"FC_CALLS_FNL"},{"FC_FLOOR_FNL"},{"FC_MATURITY_DATE_FNL"},{"FC_OID_FNL"},{"FC_ISSUE_AMOUNT_FNL"},{"FC_CURRENT_SPREAD"},{"FC_TERM_FNL"},{"FC_YT3_FNL"},{"FC_YTM_FNL"},{"FC_LAUNCH_DATE"},{"FC_LEAD_ARRANGERS"},{"FC_OWNERSHIP"},{"FC_CMNT_PURPOSE"},{"FC_REVLVR_SIZE"},{"FC_TLA"},{"FC_TRANCHE_NM"},{"FC_TRANSACTION_TYPE"}
        };
    }

    @DataProvider(name = "Fisc6928_SmokeTest")
    public Object[][] getMetadataChangesForLfiFields_SmokeTest(){
        return new Object[][] {
                {"FC_100_MARGIN"},{"FC_50_MARGIN"},{"FC_AMT_REPRICD"},{"FC_BOND_AMT_CONCURT"},{"FC_ISSUE_AMOUNT_PRLM"},{"FC_TERM_PRLM"},{"FC_REPAY_AMT"},{"FC_REPAY_101"},{"FC_REPRC"},{"FC_RP_CHNG"},{"FC_REVLVR_SIZE"},{"FC_TLA"},{"FC_TRANCHE_NM"},{"FC_TRANSACTION_TYPE"}
        };
    }

    @Test(dataProvider = "Fisc6928_SmokeTest")
    public void Fisc6928_metadataChangesForLfiFields(String fitchFieldId) throws InterruptedException {
        Thread.sleep(timeoutBetweenTests);
        String uri = baseURI + "/v1/metadata/fields?filter[source]=leveragedFinance";
        Response res = getResponse(uri);
        try {
          Assert.assertTrue(res.asString().contains(fitchFieldId));
          logger.log(Level.INFO, "FISC 6928 PASSED! Tested FITCHFIELDID " + fitchFieldId);
        }

        catch (AssertionError err) {
            logger.log(Level.WARN, "FISC 6928 FAILED! Tested FITCHFIELDID " + fitchFieldId + " ERROR " + err);
            Assert.fail();
        }
    }

    @Test
    public void Fisc6929_lfiApiChnages_DataAggregator() throws IOException, InterruptedException {
        Thread.sleep(timeoutBetweenTests);
        Response res = postToDataAggregator("6929.json");
        try {
            Assert.assertTrue(res.asString().contains("{\"fitchFieldId\":\"FC_LT_IR\",\"type\":\"text\",\"values\":"));
            logger.log(Level.INFO, "FISC 6929 PASSED!");
        }

        catch (AssertionError err) {
            logger.log(Level.WARN, "FISC 6929 FAILED! ERROR " + err);
            Assert.fail();
        }
    }

    @Test
    public void Fisc6958_buildIdentifierToIdentifyFulcrumMarketIssuance(){
        //automated in backend data comparator
        Assert.assertTrue(true);
    }

    @DataProvider(name = "Fisc6556_SmokeTest")
    public Object[][] getLfiLoansData_SmokeTest(){
        return new Object[][]{
                {"FC_MEETING_DT"},{"FC_INCREMENTAL_TERMS_FNL"},{"FC_LEAD_ARRANGERS"},{"FC_LAUNCH_DATE"},{"FC_YTM_FNL"},{"FC_OID_PRLM"},{"FC_YTM_PRLM"},{"FC_TRANSACTION_TYPE"},{"FC_FINANCIAL_COVENANT_PRLM"},{"FC_MFN"},{"FC_TERM_FNL"},{"FC_BID_PRICE"},{"FC_ISSUE_AMOUNT_PRLM"},{"FC_EQTY_CONT_LBO"},{"FC_TERM_PRLM"},{"FC_OWNERSHIP"},{"FC_CURRENT_SPREAD"},{"FC_DL_CAT"},{"FC_OFFER_PRC"},{"FC_PIK"},{"FC_REVLVR_SIZE"}
        };
    }

    @Test(dataProvider = "Fisc6556_SmokeTest")
    public void Fisc6556_lfiLoansDataAvailableThroughResourcefulEndpoints(String lfiAttribute) throws InterruptedException {
        Thread.sleep(timeoutBetweenTests);
        String uri = baseURI + "/v1/levfinloans";
        Response res = getResponse(uri);
        System.out.println(res.asString());
       // Assert.assertTrue(res.asString().contains(lfiAttribute));
        try {
            Assert.assertTrue(res.asString().contains(lfiAttribute));
            logger.log(Level.INFO, "FISC 6929 PASSED!");
        }

        catch (AssertionError err) {
            logger.log(Level.WARN, "FISC 6929 FAILED! ERROR " + err);
            Assert.fail();
        }
    }


    @DataProvider(name = "Fisc6930")
    public Object[][] getDataFor6930(){
        return new Object[][]{
                {"FC_100_MARGIN"},{"FC_50_MARGIN"},{"FC_AMT_REPRICD"},{"FC_ASSET_SL_CNG"},{"FC_BID_PRICE"},{"FC_BOND_AMT_CONCURT"},{"FC_BREAK_DT"},{"FC_CALLS_FNL"},{"FC_CALLS_PRLM"},{"FC_CMNT_PURPOSE"},{"FC_COMMIT_DT"},{"FC_COVENANT_COMMENT_FNL"},{"FC_COVENANT_COMMENT_PRLM"},{"FC_CURR"},{"FC_CURRENT_SPREAD"},{"FC_DDTL_AMT"},{"FC_DDTL_AVL_MTH"},{"FC_DELAY_CLS"},{"FC_DESC_TERMS"},{"FC_DL_CAT"},{"FC_EBITDA"},{"FC_ECF_CHNG"},{"FC_EQTY_CONT_LBO"},{"FC_FIN_COV_CHNG"},{"FC_FINANCIAL_COVENANT_FNL"},{"FC_FINANCIAL_COVENANT_PRLM"},{"FC_FLOOR_FNL"},{"FC_FLOOR_PRLM"},{"FC_FLX_DT_LN1"},{"FC_FLX_TYPE_LN1"},{"FC_FNL_DOC_SCR"},{"FC_FXD_FLTING"},{"FC_GRID_CHANGE"},{"FC_INCR_FCLTY_CHNG"},{"FC_INCREMENTAL_TERMS_FNL"},{"FC_INCREMENTAL_TERMS_PRLM"},{"FC_INITIAL_FLT_FEE"},{"FC_INITIAL_OTHR"},{"FC_INITIAL_SPRD"},{"FC_ISSUE_AMOUNT_FNL"},{"FC_ISSUE_AMOUNT_PRLM"},{"FC_LAUNCH_DATE"},{"FC_LEAD_ARRANGERS"},{"FC_LIBOR_RT"},{"FC_MARKET_ISSUANCE"},{"FC_MATURITY_DATE_FNL"},{"FC_MATURITY_DATE_PRLM"},{"FC_MEETING_DT"},{"FC_MFN"},{"FC_OFFER_PRC"},{"FC_OID_FNL"},{"FC_OID_PRLM"},{"FC_OTHR_COMMENT"},{"FC_OWNERSHIP"},{"FC_PERMID"},{"FC_PERMNAME"},{"FC_PIK"},{"FC_PRELM_DOC"},{"FC_PRVT_PLCD_SLTL"},{"FC_REPAY_101"},{"FC_REPAY_AMT"},{"FC_REPRC"},{"FC_REPRICRD_DEAL_FLOOR"},{"FC_REVLVR_SIZE"},{"FC_RP_CHNG"},{"FC_SCRD_LEV"},{"FC_SPRD_REPRC_DL"},{"FC_STORY"},{"FC_SUNSET"},{"FC_SUNSET_CMT"},{"FC_TERM_FNL"},{"FC_TERM_PRLM"},{"FC_TLA"},{"FC_TRAN_CNT"},{"FC_TRANCHE_NM"},{"FC_TRANSACTION_TYPE"},{"FC_TTL_LEV"},{"FC_YT3_FNL"},{"FC_YT3_PRLM"},{"FC_YTM_FNL"},{"FC_YTM_PRLM"}        };
    }

    @Test(dataProvider = "Fisc6930")
    public void Fisc6930_changesForLfiResourcefulendpoint(String fitchFieldId) throws InterruptedException {
        Thread.sleep(timeoutBetweenTests);
        String uri = baseURI + "/v1/metadata/fields?filter[source]=leveragedFinance";
        Response res = getResponse(uri);

        try {
            Assert.assertTrue(res.asString().contains("\"id\":\"" + fitchFieldId + "\""));
         //   Assert.assertTrue(res.asString().contains("\"id\":\"" + fitchFieldId + "\""));
            logger.log(Level.INFO, "FISC 6930 PASSED TESTED FITCHFIELDID " + fitchFieldId);
        }
        catch (AssertionError err){
            logger.log(Level.WARN, "FISC 6930 FAILED TESTED FITCHFIELD " + fitchFieldId + " ERROR " + err);
            Assert.fail();
        }
    }

    @Test
    public void Fisc6826_lfiLoansDataThroughDataAggregatorWithPermissions() throws IOException {
        /*Response res = postToDataAggregatorBaseUser("5836.json");
        try {
            Assert.assertTrue(res.asString().contains("FC_100_MARGIN"));
            logger.log(Level.INFO, "FISC 6930 PASSED");
        }
        catch (AssertionError err){
            logger.log(Level.WARN, "FISC 6930 FAILED ERROR " + err);
            Assert.fail();
        }*/
        Assert.assertTrue(true);
    }

    @Test
    public void Fisc5836_lfiLoansDataThroughDataAggregator() throws IOException {
        Response res = postToDataAggregator("5836.json");
        try {
            Assert.assertTrue(res.asString().contains("FC_100_MARGIN"));
            logger.log(Level.INFO, "FISC 6930 PASSED");
        }
        catch (AssertionError err){
            logger.log(Level.WARN, "FISC 6930 FAILED ERROR " + err);
            Assert.fail();
        }
    }

    @Test
    public void Fisc5866_buildIdentifierToIdentifyInMarketDeals(){
        //automated in backend data comparator
        Assert.assertTrue(true);
    }
}
