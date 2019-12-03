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
import java.util.*;

public class T1_Sprint_15 extends Configuration {

    Logger logger = LoggerInitialization.setupLogger("T1_Sprint_15");

    private Integer timeoutBetweenTests = 2000;

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
            Assert.assertEquals(rowCount, 135);
            logger.info("FISC 6569 PASSED ALL CS LOANS AND LFI BONDS ARE PRESENT IN CONFIGURATION SCHEMA");
        } catch (AssertionError err) {
            logger.error("FISC 6569 FAILED !!! NOT ALL CS LOANS AND LFI BONDS ARE PRESENT IN CONFIGURATION SCHEMA");
            Assert.fail();
        }
    }

    @DataProvider(name="Fisc6569")
    public Object[][] getDataFromPostgres_6569(){
        return postgresUtils.getDataFromPostgres("6569.sql", POSTGRES, true);
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
    public void Fisc6569_deepValidation(){
        Object[][] postgresData = postgresUtils.getDataFromPostgres("6569_deepValidation.sql", POSTGRES, true);
        Object[][] expectedData = new Object[][]{
                {"FC_DEAL_ID","transaction_element","Tranche","Loan public data","FC_DEAL_ID","security_attributes","true","null","Final","capstr"},
                {"FC_PERMID","transaction_element","Tranche","Loan public data","FC_PERM_ID","security_attributes","true","null","Final","capstr"},
                {"FC_MARKET_ISSUANCE","transaction_element","Tranche","Loan public data","FC_MARKET_ISSUANCE","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_ENTITY_NAME","transaction_element","Tranche","Loan public data","FC_COMPANY","security_attributes","true","null","Final","capstr"},
                {"FC_LAUNCH_DATE","transaction","Tranche","Loan public data","FC_LAUNCH_DATE","security_attributes","true","launch_date","Final","capstr"},
                {"FC_CURR","transaction_element","Tranche","Loan public data","FC_CURR","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_AMOUNT","transaction_element","Tranche","Loan public data","FC_ISSUE_AMOUNT","security_attributes","true","null","Final","capstr"},
                {"FC_TRANSACTION_TYPE","transaction","Tranche","Loan public data","FC_TRANSACTION_TYPE","security_attributes","true","purpose","Final","capstr"},
                {"FC_EQUITY_OWNERSHIP","transaction_element","Tranche","Loan public data","FC_EQUITY_OWNERSHIP","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_MARKET_SECTOR_DESC","transaction_element","Tranche","Loan public data","FC_CS_MKT_SECTOR","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_COUNTRY","transaction_element","Tranche","Loan public data","FC_CS_COUNTRY","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUER_ENTITY_NAME","transaction_element","Tranche","Loan public data","FC_CS_ISSUER_NAME","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_AMOUNT_EURO","transaction_element","Tranche","Loan public data","FC_ISSUE_AMOUNT_EURO","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_AMOUNT_USD","transaction_element","Tranche","Loan public data","FC_ISSUE_AMOUNT_USD","security_attributes","true","null","Final","capstr"},
                {"FC_1L_LEV","transaction_element","Tranche","Loan public data","FC_1L_LEV","security_attributes","true","null","Final","capstr"},
                {"FC_SCRD_LEV","transaction_element","Tranche","Loan public data","FC_2L_LEV","security_attributes","true","null","Final","capstr"},
                {"FC_TTL_LEV","transaction_element","Tranche","Loan public data","FC_TTL_LEV","security_attributes","true","null","Final","capstr"},
                {"FC_SPCL_FEATURES","transaction_element","Tranche","Loan public data","FC_SPCL_FEATURES","security_attributes","true","null","Final","capstr"},
                {"FC_MEETING_DT","transaction_element","Tranche","Loan public data","FC_MEETING_DT","security_attributes","true","null","Final","capstr"},
                {"FC_COMMIT_DT","transaction_element","Tranche","Loan public data","FC_COMMIT_DT","security_attributes","true","null","Final","capstr"},
                {"FC_CLOSING_DT","transaction_element","Tranche","Loan public data","FC_CLOSING_DT","security_attributes","true","null","Final","capstr"},
                {"FC_TERM_LOANB","transaction_element","Tranche","Loan public data","FC_TERM_LOANB","security_attributes","true","null","Final","capstr"},
                {"FC_TLB_CCY","transaction_element","Tranche","Loan public data","FC_TLB_CCY","security_attributes","true","null","Final","capstr"},
                {"FC_TLB_ISSUE_AMOUNT","transaction_element","Tranche","Loan public data","FC_TLB_ISSUE_AMOUNT","security_attributes","true","null","Final","capstr"},
                {"FC_TLB_RATE","transaction_element","Tranche","Loan public data","FC_TLB_RATE","security_attributes","true","null","Final","capstr"},
                {"FC_TLB_MARGIN","transaction_element","Tranche","Loan public data","FC_TLB_MARGIN","security_attributes","true","null","Final","capstr"},
                {"FC_TLB_FLOOR","transaction_element","Tranche","Loan public data","FC_TLB_FLOOR","security_attributes","true","null","Final","capstr"},
                {"FC_TLB_OID","transaction_element","Tranche","Loan public data","FC_TLB_OID","security_attributes","true","null","Final","capstr"},
                {"FC_TLB_TERM","transaction_element","Tranche","Loan public data","FC_TLB_TERM","security_attributes","true","null","Final","capstr"},
                {"FC_TLB_PAYMENT","transaction_element","Tranche","Loan public data","FC_TLB_PAYMENT","security_attributes","true","null","Final","capstr"},
                {"FC_TLB_EURO","transaction_element","Tranche","Loan public data","FC_TLB_EURO","security_attributes","true","null","Final","capstr"},
                {"FC_TENOR","transaction_element","Tranche","Loan public data","FC_TENOR","security_attributes","true","null","Final","capstr"},
                {"FC_3MNTH_LIBOR","transaction_element","Tranche","Loan public data","FC_3MNTH_LIBOR","security_attributes","true","null","Final","capstr"},
                {"FC_YTM","transaction_element","Tranche","Loan public data","FC_YTM","security_attributes","true","null","Final","capstr"},
                {"FC_PRICE_TALK","transaction_element","Tranche","Loan public data","FC_PRICE_TALK","security_attributes","true","null","Final","capstr"},
                {"FC_2L","transaction_element","Tranche","Loan public data","FC_2L","security_attributes","true","null","Final","capstr"},
                {"FC_2L_CCY","transaction_element","Tranche","Loan public data","FC_2L_CCY","security_attributes","true","null","Final","capstr"},
                {"FC_2L_ISSUE_AMOUNT","transaction_element","Tranche","Loan public data","FC_2L_ISSUE_AMOUNT","security_attributes","true","null","Final","capstr"},
                {"FC_2L_RATE","transaction_element","Tranche","Loan public data","FC_2L_RATE","security_attributes","true","null","Final","capstr"},
                {"FC_2L_MARGIN","transaction_element","Tranche","Loan public data","FC_2L_MARGIN","security_attributes","true","null","Final","capstr"},
                {"FC_2L_FLOOR","transaction_element","Tranche","Loan public data","FC_2L_FLOOR","security_attributes","true","null","Final","capstr"},
                {"FC_2LOID","transaction_element","Tranche","Loan public data","FC_2LOID","security_attributes","true","null","Final","capstr"},
                {"FC_2L_TERM","transaction_element","Tranche","Loan public data","FC_2L_TERM","security_attributes","true","null","Final","capstr"},
                {"FC_2L_PAYMENT","transaction_element","Tranche","Loan public data","FC_2L_PAYMENT","security_attributes","true","null","Final","capstr"},
                {"FC_2L_EURO","transaction_element","Tranche","Loan public data","FC_2L_EURO","security_attributes","true","null","Final","capstr"},
                {"FC_AGENT_ID","transaction_element","Tranche","Loan public data","FC_AGENT_ID","security_attributes","true","null","Final","capstr"},
                {"FC_CALL_PROTECTION","transaction_element","Tranche","Bond public data","FC_CALL_PROTECTION","security_attributes","true","null","Final","levfin"},
                {"FC_CONCURRENT_LOAN_ID","transaction_element","Tranche","Bond public data","FC_CONCURRENT_LOAN_ID","security_attributes","true","null","Final","levfin"},
                {"FC_INDUSTRY_DESCRIPTION","transaction_element","Tranche","Bond public data","FC_INDUSTRY_DESCRIPTION","security_attributes","true","null","Final","levfin"},
                {"FC_INDUSTRY","transaction_element","Tranche","Bond public data","FC_INDUSTRY","security_attributes","true","null","Final","levfin"},
                {"FC_ISSUER_ENTITY_NAME","transaction_element","Tranche","Bond public data","FC_ISSUER_NAME","security_attributes","true","null","Final","levfin"},
                {"FC_LFYHYID","transaction_element","Tranche","Bond public data","FC_LFYHYID","security_attributes","true","null","Final","levfin"},
                {"FC_MARKET_ISSUANCE","transaction_element","Tranche","Bond public data","FC_MARKETPLACE","security_attributes","true","null","Final","levfin"},
                {"FC_PURPOSE","transaction_element","Tranche","Bond public data","FC_PURPOSE","security_attributes","true","null","Final","levfin"},
                {"FC_SPREAD","transaction_element","Tranche","Bond public data","FC_SPREAD","security_attributes","true","null","Final","levfin"},
                {"FC_UNDERWRITER","transaction_element","Tranche","Bond public data","FC_UNDERWRITER","security_attributes","true","null","Final","levfin"},
                {"FC_1ST_CALL_DT","transaction_element","Tranche","Bond public data","FC_1ST_CALL_DT","security_attributes","true","null","Final","levfin"},
                {"FC_1ST_CALL_PRICE","transaction_element","Tranche","Bond public data","FC_1ST_CALL_PRICE","security_attributes","true","null","Final","levfin"},
                {"FC_LAUNCH_DATE","transaction_element","Tranche","Bond public data","FC_ANNOUNCED_DATE","security_attributes","true","null","Final","levfin"},
                {"FC_BOND_TYPE","transaction_element","Tranche","Bond public data","FC_BOND_TYPE","security_attributes","true","null","Final","levfin"},
                {"FC_CALL_PERCENTAGE","transaction_element","Tranche","Bond public data","FC_CALL_PERCENTAGE","security_attributes","true","null","Final","levfin"},
                {"FC_CALL_PRIM","transaction_element","Tranche","Bond public data","FC_CALL_PROTECTION","security_attributes","true","null","Final","levfin"},
                {"FC_CALL_SCHEDULE","transaction_element","Tranche","Bond public data","FC_CALL_SCHEDULE","security_attributes","true","null","Final","levfin"},
                {"FC_CASH_PIK","transaction_element","Tranche","Bond public data","FC_CASH_PIK_TOGGLE","security_attributes","true","null","Final","levfin"},
                {"FC_CHANGE_OF_CONTROL","transaction_element","Tranche","Bond public data","FC_CHANGE_OF_CONTROL","security_attributes","true","null","Final","levfin"},
                {"FC_CHANGE_ON_BREAK","transaction_element","Tranche","Bond public data","FC_CHANGE_ON_BREAK","security_attributes","true","null","Final","levfin"},
                {"FC_DAYS_IN_MARKET","transaction_element","Tranche","Bond public data","FC_DAYS_IN_MARKET","security_attributes","true","null","Final","levfin"},
                {"FC_DEBUT","transaction_element","Tranche","Bond public data","FC_DEBUT","security_attributes","true","null","Final","levfin"},
                {"FC_DESCRIPTION","transaction_element","Tranche","Bond public data","FC_DESCRIPTION","security_attributes","true","null","Final","levfin"},
                {"FC_FNL_DOC_SCR","transaction_element","Tranche","Bond public data","FC_DOC_SCORE","security_attributes","true","null","Final","levfin"},
                {"FC_EBITDA","transaction_element","Tranche","Bond public data","FC_EBITDA","security_attributes","true","null","Final","levfin"},
                {"FC_EQUITY_PERCENTAGE","transaction_element","Tranche","Bond public data","FC_EQUITY_PERCENTAGE","security_attributes","true","null","Final","levfin"},
                {"FC_EQUITY_CLAW_PERCENTAGE","transaction_element","Tranche","Bond public data","FC_EQUITY_CLAW_PERCENTAGE","security_attributes","true","null","Final","levfin"},
                {"FC_EQUITY_CLAW_PRICE","transaction_element","Tranche","Bond public data","FC_EQUITY_CLAW_PRICE","security_attributes","true","null","Final","levfin"},
                {"FC_EQUITY_CLAW_YEARS","transaction_element","Tranche","Bond public data","FC_EQUITY_CLAW_YEARS","security_attributes","true","null","Final","levfin"},
                {"FC_ISSUE_COUPON_RATE","transaction_element","Tranche","Bond public data","FC_ISSUE_COUPON_RATE_FNL","security_attributes","true","null","Final","levfin"},
                {"FC_ISSUE_PRICE_FNL","transaction_element","Tranche","Bond public data","FC_ISSUE_PRICE_FNL","security_attributes","true","null","Final","levfin"},
                {"FC_MATURITY_DATE","transaction_element","Tranche","Bond public data","FC_MATURITY_DATE_FNL","security_attributes","true","null","Final","levfin"},
                {"FC_ISSUE_AMOUNT","transaction_element","Tranche","Bond public data","FC_ISSUE_AMOUNT_FNL","security_attributes","true","null","Final","levfin"},
                {"FC_FIXED_FLOATING","transaction_element","Tranche","Bond public data","FC_FIXED_FLOATING","security_attributes","true","null","Final","levfin"},
                {"FC_ISSUE_PRICE_PRLM","transaction_element","Tranche","Bond public data","FC_ISSUE_PRICE_PRLM","security_attributes","true","null","Final","levfin"},
                {"FC_SIZE_PRLM","transaction_element","Tranche","Bond public data","FC_SIZE_PRLM","security_attributes","true","null","Final","levfin"},
                {"FC_TALK_PRLM","transaction_element","Tranche","Bond public data","FC_TALK_PRLM","security_attributes","true","null","Final","levfin"},
                {"FC_SCRD_LEV","transaction_element","Tranche","Bond public data","FC_LEVERAGED_AHEAD_NOTES","security_attributes","true","null","Final","levfin"},
                {"FC_TTL_LEV","transaction_element","Tranche","Bond public data","FC_LEVERAGED_NOTES","security_attributes","true","null","Final","levfin"},
                {"FC_NC_YEARS","transaction_element","Tranche","Bond public data","FC_NC_YEARS","security_attributes","true","null","Final","levfin"},
                {"FC_NOTES","transaction_element","Tranche","Bond public data","FC_NOTES","security_attributes","true","null","Final","levfin"},
                {"FC_ISSUE_CUSIP","transaction_element","Tranche","Bond public data","FC_OTHER_ID","security_attributes","true","null","Final","levfin"},
                {"FC_OWNERSHIP","transaction_element","Tranche","Bond public data","FC_OWNERSHIP","security_attributes","true","null","Final","levfin"},
                {"FC_PRICING_DT","transaction_element","Tranche","Bond public data","FC_PRICING_DT","security_attributes","true","null","Final","levfin"},
                {"FC_TRANSACTION_TYPE","transaction_element","Tranche","Bond public data","FC_BOND_PURPOSE","security_attributes","true","null","Final","levfin"},
                {"FC_CMNT_PURPOSE","transaction_element","Tranche","Bond public data","FC_PURPOSE_DESC","security_attributes","true","null","Final","levfin"},
                {"FC_ROAD_SHOW_DATE","transaction_element","Tranche","Bond public data","FC_ROAD_SHOW_DATE","security_attributes","true","null","Final","levfin"},
                {"FC_SECONDARY","transaction_element","Tranche","Bond public data","FC_SECONDARY","security_attributes","true","null","Final","levfin"},
                {"FC_SENIORITY","transaction_element","Tranche","Bond public data","FC_SENIORITY","security_attributes","true","null","Final","levfin"},
                {"FC_SPECIAL_CALL","transaction_element","Tranche","Bond public data","FC_SPECIAL_CALL","security_attributes","true","null","Final","levfin"},
                {"FC_LIBOR_SPREAD","transaction_element","Tranche","Bond public data","FC_SPREAD","security_attributes","true","null","Final","levfin"},
                {"FC_TARGET","transaction_element","Tranche","Bond public data","FC_TARGET","security_attributes","true","null","Final","levfin"},
                {"FC_TENOR","transaction_element","Tranche","Bond public data","FC_TERM","security_attributes","true","null","Final","levfin"},
                {"FC_TRANSACTION","transaction_element","Tranche","Bond public data","FC_TRANSACTION","security_attributes","true","null","Final","levfin"},
                {"FC_YTM","transaction_element","Tranche","Bond public data","FC_YTM","security_attributes","true","null","Final","levfin"},
                {"FC_PERMID","transaction_element","Tranche","Bond public data","FC_PERM_ID","security_attributes","true","null","Final","levfin"},
                {"FC_PERMNAME","transaction_element","Tranche","Bond public data","FC_PERMNAME","security_attributes","true","null","Final","levfin"},
                {"FC_AGENT_ID","transaction_element","Tranche","Bond public data","FC_AGENT_ID","security_attributes","true","null","Final","levfin"},
                {"FC_3MNTH_LIBOR","transaction_element","Tranche","Bond public data","FC_LIBOR_RT","security_attributes","true","null","Final","capstr"},
                {"FC_ADD_ON","transaction_element","Tranche","Bond public data","FC_ADD_ON","security_attributes","true","null","Final","capstr"},
                {"FC_AGENT_ID","transaction_element","Tranche","Bond public data","FC_AGENT_ID","security_attributes","true","null","Final","capstr"},
                {"FC_BONDID","transaction_element","Tranche","Bond public data","FC_BONDID","security_attributes","true","null","Final","capstr"},
                {"FC_CALL_STRUCTURE","transaction_element","Tranche","Bond public data","FC_CALL_STRUCTURE","security_attributes","true","null","Final","capstr"},
                {"FC_CASH_PIK","transaction_element","Tranche","Bond public data","FC_CASH_PIK","security_attributes","true","null","Final","capstr"},
                {"FC_DEAL_TYPE","transaction_element","Tranche","Bond public data","FC_DEAL_TYPE","security_attributes","true","null","Final","capstr"},
                {"FC_EQUITY_CLAW_PERCENTAGE","transaction_element","Tranche","Bond public data","FC_EQUITY_CLAW_PERCENTAGE","security_attributes","true","null","Final","capstr"},
                {"FC_EQUITY_CLAW_PRICE","transaction_element","Tranche","Bond public data","FC_EQUITY_CLAW_PRICE","security_attributes","true","null","Final","capstr"},
                {"FC_EQUITY_CLAW_YEARS","transaction_element","Tranche","Bond public data","FC_EQUITY_CLAW_YEARS","security_attributes","true","null","Final","capstr"},
                {"FC_EXPECTED","transaction_element","Tranche","Bond public data","FC_EXPECTED","security_attributes","true","null","Final","capstr"},
                {"FC_FLOOR","transaction_element","Tranche","Bond public data","FC_FLOOR","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_AMOUNT","transaction_element","Tranche","Bond public data","FC_ISSUE_AMOUNT","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_AMOUNT_EURO","transaction_element","Tranche","Bond public data","FC_EURO_EQV","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_AMOUNT_USD","transaction_element","Tranche","Bond public data","FC_USD_EQV","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_COUPON_RATE","transaction_element","Tranche","Bond public data","FC_ISSUE_COUPON_RATE","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_CURRENCY","transaction_element","Tranche","Bond public data","FC_ISSUE_CURRENCY","security_attributes","true","null","Final","capstr"},
                {"FC_ISSUE_STATUS","transaction_element","Tranche","Bond public data","FC_ISSUE_STATUS","security_attributes","true","null","Final","capstr"},
                {"FC_LAUNCH_DATE","transaction_element","Tranche","Bond public data","FC_LAUNCH_DATE","security_attributes","true","launch_date","Final","capstr"},
                {"FC_MARKET_ISSUANCE","transaction_element","Tranche","Bond public data","FC_MARKET_ISSUANCE","security_attributes","true","null","Final","capstr"},
                {"FC_MATURITY_DATE","transaction_element","Tranche","Bond public data","FC_MATURITY_DATE","security_attributes","true","null","Final","capstr"},
                {"FC_OID_ISSUEPRICE","transaction_element","Tranche","Bond public data","FC_OID_ISSUEPRICE","security_attributes","true","null","Final","capstr"},
                {"FC_PERMID","transaction_element","Tranche","Bond public data","FC_PERMID","security_attributes","true","null","Final","capstr"},
                {"FC_PRICE_TALK","transaction_element","Tranche","Bond public data","FC_PRICE_TALK","security_attributes","true","null","Final","capstr"},
                {"FC_PRICING_DT","transaction_element","Tranche","Bond public data","FC_PRICING_DT","security_attributes","true","null","Final","capstr"},
                {"FC_SETTLEMENT_DATE","transaction_element","Tranche","Bond public data","FC_SETTLEMENT_DATE","security_attributes","true","null","Final","capstr"},
                {"FC_SPCL_FEATURES","transaction_element","Tranche","Bond public data","FC_SPECIAL","security_attributes","true","null","Final","capstr"},
                {"FC_TENOR","transaction_element","Tranche","Bond public data","FC_TENOR","security_attributes","true","null","Final","capstr"},
                {"FC_TRANSACTION_TYPE","transaction","Tranche","Bond public data","FC_TRANSACTION_TYPE","security_attributes","true","purpose","Final","capstr"},
                {"FC_TTL_LEV","transaction_element","Tranche","Bond public data","FC_TOTAL_LEVERAGE","security_attributes","true","null","Final","capstr"},
                {"FC_YTM","transaction_element","Tranche","Bond public data","FC_YTM","security_attributes","true","null","Final","capstr"},
        };

        HashMap<ConfigSchemaKey, ArrayList<Object>> expectedDataMap = new HashMap<ConfigSchemaKey, ArrayList<Object>>();
        HashMap<ConfigSchemaKey, ArrayList<Object>> postgresDataMap = new HashMap<ConfigSchemaKey, ArrayList<Object>>();

        for (Object[] expectedDataRow : expectedData){
            ArrayList<Object> expectedDataList = new ArrayList<Object>();
            for (Object expectedDataItem : expectedDataRow){
                expectedDataList.add(expectedDataItem);
            }
            expectedDataMap.put(new ConfigSchemaKey(expectedDataRow[0], expectedDataRow[3], expectedDataRow[9]), expectedDataList);
        }

        for (Object[] postgresDataRow : postgresData) {
            ArrayList<Object> postgresDataList = new ArrayList<Object>();
            for (Object postgresDataItem : postgresDataRow) {
                postgresDataList.add(postgresDataItem);
            }
            postgresDataMap.put(new ConfigSchemaKey(postgresDataRow[0], postgresDataRow[3], postgresDataRow[9]), postgresDataList);
        }

        Set<ConfigSchemaKey> expectedKeys = expectedDataMap.keySet();

        List<AssertionError> errorsList = new ArrayList<AssertionError>();

        for (ConfigSchemaKey expectedKey : expectedKeys) {
            ArrayList<Object> expectedDataList = expectedDataMap.get(expectedKey);
            ArrayList<Object> postgresDataList = postgresDataMap.get(expectedKey);

            System.out.println(expectedDataList);
            System.out.println(postgresDataList);

            boolean isAttributePassed = expectedDataList.equals(postgresDataList);

            System.out.println(expectedKey + "     " + isAttributePassed);

        }

    }

    @Test
    public void Fisc6570_CSLoans_LFIBonds_MasterSchema(){

    }

    @DataProvider(name="Fisc_7315_FunctionalTest")
    public Object[][] getDataFor7315_FunctionalTest(){
        return new Object[][] {
                {"FC_AVG_YTM_LN1_C","1L Average YTM","1L Average YTM","Calculation for first lien only Yields to maturity","Leveraged Finance"},
                {"FC_COUNT_LN1_C","1L Count","1L Count","Calculation for first lien only (1 for first lien deals only)","Leveraged Finance"},
                {"FC_FLOOR_LN1_C","1L Floor","1L Floor","Calculation for first lien only floors","Leveraged Finance"},
                {"FC_OID_AVG1_C","1L OID Average","1L OID Average","Calculation for first lien only Yields to maturity","Leveraged Finance"},
                {"FC_RATCAT_LN1_C","1L RatCat","1L RatCat","Calculation for first lien only rating category","Leveraged Finance"},
                {"FC_SPRD_AVG_LN1_C","1L Spread Avg","1L Spread Avg","Calculation for first lien only spreads","Leveraged Finance"},
                {"FC_AVG_YTM_LN2_C","2L Average YTM","2L Average YTM","Calculation for second lien only Yields to maturity","Leveraged Finance"},
                {"FC_COUNT_LN2_C","2L Count","2L Count","Calculation for second lien only (1 for second lien deals only)","Leveraged Finance"},
                {"FC_FLOOR_LN2_C","2L Floor","2L Floor","Calculation for second lien only floors","Leveraged Finance"},
                {"FC_OID_AVG2_C","2L OID Average","2L OID Average","Calculation for second lien only Yields to maturity","Leveraged Finance"},
                {"FC_RATCAT_LN2_C","2L RatCat","2L RatCat","Calculation for second lien only rating category","Leveraged Finance"},
                {"FC_SPRD_AVG_LN2_C","2L Spread Avg","2L Spread Avg","Calculation for second lien only spreads","Leveraged Finance"},
                {"FC_B3_C","B3","B3","Calculation to identify B3 rated deals","Leveraged Finance"},
                {"FC_CALL_MONTHS_C","Call Months","Call Months","Calculation for identifying the number of months of call protection","Leveraged Finance"},
                {"FC_SPREAD_CAT_C","Final Spread Cat","Final Spread Cat","calculation for average of high and low spread range (from current Spread)","Leveraged Finance"},
                {"FC_SPRD_TIGHT_C","Final Spread Tight","Final Spread Tight","calculation that seperates a range of spread from Current Spread (lower spread)","Leveraged Finance"},
                {"FC_SPRD_WIDE_C","Final Spread Wide","Final Spread Wide","calculation that seperates a range of spread from Current Spread (higher spread)","Leveraged Finance"},
                {"FC_OID_CAT_C","FinalOID Cat","FinalOID Cat","calculation for average of high and low Issue price range (from current Issue price)","Leveraged Finance"},
                {"FC_OID_TIGHT_C","FinalOID Tight","FinalOID Tight","calculation that seperates a range of Issue price from Current Issue price (lower Issue price)","Leveraged Finance"},
                {"FC_OID_WIDE_C","FinalOID Wide","FinalOID Wide","calculation that seperates a range of Issue price from Current Issue price (higher Issue price)","Leveraged Finance"},
                {"FC_ORG_SPRD_CAT_C","Original Spread cat","Original Spread cat","calculation for average of high and low spread range (from original Spread)","Leveraged Finance"},
                {"FC_ORG_SPRD1_C","Original Spread Tight","Original Spread Tight","calculation that seperates a range of spread from original Spread (lower spread)","Leveraged Finance"},
                {"FC_ORG_SPRD2_C","Original Spread Wide","Original Spread Wide","calculation that seperates a range of spread from original Spread (higher spread)","Leveraged Finance"},
                {"FC_ORG_OID1_C","OriginalOID Tight","OriginalOID Tight","calculation that seperates a range of Issue price from original Issue price (lower Issue price)","Leveraged Finance"},
                {"FC_ORG_OID2_C","OriginalOID Wide","OriginalOID Wide","calculation that seperates a range of Issue price from original Issue price (higher Issue price)","Leveraged Finance"},
                {"FC_ORG_OID_CAT_C","OringalOID cat","OringalOID cat","calculation for average of high and low Issue price range (from original Issue price)","Leveraged Finance"},
                {"FC_FLOOR2_C","Preliminary Calc Floor","Preliminary Calc Floor","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_CALL_PROT_LN2_C","Preliminary Call protection","Preliminary Call protection","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_COVENANT_LN2_C","Preliminary Covenants","Preliminary Covenants","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_FNCL_COV_LN2_C","Preliminary Financial covenants","Preliminary Financial covenants","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_INCRMT_FCTLY2_C","Preliminary Incremental facility","Preliminary Incremental facility","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_ISSUE2_C","Preliminary Issue","Preliminary Issue","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_LEV_TRSN_LN2_C","Preliminary Leverage","Preliminary Leverage","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_OID2_C","Preliminary OID","Preliminary OID","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_OTHER2_C","Preliminary Other","Preliminary Other","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_SPRD2_C","Preliminary Spread","Preliminary Spread","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_TENOR2_C","Preliminary Tenor","Preliminary Tenor","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_YT3_YR2_C","Preliminary YT-3 year","Preliminary YT-3 year","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_YTM2_C","Preliminary YTM","Preliminary YTM","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_PRC_DT_C","Price Date","Price Date","Calculation to provide a date where a transaction has not cleared (max of Launch and flex date)","Leveraged Finance"},
                {"FC_PURPOSE_C","Purpose","Purpose","Calculation for identifying an purpose classification as M&A or not","Leveraged Finance"},
                {"FC_SAVNG_C","Savings","Savings","Calculation to identify the amount of savings for a transaction that is lowering the current coupon","Leveraged Finance"},
                {"FC_SPONSORED_C","Sponsored","Sponsored","Calculation for identifying an ownership classification as sponsored or not","Leveraged Finance"},
                {"FC_STRETCH_C","Stretch","Stretch","Calculation to identify deals with sub debt","Leveraged Finance"},
                {"FC_FLOOR1_C","Updated Calc Floor","Updated Calc Floor","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_CALL_PROT_LN1_C","Updated Call protection","Updated Call protection","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_COVENANT_LN1_C","Updated Covenants","Updated Covenants","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_FNCL_COV_LN1_C","Updated Financial covenants","Updated Financial covenants","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_INCRMT_FCTLY1_C","Updated Incremental facility","Updated Incremental facility","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_ISSUE1_C","Updated Issue","Updated Issue","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_LEV_TRSN_LN1_C","Updated Leverage","Updated Leverage","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_OID1_C","Updated OID","Updated OID","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_OTHER1_C","Updated Other","Updated Other","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_SPRD1_C","Updated Spread","Updated Spread","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_TENOR1_C","Updated Tenor","Updated Tenor","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_YT3_YR1_C","Updated YT-3 year","Updated YT-3 year","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_YTM1_C","Updated YTM","Updated YTM","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_PRVT_PLCD_SLTL","Privately Placed SLTLs","Privately Placed SLTLs","Second lien tranches privately placed in association with the transaction","Leveraged Finance"}
//last line is added which is a pre-existing field just to see if the logic is working
        };
    }

    @Test(dataProvider = "Fisc_7315_FunctionalTest")
    public void Fisc7315_LFIMetadataEnhacements(String fitchFieldId, String displayName, String fitchFieldDesc, String fieldDefinition, String permissionsRequired) throws InterruptedException {
        Thread.sleep(timeoutBetweenTests);
        String uri = baseURI + "/v1/metadata/fields/" + fitchFieldId;
        Response res = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);

        try {
            Assert.assertTrue(res.asString().contains("\"id\":\"" + fitchFieldId + "\""));
            Assert.assertTrue(res.asString().contains("\"displayName\":\"" + displayName + "\""));
            Assert.assertTrue(res.asString().contains("\"fitchFieldDesc\":\"" + fitchFieldDesc + "\""));
            Assert.assertTrue(res.asString().contains("\"fieldDefinition\":\"" + fieldDefinition + "\""));
            Assert.assertTrue(res.asString().contains("\"permissionsRequired\":[\"" + permissionsRequired + "\"]"));
            logger.info("FISC 7315 PASSED! Tested FITCHFIELDID: " + fitchFieldId + " DISPLAYNAME: " + displayName + " FITCHFIELDDESC " + fitchFieldDesc + " FIELDDEFINITION " + fieldDefinition + " PERMISSION " + permissionsRequired);
        }
        catch (AssertionError err){
            logger.warn("FISC 7315 FAILED! Tested FITCHFIELDID: "  + fitchFieldId + " ERROR: " + err);
            Assert.fail();
        }
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
