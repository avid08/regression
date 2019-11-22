package com.backendutils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class PostgresUtils {

    public Connection connectToPostgreDatabase(Env.Postgres env) {
        try {
            switch (env) {
                case QA:
                    Connection qaConnection = DriverManager.getConnection(EnvConfig.Postgres.QA.HOSTNAME, EnvConfig.Postgres.QA.USERNAME, EnvConfig.Postgres.QA.PASSWORD);
                    System.out.println("Connected to PostgreSQL QA database");
                    return qaConnection;

                case STG:
                    Connection stgConnection = DriverManager.getConnection(EnvConfig.Postgres.STG.HOSTNAME, EnvConfig.Postgres.STG.USERNAME, EnvConfig.Postgres.STG.PASSWORD);
                    System.out.println("Connected to PostgreSQL STG database");
                    return stgConnection;
                    
                case PROD:Connection prodConnection = DriverManager.getConnection(EnvConfig.Postgres.PROD.HOSTNAME, EnvConfig.Postgres.PROD.USERNAME, EnvConfig.Postgres.PROD.PASSWORD);
                    System.out.println("Connected to PostgreSQL PROD database");
                    return prodConnection;



                    default:
                        Connection defaultConnection = DriverManager.getConnection(EnvConfig.Postgres.QA.HOSTNAME, EnvConfig.Postgres.QA.USERNAME, EnvConfig.Postgres.QA.PASSWORD);
                        System.out.println("Connected to PostgreSQL QA database as default");
                        return defaultConnection;

            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            return null;
        }
    }

    public ResultSet executePostgreScript(Connection conn, String postgreFilePath) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(postgreFilePath));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = in.readLine()) != null) {
                sb.append(str + "\n");
            }
            in.close();

            Statement stmt = null;
            System.out.println("Creating statement");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(sb.toString());
            return rs;
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            return null;
        }
    }

    public static int getRowCount(ResultSet rs) {
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

    private static Object[][] removeRowFrom2dArray(Object[][] array, int rowNumber){
        Object[][] result = new Object[array.length - 1][array[0].length];
        int count = 0;
        if (rowNumber == array.length - 1) {
            for (int r = 0; r < array.length - 1; r++) {
                for (int c = 0; c < array[0].length; c++) {
                    if (rowNumber == r)
                        r++;
                    result[count][c] = array[r][c];
                }
                count++;
            }
        } else {
            for (int r = 0; r < array.length; r++) {
                for (int c = 0; c < array[0].length; c++) {
                    if (rowNumber == r)
                        r++;
                    result[count][c] = array[r][c];
                }
                count++;
            }
        }
        return result;
    }

    public Object[][] resultSetToArray(ResultSet rs, boolean skipColumnNames) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int rowCount = getRowCount(rs) + 1;
            int columnCount = rsmd.getColumnCount();
            Object[][] postgresArray = new Object[rowCount][columnCount];

            if (skipColumnNames == false) {
                for (int i = 0; i < columnCount; i++) {
                    postgresArray[0][i] = rsmd.getColumnName(i + 1);
                }
            }
                int row = 1;
                while (rs.next()) {
                    for (int i = 0; i < columnCount; i++) {
                        postgresArray[row][i] = rs.getObject(i + 1);
                    }
                    row++;
                }
                if (skipColumnNames == true) {
                    postgresArray = removeRowFrom2dArray(postgresArray, 0);
                }
            return postgresArray;
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            return null;
        }
    }
}
