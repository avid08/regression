package com.fulcrum.api;

import com.backendutils.Env;
import com.backendutils.ExcelUtils;
import com.backendutils.MySqlUtils;
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

    private HashMap<LFILoansKey, ArrayList<Object>> lfiLoansMySqlMap = new HashMap<>();
    private HashMap<LFIBondsKey, ArrayList<Object>> lfiBondsMySqlMap = new HashMap<>();
    private HashMap<Object, Object> csLoansMySqlMap = new HashMap<>();
    private HashMap<CSBondsKey, ArrayList<Object>> csBondsMySqlMap = new HashMap<>();


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

    @Test(enabled=false)
    public void getExcelData() throws IOException {
       Object[][] excelData = excelUtils.getDataFromExcel(getFullResourcePath("LFIBonds.xlsx"),"source");
    }

    @Test
    public void getMySqlData() throws SQLException {
        //   HashMap<LFIBondsKey, Object> lfiBondsMySqlMap = getLfiBondsMySqlMap(QA);
        //   HashMap<CSBondsKey, Object> csBondsMySqlMap = getCsBondsMySqlMap(QA);
        //   HashMap<LFILoansKey, Object> lfiLoansMySqlMap = getLfiLoansMySqlMap(QA);
       Object[][] csLoansMySqlArray = getCsLoansMySqlArray(QA);
   }
}
