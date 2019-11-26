package com.fulcrum.api;

import com.apiutils.APIUtils;
import com.backendutils.ArrayUtils;
import com.backendutils.Env;
import com.backendutils.PostgresUtils;
import com.configuration.LoggerInitialization;
import com.configuration.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class T1_Sprint_15 extends Configuration {

    Logger logger = LoggerInitialization.setupLogger("T1_Sprint_15");

    private static String getFullSqlFilePath(String sqlFileName){
        return Resources.getResource(sqlFileName).toString().substring(6);
    }

    PostgresUtils postgresUtils = new PostgresUtils();
    APIUtils apiUtils = new APIUtils();
    ArrayUtils arrayUtils = new ArrayUtils();
    Connection conn = postgresUtils.connectToPostgreDatabase(Env.Postgres.QA);

    @Test
    public void Fisc6569_CSLoans_LFIBonds_ConfigSchema_validateIfAllExist() throws SQLException {
        System.out.println("=====================FISC_6569========================");
        try {
            ResultSet rs = postgresUtils.executePostgreScript(conn, getFullSqlFilePath("6569.sql"));
            int rowCount = postgresUtils.getRowCount(rs);
            System.out.println("THERE ARE " + rowCount + " ATTRBIBUTES PRESENT IN CONFIG SCHEMA");
            Assert.assertEquals(rowCount, 105);
            logger.info("FISC 6569 PASSED ALL CS LOANS AND LFI BONDS ARE PRESENT IN CONFIGURATION SCHEMA");
        } catch (AssertionError err) {
            logger.error("FISC 6569 FAILED !!! NOT ALL CS LOANS AND LFI BONDS ARE PRESENT IN CONFIGURATION SCHEMA");
            Assert.fail();
        }
    }

    @DataProvider(name="Fisc6569")
    public Object[][] getDataFromPostgres_6569(){
        return postgresUtils.getDataFromPostgres("7308.sql", POSTGRES, true);
    }

    @Test(dataProvider = "Fisc6569")
    public void Fisc6569_CSLoans_LFIBonds_ConfigSchema_validateData(Object id, Object attributeName, Object enabled, Object last_updated){
        try {
            Assert.assertTrue(Boolean.parseBoolean(String.valueOf(enabled)));
            Assert.assertNotNull(last_updated);
            logger.info("FISC 6569 PASSED ID " + id + " ATTRBIUTE NAME " + attributeName + " ENABLED " + enabled + " LAST UPDATED " + last_updated);
        }
        catch (AssertionError err) {
            logger.error("FISC 6569 FAILED !!! ID " + id + " ATTRBIUTE NAME " + attributeName + " ENABLED " + enabled + " LAST UPDATED " + last_updated);
            Assert.fail();
        }
    }

    @Test
    public void Fisc6570_CSLoans_LFIBonds_MasterSchema(){

    }

    @Test
    public void Fisc7315_LFIEnhacementsMetadata(){

    }

    @DataProvider(name = "Fisc7316")
    public Object[][] getData_7316() throws IOException {
        Object[][] postgresData = postgresUtils.getDataFromPostgres("7316.sql", POSTGRES, true);
        Object[][] allData = new Object[postgresData.length][postgresData[0].length+2];

        Response apiResponse = apiUtils.postToDataAggregator("7316.json", AuthrztionValue, XappClintIDvalue, dataPostUrl);

        ArrayList<String> values = apiResponse.path("data.attributes.entities[0].values");
        int numberOfValues = values.size();
        HashMap<String, String> apiValues = new HashMap<>();

        for (int i = 0; i < numberOfValues; i++){
            String apiFitchFieldId = apiResponse.path("data.attributes.entities[0].values[" + i + "].fitchFieldId");
            //String apiValue = apiResponse.path("data.attributes.entities[0].values[" + i + "].values[0].value[0]");
            String apiValue = "aaaa";
            apiValues.put(apiFitchFieldId, apiValue);
        }

        for (int j=0; j<postgresData.length; j++){
            System.arraycopy(postgresData[j],0,allData[j],0,postgresData[j].length);
            allData[j][postgresData[j].length+1]=apiValues;
        }
        return allData;
    }

    @Test(dataProvider = "Fisc7316")
    public void Fisc7316_LFIEnhacementsMetadata_DataAggregator(Object postgresFieldId, Object postgresValue, Object nullObj, HashMap<String, String> apiValues){
        System.out.println(postgresFieldId + "    " + postgresValue + "    " + apiValues.get(postgresFieldId));
    }

    @DataProvider(name = "Fisc7317")
    public Object[][] getData_7317(){
        String findAllUri = baseURI+"/v1/securities?filter[sourceName]=LFI&filter[sourceType]=Loans";
        String findOneUri = baseURI+"/v1/securities/122860754";
        Object[][] postgresData = postgresUtils.getDataFromPostgres("7317.sql", POSTGRES, true);
        Response findAllApiData = apiUtils.getResponse(findAllUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response findOneApiData = apiUtils.getResponse(findOneUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);

        Object[][] postgres_findAll_Data = new Object[postgresData.length][postgresData[0].length+2];
        for (int i=0; i<postgresData.length; i++){
            System.arraycopy(postgresData[i],0,postgres_findAll_Data[i],0,postgresData[i].length);
            postgres_findAll_Data[i][postgresData[i].length+1]=findAllApiData;
        }

        Object[][] data = new Object[postgres_findAll_Data.length][postgres_findAll_Data[0].length+2];
        for (int j=0; j<postgres_findAll_Data.length; j++){
            System.arraycopy(postgres_findAll_Data[j],0,data[j],0,postgres_findAll_Data[j].length);
            data[j][postgres_findAll_Data[j].length+1]=findOneApiData;
        }
        return data;
    }

    @Test(dataProvider = "Fisc7317")
    public void Fisc7317_LFILoansFieldsEnhacements_ResourcefulEndpoint(Object postgresFitchFieldId, Object nullObj2, Response findAllApiResponse, Object nullObj3, Response findOneApiResponse){
        System.out.println(postgresFitchFieldId + "       " + findAllApiResponse.asString() + "    " + findOneApiResponse.asString());
    }

}
