package com.backendutils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class MySqlUtils {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = EnvConfig.MySQL.QA.HOSTNAME;
    static final String USER = EnvConfig.MySQL.QA.USERNAME;
    static final String PASS = EnvConfig.MySQL.QA.PASSWORD;

    public Connection connectToMySqlDatabase() {
        Connection conn = null;

        try {
            //    Class.forName(JDBC_DRIVER).newInstance();
            System.out.println("Connecting to MySQL Database");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully");
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);      }
        return conn;
    }

    public ResultSet executeMySqlScript(Connection conn, String databaseName, String mySqlFilePath) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(mySqlFilePath));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = in.readLine()) != null) {
                sb.append(str + "\n");
            }
            in.close();

            Statement stmt = null;
            System.out.println("Creating statement");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Running MySQL script");
            stmt.executeQuery("USE " + databaseName + ";");
            ResultSet rs = stmt.executeQuery(sb.toString());
            return rs;
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            return null;
        }
    }

    private static int getRowCount(ResultSet rs) {
        int size = 0;
        try {
            rs.last();
            size = rs.getRow();
            rs.beforeFirst();
        } catch (Exception ex) {
            return 0;
        }
        return size;
    }

    public Object[][] resultSetToArray(ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int rowCount = getRowCount(rs) + 1;
            int columnCount = rsmd.getColumnCount();
            Object[][] mySqlArray = new Object[rowCount][columnCount];

            for (int i = 0; i < columnCount; i++) {
                mySqlArray[0][i] = rsmd.getColumnName(i + 1);
            }

            int row = 1;
            while (rs.next()) {
                for (int i = 0; i < columnCount; i++) {
                    mySqlArray[row][i] = rs.getObject(i + 1);
                }
                row++;
            }
            return mySqlArray;
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            return null;
        }
    }
}
