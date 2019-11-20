package com.fulcrum.api;

import com.backendutils.Env;
import com.backendutils.PostgresUtils;
import com.configuration.LoggerInitialization;
import com.configuration.api.Configuration;
import com.google.common.io.Resources;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class T1_Sprint_15 extends Configuration {

    Logger logger = LoggerInitialization.setupLogger("T1_Sprint_15");

    private static String getFullSqlFilePath(String sqlFileName){
        return Resources.getResource(sqlFileName).toString().substring(6);
    }

    PostgresUtils postgresUtils = new PostgresUtils();
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
    public Object[][] getDataFromPostgres(){
        ResultSet rs = postgresUtils.executePostgreScript(conn, getFullSqlFilePath("6569.sql"));
        Object[][] postgresData = postgresUtils.resultSetToArray(rs, true);
        return postgresData;
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

    @Test
    public void Fisc7316_LFIEnhacementsMetadata_DataAggregator(){

    }

    @Test
    public void Fisc7317_LFILoansFieldsEnhacements_ResourcefulEndpoint(){

    }

}
