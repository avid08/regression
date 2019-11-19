package com.backendutils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class MySqlUtils {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";


    public Connection connectToMySqlDatabase(Env.MySQL env) throws SQLException {
        try {
            switch (env) {
                case QA:
                    Connection qaConn = null;
                    final String DB_URL_QA = EnvConfig.MySQL.QA.HOSTNAME;
                    final String USER_QA = EnvConfig.MySQL.QA.USERNAME;
                    final String PASS_QA = EnvConfig.MySQL.QA.PASSWORD;
                    System.out.println("Connecting to MySQL QA Database");
                    qaConn = DriverManager.getConnection(DB_URL_QA, USER_QA, PASS_QA);
                    System.out.println("Connected database successfully");
                    return qaConn;
                case PROD:
                    Connection prodConn = null;
                    final String DB_URL_PROD = EnvConfig.MySQL.PROD.HOSTNAME;
                    final String USER_PROD = EnvConfig.MySQL.PROD.USERNAME;
                    final String PASS_PROD = EnvConfig.MySQL.PROD.PASSWORD;
                    System.out.println("Connecting to MySQL PROD Database");
                    prodConn = DriverManager.getConnection(DB_URL_PROD, USER_PROD, PASS_PROD);
                    System.out.println("Connected database successfully");
                    return prodConn;
                default:
                    Connection defaultConn = null;
                    final String DB_URL_DEFAULT_QA = EnvConfig.MySQL.QA.HOSTNAME;
                    final String USER_DEFAULT_QA = EnvConfig.MySQL.QA.USERNAME;
                    final String PASS_DEFAULT_QA = EnvConfig.MySQL.QA.PASSWORD;
                    System.out.println("Connecting to MySQL QA Database");
                    defaultConn = DriverManager.getConnection(DB_URL_DEFAULT_QA, USER_DEFAULT_QA, PASS_DEFAULT_QA);
                    System.out.println("Connected database successfully");
                    return defaultConn;
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception occured: " + ex);
            return null;
        }
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
