package com.fulcrum.api;

import com.apiutils.APIUtils;
import com.backendutils.ArrayUtils;
import com.backendutils.Env;
import com.backendutils.MongoUtils;
import com.backendutils.PostgresUtils;
import com.configuration.LoggerInitialization;
import com.configuration.api.Configuration;
import com.google.common.io.Resources;
import com.jayway.restassured.response.Response;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.backendutils.Env.MySQL.QA;

public class T1_Sprint_15 extends Configuration {

    Logger logger = LoggerInitialization.setupLogger("T1_Sprint_15");

    private Integer timeoutBetweenTests = 2000;

    private static String getFullSqlFilePath(String sqlFileName) {
        return Resources.getResource(sqlFileName).toString().substring(6);
    }

    PostgresUtils postgresUtils = new PostgresUtils();
    APIUtils apiUtils = new APIUtils();
    ArrayUtils arrayUtils = new ArrayUtils();
    FulcrumUtils fulcrumUtils = new FulcrumUtils();
    MongoUtils mongoUtils = new MongoUtils();
    Connection conn = postgresUtils.connectToPostgreDatabase(Env.Postgres.PROD);

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

    @DataProvider(name = "Fisc6569")
    public Object[][] getDataFromPostgres_6569() {
        return postgresUtils.getDataFromPostgres("6569.sql", POSTGRES, true);
    }

    @Test(dataProvider = "Fisc6569")
    public void Fisc6569_CSLoans_LFIBonds_ConfigSchema_validateData(Object id, Object attributeName, Object enabled, Object last_updated) {
        try {
            Assert.assertTrue(Boolean.parseBoolean(String.valueOf(enabled)));
            Assert.assertNotNull(last_updated);
            logger.info("FISC 6569 PASSED ID " + id + " ATTRBIUTE NAME " + attributeName + " ENABLED " + enabled + " LAST UPDATED " + last_updated);
        } catch (AssertionError err) {
            logger.error("FISC 6569 FAILED !!! ID " + id + " ATTRBIUTE NAME " + attributeName + " ENABLED " + enabled + " LAST UPDATED " + last_updated);
            Assert.fail();
        }
    }

    @Test
    public void Fisc6569_deepValidation() {
        Object[][] postgresData = postgresUtils.getDataFromPostgres("6569_deepValidation.sql", POSTGRES, true);
        Object[][] expectedData = new Object[][]{
                {"FC_DEAL_ID", "transaction_element", "Tranche", "Loan public data", "FC_DEAL_ID", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_PERMID", "transaction_element", "Tranche", "Loan public data", "FC_PERM_ID", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_MARKET_ISSUANCE", "transaction_element", "Tranche", "Loan public data", "FC_MARKET_ISSUANCE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_ENTITY_NAME", "transaction_element", "Tranche", "Loan public data", "FC_COMPANY", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_LAUNCH_DATE", "transaction", "Tranche", "Loan public data", "FC_LAUNCH_DATE", "security_attributes", "true", "launch_date", "null", "capstr"},
                {"FC_CURR", "transaction_element", "Tranche", "Loan public data", "FC_CURR", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_AMOUNT", "transaction_element", "Tranche", "Loan public data", "FC_ISSUE_AMOUNT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TRANSACTION_TYPE", "transaction", "Tranche", "Loan public data", "FC_TRANSACTION_TYPE", "security_attributes", "true", "purpose", "null", "capstr"},
                {"FC_EQUITY_OWNERSHIP", "transaction_element", "Tranche", "Loan public data", "FC_EQUITY_OWNERSHIP", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_MARKET_SECTOR_DESC", "transaction_element", "Tranche", "Loan public data", "FC_CS_MKT_SECTOR", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_COUNTRY", "transaction_element", "Tranche", "Loan public data", "FC_CS_COUNTRY", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUER_ENTITY_NAME", "transaction_element", "Tranche", "Loan public data", "FC_CS_ISSUER_NAME", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_AMOUNT_EURO", "transaction_element", "Tranche", "Loan public data", "FC_ISSUE_AMOUNT_EURO", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_AMOUNT_USD", "transaction_element", "Tranche", "Loan public data", "FC_ISSUE_AMOUNT_USD", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_1L_LEV", "transaction_element", "Tranche", "Loan public data", "FC_1L_LEV", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2SCRD_LEV", "transaction_element", "Tranche", "Loan public data", "FC_2L_LEV", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TTL_LEV", "transaction_element", "Tranche", "Loan public data", "FC_TTL_LEV", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_SPCL_FEATURES", "transaction_element", "Tranche", "Loan public data", "FC_SPCL_FEATURES", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_MEETING_DT", "transaction_element", "Tranche", "Loan public data", "FC_MEETING_DT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_COMMIT_DT", "transaction_element", "Tranche", "Loan public data", "FC_COMMIT_DT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_CLOSING_DT", "transaction_element", "Tranche", "Loan public data", "FC_CLOSING_DT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TERM_LOANB", "transaction_element", "Tranche", "Loan public data", "FC_TERM_LOANB", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TLB_CCY", "transaction_element", "Tranche", "Loan public data", "FC_TLB_CCY", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TLB_ISSUE_AMOUNT", "transaction_element", "Tranche", "Loan public data", "FC_TLB_ISSUE_AMOUNT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TLB_RATE", "transaction_element", "Tranche", "Loan public data", "FC_TLB_RATE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TLB_MARGIN", "transaction_element", "Tranche", "Loan public data", "FC_TLB_MARGIN", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TLB_FLOOR", "transaction_element", "Tranche", "Loan public data", "FC_TLB_FLOOR", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TLB_OID", "transaction_element", "Tranche", "Loan public data", "FC_TLB_OID", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TLB_TERM", "transaction_element", "Tranche", "Loan public data", "FC_TLB_TERM", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TLB_PAYMENT", "transaction_element", "Tranche", "Loan public data", "FC_TLB_PAYMENT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TLB_EURO", "transaction_element", "Tranche", "Loan public data", "FC_TLB_EURO", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TENOR", "transaction_element", "Tranche", "Loan public data", "FC_TENOR", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_3MNTH_LIBOR", "transaction_element", "Tranche", "Loan public data", "FC_3MNTH_LIBOR", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_YTM", "transaction_element", "Tranche", "Loan public data", "FC_YTM", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_PRICE_TALK", "transaction_element", "Tranche", "Loan public data", "FC_PRICE_TALK", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2L", "transaction_element", "Tranche", "Loan public data", "FC_2L", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2L_CCY", "transaction_element", "Tranche", "Loan public data", "FC_2L_CCY", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2L_ISSUE_AMOUNT", "transaction_element", "Tranche", "Loan public data", "FC_2L_ISSUE_AMOUNT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2L_RATE", "transaction_element", "Tranche", "Loan public data", "FC_2L_RATE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2L_MARGIN", "transaction_element", "Tranche", "Loan public data", "FC_2L_MARGIN", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2L_FLOOR", "transaction_element", "Tranche", "Loan public data", "FC_2L_FLOOR", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2LOID", "transaction_element", "Tranche", "Loan public data", "FC_2LOID", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2L_TERM", "transaction_element", "Tranche", "Loan public data", "FC_2L_TERM", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2L_PAYMENT", "transaction_element", "Tranche", "Loan public data", "FC_2L_PAYMENT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_2L_EURO", "transaction_element", "Tranche", "Loan public data", "FC_2L_EURO", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_AGENT_ID", "transaction_element", "Tranche", "Loan public data", "FC_AGENT_ID", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_CALL_PROTECTION", "transaction_element", "Tranche", "Bond public data", "FC_CALL_PROTECTION", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_CONCURRENT_LOAN_ID", "transaction_element", "Tranche", "Bond public data", "FC_CONCURRENT_LOAN_ID", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_INDUSTRY_DESCRIPTION", "transaction_element", "Tranche", "Bond public data", "FC_INDUSTRY_DESCRIPTION", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_INDUSTRY", "transaction_element", "Tranche", "Bond public data", "FC_INDUSTRY", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_ISSUER_ENTITY_NAME", "transaction_element", "Tranche", "Bond public data", "FC_ISSUER_NAME", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_LFYHYID", "transaction_element", "Tranche", "Bond public data", "FC_LFYHYID", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_MARKET_ISSUANCE", "transaction_element", "Tranche", "Bond public data", "FC_MARKETPLACE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_PURPOSE", "transaction_element", "Tranche", "Bond public data", "FC_PURPOSE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_SPREAD", "transaction_element", "Tranche", "Bond public data", "FC_SPREAD", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_UNDERWRITER", "transaction_element", "Tranche", "Bond public data", "FC_UNDERWRITER", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_1ST_CALL_DT", "transaction_element", "Tranche", "Bond public data", "FC_1ST_CALL_DT", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_1ST_CALL_PRICE", "transaction_element", "Tranche", "Bond public data", "FC_1ST_CALL_PRICE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_LAUNCH_DATE", "transaction_element", "Tranche", "Bond public data", "FC_ANNOUNCED_DATE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_BOND_TYPE", "transaction_element", "Tranche", "Bond public data", "FC_BOND_TYPE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_CALL_PERCENTAGE", "transaction_element", "Tranche", "Bond public data", "FC_CALL_PERCENTAGE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_CALL_PRIM", "transaction_element", "Tranche", "Bond public data", "FC_CALL_PROTECTION", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_CALL_SCHEDULE", "transaction_element", "Tranche", "Bond public data", "FC_CALL_SCHEDULE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_CASH_PIK", "transaction_element", "Tranche", "Bond public data", "FC_CASH_PIK_TOGGLE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_CHANGE_OF_CONTROL", "transaction_element", "Tranche", "Bond public data", "FC_CHANGE_OF_CONTROL", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_CHANGE_ON_BREAK", "transaction_element", "Tranche", "Bond public data", "FC_CHANGE_ON_BREAK", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_DAYS_IN_MARKET", "transaction_element", "Tranche", "Bond public data", "FC_DAYS_IN_MARKET", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_DEBUT", "transaction_element", "Tranche", "Bond public data", "FC_DEBUT", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_DESCRIPTION", "transaction_element", "Tranche", "Bond public data", "FC_DESCRIPTION", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_FNL_DOC_SCR", "transaction_element", "Tranche", "Bond public data", "FC_DOC_SCORE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_EBITDA", "transaction_element", "Tranche", "Bond public data", "FC_EBITDA", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_EQUITY_PERCENTAGE", "transaction_element", "Tranche", "Bond public data", "FC_EQUITY_PERCENTAGE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_EQUITY_CLAW_PERCENTAGE", "transaction_element", "Tranche", "Bond public data", "FC_EQUITY_CLAW_PERCENTAGE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_EQUITY_CLAW_PRICE", "transaction_element", "Tranche", "Bond public data", "FC_EQUITY_CLAW_PRICE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_EQUITY_CLAW_YEARS", "transaction_element", "Tranche", "Bond public data", "FC_EQUITY_CLAW_YEARS", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_ISSUE_COUPON_RATE", "transaction_element", "Tranche", "Bond public data", "FC_ISSUE_COUPON_RATE_FNL", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_ISSUE_PRICE_FNL", "transaction_element", "Tranche", "Bond public data", "FC_ISSUE_PRICE_FNL", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_MATURITY_DATE", "transaction_element", "Tranche", "Bond public data", "FC_MATURITY_DATE_FNL", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_ISSUE_AMOUNT", "transaction_element", "Tranche", "Bond public data", "FC_ISSUE_AMOUNT_FNL", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_FIXED_FLOATING", "transaction_element", "Tranche", "Bond public data", "FC_FIXED_FLOATING", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_ISSUE_PRICE_PRLM", "transaction_element", "Tranche", "Bond public data", "FC_ISSUE_PRICE_PRLM", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_SIZE_PRLM", "transaction_element", "Tranche", "Bond public data", "FC_SIZE_PRLM", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_TALK_PRLM", "transaction_element", "Tranche", "Bond public data", "FC_TALK_PRLM", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_SCRD_LEV", "transaction_element", "Tranche", "Bond public data", "FC_LEVERAGED_AHEAD_NOTES", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_TTL_LEV", "transaction_element", "Tranche", "Bond public data", "FC_LEVERAGED_NOTES", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_NC_YEARS", "transaction_element", "Tranche", "Bond public data", "FC_NC_YEARS", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_NOTES", "transaction_element", "Tranche", "Bond public data", "FC_NOTES", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_ISSUE_CUSIP", "transaction_element", "Tranche", "Bond public data", "FC_OTHER_ID", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_OWNERSHIP", "transaction_element", "Tranche", "Bond public data", "FC_OWNERSHIP", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_PRICING_DT", "transaction_element", "Tranche", "Bond public data", "FC_PRICING_DT", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_TRANSACTION_TYPE", "transaction_element", "Tranche", "Bond public data", "FC_BOND_PURPOSE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_CMNT_PURPOSE", "transaction_element", "Tranche", "Bond public data", "FC_PURPOSE_DESC", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_ROAD_SHOW_DATE", "transaction_element", "Tranche", "Bond public data", "FC_ROAD_SHOW_DATE", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_SECONDARY", "transaction_element", "Tranche", "Bond public data", "FC_SECONDARY", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_SENIORITY", "transaction_element", "Tranche", "Bond public data", "FC_SENIORITY", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_SPECIAL_CALL", "transaction_element", "Tranche", "Bond public data", "FC_SPECIAL_CALL", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_LIBOR_SPREAD", "transaction_element", "Tranche", "Bond public data", "FC_SPREAD", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_TARGET", "transaction_element", "Tranche", "Bond public data", "FC_TARGET", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_TENOR", "transaction_element", "Tranche", "Bond public data", "FC_TERM", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_TRANSACTION", "transaction_element", "Tranche", "Bond public data", "FC_TRANSACTION", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_YTM", "transaction_element", "Tranche", "Bond public data", "FC_YTM", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_PERMID", "transaction_element", "Tranche", "Bond public data", "FC_PERM_ID", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_PERMNAME", "transaction_element", "Tranche", "Bond public data", "FC_PERMNAME", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_AGENT_ID", "transaction_element", "Tranche", "Bond public data", "FC_AGENT_ID", "security_attributes", "true", "null", "null", "levfin"},
                {"FC_3MNTH_LIBOR", "transaction_element", "Tranche", "Bond public data", "FC_LIBOR_RT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ADD_ON", "transaction_element", "Tranche", "Bond public data", "FC_ADD_ON", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_AGENT_ID", "transaction_element", "Tranche", "Bond public data", "FC_AGENT_ID", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_BONDID", "transaction_element", "Tranche", "Bond public data", "FC_BONDID", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_CALL_STRUCTURE", "transaction_element", "Tranche", "Bond public data", "FC_CALL_STRUCTURE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_CASH_PIK", "transaction_element", "Tranche", "Bond public data", "FC_CASH_PIK", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_DEAL_TYPE", "transaction_element", "Tranche", "Bond public data", "FC_DEAL_TYPE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_EQUITY_CLAW_PERCENTAGE", "transaction_element", "Tranche", "Bond public data", "FC_EQUITY_CLAW_PERCENTAGE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_EQUITY_CLAW_PRICE", "transaction_element", "Tranche", "Bond public data", "FC_EQUITY_CLAW_PRICE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_EQUITY_CLAW_YEARS", "transaction_element", "Tranche", "Bond public data", "FC_EQUITY_CLAW_YEARS", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_EXPECTED", "transaction_element", "Tranche", "Bond public data", "FC_EXPECTED", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_FLOOR", "transaction_element", "Tranche", "Bond public data", "FC_FLOOR", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_AMOUNT", "transaction_element", "Tranche", "Bond public data", "FC_ISSUE_AMOUNT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_AMOUNT_EURO", "transaction_element", "Tranche", "Bond public data", "FC_EURO_EQV", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_AMOUNT_USD", "transaction_element", "Tranche", "Bond public data", "FC_USD_EQV", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_COUPON_RATE", "transaction_element", "Tranche", "Bond public data", "FC_ISSUE_COUPON_RATE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_CURRENCY", "transaction_element", "Tranche", "Bond public data", "FC_ISSUE_CURRENCY", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_ISSUE_STATUS", "transaction_element", "Tranche", "Bond public data", "FC_ISSUE_STATUS", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_LAUNCH_DATE", "transaction", "Tranche", "Bond public data", "FC_LAUNCH_DATE", "security_attributes", "true", "launch_date", "null", "capstr"},
                {"FC_MARKET_ISSUANCE", "transaction_element", "Tranche", "Bond public data", "FC_MARKET_ISSUANCE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_MATURITY_DATE", "transaction_element", "Tranche", "Bond public data", "FC_MATURITY_DATE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_OID_ISSUEPRICE", "transaction_element", "Tranche", "Bond public data", "FC_OID_ISSUEPRICE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_PERMID", "transaction_element", "Tranche", "Bond public data", "FC_PERMID", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_PRICE_TALK", "transaction_element", "Tranche", "Bond public data", "FC_PRICE_TALK", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_PRICING_DT", "transaction_element", "Tranche", "Bond public data", "FC_PRICING_DT", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_SETTLEMENT_DATE", "transaction_element", "Tranche", "Bond public data", "FC_SETTLEMENT_DATE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_SPCL_FEATURES", "transaction_element", "Tranche", "Bond public data", "FC_SPECIAL", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TENOR", "transaction_element", "Tranche", "Bond public data", "FC_TENOR", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_TRANSACTION_TYPE", "transaction", "Tranche", "Bond public data", "FC_TRANSACTION_TYPE", "security_attributes", "true", "purpose", "null", "capstr"},
                {"FC_TTL_LEV", "transaction_element", "Tranche", "Bond public data", "FC_TOTAL_LEVERAGE", "security_attributes", "true", "null", "null", "capstr"},
                {"FC_YTM", "transaction_element", "Tranche", "Bond public data", "FC_YTM", "security_attributes", "true", "null", "null", "capstr"},
        };

        HashMap<ConfigSchemaKey, ArrayList<Object>> expectedDataMap = new HashMap<ConfigSchemaKey, ArrayList<Object>>();
        HashMap<ConfigSchemaKey, ArrayList<Object>> postgresDataMap = new HashMap<ConfigSchemaKey, ArrayList<Object>>();

        for (Object[] expectedDataRow : expectedData) {
            ArrayList<Object> expectedDataList = new ArrayList<Object>();
            for (Object expectedDataItem : expectedDataRow) {
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
            boolean isAttributePassed = arrayUtils.areListsOfObjectsEqual(expectedDataList, postgresDataList);
            try {
                Assert.assertTrue(isAttributePassed);
                logger.info("FISC 6569 PASSED " + expectedKey.getAttributeName() + "       " + expectedKey.getInformationProvenance() + "      " + expectedKey.getSourceCategory());
            }
            catch (AssertionError err) {
                errorsList.add(err);
                logger.error("FISC 6569 FAILED " + expectedKey.getAttributeName() + "       " + expectedKey.getInformationProvenance() + "      " + expectedKey.getSourceCategory() + "     " + err);
                System.out.println("FISC 6569 FAILED " + expectedKey.getAttributeName() + "       " + expectedKey.getInformationProvenance() + "      " + expectedKey.getSourceCategory() + "     " + err);
                continue;
            }
        }
        Assert.assertEquals(errorsList.size(), 0);
    }

    @Test(enabled = false)
    public void Fisc6570_LFIBonds_MasterSchema() throws SQLException {
        HashMap<LFIBondsKey, ArrayList<Object>> lfiBondsMySqlMap = fulcrumUtils.getLfiBondsMySqlMap_customQuery(QA, "LFIBonds_MySQL.sql", 0, 1);
        HashMap<LFIBondsKey, ArrayList<Object>> lfiBondsPostgresMap = fulcrumUtils.getLfiBondsPostgresMap_customQuery( "LFIBonds_Postgres.sql", 0, 1);

        Set<LFIBondsKey> lfiBondsKeySet = lfiBondsMySqlMap.keySet();
        Set<LFIBondsKey> lfiBondsKeySetPostgres = lfiBondsPostgresMap.keySet();

        System.out.println(lfiBondsMySqlMap);
        System.out.println(lfiBondsPostgresMap);

        System.out.println("\n\n\n");

        for (LFIBondsKey key : lfiBondsKeySet) {
            System.out.println("MYSQL KEY " + key.getAgentId() + "        " + key.getLfyHyId());
        }

        System.out.println("\n\n\n");

        for (LFIBondsKey key : lfiBondsKeySetPostgres) {
            System.out.println("POSTGRES KEY " + key.getAgentId() + "        " + key.getLfyHyId());
        }



        List<AssertionError> errorsList = new ArrayList<AssertionError>();

        int i = 0;

        for (LFIBondsKey lfiBondsKey : lfiBondsKeySet) {
            ArrayList<Object> lfiBondsMySqlList = lfiBondsMySqlMap.get(lfiBondsKey);
            ArrayList<Object> lfiBondsPostrgesList = lfiBondsPostgresMap.get(lfiBondsKey);
            boolean isRowPassed = arrayUtils.areListsOfObjectsEqual(lfiBondsMySqlList, lfiBondsPostrgesList);
            try {
                Assert.assertTrue(isRowPassed);
                //System.out.println(i + "        LFIBONDS PASSED      AGENT ID  " + lfiBondsKey.getAgentId() + "         LFY HY ID  " + lfiBondsKey.getLfyHyId());
                i++;
                logger.info("LFIBONDS PASSED " + lfiBondsKey.getAgentId() + "       " + lfiBondsKey.getLfyHyId());
            }
            catch (AssertionError err) {
                errorsList.add(err);
                logger.error("LFIBONDS FAILED   AGENT ID " + lfiBondsKey.getAgentId() + "      LFY HY ID  " + lfiBondsKey.getLfyHyId() + "      " + err);
                System.out.println("LFIBONDS FAILED   AGENT ID " + lfiBondsKey.getAgentId() + "      LFY HY ID  " + lfiBondsKey.getLfyHyId() + "      " + err);
                continue;
            }
        }
        Assert.assertEquals(errorsList.size(), 0);
    }

    HashMap<CSBondsKey, ArrayList<Object>> csBondsMySqlMap = null;
    HashMap<CSBondsKey, ArrayList<Object>> csBondsPostgresMap = null;

    @Test(enabled = false)
    public void Fisc6570_CSBonds_MasterSchema() throws SQLException {

        HashMap<CSBondsKey, ArrayList<Object>> csBondsMySqlMap = fulcrumUtils.getCsBondsMySqlMap_customQuery(QA, "6570_CSBonds_MySQL.sql", 0, 2);
        HashMap<CSBondsKey, ArrayList<Object>> csBondsPostgresMap = fulcrumUtils.getCsBondsPostgresMap_customQuery( "6570_CSBonds_Postgres.sql", 0, 2);

        Set<CSBondsKey> csBondsKeySet = csBondsMySqlMap.keySet();
        List<AssertionError> errorsList = new ArrayList<AssertionError>();

        int i = 0;

        for (CSBondsKey csBondsKey : csBondsKeySet) {
            ArrayList<Object> csBondsMySqlList = csBondsMySqlMap.get(csBondsKey);
            ArrayList<Object> csBondsPostgresList = csBondsPostgresMap.get(csBondsKey);
            boolean isRowPassed = arrayUtils.areListsOfObjectsEqual(csBondsMySqlList, csBondsPostgresList);
            try {
                Assert.assertTrue(isRowPassed);
                //System.out.println(i + "        CSBONDS PASSED      AGENT ID  " + lfiBondsKey.getAgentId() + "         LFY HY ID  " + lfiBondsKey.getLfyHyId());
                i++;
                logger.info("CSBONDS PASSED " + csBondsKey.getAgentId() + "       " + csBondsKey.getBondId());
            }
            catch (AssertionError err) {
                errorsList.add(err);
                logger.error("CSBONDS FAILED   AGENT ID " + csBondsKey.getAgentId() + "      BOND ID  " + csBondsKey.getBondId() + "      " + err);
                System.out.println("CSBONDS FAILED   AGENT ID " + csBondsKey.getAgentId() + "      BOND ID  " + csBondsKey.getBondId() + "      " + err);
                continue;
            }
        }
        Assert.assertEquals(errorsList.size(), 0);
    }

    @Test(enabled = false)
    public void Fisc6570_CSLoans_MasterSchema() throws SQLException {
        HashMap<CSLoansKey, ArrayList<Object>> csLoansMySqlMap = fulcrumUtils.getCsLoansMySqlMap_customQuery(QA, "6570_CSLoans_MySQL.sql",0,1);
        HashMap<CSLoansKey, ArrayList<Object>> csLoansPostgresMap = fulcrumUtils.getCsLoansPostgresMap_customQuery("6570_CSLoans_Postgres.sql",0,1);

        Set<CSLoansKey> csLoansKeySet = csLoansMySqlMap.keySet();
        Set<CSLoansKey> csLoansKeySetPostgres = csLoansPostgresMap.keySet();

        List<AssertionError> errorsList = new ArrayList<AssertionError>();

        int i = 0;

        for (CSLoansKey csLoansKey : csLoansKeySet){
            ArrayList<Object> csLoansMySqlList = csLoansMySqlMap.get(csLoansKey);
            ArrayList<Object> csLoansPostgresList = csLoansPostgresMap.get(csLoansKey);
            boolean isRowPassed = arrayUtils.areListsOfObjectsEqual(csLoansMySqlList, csLoansPostgresList);
            try {
                Assert.assertTrue(isRowPassed);
                System.out.println(i + "        CSLOANS PASSED      AGENT ID  " + csLoansKey.getAgentId() + "         FC_TLB_EURO  " + csLoansKey.getFcTlbEuro());
                i++;
                //  logger.info("CSLOANS PASSED " + csLoansKey.getAgentId() + "       " + csLoansKey.getFcTlbEuro());
            }
            catch (AssertionError err) {
                errorsList.add(err);
                logger.error("CSLOANS FAILED   AGENT ID " + csLoansKey.getAgentId() + "      FC_TLB_EURO  " + csLoansKey.getFcTlbEuro() + "      " + err);
                System.out.println("CSLOANS FAILED   AGENT ID " + csLoansKey.getAgentId() + "      FC_TLB_EURO  " + csLoansKey.getFcTlbEuro() + "      " + err);
                System.out.println(csLoansKey.getAgentId() + "     " + csLoansKey.getFcTlbEuro());
                System.out.println("MYSQL " + csLoansMySqlList);
                System.out.println("POSTGRES " + csLoansPostgresList);
                System.out.println("\n");
                continue;
            }
        }
        Assert.assertEquals(errorsList.size(), 0);
    }

    @DataProvider(name = "Fisc_7315_FunctionalTest")
    public Object[][] getDataFor7315_FunctionalTest() {
        return new Object[][]{
                {"FC_100_MARGIN","100% Margin","100% Margin","How long the issuer has to pay 100% margin on the delayed portion of the loan","Leveraged Finance"},
                {"FC_50_MARGIN","50% Margin","50% Margin","How long the issuer has to pay 50% margin on the delayed portion of the loan","Leveraged Finance"},
                {"FC_AMT_REPRICD","Amount being Repriced","Amount being Repriced","Input field for the amount of outstanding money for a transaction that is lowering the issuers current coupon","Leveraged Finance"},
                {"FC_ASSET_SL_CNG","Asset Sale Change","Asset Sale Change","Asset Sale language input if changed during syndication","Leveraged Finance"},
                {"FC_BID_PRICE","Bid","Bid","Bid price of the tranche at first pricing","Leveraged Finance"},
                {"FC_BREAK_DT","Break Date","Break Date","Date the deal began trading","Leveraged Finance"},
                {"FC_BOND_AMT_CONCURT","Concurrent Bonds","Concurrent Bonds","Bond size issued as part of the transaction","Leveraged Finance"},
                {"FC_CURR","Currency","Currency","Currency of the transaction (always US Dollar)","Leveraged Finance\",\"Capital Structure"},
                {"FC_COVENANT_COMMENT_FNL","Current Covenants","Current Covenants","Covenant language of the tranche (Final or current)","Leveraged Finance"},
                {"FC_FINANCIAL_COVENANT_FNL","Current Financial Covenants","Current Financial Covenants","Financial covenantsof the tranche (Final or current)","Leveraged Finance"},
                {"FC_INCREMENTAL_TERMS_FNL","Current Incremental Terms","Current Incremental Terms","Terms of incremental issuance of the tranche (Final or current)","Leveraged Finance"},
                {"FC_OTHR_COMMENT","Current Other","Current Other","Other terms of the tranche (Final or current)","Leveraged Finance"},
                {"FC_DDTL_AMT","DDTL Amount","DDTL Amount","Amount of a delayed draw portion of the loan","Leveraged Finance"},
                {"FC_DDTL_AVL_MTH","DDTL Availability (Months)","DDTL Availability (Months)","Number of months of availability of the delayed draw portion of the loan","Leveraged Finance"},
                {"FC_DL_CAT","Deal Category","Deal Category","Terms of incremental issuance of the tranche","Leveraged Finance"},
                {"FC_DELAY_CLS","Delayed Close","Delayed Close","Input field to identify transaction that are delayed closing to avoid paying higher than par","Leveraged Finance"},
                {"FC_DESC_TERMS","Description","Description","Description of delayed draw terms","Leveraged Finance"},
                {"FC_EBITDA","EBITDA","EBITDA","Pro Forma EBITDA","Leveraged Finance"},
                {"FC_ECF_CHNG","ECF Change","ECF Change","Excess Cash Flow language input if changed during syndication","Leveraged Finance"},
                {"FC_EQTY_CONT_LBO","Equity %","Equity %","Equity contribution Percent for LBOs","Leveraged Finance"},
                {"FC_FIN_COV_CHNG","Fin Cov Changes","Fin Cov Changes","Financial covenant language input if changed during syndication","Leveraged Finance"},
                {"FC_FNL_DOC_SCR","Final Documentation Score","Final Documentation Score","Documentation Score from Covenant Review (final)","Leveraged Finance"},
                {"FC_SCRD_LEV","FirstLienLev","FirstLienLev","Pro Forma Secured Leverage","Leveraged Finance\",\"Capital Structure"},
                {"FC_FXD_FLTING","Fixed/Floating","Fixed/Floating","Identify deals that are fixed rate rather than floating over LIBOR","Leveraged Finance"},
                {"FC_FLX_DT_LN1","Flex Date","Flex Date","Date the pricing changed during syndication","Leveraged Finance"},
                {"FC_FLX_TYPE_LN1","Flex Type","Flex Type","Type of pricing change (UP or DOWN) during syndication","Leveraged Finance"},
                {"FC_REPRICRD_DEAL_FLOOR","Floor of Repriced Deal","Floor of Repriced Deal","Input field for the original libor floor of a transaction that is lowering the issuers current coupon","Leveraged Finance"},
                {"FC_GRD_CHNG","Grid Change","Grid Change","Pricing Grid language input if changed during syndication","Leveraged Finance"},
                {"FC_FL_REPRCD_DEAL","Floor of Repriced Deal","Floor of Repriced Deal","Input field for the original libor floor of a transaction that is lowering the issuers current coupon","Leveraged Finance"},
                {"FC_INCR_FCLTY_CHNG","Incr Facility Change","Incr Facility Change","Incremental facility language input if changed during syndication","Leveraged Finance"},
                {"FC_CALLS_PRLM","Initial Calls","Initial Calls","Call premium language of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_COVENANT_COMMENT_PRLM","Initial Covenants","Initial Covenants","Covenant language of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_FINANCIAL_COVENANT_PRLM","Initial Financial Covenants","Initial Financial Covenants","Financial covenantsof the tranche (as of launch date)","Leveraged Finance"},
                {"FC_INITIAL_FLT_FEE","Initial Flat Fee","Initial Flat Fee","Fee paid for delayed draw loan","Leveraged Finance"},
                {"FC_FLOOR_PRLM","Initial Floor","Initial Floor","Libor floor of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_INCREMENTAL_TERMS_PRLM","Initial Incremental Terms","Initial Incremental Terms","Terms of incremental issuance of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_MATURITY_DATE_PRLM","Initial Maturity Date","Initial Maturity Date","Maturity date of the tranche (as of launch date) - If known","Leveraged Finance"},
                {"FC_OID_PRLM","Initial OID","Initial OID","Issue price of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_INITIAL_OTHR","Initial Other","Initial Other","Other terms of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_ISSUE_AMOUNT_PRLM","Initial Size","Initial Size","$ amount of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_INITIAL_SPRD","Initial Spread","Initial Spread","Libor Spread of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_TERM_PRLM","Initial Term","Initial Term","Years to maturity the tranche (as of launch date)","Leveraged Finance"},
                {"FC_YT3_PRLM","Initial YT-3","Initial YT-3","Yield to 3 year call of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_YTM_PRLM","Initial YTM","Initial YTM","Yield to Maturity of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_LIBOR_RT","LIBOR","LIBOR","LIBOR rate as of the time of pricing of a transaction","Leveraged Finance"},
                {"FC_MARKET_ISSUANCE","Market of Issuance","Market of Issuance","Market of issuance of the underlying security- US or Europe","Leveraged Finance\",\"Capital Structure"},
                {"FC_MEETING_DT","Meeting","Meeting","Date of the bank meeting with potential investors","Leveraged Finance\",\"Capital Structure"},
                {"FC_MFN","MFN","MFN","Basis points of MFN Protection","Leveraged Finance"},
                {"FC_OFFER_PRC","Offer","Offer","Offer price of the tranche at first pricing","Leveraged Finance"},
                {"FC_PERMID","PERMID","PERMID","Thomson Reuters Open Source company ID","Leveraged Finance\",\"Capital Structure"},
                {"FC_PERMNAME","PERMNAME","PERMNAME","Thomson Reuters Open Source company Name","Leveraged Finance"},
                {"FC_PIK","PIK","PIK","Identify deals that are not paying all cash","Leveraged Finance"},
                {"FC_PRELM_DOC","Preliminary DocumentationScore","Preliminary DocumentationScore","Documentation Score from Covenant Review (preliminary)","Leveraged Finance"},
                {"FC_PRVT_PLCD_SLTL","Privately Placed SLTLs","Privately Placed SLTLs","Second lien tranches privately placed in association with the transaction","Leveraged Finance"},
                {"FC_REPAY_AMT","Repay Amount","Repay Amount","Amount of institutional debt being repaid in association with the transaction","Leveraged Finance"},
                {"FC_REPAY_101","Repay at 101","Repay at 101","Input field to identify transaction that are repaying outstanding debt at higher than par","Leveraged Finance"},
                {"FC_REPRC","Repricing","Repricing","Input field to identify if the transaction is lowering the issuers current coupon","Leveraged Finance"},
                {"FC_RP_CHNG","RP Change","RP Change","Restricted Payment basket input if changed during syndication","Leveraged Finance"},
                {"FC_SPRD_REPRC_DL","Spread of Repiced Deals","Spread of Repiced Deals","Input field for the original spread of a transaction that is lowering the issuers current coupon","Leveraged Finance"},
                {"FC_STORY","Story","Story","Link to story input if documentation changed during syndication","Leveraged Finance"},
                {"FC_SUNSET","Sunset","Sunset","Number of months for the MFN to be removed","Leveraged Finance"},
                {"FC_SUNSET_CMT","Sunset Change","Sunset Change","Sunset language input if changed during syndication","Leveraged Finance"},
                {"FC_TTL_LEV","TotalLev","TotalLev","Pro Forma Leverage (Overall debt vs overall assets)","Leveraged Finance\",\"Capital Structure"},
                {"FC_TRAN_CNT","Transaction Count","Transaction Count","This is an input field to identify only one tranche per transaction","Leveraged Finance"},
                {"FC_AVG_YTM_LN1_C","1L Average YTM","1L Average YTM","Calculation for first lien only yields to maturity","Leveraged Finance"},
                {"FC_COUNT_LN1_C","1L Count","1L Count","Calculation for first lien only (1 for first lien deals only)","Leveraged Finance"},
                {"FC_FLOOR_LN1_C","1L Floor","1L Floor","Calculation for first lien only floors","Leveraged Finance"},
                {"FC_OID_AVG1_C","1L OID Average","1L OID Average","Calculation for first lien only yields to maturity","Leveraged Finance"},
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
                {"FC_SPREAD_CAT_C","Final Spread Cat","Final Spread Cat","Calculation for average of high and low spread range (from current Spread)","Leveraged Finance"},
                {"FC_SPRD_TIGHT_C","Final Spread Tight","Final Spread Tight","Calculation that separates a range of spread from Current Spread (lower spread)","Leveraged Finance"},
                {"FC_SPRD_WIDE_C","Final Spread Wide","Final Spread Wide","Calculation that separates a range of spread from Current Spread (higher spread)","Leveraged Finance"},
                {"FC_OID_CAT_C","Final OID Cat","Final OID Cat","Calculation for average of high and low Issue price range (from current Issue price)","Leveraged Finance"},
                {"FC_OID_TIGHT_C","Final OID Tight","Final OID Tight","Calculation that separates a range of Issue price from Current Issue price (lower Issue price)","Leveraged Finance"},
                {"FC_OID_WIDE_C","Final OID Wide","Final OID Wide","Calculation that separates a range of Issue price from Current Issue price (higher Issue price)","Leveraged Finance"},
                {"FC_ORG_SPRD_CAT_C","Original Spread Cat","Original Spread Cat","Calculation for average of high and low spread range (from original Spread)","Leveraged Finance"},
                {"FC_ORG_SPRD1_C","Original Spread Tight","Original Spread Tight","Calculation that separates a range of spread from original Spread (lower spread)","Leveraged Finance"},
                {"FC_ORG_SPRD2_C","Original Spread Wide","Original Spread Wide","Calculation that separates a range of spread from original Spread (higher spread)","Leveraged Finance"},
                {"FC_ORG_OID1_C","Original OID Tight","Original OID Tight","Calculation that separates a range of Issue price from original Issue price (lower Issue price)","Leveraged Finance"},
                {"FC_ORG_OID2_C","Original OID Wide","Original OID Wide","Calculation that separates a range of Issue price from original Issue price (higher Issue price)","Leveraged Finance"},
                {"FC_ORG_OID_CAT_C","Original OID Cat","Original OID Cat","Calculation for average of high and low Issue price range (from original Issue price)","Leveraged Finance"},
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
                {"FC_TENOR","Updated Tenor","Updated Tenor","Years to maturity the tranche (Final or current)","Leveraged Finance\",\"Capital Structure"},
                {"FC_YT3_YR1_C","Updated YT-3 year","Updated YT-3 year","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_YTM1_C","Updated YTM","Updated YTM","Field is a calculation for internal purposes of creating an output for publication","Leveraged Finance"},
                {"FC_INITIAL_COV","Initial Covenants","Initial Covenants","Covenant language of the tranche (as of launch date)","Leveraged Finance"},
                {"FC_CALLS_FNL","Current Calls","Current Calls","Call premium language of the tranche (Final or current)","base"},
                {"FC_FLOOR_FNL","Current Floor","Current Floor","Libor floor of the tranche (Final or current)","base"},
                {"FC_OID_FNL","Current OID","Current OID","Issue price of the tranche (Final or current)","base"},
                {"FC_CURRENT_SPREAD","Current Spread","Current Spread","Libor Spread of the tranche (Final or current)","base"},
                {"FC_TERM_FNL","Current Term","Current Term","Years to maturity the tranche (Final or current)","base"},
                {"FC_YT3_FNL","Current YT-3","Current YT-3","Yield to 3 year call of the tranche (Final or current)","base"},
                {"FC_YTM","Final Yield to Maturity","Final Yield to Maturity","Yield to Maturity of the tranche (Final or current)","base\",\"Capital Structure\",\"Leveraged Finance"},
                {"FC_LAUNCH_DATE","Launch Date","Launch Date","Date the deal was brought to market","base\",\"Leveraged Finance\",\"Capital Structure"},
                {"FC_LEAD_ARRANGERS","Lead Arrangers","Lead Arrangers","List of arranging banks","base"},
                {"FC_OWNERSHIP","Ownership","Ownership","Sponsors or Ticker if public","base\",\"Leveraged Finance"},
                {"FC_CMNT_PURPOSE","Purpose comment","Purpose comment","Description of the transaction","base\",\"Leveraged Finance"},
                {"FC_REVLVR_SIZE","Revolver","Revolver","Associated Revolving Credit facility size","base"},
                {"FC_TLA","TLA","TLA","Associated TLA Credit facility size","base"},
                {"FC_TRANCHE_NM","Tranche Name","Tranche Name","Name of the security","base"},
                {"FC_TRANSACTION_TYPE","Transaction","Transaction","Purpose of the transaction (Identifies what the bond is for as a category option)","base\",\"Capital Structure\",\"Leveraged Finance"},
                {"FC_DEAL_ID","Deal ID","Deal ID","Unique ID for each deal","Capital Structure"},
                {"FC_EQUITY_OWNERSHIP","Equity Ownership","Equity Ownership","Owner of entity","Capital Structure"},
                {"FC_ISSUER_ENTITY_NAME","Issuer ","Issuer ","A legal entity that develops, registers and sells securities to finance its operations. The issuer of the bond","Capital Structure\",\"Leveraged Finance"},
                {"FC_ISSUE_AMOUNT_EURO","Euro Equiv (m)","Euro Equiv (m)","The amount of euro obtained by converting a foreign currency involved in a computation into euro at the spot rate for the purchase of euro ","Capital Structure"},
                {"FC_ISSUE_AMOUNT_USD","USD Equiv (m)","USD Equiv (m)","The amount of U.S. dollars obtained by converting a foreign currency involved in a computation into U.S. dollars at the spot rate for the purchase of U.S. dollars ","Capital Structure"},
                {"FC_1L_LEV","First Lien Lev","First Lien Lev","The amount of total debt the company has over the total assets","Capital Structure"},
                {"FC_2SCRD_LEV","2nd Lien Lev","2nd Lien Lev","First Lien + 2nd lien over total assets","Capital Structure"},
                {"FC_SPCL_FEATURES","Special features","Special features","If the debt is portable, the instrument has any restriction (Any special features of the tranche )","Capital Structure"},
                {"FC_COMMIT_DT","Commitment Date","Commitment Date","Date when the investors announce banks whether an investment is made and how much of financing is done","Capital Structure"},
                {"FC_CLOSING_DT","Closing Date","Closing Date","The date when the financing is closed- its available in secondary market. ","Capital Structure"},
                {"FC_TERM_LOANB","Term Loan B","Term Loan B","A term loan made availableto institutional investors. Multiple tranches of a facility - term Loan tranches are available to institutional investors","Capital Structure"},
                {"FC_TLB_CCY","TLB Ccy","TLB Ccy","Term LoanB currency of the transaction","Capital Structure"},
                {"FC_TLB_ISSUE_AMOUNT","TLB Size (m)","TLB Size (m)","Term LoanB amount of the tranche","Capital Structure"},
                {"FC_TLB_RATE","TLB Rate","TLB Rate","Term Loan B Rate","Capital Structure"},
                {"FC_TLB_MARGIN","TLB Margin (%/bps)","TLB Margin (%/bps)","Term Loan B Margin of the tranche","Capital Structure"},
                {"FC_TLB_FLOOR","TLB Floor","TLB Floor","Term Loan B Floor of the trance","Capital Structure"},
                {"FC_TLB_OID","TLB OID","TLB OID","Term Loan B issue of price of the tranche","Capital Structure"},
                {"FC_TLB_TERM","TLB Term (yrs)","TLB Term (yrs)","Term Loan B years to maturity the tranche","Capital Structure"},
                {"FC_TLB_PAYMENT","TLB Payment","TLB Payment","Term Loan B payment","Capital Structure"},
                {"FC_TLB_EURO","TLB EUR Equiv","TLB EUR Equiv","The amount of euro obtained by converting a foreign currency involved in a computation into euro at the spot rate for the purchase of euro for Term Loan B","Capital Structure"},
                {"FC_3MNTH_LIBOR","3 month Libor","3 month Libor","3 month LIBOR rate when the record was created","Capital Structure"},
                {"FC_PRICE_TALK","Price Talk","Price Talk","Price talk is the discussion of the appropriate price for an upcoming security issue","Capital Structure"},
                {"FC_2L","2nd Lien","2nd Lien","A lien on a property which is subordinate to a more senior mortgage or loan","Capital Structure"},
                {"FC_2L_CCY","2nd Lien Ccy","2nd Lien Ccy","Currency of the Second Lien","Capital Structure"},
                {"FC_2L_ISSUE_AMOUNT","2nd Lien Size (m)","2nd Lien Size (m)","Amount of the Second Lien","Capital Structure"},
                {"FC_2L_RATE","2nd Lien Rate","2nd Lien Rate","Rate of the Second Lien","Capital Structure"},
                {"FC_2L_MARGIN","2nd Lien Margin (%/bps)","2nd Lien Margin (%/bps)","Second Lien margin of the tranche","Capital Structure"},
                {"FC_2L_FLOOR","2nd Lien Floor","2nd Lien Floor","Second Lien floor of the tranche","Capital Structure"},
                {"FC_2LOID","2nd Lien OID","2nd Lien OID","Second Lien price of the tranche","Capital Structure"},
                {"FC_2L_TERM","2nd Lien Term (yrs)","2nd Lien Term (yrs)","Second Lien years to maturity of the tranche ","Capital Structure"},
                {"FC_2L_PAYMENT","2nd Lien Payment","2nd Lien Payment","If its amortizing- when it is repaid at life or at maturity. Bullet represnts maturity, amortizing represents life ","Capital Structure"},
                {"FC_2L_EURO","2nd Lien EUR Equiv","2nd Lien EUR Equiv","The amount of euro obtained by converting a foreign currency involved in a computation into euro at the spot rate for the purchase of euro for the Second Lien","Capital Structure"},
                {"FC_ISSUE_ENTITY_NAME","Company","Company","Legal name of the company","Capital Structure"},
                {"FC_AGENT_ID","Agent ID","Agent ID","Internal unique identifier of the entity","Capital Structure\",\"Leveraged Finance"},
                {"FC_CALL_PROTECTION","Call","Call","A call protection is a protective provision of a callable security prohibiting the issuer from calling back the security during a period early in its life","Leveraged Finance"},
                {"FC_CONCURRENT_LOAN_ID","LFI Loan ID","LFI Loan ID","Unique LFI Loan ID","Leveraged Finance"},
                {"FC_INDUSTRY_DESCRIPTION","Industry Description","Industry Description","Description of the industry","Leveraged Finance"},
                {"FC_INDUSTRY","Industry","Industry","The industry the bond is issued in","Leveraged Finance"},
                {"FC_LFYHYID","LFIHYID","LFIHYID","Unique identifier of the bond","Leveraged Finance"},
                {"FC_PURPOSE","Use of Proceeds","Use of Proceeds","The purpose of raising capital","Leveraged Finance"},
                {"FC_SPREAD","Spread","Spread","Spread of the bond","Leveraged Finance"},
                {"FC_UNDERWRITER","Underwriter","Underwriter","The underwriter for the bond","Leveraged Finance"},
                {"FC_1ST_CALL_DT","1st Call Date","1st Call Date","1st Call Date","Leveraged Finance"},
                {"FC_1ST_CALL_PRICE","1st Call Price","1st Call Price","1st Call Date","Leveraged Finance"},
                {"FC_BOND_TYPE","Bond Type","Bond Type","Type of bond","Leveraged Finance"},
                {"FC_CALL_PERCENTAGE","Call %","Call %","Call Percent Premium","Leveraged Finance"},
                {"FC_CALL_PRIM","Call Protection","Call Protection","Call premium language of the tranche (Final or current)","Leveraged Finance"},
                {"FC_CALL_SCHEDULE","Call Schedule","Call Schedule","Description of Call Schedule","Leveraged Finance"},
                {"FC_CASH_PIK","CASH/PIK/TOGGLE","CASH/PIK/TOGGLE","Type of PIK (Payment in Kind)","Leveraged Finance\",\"Capital Structure"},
                {"FC_CHANGE_OF_CONTROL","Change of control","Change of control","Change of Control Description","Leveraged Finance"},
                {"FC_CHANGE_ON_BREAK","Change on Break","Change on Break","Break Change","Leveraged Finance"},
                {"FC_DAYS_IN_MARKET","Days in Market","Days in Market","Days in Market","Leveraged Finance"},
                {"FC_DEBUT","Debut","Debut","Is the issuer debut to bonds (Yes or NO)","Leveraged Finance"},
                {"FC_DESCRIPTION","Description","Description","Description of the bond","Leveraged Finance"},
                {"FC_EQUITY_PERCENTAGE","Equity %","Equity %","Equity contribution Percent for LBOs","Leveraged Finance"},
                {"FC_EQUITY_CLAW_PERCENTAGE","Equity Claw %","Equity Claw %","Equity Claw %","Leveraged Finance\",\"Capital Structure"},
                {"FC_EQUITY_CLAW_PRICE","Equity Claw Price","Equity Claw Price","Equity Claw Price","Leveraged Finance\",\"Capital Structure"},
                {"FC_EQUITY_CLAW_YEARS","Equity Claw Years","Equity Claw Years","Equity Claw Years","Leveraged Finance\",\"Capital Structure"},
                {"FC_ISSUE_PRICE_FNL","Final Issue Price","Final Issue Price","Issue price of the tranche (Final or current)","Leveraged Finance"},
                {"FC_FIXED_FLOATING","Fixed/Floating","Fixed/Floating","Payment Type","Leveraged Finance"},
                {"FC_ISSUE_PRICE_PRLM","Initial Issue Price","Initial Issue Price","Issue price of the tranche (Initial)","Leveraged Finance"},
                {"FC_SIZE_PRLM","Initial Size","Initial Size","$ amount of the tranche (Initial)","Leveraged Finance"},
                {"FC_TALK_PRLM","Initial Talk","Initial Talk","Coupon of the tranche (Initial)","Leveraged Finance"},
                {"FC_NC_YEARS","NC Years","NC Years","Years of NC","Leveraged Finance"},
                {"FC_NOTES","Notes","Notes","Other terms of the tranche (Final or current)","Leveraged Finance"},
                {"FC_PRICING_DT","Pricing Date","Pricing Date","Date the deal was brought to market","Leveraged Finance\",\"Capital Structure"},
                {"FC_ROAD_SHOW_DATE","Road Show Dates","Road Show Dates","Date of the roadshow","Leveraged Finance"},
                {"FC_SECONDARY","Secondary","Secondary","Price after close","Leveraged Finance"},
                {"FC_SENIORITY","Seniority","Seniority","Seniority","Leveraged Finance"},
                {"FC_SPECIAL_CALL","Special Call","Special Call","Special Call Description","Leveraged Finance"},
                {"FC_LIBOR_SPREAD","T+","T+","Libor Spread of the tranche (Final or current)","Leveraged Finance"},
                {"FC_TARGET","Target","Target","Target of Issuance","Leveraged Finance"},
                {"FC_TRANSACTION","Transaction","Transaction","Transaction Count","Leveraged Finance"},
                {"FC_ADD_ON","Add on","Add on","Identifies if there is already a bond with that maturity date and coupon. It is an additional issuance of that same bond","Capital Structure"},
                {"FC_BONDID","Bond ID","Bond ID","CS Bond ID ","Capital Structure"},
                {"FC_CALL_STRUCTURE","Call structure","Call structure","Describes the call protection that the bond carries","Capital Structure"},
                {"FC_DEAL_TYPE","Deal Type","Deal Type","Identifies the type of bond ","Capital Structure"},
                {"FC_EXPECTED","Expected/Actual","Expected/Actual","Relates to the Rating value","Capital Structure"},
                {"FC_FLOOR","Floor","Floor","Libor floor of the tranche","Capital Structure"},
                {"FC_ISSUE_STATUS","Issue Status","Issue Status","Identifies old debt from current debt ","Capital Structure"},
                {"FC_OID_ISSUEPRICE","OID","OID","Original Issue Discount (OID) or Price of the tranche","Capital Structure"},
                {"FC_SETTLEMENT_DATE","Settlement Date","Settlement Date","This is the date used for calculating accrued interest","Capital Structure"}
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
        } catch (AssertionError err) {
            logger.warn("FISC 7315 FAILED! Tested FITCHFIELDID: " + fitchFieldId + " ERROR: " + err);
            Assert.fail();
        }
    }

    private boolean isArrayListContainingTheSameStrings(ArrayList<String> list, String value){
        boolean result = true;
        for (int i = 0; i < list.size(); i++){
            if (!list.get(i).equals(value)) {
                result = false;
                break;
            }
        }
        return result;
    }

    @DataProvider(name = "Fisc7317_sourceNameAndType")
    public Object[][] getData_7317_sourceNameAndType(){
        String lfiOnlyUri = baseURI + "/v1/securities?filter[sourceName]=LFI";
        String csOnlyUri = baseURI + "/v1/securities?filter[sourceName]=CS";
        String lfiAndCsUri = baseURI + "/v1/securities?filter[sourceName]=LFI,CS&page[size]=1000";
        String lfiLoansUri = baseURI + "/v1/securities?filter[sourceName]=LFI&filter[sourceType]=Loans";
        String csLoansUri = baseURI + "/v1/securities?filter[sourceName]=CS&filter[sourceType]=Loans";
        String lfiAndCsLoansUri = baseURI + "/v1/securities?filter[sourceName]=CS,LFI&filter[sourceType]=Loans&page[size]=1000";
        String lfiBondsUri = baseURI + "/v1/securities?filter[sourceName]=LFI&filter[sourceType]=Bonds";
        String csBondsUri = baseURI + "/v1/securities?filter[sourceName]=CS&filter[sourceType]=Bonds";
        String lfiAndCsBondsUri = baseURI + "/v1/securities?filter[sourceName]=CS,LFI&filter[sourceType]=Bonds&page[size]=1000";
        String lfiLoansAndBondsUri = baseURI + "/v1/securities?filter[sourceName]=LFI&filter[sourceType]=Loans,Bonds";
        String csLoansAndBondsUri = baseURI + "/v1/securities?filter[sourceName]=CS&filter[sourceType]=Loans,Bonds";

        Response lfiOnlyResponse = apiUtils.getResponse(lfiOnlyUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response csOnlyResponse = apiUtils.getResponse(csOnlyUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response lfiAndCsResponse = apiUtils.getResponse(lfiAndCsUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response lfiLoansResponse = apiUtils.getResponse(lfiLoansUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response csLoansResponse = apiUtils.getResponse(csLoansUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response lfiAndCsLoansResponse = apiUtils.getResponse(lfiAndCsLoansUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response lfiBondsResponse = apiUtils.getResponse(lfiBondsUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response csBondsResponse = apiUtils.getResponse(csBondsUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response lfiAndCsBondsResponse = apiUtils.getResponse(lfiAndCsBondsUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response lfiLoansAndBondsResponse = apiUtils.getResponse(lfiLoansAndBondsUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        Response csLoansAndBondsResponse = apiUtils.getResponse(csLoansAndBondsUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);

        return new Object[][]{
                {lfiOnlyResponse, "LFI", "", false, false},
                {csOnlyResponse, "CS", "", false, false},
                {lfiAndCsResponse, "LFI", "", false, true},
                {lfiAndCsResponse, "CS", "", false, true},
                {lfiLoansResponse, "LFI", "Loans", true, false},
                {csLoansResponse, "CS", "Loans", true, false},
                {lfiAndCsLoansResponse, "CS", "Loans", true, true},
                {lfiAndCsLoansResponse, "LFI", "Loans", true, true},
                {lfiBondsResponse, "LFI", "Bonds", true, false},
                {csBondsResponse, "CS", "Bonds", true, false},
                {lfiAndCsBondsResponse, "CS", "Bonds", true, true},
                {lfiAndCsBondsResponse, "LFI", "Bonds", true, true},
                {lfiLoansAndBondsResponse, "LFI", "Bonds", true, true},
                {lfiLoansAndBondsResponse, "LFI", "Loans", true, true},
                {csLoansAndBondsResponse, "CS", "Bonds", true, true },
                {csLoansAndBondsResponse, "CS", "Loans", true, true },
        };
    }

    @Test(dataProvider = "Fisc7317_sourceNameAndType")
    public void Fisc7317_validateSourceNameAndType(Response res, String expectedSourceName, String expectedSourceType, boolean isExpectedSourceChecked, boolean areMultipleSelectionsTogetherInResponse){
        try {
            if (isExpectedSourceChecked == false) {
                if (areMultipleSelectionsTogetherInResponse == false) {
                    ArrayList<String> sourceNamesList = res.path("data.attributes.sourceName");
                    boolean isPassed = isArrayListContainingTheSameStrings(sourceNamesList, expectedSourceName);
                    Assert.assertTrue(isPassed);
                    logger.info("FISC 7317 PASSED " + expectedSourceName);
                } else if (areMultipleSelectionsTogetherInResponse == true) {
                    ArrayList<String> sourceNamesList = res.path("data.attributes.sourceName");
                    Assert.assertTrue(sourceNamesList.contains("LFI"));
                    Assert.assertTrue(sourceNamesList.contains("CS"));
                    Assert.assertTrue(sourceNamesList.contains("LFI") && sourceNamesList.contains("CS"));
                    logger.info("FISC 7317 PASSED LFI AND/OR CS ");
                }
            } else if (isExpectedSourceChecked == true) {
                if (areMultipleSelectionsTogetherInResponse == false) {
                    ArrayList<String> sourceNamesList = res.path("data.attributes.sourceName");
                    ArrayList<String> sourceTypesList = res.path("data.attributes.SourceType");
                    boolean isSourceNamePassed = isArrayListContainingTheSameStrings(sourceNamesList, expectedSourceName);
                    boolean isSourceTypePassed = isArrayListContainingTheSameStrings(sourceNamesList, expectedSourceName);
                    Assert.assertTrue(isSourceNamePassed && isSourceTypePassed);
                    logger.info("FISC 7317 PASSED " + expectedSourceName + "    " + expectedSourceType);
                } else if (areMultipleSelectionsTogetherInResponse == true) {
                    ArrayList<String> sourceNamesList = res.path("data.attributes.sourceName");
                    ArrayList<String> sourceTypesList = res.path("data.attributes.SourceType");
                    Assert.assertTrue(sourceNamesList.contains("LFI") || sourceNamesList.contains("CS"));
                    Assert.assertTrue(sourceTypesList.contains("Loans") || sourceTypesList.contains("Bonds"));
                    logger.info("FISC 7317 PASSED LFI/CS LOANS/BONDS");
                }
            }
        }   catch (AssertionError err){
            logger.error("FISC 7317 FAILED " + expectedSourceName + "    " + expectedSourceType + " ERROR " + err);
            System.out.println("FISC 7317 FAILED " + expectedSourceName + "    " + expectedSourceType + " ERROR " + err);
            Assert.fail();
        }
    }

    @Test
    public void Fisc7317_validateInMarketDoneAndPagination(){
        String doneUri = baseURI + "/v1/securities?filter[sourceName]=LFI&filter[dealType]=Done&page[size]=50";
        String inMarketUri = baseURI + "/v1/securities?filter[sourceName]=LFI&filter[dealType]=In-Market";
        String inMarketDoneUri = baseURI + "/v1/securities?filter[sourceName]=LFI&filter[dealType]=In-Market,Done";
        String pageSizeUri = baseURI + "/v1/securities?page[size]=75";
        String pageNumberUri = baseURI + "/v1/securities?page[number]=5";

        Response doneResponse = apiUtils.getResponse(doneUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        ArrayList<String> fcBreakDts = doneResponse.path("data.attributes.FC_BREAK_DT");
        for (int i = 0; i < fcBreakDts.size(); i++){
            try {
                Assert.assertTrue(fcBreakDts.get(i) != null);
                logger.info("FISC 7317 PASSED DONE FILTER  BC_BREAK_DT " + fcBreakDts.get(i));
            } catch (AssertionError err){
                System.out.println("FISC 7317 FAILED DONE FILTER FC_BREAK_DT " + fcBreakDts.get(i));
                logger.error("FISC 7317 FAILED DONE FILTER FC_BREAK_DT " + fcBreakDts.get(i));
                Assert.fail();
            }
        }

        Response inMarketResponse = apiUtils.getResponse(inMarketUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        ArrayList<String> fcBreakDts_shouldNotBe = inMarketResponse.path("data.attributes.FC_BREAK_DT");
        for (int i = 0; i < fcBreakDts_shouldNotBe.size(); i++){
            try {
                Assert.assertTrue(fcBreakDts_shouldNotBe.get(i) == null);
                logger.info("FISC 7317 PASSED IN MARKET FILTER  FC_BREAK_DT IS NOT PRESENT");
            } catch (AssertionError err){
                System.out.println("FISC 7317 FAILED IN MARKET FILTER FC_BREAK_DT IS PRESENT");
                logger.error("FISC 7317 FAILED IN MARKET FILTER FC_BREAK_DT IS PRESENT");
                Assert.fail();
            }
        }

        Response inMarketDoneResponse = apiUtils.getResponse(inMarketDoneUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);

        Response pageSizeResponse = apiUtils.getResponse(pageSizeUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        ArrayList<String> idsList = pageSizeResponse.path("data.id");
        try {
            Assert.assertEquals(idsList.size(), 75);
            logger.info("FISC 7317 PASSED PAGINATION");
        } catch (AssertionError err){
            System.out.println("FISC 7317 FAILED PAGINATION");
            logger.error("FISC 7317 FAILED PAGINATION");
            Assert.fail();
        }

        Response pageNumberResponse = apiUtils.getResponse(pageNumberUri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        try {
            Assert.assertTrue(pageNumberResponse.path("links.first").toString().contains("page[size]=50&page[number]=0"));
            Assert.assertTrue(pageNumberResponse.path("links.next").toString().contains("page[size]=50&page[number]=6"));
            Assert.assertTrue(pageNumberResponse.path("links.prev").toString().contains("page[size]=50&page[number]=4"));
            logger.info("FISC 7317 PASSED PAGE NUMBER");
        } catch (AssertionError err){
            System.out.println("FISC 7317 FAILED PAGE NUMBER");
            logger.error("FISC 7317 FAILED PAGE NUMBER");
            Assert.fail();
        }

    }

    private HashMap<FulcrumPostgresKey, Object> postgresExpectedData = new HashMap<>();

    private HashMap<FulcrumPostgresKey, Object> getPostgresExpectedData(String sql) throws SQLException {
        Object[][] postgresData = postgresUtils.getDataFromPostgresFromStringQuery(sql, Env.Postgres.QA, true);
        for (Object[] postgresDataRow : postgresData){
            postgresExpectedData.put(new FulcrumPostgresKey(postgresDataRow[0], postgresDataRow[1]), postgresDataRow[2]);
        }
        return postgresExpectedData;
    }

    @DataProvider(name = "Fisc7317_lfiLoansSecurityIds")
    public Object[][] getLfiLoansSecutiryIds() {
            return postgresUtils.getDataFromPostgres("7317_LfiLoansSecurityIds.sql", Env.Postgres.QA, true);
    }

    @DataProvider(name = "Fisc7317_lfiBondsSecurityIds")
    public Object[][] getLfiBondsSecurityIds() {
            return postgresUtils.getDataFromPostgres("7317_LfiBondsSecurityIds.sql", Env.Postgres.QA, true);
    }

    @DataProvider(name = "Fisc7317_csLoansSecurityIds")
    public Object[][] getCsLoansSecurityIds() {
            return postgresUtils.getDataFromPostgres("7317_csLoansSecurityIds.sql", Env.Postgres.QA, true);
    }

    @DataProvider(name = "Fisc7317_csBondsSecurityIds")
    public Object[][] getCsBondsSecurityIds() {
            return postgresUtils.getDataFromPostgres("7317_csBondsSecurityIds.sql", Env.Postgres.QA, true);
    }

    HashMap<FulcrumPostgresKey, Object> lfiLoansExpectedData = null;
    HashMap<FulcrumPostgresKey, Object> lfiBondsExpectedData = null;
    HashMap<FulcrumPostgresKey, Object> csLoansExpectedData = null;
    HashMap<FulcrumPostgresKey, Object> csBondsExpectedData = null;
    MongoCollection<Document> collection = null;

    String uri = null;
    Response res = null;
    String riskConnectFlag = null;

    @Test(dataProvider = "Fisc7317_lfiLoansSecurityIds")
    public void Fisc7316_validateDataAggregator_LfiLoans(Object securityId, Object agentId) throws IOException, SQLException, InterruptedException {
        Thread.sleep(5000);
        String sql = "select issuer_entity_id, field_id, value, security_id, source_name\n" +
                "from master.security_attributes sa\n" +
                "join master.sources ms on sa.source_id = ms.source_id\n" +
                "join master.securities sec on security_id = sec.fc_sec_id\n" +
                "where ms.source_id=5 and security_id=" + securityId + " and field_id in ('FC_AVG_YTM_LN1_C', 'FC_COUNT_LN1_C', 'FC_FLOOR_LN1_C', 'FC_OID_AVG1_C', 'FC_RATCAT_LN1_C', 'FC_SPRD_AVG_LN1_C', 'FC_AVG_YTM_LN2_C', 'FC_COUNT_LN2_C', 'FC_FLOOR_LN2_C', 'FC_OID_AVG2_C', 'FC_RATCAT_LN2_C', 'FC_SPRD_AVG_LN2_C', 'FC_B3_C', 'FC_CALL_MONTHS_C', 'FC_SPREAD_CAT_C', 'FC_SPRD_TIGHT_C', 'FC_SPRD_WIDE_C', 'FC_OID_CAT_C', 'FC_OID_TIGHT_C', 'FC_OID_WIDE_C', 'FC_ORG_SPRD_CAT_C', 'FC_ORG_SPRD1_C', 'FC_ORG_SPRD2_C', 'FC_ORG_OID1_C', 'FC_ORG_OID2_C', 'FC_ORG_OID_CAT_C', 'FC_FLOOR2_C', 'FC_CALL_PROT_LN2_C', 'FC_COVENANT_LN2_C', 'FC_FNCL_COV_LN2_C', 'FC_INCRMT_FCTLY2_C', 'FC_ISSUE2_C', 'FC_LEV_TRSN_LN2_C', 'FC_OID2_C', 'FC_OTHER2_C', 'FC_SPRD2_C', 'FC_TENOR2_C', 'FC_YT3_YR2_C', 'FC_YTM2_C', 'FC_PRC_DT_C', 'FC_PURPOSE_C', 'FC_SAVNG_C', 'FC_SPONSORED_C', 'FC_STRETCH_C', 'FC_FLOOR1_C', 'FC_CALL_PROT_LN1_C', 'FC_COVENANT_LN1_C', 'FC_FNCL_COV_LN1_C', 'FC_INCRMT_FCTLY1_C', 'FC_ISSUE1_C', 'FC_LEV_TRSN_LN1_C', 'FC_OID1_C', 'FC_OTHER1_C', 'FC_SPRD1_C', 'FC_TENOR1_C', 'FC_YT3_YR1_C', 'FC_YTM1_C')\n";


        String json = "{\n" +
                "  \"data\": {\n" +
                "    \"type\": \"valueRequest\",\n" +
                "    \"attributes\": {\n" +
                "      \"dateOptions\": {\n" +
                "        \"dates\": []\n" +
                "      },\n" +
                "      \"entities\": [\n" +
                "        {\n" +
                "          \"id\": \"" + agentId + "\",\n" +
                "          \"type\": \"fitchId\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"fitchFieldIds\": [\n" +
                "        \"FC_AVG_YTM_LN1_C\",\n" +
                "        \"FC_COUNT_LN1_C\",\n" +
                "        \"FC_FLOOR_LN1_C\",\n" +
                "        \"FC_OID_AVG1_C\",\n" +
                "        \"FC_RATCAT_LN1_C\",\n" +
                "        \"FC_SPRD_AVG_LN1_C\",\n" +
                "        \"FC_AVG_YTM_LN2_C\",\n" +
                "        \"FC_COUNT_LN2_C\",\n" +
                "        \"FC_FLOOR_LN2_C\",\n" +
                "        \"FC_OID_AVG2_C\",\n" +
                "        \"FC_RATCAT_LN2_C\",\n" +
                "        \"FC_SPRD_AVG_LN2_C\",\n" +
                "        \"FC_B3_C\",\n" +
                "        \"FC_CALL_MONTHS_C\",\n" +
                "        \"FC_SPREAD_CAT_C\",\n" +
                "        \"FC_SPRD_TIGHT_C\",\n" +
                "        \"FC_SPRD_WIDE_C\",\n" +
                "        \"FC_OID_CAT_C\",\n" +
                "        \"FC_OID_TIGHT_C\",\n" +
                "        \"FC_OID_WIDE_C\",\n" +
                "        \"FC_ORG_SPRD_CAT_C\",\n" +
                "        \"FC_ORG_SPRD1_C\",\n" +
                "        \"FC_ORG_SPRD2_C\",\n" +
                "        \"FC_ORG_OID1_C\",\n" +
                "        \"FC_ORG_OID2_C\",\n" +
                "        \"FC_ORG_OID_CAT_C\",\n" +
                "        \"FC_FLOOR2_C\",\n" +
                "        \"FC_CALL_PROT_LN2_C\",\n" +
                "        \"FC_COVENANT_LN2_C\",\n" +
                "        \"FC_FNCL_COV_LN2_C\",\n" +
                "        \"FC_INCRMT_FCTLY2_C\",\n" +
                "        \"FC_ISSUE2_C\",\n" +
                "        \"FC_LEV_TRSN_LN2_C\",\n" +
                "        \"FC_OID2_C\",\n" +
                "        \"FC_OTHER2_C\",\n" +
                "        \"FC_SPRD2_C\",\n" +
                "        \"FC_TENOR2_C\",\n" +
                "        \"FC_YT3_YR2_C\",\n" +
                "        \"FC_YTM2_C\",\n" +
                "        \"FC_PRC_DT_C\",\n" +
                "        \"FC_PURPOSE_C\",\n" +
                "        \"FC_SAVNG_C\",\n" +
                "        \"FC_SPONSORED_C\",\n" +
                "        \"FC_STRETCH_C\",\n" +
                "        \"FC_FLOOR1_C\",\n" +
                "        \"FC_CALL_PROT_LN1_C\",\n" +
                "        \"FC_COVENANT_LN1_C\",\n" +
                "        \"FC_FNCL_COV_LN1_C\",\n" +
                "        \"FC_INCRMT_FCTLY1_C\",\n" +
                "        \"FC_ISSUE1_C\",\n" +
                "        \"FC_LEV_TRSN_LN1_C\",\n" +
                "        \"FC_OID1_C\",\n" +
                "        \"FC_OTHER1_C\",\n" +
                "        \"FC_SPRD1_C\",\n" +
                "        \"FC_TENOR1_C\",\n" +
                "        \"FC_YT3_YR1_C\",\n" +
                "        \"FC_YTM1_C\"\n" +
                "      ],\n" +
                "      \"options\": []\n" +
                "    }\n" +
                "  }\n" +
                "}";

        res = null;
        res = apiUtils.postToDataAggregatorStringPayload(json, AuthrztionValue, XappClintIDvalue, dataPostUrl);
        lfiLoansExpectedData = null;
        lfiLoansExpectedData = getPostgresExpectedData(sql);
        collection = mongoUtils.connectToMongoDatabase(CAL).getDatabase("esp-9").getCollection("fitch_entity");
        AggregateIterable<Document> riskConnFlgInfo = collection.aggregate(
                Arrays.asList(
                        new Document()
                                .append("$match", new Document()
                                        .append("agentID", agentId)
                                ),
                        new Document()
                                .append("$project", new Document()
                                        .append("agentID", 1.0)
                                        .append("riskConnectFlg", 1.0)
                                )
                )
        );


        riskConnectFlag = null;
        for (Document riskConnFlgInfoDocument : riskConnFlgInfo){
            riskConnectFlag = riskConnFlgInfoDocument.get("riskConnectFlg").toString();
        }

        System.out.println(riskConnectFlag);

        List<AssertionError> errorsList = new ArrayList<AssertionError>();

        for (FulcrumPostgresKey lfiLoansExpectedDataKey : lfiLoansExpectedData.keySet()) {
            if (riskConnectFlag.equals("Y")) {
                try {
                    Object value = lfiLoansExpectedData.get(lfiLoansExpectedDataKey);
                    boolean testResult = res.asString().contains(String.valueOf(value).substring(1, 5));
                    Assert.assertTrue(testResult);
                    System.out.println(res.asString());
                    System.out.println(testResult + "       " + lfiLoansExpectedDataKey.getSecurityId() + "     " + lfiLoansExpectedDataKey.getFieldId() + "        " + lfiLoansExpectedData.get(lfiLoansExpectedDataKey));
                    System.out.println("\n");
                    logger.info("FISC 7316 DATA AGGREGATOR PASSED " + lfiLoansExpectedDataKey.getSecurityId() + "       " + lfiLoansExpectedDataKey.getFieldId());
                } catch (StringIndexOutOfBoundsException ex) {
                    Object value = lfiLoansExpectedData.get(lfiLoansExpectedDataKey);
                    boolean testResult = res.asString().contains(String.valueOf(value));
                    Assert.assertTrue(testResult);
                    System.out.println(res.asString());
                    System.out.println(testResult + "       " + lfiLoansExpectedDataKey.getSecurityId() + "     " + lfiLoansExpectedDataKey.getFieldId() + "        " + lfiLoansExpectedData.get(lfiLoansExpectedDataKey));
                    System.out.println("\n");
                    logger.info("FISC 7316 DATA AGGREGATOR PASSED " + lfiLoansExpectedDataKey.getSecurityId() + "       " + lfiLoansExpectedDataKey.getFieldId());
                } catch (AssertionError err) {
                    errorsList.add(err);
                    logger.error("FISC 7316 DATA AGGREGATOR FAILED " + lfiLoansExpectedDataKey.getSecurityId() + " " + lfiLoansExpectedDataKey.getFieldId());
                    Assert.fail();
                    continue;
                }
            } else if (riskConnectFlag.equals("N")) {
                try {
                    boolean testResult = res.asString().contains(String.valueOf("\"type\":\"fitchId\",\"isMissing\":true"));
                    Assert.assertTrue(testResult);
                    logger.info("FISC 7316 DATA AGGREGATOR PASSED " + lfiLoansExpectedDataKey.getSecurityId() + "       " + lfiLoansExpectedDataKey.getFieldId());
                } catch (AssertionError err) {
                    errorsList.add(err);
                    logger.error("FISC 7316 DATA AGGREGATOR FAILED " + lfiLoansExpectedDataKey.getSecurityId() + " " + lfiLoansExpectedDataKey.getFieldId());
                    Assert.fail();
                    continue;
                }
            }
        }

        System.out.println(res.asString());
        Assert.assertEquals(errorsList.size(), 0);
        lfiLoansExpectedData.clear();
        lfiLoansExpectedData = null;
        res = null;
        riskConnectFlag = null;
    }

    @Test(dataProvider = "Fisc7317_lfiLoansSecurityIds")
    public void Fisc7317_validateDataBetweenMySqlAndPostgres_LfiLoans(Object securityId, Object agentId) throws SQLException, InterruptedException {
        Thread.sleep(2000);
        lfiLoansExpectedData = null;
        uri = null;
        res = null;

        String sql = "select security_id, field_id, value, source_name\n" +
                "from master.security_attributes sa\n" +
                "  join master.sources ms on sa.source_id = ms.source_id\n" +
                "where ms.source_id=5 and security_id=" + securityId;

        lfiLoansExpectedData = null;
        lfiLoansExpectedData = getPostgresExpectedData(sql);

        Set<FulcrumPostgresKey> lfiLoansExpectedDataKeys = lfiLoansExpectedData.keySet();

        int i = 0;

        uri = null;
        uri = baseURI + "/v1/securities/" + securityId;

        System.out.println("TESTING URI " + uri);

        res = null;
        res = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        System.out.println(res.asString());
        System.out.println("\n\n\n");
        List<AssertionError> errorsList = new ArrayList<AssertionError>();

        for (FulcrumPostgresKey lfiLoansExpectedDataKey : lfiLoansExpectedDataKeys) {
            try {
                System.out.println(lfiLoansExpectedDataKey.getSecurityId() + "        " + lfiLoansExpectedData.get(lfiLoansExpectedDataKey).toString() + "        " + res.asString());
                Assert.assertTrue(res.asString().contains(lfiLoansExpectedData.get(lfiLoansExpectedDataKey).toString()));
               // Assert.assertTrue(res.asString().contains("\"" + lfiLoansExpectedDataKey.getFieldId() + "\":\"" + lfiLoansExpectedData.get(lfiLoansExpectedDataKey) + "\""));
                logger.info("FISC 7317 PASSED SECURITY_ID  " + lfiLoansExpectedDataKey.getSecurityId() + "          " + "\"" + lfiLoansExpectedDataKey.getFieldId() + "\": \"" + lfiLoansExpectedData.get(lfiLoansExpectedDataKey) + "\"");
            } catch (AssertionError err){
                errorsList.add(err);
                logger.error("FISC 7317 FAILED SECURITY_ID  " + lfiLoansExpectedDataKey.getSecurityId() + "          " + "\"" + lfiLoansExpectedDataKey.getFieldId());
                System.out.println("FISC 7317 FAILED SECURITY_ID  " + lfiLoansExpectedDataKey.getSecurityId() + "          " + "\"" + lfiLoansExpectedDataKey.getFieldId());
                continue;
            }
        }
        Assert.assertEquals(errorsList.size(), 0);
        lfiLoansExpectedData.clear();
        lfiLoansExpectedData = null;
        uri = null;
        res=null;
    }

    @Test(dataProvider = "Fisc7317_lfiBondsSecurityIds")
    public void Fisc7317_validateDataBetweenMySqlAndPostgres_LfiBonds(Object securityId, Object agentId) throws SQLException, InterruptedException {
        Thread.sleep(2000);
        String sql = "select security_id, field_id, value, source_name\n" +
                "from master.security_attributes sa\n" +
                "  join master.sources ms on sa.source_id = ms.source_id\n" +
                "where ms.source_id=6 and security_id=" + securityId;


        lfiBondsExpectedData = null;
        lfiBondsExpectedData = getPostgresExpectedData(sql);

        Set<FulcrumPostgresKey> lfiBondsExpectedDataKeys = lfiBondsExpectedData.keySet();

        int i = 0;

        uri = null;
        uri = baseURI + "/v1/securities/" + securityId;

        System.out.println("TESTING URI " + uri);

        res = null;
        res = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        System.out.println(res.asString());
        System.out.println("\n\n\n");
        List<AssertionError> errorsList = new ArrayList<AssertionError>();


        for (FulcrumPostgresKey lfiBondsExpectedDataKey : lfiBondsExpectedDataKeys) {
            try {
                System.out.println(lfiBondsExpectedDataKey.getSecurityId() + "        " + lfiBondsExpectedData.get(lfiBondsExpectedDataKey).toString() + "        " + res.asString());
                Assert.assertTrue(res.asString().contains(lfiBondsExpectedData.get(lfiBondsExpectedDataKey).toString()));
                // Assert.assertTrue(res.asString().contains("\"" + lfiLoansExpectedDataKey.getFieldId() + "\":\"" + lfiLoansExpectedData.get(lfiLoansExpectedDataKey) + "\""));
                logger.info("FISC 7317 PASSED SECURITY_ID  " + lfiBondsExpectedDataKey.getSecurityId() + "          " + "\"" + lfiBondsExpectedDataKey.getFieldId() + "\": \"" + lfiBondsExpectedData.get(lfiBondsExpectedDataKey) + "\"");
            } catch (AssertionError err){
                errorsList.add(err);
                logger.error("FISC 7317 FAILED SECURITY_ID  " + lfiBondsExpectedDataKey.getSecurityId() + "          " + "\"" + lfiBondsExpectedDataKey.getFieldId());
                System.out.println("FISC 7317 FAILED SECURITY_ID  " + lfiBondsExpectedDataKey.getSecurityId() + "          " + "\"" + lfiBondsExpectedDataKey.getFieldId());
                continue;
            }
        }

        lfiBondsExpectedData.clear();
        lfiBondsExpectedData = null;
        uri = null;
        res = null;
    }

    @Test(dataProvider = "Fisc7317_csLoansSecurityIds")
    public void Fisc7317_validateDataBetweenMySqlAndPostgres_CSLoans(Object securityId, Object agentId) throws SQLException, InterruptedException {
        Thread.sleep(2000);
        String sql = "select security_id, field_id, value, source_name\n" +
                "from master.security_attributes sa\n" +
                "  join master.sources ms on sa.source_id = ms.source_id\n" +
                "where ms.source_id=7 and security_id=" + securityId;

        csLoansExpectedData = null;
        csLoansExpectedData = getPostgresExpectedData(sql);

        Set<FulcrumPostgresKey> csLoansExpectedDataKeys = csLoansExpectedData.keySet();

        int i = 0;

        uri = null;
        uri = baseURI + "/v1/securities/" + securityId;

        System.out.println("TESTING URI " + uri);

        res = null;
        res = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        System.out.println(res.asString());
        System.out.println("\n\n\n");
        List<AssertionError> errorsList = new ArrayList<AssertionError>();

        for (FulcrumPostgresKey csLoansExpectedDataKey : csLoansExpectedDataKeys) {
            try {
                System.out.println(csLoansExpectedDataKey.getSecurityId() + "        " + csLoansExpectedData.get(csLoansExpectedDataKey).toString() + "        " + res.asString());
                Assert.assertTrue(res.asString().contains(csLoansExpectedData.get(csLoansExpectedDataKey).toString()));
                // Assert.assertTrue(res.asString().contains("\"" + lfiLoansExpectedDataKey.getFieldId() + "\":\"" + lfiLoansExpectedData.get(lfiLoansExpectedDataKey) + "\""));
                logger.info("FISC 7317 PASSED SECURITY_ID  " + csLoansExpectedDataKey.getSecurityId() + "          " + "\"" + csLoansExpectedDataKey.getFieldId() + "\": \"" + csLoansExpectedData.get(csLoansExpectedDataKey) + "\"");
            } catch (AssertionError err){
                errorsList.add(err);
                logger.error("FISC 7317 FAILED SECURITY_ID  " + csLoansExpectedDataKey.getSecurityId() + "          " + "\"" + csLoansExpectedDataKey.getFieldId());
                System.out.println("FISC 7317 FAILED SECURITY_ID  " + csLoansExpectedDataKey.getSecurityId() + "          " + "\"" + csLoansExpectedDataKey.getFieldId());
                continue;
            }
        }
        csLoansExpectedData.clear();
        csLoansExpectedData = null;
        uri = null;
        res = null;
    }

    @Test(dataProvider = "Fisc7317_csBondsSecurityIds")
    public void Fisc7317_validateDataBetweenMySqlAndPostgres_csBonds(Object securityId, Object agentId) throws SQLException, InterruptedException {
        Thread.sleep(2000);
        String sql = "select security_id, field_id, value, source_name\n" +
                "from master.security_attributes sa\n" +
                "  join master.sources ms on sa.source_id = ms.source_id\n" +
                "where ms.source_id=8 and security_id=" + securityId;

        csBondsExpectedData = null;
        csBondsExpectedData = getPostgresExpectedData(sql);

        Set<FulcrumPostgresKey> csBondsExpectedDataKeys = csBondsExpectedData.keySet();

        int i = 0;

        uri = null;
        uri = baseURI + "/v1/securities/" + securityId;

        System.out.println("TESTING URI " + uri);

        res = null;
        res = apiUtils.getResponse(uri, AuthrztionValue, XappClintIDvalue, acceptValue, contentValue);
        System.out.println(res.asString());
        System.out.println("\n\n\n");
        List<AssertionError> errorsList = new ArrayList<AssertionError>();

        for (FulcrumPostgresKey csBondsExpectedDataKey : csBondsExpectedDataKeys) {
            try {
                System.out.println(csBondsExpectedDataKey.getSecurityId() + "        " + csBondsExpectedData.get(csBondsExpectedDataKey).toString() + "        " + res.asString());
                Assert.assertTrue(res.asString().contains(csBondsExpectedData.get(csBondsExpectedDataKey).toString()));
                // Assert.assertTrue(res.asString().contains("\"" + lfiLoansExpectedDataKey.getFieldId() + "\":\"" + lfiLoansExpectedData.get(lfiLoansExpectedDataKey) + "\""));
                logger.info("FISC 7317 PASSED SECURITY_ID  " + csBondsExpectedDataKey.getSecurityId() + "          " + "\"" + csBondsExpectedDataKey.getFieldId() + "\": \"" + csBondsExpectedData.get(csBondsExpectedDataKey) + "\"");
            } catch (AssertionError err){
                errorsList.add(err);
                logger.error("FISC 7317 FAILED SECURITY_ID  " + csBondsExpectedDataKey.getSecurityId() + "          " + "\"" + csBondsExpectedDataKey.getFieldId());
                System.out.println("FISC 7317 FAILED SECURITY_ID  " + csBondsExpectedDataKey.getSecurityId() + "          " + "\"" + csBondsExpectedDataKey.getFieldId());
                continue;
            }
        }
        csBondsExpectedData.clear();
        csBondsExpectedData = null;
        uri = null;
        res = null;
    }
}
