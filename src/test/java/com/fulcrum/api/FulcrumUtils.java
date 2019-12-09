package com.fulcrum.api;

import com.backendutils.Env;
import com.backendutils.ExcelUtils;
import com.backendutils.MySqlUtils;
import com.backendutils.PostgresUtils;
import com.configuration.api.Configuration;
import com.google.common.io.Resources;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.backendutils.Env.MySQL.QA;
import static com.backendutils.FileUtils.getFullResourcePath;

public class FulcrumUtils extends Configuration {
    com.backendutils.ExcelUtils excelUtils = new ExcelUtils();
    com.backendutils.MySqlUtils mySqlUtils = new MySqlUtils();
    PostgresUtils postgresUtils = new PostgresUtils();

    private HashMap<LFILoansKey, ArrayList<Object>> lfiLoansMySqlMap = new HashMap<>();
    private HashMap<LFIBondsKey, ArrayList<Object>> lfiBondsMySqlMap = new HashMap<>();
    private HashMap<CSLoansKey, ArrayList<Object>> csLoansMySqlMap = new HashMap<>();
    private HashMap<CSLoansKey, ArrayList<Object>> csLoansPostgresMap = new HashMap<>();
    private HashMap<CSBondsKey, ArrayList<Object>> csBondsMySqlMap = new HashMap<>();
    private HashMap<CSBondsKey, ArrayList<Object>> csBondsPostgresMap = new HashMap<>();
    private HashMap<CovRevKey, ArrayList<Object>> covRevMySqlMap = new HashMap<>();



    public HashMap<LFIBondsKey, ArrayList<Object>> getLfiBondsMySqlMap(Env.MySQL env) throws SQLException {
        Object[][] mySqlData = mySqlUtils.getDataFromMySQL(env, "prodstage", "LFIBonds.sql");
        for (Object[] mySqlDataRow : mySqlData){
            ArrayList<Object> mySqlDataList = new ArrayList<Object>();
            for (Object mySqlDataItem : mySqlDataRow){
                mySqlDataList.add(mySqlDataItem);
            }
            lfiBondsMySqlMap.put(new LFIBondsKey(mySqlDataRow[0], mySqlDataRow[1]), mySqlDataList);
        }
        return lfiBondsMySqlMap;
    }

    public HashMap<CSBondsKey, ArrayList<Object>> getCsBondsMySqlMap(Env.MySQL env) throws SQLException{
        Object[][] mySqlData = mySqlUtils.getDataFromMySQL(env, "prodstage", "CSBonds.sql");
        for (Object[] mySqlDataRow : mySqlData){
            ArrayList<Object> mySqlDataList = new ArrayList<Object>();
            for (Object mySqlDataItem : mySqlDataRow){
                mySqlDataList.add(mySqlDataItem);
            }
            csBondsMySqlMap.put(new CSBondsKey(mySqlDataRow[0], mySqlDataRow[1]), mySqlDataList);
        }
        return csBondsMySqlMap;
    }

    public HashMap<CSLoansKey, ArrayList<Object>> getCsLoansMySqlMap_customQuery(Env.MySQL env, String queryFileName, Integer agentIdColumnNumber, Integer fcTlbEuroColumnNumber) throws SQLException {
        Object[][] mySqlData = mySqlUtils.getDataFromMySQL(env, "prodstage", queryFileName);
        for (Object[] mySqlDataRow : mySqlData){
            ArrayList<Object> mySqlDataList = new ArrayList<Object>();
            for (Object mySqlDataItem : mySqlDataRow){
                mySqlDataList.add(mySqlDataItem);
            }
            csLoansMySqlMap.put(new CSLoansKey(mySqlDataRow[agentIdColumnNumber], mySqlDataRow[fcTlbEuroColumnNumber]), mySqlDataList);
        }
        return csLoansMySqlMap;
    }

    public HashMap<CSLoansKey, ArrayList<Object>> getCsLoansPostgresMap_customQuery(String queryFileName, Integer agentIdColumnNumber, Integer fcTlbEuroColumnNumber) throws SQLException {
        Object[][] postgresData = postgresUtils.getDataFromPostgres(queryFileName, Env.Postgres.QA, true);
        for (Object[] postgresDataRow : postgresData){
            ArrayList<Object> postgresDataList = new ArrayList<Object>();
            for (Object postgresDataItem : postgresDataRow){
                postgresDataList.add(postgresDataItem);
            }
            csLoansPostgresMap.put(new CSLoansKey(postgresDataRow[agentIdColumnNumber], postgresDataRow[fcTlbEuroColumnNumber]), postgresDataList);
        }
        return csLoansPostgresMap;
    }

    public HashMap<CSBondsKey, ArrayList<Object>> getCsBondsMySqlMap_customQuery(Env.MySQL env, String queryFileName, Integer agentIdColumnNumber, Integer bondIdColumnNumber) throws SQLException{
        Object[][] mySqlData = mySqlUtils.getDataFromMySQL(env, "prodstage", queryFileName);
        for (Object[] mySqlDataRow : mySqlData){
            ArrayList<Object> mySqlDataList = new ArrayList<Object>();
            for (Object mySqlDataItem : mySqlDataRow){
                mySqlDataList.add(mySqlDataItem);
            }
            csBondsMySqlMap.put(new CSBondsKey(mySqlDataRow[agentIdColumnNumber], mySqlDataRow[bondIdColumnNumber]), mySqlDataList);
        }
        return csBondsMySqlMap;
    }

    public HashMap<CSBondsKey, ArrayList<Object>> getCsBondsPostgresMap_customQuery(String queryFileName, Integer agentIdColumnNumber, Integer bondIdColumnNumber) throws SQLException{
        Object[][] postgresData = postgresUtils.getDataFromPostgres(queryFileName, Env.Postgres.QA, true);
        for (Object[] postgresDataRow : postgresData){
            ArrayList<Object> postgresDataList = new ArrayList<Object>();
            for (Object postgresDataItem : postgresDataRow){
                postgresDataList.add(postgresDataItem);
            }
            csBondsPostgresMap.put(new CSBondsKey(postgresDataRow[agentIdColumnNumber], postgresDataRow[bondIdColumnNumber]), postgresDataList);
        }
        return csBondsPostgresMap;
    }

    public HashMap<LFILoansKey, ArrayList<Object>> getLfiLoansMySqlMap(Env.MySQL env) throws SQLException {
        Object[][] mySqlData = mySqlUtils.getDataFromMySQL(env, "prodstage", "LFILoans.sql");
        for (Object[] mySqlDataRow : mySqlData){
            ArrayList<Object> mySqlDataList = new ArrayList<Object>();
            for (Object mySqlDataItem : mySqlDataRow){
                mySqlDataList.add(mySqlDataItem);
            }
            lfiLoansMySqlMap.put(new LFILoansKey(mySqlDataRow[0], mySqlDataRow[1]), mySqlDataList);
        }
        return lfiLoansMySqlMap;
    }

    public Object[][] getCsLoansMySqlArray(Env.MySQL env) throws SQLException {
        Object[][] mySqlData = mySqlUtils.getDataFromMySQL(env, "prodstage", "CSLoans.sql");
        for (int i = 1; i<mySqlData.length; i++) {
            for (int j = 1; j < mySqlData[i].length; j++){
                System.out.println((i - 1) + "      " + mySqlData[i][0] + "      " + mySqlData[0][j] + "         " + mySqlData[i][j]);
            }
        }
        return mySqlData;
    }

    public HashMap<CovRevKey, ArrayList<Object>> getCovenantReviewData(Env.MySQL env) throws SQLException {
        Object[][] mySqlData = mySqlUtils.getDataFromMySQL(env, "prodstage", "covenant_review.sql");
        for (Object[] mySqlDataRow : mySqlData){
            ArrayList<Object> mySqlDataList = new ArrayList<Object>();
            for (Object mySqlDataItem : mySqlDataRow){
                mySqlDataList.add(mySqlDataItem);
            }
            covRevMySqlMap.put(new CovRevKey(mySqlDataRow[0], mySqlDataRow[1]), mySqlDataList);
        }
        return covRevMySqlMap;
    }
}
