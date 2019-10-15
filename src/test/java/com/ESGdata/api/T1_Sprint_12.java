package com.ESGdata.api;

import com.apiutils.APIUtils;
import com.backendutils.Env;
import com.backendutils.MongoUtils;
import com.backendutils.MySqlUtils;
import com.backendutils.PostgresUtils;
import com.configuration.api.Configuration;
import com.configuration.LoggerInitialization;
import com.google.common.io.Resources;
import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.response.Response;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class T1_Sprint_12 extends Configuration {

    Logger logger = LoggerInitialization.setupLogger("T1_Sprint_12");
    APIUtils apiUtils = new APIUtils();
    PostgresUtils postgresUtils = new PostgresUtils();

    private Integer timeoutBetweenTests = 2000;

    private Env.Postgres postgresEnvironment = Env.Postgres.PROD;

    private Object[][] getDataFromPostgres(String sqlFileName, Env.Postgres db, boolean skipColumnNames){
        Connection conn = postgresUtils.connectToPostgreDatabase(db);
        ResultSet rs = postgresUtils.executePostgreScript(conn, Resources.getResource(sqlFileName).getPath());
        Object[][] data = postgresUtils.resultSetToArray(rs, skipColumnNames);
        return data;
    }

    private Object[][] append(Object[][] a, Object[][] b) {
        Object[][] result = new Object[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    @DataProvider(name="Fisc7308")
    public Object[][] getDataFor7308(){
        return getDataFromPostgres("7308.sql", postgresEnvironment, true);
    }

    @Test(dataProvider = "Fisc7308")
    public void Fisc7308_BankScoreCardIntoConfigSchema(Object attributeName, Object resourceType, Object jsonRoot, Object filter){
        ArrayList<String> listOfAttributes = new ArrayList<>();
        listOfAttributes.add("FC_ASST_QUAL_VIAB_RTNG_BNK_NAV");
        listOfAttributes.add("FC_ASST_QUAL_VIAB_BNK_NAV");
        listOfAttributes.add("FC_ASST_QUAL_VIAB_ALERT_BNK_NAV");
        listOfAttributes.add("FC_EARN_PROF_VIAB_RTNG_BNK_NAV");
        listOfAttributes.add("FC_EARN_PROF_VIAB_BNK_NAV");
        listOfAttributes.add("FC_EARN_PROF_VIAB_ALERT_BNK_NAV");
        listOfAttributes.add("FC_CAP_LEV_BNK_NAV");
        listOfAttributes.add("FC_CAP_LEV_VIAB_BNK_NAV");
        listOfAttributes.add("FC_CAP_LEV_VIAB_ALERT_BNK_NAV");
        listOfAttributes.add("FC_FUND_LIQ_BNK_NAV");
        listOfAttributes.add("FC_FUND_LIQ_VIAB_BNK_NAV");
        listOfAttributes.add("FC_FUND_LIQ_VIAB_ALERT_BNK_NAV");

        try {
            Assert.assertTrue(listOfAttributes.contains(attributeName));
            Assert.assertTrue(resourceType.toString().equals("RATINGS_NAVIGATOR_INSTANCE"));
            Assert.assertTrue(jsonRoot.toString().equals("$.RNI.ThisEntity.ThisRatingAction[]"));
            if (attributeName.toString().contains("FC_ASST")){
                Assert.assertTrue(filter.toString().contains("$.Type=\"BANK\"; $$.IDX=\"VR\"; $$.Factors[].IDX=\"FinProfile\"; $$.Factors[].SubFactors[].IDX=\"AssetQual\""));
            }
            else if (attributeName.toString().contains("FC_EARN_PROF")){
                Assert.assertTrue(filter.toString().contains("$.Type=\"BANK\"; $$.IDX=\"VR\"; $$.Factors[].IDX=\"FinProfile\"; $$.Factors[].SubFactors[].IDX=\"Earn&Prof\""));
            }
            else if (attributeName.toString().contains("FC_CAP_LEV")){
                Assert.assertTrue(filter.toString().contains("$.Type=\"BANK\"; $$.IDX=\"VR\"; $$.Factors[].IDX=\"FinProfile\"; $$.Factors[].SubFactors[].IDX=\"Cap&Lev\""));
            }
            else if (attributeName.toString().contains("FC_FUND_LIQ")){
                Assert.assertTrue(filter.toString().contains("$.Type=\"BANK\"; $$.IDX=\"VR\"; $$.Factors[].IDX=\"FinProfile\"; $$.Factors[].SubFactors[].IDX=\"Fund&Liq\""));
            }
            else throw new AssertionError();
            logger.log(Level.INFO, "FISC 7308 PASSED ATTRIBUTE " + attributeName);
        }
        catch (AssertionError err){
            logger.log(Level.WARN, "FISC 7308 FAILED ATTRIBUTE " + attributeName + " ERROR " + err);
            Assert.fail();
        }
    }

    private HashMap<String, String> getDataFromIngestionSchema_7309() {
        Object[][] ingestionData_assetQual = getDataFromPostgres("7309_ingestion_assetqual.sql", postgresEnvironment, true);
        Object[][] ingestionData_capLev = getDataFromPostgres("7309_ingestion_caplev.sql", postgresEnvironment, true);
        Object[][] ingestionData_earnProf = getDataFromPostgres("7309_ingestion_earnprof.sql", postgresEnvironment, true);
        Object[][] ingestionData_fundLiq = getDataFromPostgres("7309_ingestion_fundliq.sql", postgresEnvironment, true);

        Object[][] assetqual_caplev = append(ingestionData_assetQual, ingestionData_capLev);
        Object[][] assetqual_caplev_earnprof = append(assetqual_caplev, ingestionData_earnProf);
        Object[][] ingestionData = append(assetqual_caplev_earnprof, ingestionData_fundLiq);

        HashMap<String, String> mapOfIngestionData = new HashMap<>();

        for (Object[] jsons : ingestionData) {
            for (Object json : jsons) {
                String publishedDates = JsonPath.read(json.toString(), "$.RNI.MetaData.Workflow.Published.PublishDate");
                String fc_asst_qual_viab_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[0].Impact.Value");
                String fc_asst_qual_viab_alert_rtng_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[0].Rating.Alert.Value");
                String fc_asst_qual_viab_rtng_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[0].Rating.Value");

                String fc_earn_prof_viab_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[1].Impact.Value");
                String fc_earn_prof_viab_alert_rtng_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[1].Rating.Alert.Value");
                String fc_earn_prof_viab_rtng_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[1].Rating.Value");

                String fc_cap_lev_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[2].Rating.Value");
                String fc_cap_lev_viab_alert_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[2].Rating.Alert.Value");
                String fc_cap_lev_viab_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[2].Impact.Value");

                String fc_fund_liq_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[3].Rating.Value");
                String fc_fund_liq_viab_alert_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[3].Rating.Alert.Value");
                String fc_fund_liq_viab_bnk_nav = JsonPath.read(json.toString(), "$.RNI.ThisEntity.ThisRatingAction[0].Factors[4].SubFactors[3].Impact.Value");

                mapOfIngestionData.put("PublishedDate", publishedDates);

                mapOfIngestionData.put("FC_ASST_QUAL_VIAB_ALERT_BNK_NAV", fc_asst_qual_viab_alert_rtng_bnk_nav);
                mapOfIngestionData.put("FC_ASST_QUAL_VIAB_BNK_NAV", fc_asst_qual_viab_bnk_nav);
                mapOfIngestionData.put("FC_ASST_QUAL_VIAB_RTNG_BNK_NAV", fc_asst_qual_viab_rtng_bnk_nav);

                mapOfIngestionData.put("FC_EARN_PROF_VIAB_ALERT_BNK_NAV", fc_earn_prof_viab_alert_rtng_bnk_nav);
                mapOfIngestionData.put("FC_EARN_PROF_VIAB_BNK_NAV", fc_earn_prof_viab_bnk_nav);
                mapOfIngestionData.put("FC_EARN_PROF_VIAB_RTNG_BNK_NAV", fc_earn_prof_viab_rtng_bnk_nav);

                mapOfIngestionData.put("FC_CAP_LEV_VIAB_ALERT_BNK_NAV", fc_cap_lev_viab_alert_bnk_nav);
                mapOfIngestionData.put("FC_CAP_LEV_BNK_NAV", fc_cap_lev_bnk_nav);
                mapOfIngestionData.put("FC_CAP_LEV_VIAB_BNK_NAV", fc_cap_lev_viab_bnk_nav);

                mapOfIngestionData.put("FC_FUND_LIQ_VIAB_ALERT_BNK_NAV", fc_fund_liq_viab_alert_bnk_nav);
                mapOfIngestionData.put("FC_FUND_LIQ_BNK_NAV", fc_fund_liq_bnk_nav);
                mapOfIngestionData.put("FC_FUND_LIQ_VIAB_BNK_NAV", fc_fund_liq_viab_bnk_nav);
            }
        }
        return mapOfIngestionData;
    }

    @DataProvider(name="Fisc7309")
    public Object[][] getDataFor7309(){
        Object[][] masterData = getDataFromPostgres("7309_master.sql", postgresEnvironment, true);
        HashMap<String, String> ingestionData = getDataFromIngestionSchema_7309();

        Object[][] allData = new Object[masterData.length][masterData[0].length+2];

        for (int i=0; i<masterData.length; i++){
            System.arraycopy(masterData[i],0,allData[i],0,masterData[i].length);
            allData[i][masterData[i].length+1]=ingestionData;
        }
        return allData;
    }

    @Test(dataProvider = "Fisc7309")
    public void Fisc7309_BankScoreCardIntoMasterSchema(Object entity_id, Object field_id, Object start_date, Object end_date, Object value, Object nullObj, HashMap<String, String> mapOfIngestionData) {
        try {
            Assert.assertTrue(field_id != null);
            Assert.assertEquals(mapOfIngestionData.get(field_id), value);
            Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(mapOfIngestionData.get("PublishedDate")).getDate(),
                                new SimpleDateFormat("yyyy-MM-dd").parse(start_date.toString()).getDate());
            logger.log(Level.INFO, "FISC 7309 PASSED ENTITY ID " + entity_id + " ATTRIBUTE " + field_id);
        }
        catch (AssertionError err){
            logger.log(Level.WARN, "FISC 7309 FAILED ENTITY ID " + entity_id + " ATTRIBUTE " + field_id + " ERROR " + err);
            Assert.fail();
        }
        catch (ParseException ex){
            logger.log(Level.WARN, "FISC 7309 WARNING ENTITY ID " + entity_id + " ATTRIBUTE " + field_id + "COULD NOT PARSE DATE   REASON: " + ex);
            Assert.fail();
        }
    }

    @DataProvider(name="Fisc7311")
    public Object[][] getDataFor7311() throws IOException {
        Response dataAggregatorResponse = apiUtils.postToDataAggregator("7311.json", AuthrztionValue, XappClintIDvalue, dataPostUrl);
        Object[][] postgresPart = getDataFromPostgres("7311.sql", postgresEnvironment, true);
        return new Object[][] {
                {"FC_ASST_QUAL_VIAB_ALERT_BNK_NAV", dataAggregatorResponse, postgresPart[0]},
                {"FC_ASST_QUAL_VIAB_BNK_NAV", dataAggregatorResponse, postgresPart[1]},
                {"FC_ASST_QUAL_VIAB_RTNG_BNK_NAV", dataAggregatorResponse, postgresPart[2]},
                {"FC_CAP_LEV_BNK_NAV", dataAggregatorResponse, postgresPart[3]},
                {"FC_CAP_LEV_VIAB_ALERT_BNK_NAV", dataAggregatorResponse, postgresPart[4]},
                {"FC_CAP_LEV_VIAB_BNK_NAV", dataAggregatorResponse, postgresPart[5]},
                {"FC_EARN_PROF_VIAB_ALERT_BNK_NAV", dataAggregatorResponse, postgresPart[6]},
                {"FC_EARN_PROF_VIAB_BNK_NAV", dataAggregatorResponse, postgresPart[7]},
                {"FC_EARN_PROF_VIAB_RTNG_BNK_NAV", dataAggregatorResponse, postgresPart[8]},
                {"FC_FUND_LIQ_BNK_NAV", dataAggregatorResponse, postgresPart[9]},
                {"FC_FUND_LIQ_VIAB_ALERT_BNK_NAV", dataAggregatorResponse, postgresPart[10]},
                {"FC_FUND_LIQ_VIAB_BNK_NAV", dataAggregatorResponse, postgresPart[11]}
        };
    }

    @Test(dataProvider = "Fisc7311")
    public void Fisc7311_BankScoreCardDataAggregator(String attribute, Response apiResponse, Object[] postgresData) throws IOException {
        String postgresAttribute = postgresData[0].toString();
        String postgresValue = postgresData[1].toString();
        String expectedStringInApiResponse = "\"fitchFieldId\":\"" + attribute + "\",\"type\":\"text\"";
        try {
            Assert.assertTrue(apiResponse.asString().contains(expectedStringInApiResponse));
            Assert.assertEquals(attribute, postgresAttribute);
            Assert.assertTrue(apiResponse.asString().contains("\"fitchFieldId\":\""+ postgresAttribute +"\""));
            Assert.assertTrue(apiResponse.asString().contains("\"value\":[\""+ postgresValue +"\"]"));
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
        String listOfAttributesFromApi = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue).asString();
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
        try {
            Assert.assertTrue(listOfLfiAttributesFromApi.contains(attribute));
            Assert.assertTrue(listOfLfiAttributesFromApi.contains("attributes"));
            Assert.assertTrue(listOfLfiAttributesFromApi.contains("displayName"));
            Assert.assertTrue(listOfLfiAttributesFromApi.contains("fitchFieldDesc"));
            Assert.assertTrue(listOfLfiAttributesFromApi.contains("permissionsRequired"));
            Assert.assertTrue(listOfLfiAttributesFromApi.contains("relationships"));
            Assert.assertTrue(listOfLfiAttributesFromApi.contains("categories"));
            Assert.assertTrue(listOfLfiAttributesFromApi.contains("links"));
            Assert.assertTrue(listOfLfiAttributesFromApi.contains("\"related\":\"" + baseURI + "/v1/metadata/fields/" + attribute + "/categories\""));
            Assert.assertTrue(listOfLfiAttributesFromApi.contains("\"type\":\"Text\""));
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
        String uri = baseURI + "/v1/metadata/fields/" + attribute + "/categories";
        Response res = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        try {
            Assert.assertEquals(res.statusCode(),200);
            Assert.assertTrue(res.asString().contains("data"));
            Assert.assertTrue(res.asString().contains("\"type\":\"categories\""));
            Assert.assertTrue(res.asString().contains("attributes\":{\"name\":"));
            Assert.assertTrue(res.asString().contains("\"links\":{\"self\""));
            Assert.assertTrue(res.asString().contains("relationships"));
            Assert.assertTrue(res.asString().contains("links"));
            Assert.assertTrue(res.asString().contains("fields"));
            Assert.assertTrue(res.asString().contains("children"));
            logger.log(Level.INFO, "FISC 7313 PASSED! RELATIONSHIP LINK OF ATTRIBUTE " + attribute + " IS WORKING");
        }
        catch(AssertionError err){
            logger.log(Level.WARN, "FISC 7313 FAILED! RELATIONSHIP LINK OF ATTRIBUTE " + attribute + " IS NOT WORKING ERROR " + err);
            Assert.fail();
        }
    }
}
