package com.fulcrum.api;

import com.backendutils.Env;
import com.backendutils.ExcelUtils;
import com.backendutils.MySqlUtils;
import com.configuration.api.Configuration;
import com.google.common.io.Resources;
import org.testng.annotations.Test;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class LFIBonds extends Configuration {
    com.backendutils.ExcelUtils excelUtils = new ExcelUtils();
    com.backendutils.MySqlUtils mySqlUtils = new MySqlUtils();

    private String getFullResourcePath(String resourceFileName){
        return Resources.getResource(resourceFileName).toString().substring(6);
    }

    @Test
    public void getExcelData(){
       Object[][] excelData = excelUtils.getDataFromExcel(getFullResourcePath("LFIBonds.xlsx"),"source");
   }

   @Test
    public void getMySqlData() throws SQLException {
        Connection conn = mySqlUtils.connectToMySqlDatabase(Env.MySQL.QA);
   }
}
