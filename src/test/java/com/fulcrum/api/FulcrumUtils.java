package com.fulcrum.api;

import com.backendutils.Env;
import com.backendutils.ExcelUtils;
import com.backendutils.MySqlUtils;
import com.configuration.api.Configuration;
import com.google.common.io.Resources;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static com.backendutils.Env.MySQL.QA;
import static com.backendutils.FileUtils.getFullResourcePath;

public class FulcrumUtils extends Configuration {
    com.backendutils.ExcelUtils excelUtils = new ExcelUtils();
    com.backendutils.MySqlUtils mySqlUtils = new MySqlUtils();

    private HashMap<LFIBondsKey, Object> lfiBondsMySqlMap = new HashMap<>();
    private HashMap<CSBondsKey, Object> csBondsMySqlMap = new HashMap<>();
    private HashMap<LFILoansKey, Object> lfiLoansMySqlMap = new HashMap<>();


    public HashMap<LFIBondsKey, Object> getLfiBondsMySqlMap(Env.MySQL env) throws SQLException {
        Object[][] mySqlData = mySqlUtils.getDataFromMySQL(env, "prodstage", "LFIBonds.sql");
        for (int i = 2; i < mySqlData.length; i++){
            for (int j = 1; j < mySqlData[i].length; j++){
                System.out.println((i-1) + "    " + mySqlData[i][0] + "      " + mySqlData[i][1] + "        " + mySqlData[0][j] + "        " + mySqlData[i][j]);
                lfiBondsMySqlMap.put(new LFIBondsKey(mySqlData[i][0], mySqlData[i][1], mySqlData[0][j]), mySqlData[i][j]);
            }
        }
        return lfiBondsMySqlMap;
    }

    public HashMap<CSBondsKey, Object> getCsBondsMySqlMap(Env.MySQL env) throws SQLException{
        Object[][] mySqlData = mySqlUtils.getDataFromMySQL(env, "prodstage", "CSBonds.sql");
        for (int i = 2; i < mySqlData.length; i++){
            for (int j = 1; j < mySqlData[i].length; j++){
                System.out.println((i-1) + "    " + mySqlData[i][0] + "      " + mySqlData[i][1] + "        " + mySqlData[0][j] + "        " + mySqlData[i][j]);
                csBondsMySqlMap.put(new CSBondsKey(mySqlData[i][0], mySqlData[i][1], mySqlData[0][j]), mySqlData[i][j]);
            }
        }
        return csBondsMySqlMap;
    }

    public HashMap<LFILoansKey, Object> getLfiLoansMySqlMap(Env.MySQL env) throws SQLException {
        Object[][] mySqlData = mySqlUtils.getDataFromMySQL(env, "prodstage", "LFILoans.sql");
        for (int i = 1; i < mySqlData.length; i++){
            for (int j = 1; j < mySqlData[i].length; j++){
                System.out.println((i-1) + "    " + mySqlData[i][0] + "        " + mySqlData[0][j] + "        " + mySqlData[i][j]);
                lfiLoansMySqlMap.put(new LFILoansKey(mySqlData[i][0], mySqlData[0][j]), mySqlData[i][j]);
            }
        }
        return lfiLoansMySqlMap;
    }

    @Test(enabled=false)
    public void getExcelData(){
       Object[][] excelData = excelUtils.getDataFromExcel(getFullResourcePath("LFIBonds.xlsx"),"source");
    }

   @Test
    public void getMySqlData() throws SQLException {
    //    HashMap<LFIBondsKey, Object> lfiBondsMySqlMap = getLfiBondsMySqlMap(QA);
    //    HashMap<CSBondsKey, Object> csBondsMySqlMap = getCsBondsMySqlMap(QA);
       HashMap<LFILoansKey, Object> lfiLoansMySqlMap = getLfiLoansMySqlMap(QA);
   }
}
