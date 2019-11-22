package com.backendutils;

import com.google.common.io.Resources;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.apache.poi.ss.usermodel.CellType.*;

public class ExcelUtils {

    public String taskNumber;

    private final static String getExcelOutputFilePath(String taskNumber){
        return System.getProperty("user.home") + "\\IdeaProjects\\apiregresssion\\regression\\src\\test\\java\\com\\logs\\result__" + taskNumber + "__" + getDateTime() + ".xlsx";
    }

    private final String EXCELOUTPUTFILEPATH = getExcelOutputFilePath(taskNumber);

    private final static String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmm");
        df.setTimeZone(TimeZone.getTimeZone("EST"));
        return df.format(new Date());
    }

    private static BigInteger getBigInteger(Object value) {
        BigInteger result = new BigInteger("0");
        if(value != null) {
            try {
                if(value instanceof BigInteger) {
                    result = (BigInteger) value;
                } else if(value instanceof String) {
                    result = new BigInteger((String) value);
                } else if(value instanceof Number) {
                    result = new BigInteger(String.valueOf(((Number) value)));
                } else {
                    //throw new ClassCastException("Not possible to coerce [" + value   + "] from class " + value.getClass() + " into a BigInteger.");
                    System.out.println("Not possible to coerce [" + value   + "] from class " + value.getClass() + " into a BigInteger.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal. " + e);
            } catch (ClassCastException e) {
                System.out.println("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal. " + e);
            } catch (Exception e) {
                System.out.println("Exception caught. " + e);
            }
        }
        return result;
    }


    public Object[][] getDataFromExcel(String excelFilePath, String sheetName) {
        try {
            FileInputStream fs = new FileInputStream(excelFilePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fs);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            int rowNum = sheet.getLastRowNum();

            Object[][] excelData = new Object[rowNum][sheet.getRow(1).getLastCellNum()];
            System.out.println(rowNum);

            for (int i = 0; i < rowNum; i++) {
                Row row = sheet.getRow(i);
                int colNum = row.getLastCellNum();
                System.out.println(colNum);
                for (int j = 0; j < colNum; j++) {
                    try {
                        XSSFCell cell = sheet.getRow(i).getCell(j);
                        switch (cell.getCellType()) {

                            case STRING:
                                excelData[i][j] = sheet.getRow(i).getCell(j).getStringCellValue();
                                System.out.println(excelData[i][1]+ "   ->  " + excelData[0][j] + "     ->  " + excelData[i][j]);
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    excelData[i][j] = sheet.getRow(i).getCell(j).getDateCellValue();
                                    System.out.println(excelData[i][1]+ "   ->  " + excelData[0][j] + "     ->  " + excelData[i][j]);
                                } else {
                                    excelData[i][j] = new BigDecimal(sheet.getRow(i).getCell(j).getNumericCellValue()).toPlainString();
                                    System.out.println(excelData[i][1]+ "   ->  " + excelData[0][j] + "     ->  " + excelData[i][j]);
                                }
                                break;
                            case BOOLEAN:
                                excelData[i][j] = sheet.getRow(i).getCell(j).getBooleanCellValue();
                                System.out.println(excelData[i][1]+ "   ->  " + excelData[0][j] + "     ->  " + excelData[i][j]);
                                break;
                            case FORMULA:
                                excelData[i][j] = sheet.getRow(i).getCell(j).getCellFormula();
                                System.out.println(excelData[i][1]+ "   ->  " + excelData[0][j] + "     ->  " + excelData[i][j]);
                                break;
                            case ERROR:
                                excelData[i][j] = sheet.getRow(i).getCell(j).getErrorCellString();
                                System.out.println(excelData[i][1]+ "   ->  " + excelData[0][j] + "     ->  " + excelData[i][j]);
                            default:
                                excelData[i][j] = sheet.getRow(i).getCell(j).getStringCellValue();
                                System.out.println(excelData[i][1]+ "   ->  " + excelData[0][j] + "     ->  " + excelData[i][j]);
                        }
                    } catch (NullPointerException ex) {
                        i++;
                        j=0;
                    }
                }
            }
            return excelData;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public void writeMySqlToExcelFile(Object[][] mySqlResults) {
        File file = new File(EXCELOUTPUTFILEPATH);
        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("mysql");

        int rowNum = 0;
        System.out.println("Creating Excel tab with MySQL data");

        for (Object[] mySqlResult : mySqlResults) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : mySqlResult) {
//                if (colNum == 0) {
//                    Cell cell = row.createCell(colNum++);
//                    if (field instanceof BigInteger) {
//                        cell.setCellValue(getBigInteger(getAgentId(field)).intValue());
//                    }
//                    else if (field instanceof Integer){
//                        cell.setCellValue(Integer.parseInt(getAgentId(field)));
//                    }
//                    else if (field instanceof String){
//                        cell.setCellValue(getAgentId(field));
//                    }
//                } else
//                    {
                    Cell cell = row.createCell(colNum++);
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    } else if (field instanceof BigInteger) {
                        cell.setCellValue(((BigInteger) field).intValue());
                    }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(EXCELOUTPUTFILEPATH);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Export from MySQL to excel is done successfully!");
    }

    public void addMySqlDataToExcelFile(Object[][] databaseResults, String excelPath) throws IOException, InvalidFormatException {
        FileInputStream inputStream = new FileInputStream(new File(excelPath));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.createSheet("mysql");

        int rowNum = 0;
        System.out.println("Creating Excel tab with MySQL data");

        for (Object[] databaseResult : databaseResults) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : databaseResult) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof BigInteger) {
                    cell.setCellValue(((BigInteger) field).intValue());
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(EXCELOUTPUTFILEPATH);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Export from MySQL to excel is done successfully!");
    }

    public void writePostgresToExcelFile(Object[][] postgresResults) throws IOException {


        FileInputStream inputStream = new FileInputStream(new File(EXCELOUTPUTFILEPATH));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.createSheet("postgres");

        int rowNum = 0;
        System.out.println("Creating Excel tab with Postgres data");

        for (Object[] postgresResult : postgresResults) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : postgresResult) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof BigInteger) {
                    cell.setCellValue(((BigInteger) field).intValue());
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(EXCELOUTPUTFILEPATH);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Export from Postgres to excel is done successfully!");
    }

    private static String colNumberToAlphabetic(int colNumber) {
        int quot = colNumber / 26;
        int rem = colNumber % 26;
        char letter = (char) ((int) 'A' + rem);
        if (quot == 0) {
            return "" + letter;
        } else {
            return colNumberToAlphabetic(quot - 1) + letter;
        }
    }

    private static String getHeadersFormula(String expectedSheetName, String actualSheetName, int rowNumber, int columnNumber) {
        String columnName = colNumberToAlphabetic(columnNumber);
        return "IF(VLOOKUP(" + expectedSheetName + "!$A" + rowNumber + "," + actualSheetName + "!$A:$EZ,COLUMN(),FALSE)=" + expectedSheetName + "!" + columnName + rowNumber + "," + expectedSheetName + "!" + columnName + rowNumber + ",\"not match\")";
    }

    private static String getCompareFormula(String expectedSheetName, String actualSheetName, int rowNumber, int columnNumber) {
        String columnName = colNumberToAlphabetic(columnNumber);
        return "IF(VLOOKUP(" + expectedSheetName + "!$A" + rowNumber + "," + actualSheetName + "!$A:$EZ,COLUMN(),FALSE)=" + expectedSheetName + "!" + columnName + rowNumber + ",\"\",CONCATENATE(\"" + expectedSheetName + ": \"," + expectedSheetName + "!" + columnName + rowNumber + ",CHAR(10),\" " + actualSheetName + ": \", " + actualSheetName + "!" + columnName + rowNumber + "))";
    }

    private static String getErrorsCountFormula(int numberOfRows, int columnNumber, String searchString) {
        String columnName = colNumberToAlphabetic(columnNumber);
        return "COUNTIF(" + columnName + "3:" + columnName + numberOfRows + ",\"*" + searchString + "*\")";
    }

    public void writeDiscrepanciesToExcelFile(Object[][] mySqlData, Object[][] postgreData, String expectedTabName, String actualTabName) throws IOException {

        System.out.println("Started comparing");

//        int numberOfRows = (mySqlData.length+1 > postgreData.length+1) ? mySqlData.length+1 : postgreData.length + 1;
//        int numberOfColumns = (mySqlData[0].length > postgreData[0].length) ? mySqlData[0].length : postgreData[0].length;

        int numberOfRows = mySqlData.length + 1;
        int numberOfColumns = mySqlData[0].length;

        FileInputStream inputStream = new FileInputStream(new File(EXCELOUTPUTFILEPATH));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.createSheet("compare");

        for (int i = 0; i < numberOfRows; i++) {
            Row row = sheet.createRow(i);
            if (i == 0) {
                for (int j = 1; j < numberOfColumns; j++) {
                    Cell errorCountCell = row.createCell(j);
                    errorCountCell.setCellFormula(getErrorsCountFormula(numberOfRows, j, actualTabName));
                }
            } else if (i == 1) {
                for (int j = 0; j < numberOfColumns; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellFormula(getHeadersFormula(expectedTabName, actualTabName, i, j));
                }
            } else {
                Cell cell = row.createCell(0);
                cell.setCellFormula(getHeadersFormula(expectedTabName, actualTabName, i, 0));

                for (int j = 1; j < numberOfColumns; j++) {
                    Cell compareCell = row.createCell(j);
//                System.out.println(getCompareFormula("mysql", "postgres", i, j));
                    compareCell.setCellFormula(getCompareFormula(expectedTabName, actualTabName, i, j));
                }
            }
        }

        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor((short) 10);
        font.setFontHeight((short) 500);

        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
        cellStyle.setFont(font);

        Row sumOfDiscrepanciesRow = sheet.getRow(0);
        sumOfDiscrepanciesRow.setHeight((short) 625);
        Cell sumOfDiscrepanciesCell = sumOfDiscrepanciesRow.createCell(0);
        sumOfDiscrepanciesCell.setCellStyle(cellStyle);
        sumOfDiscrepanciesCell.setCellFormula("SUM(B1:" + colNumberToAlphabetic(numberOfColumns) + "1)");

        try {
            FileOutputStream outputStream = new FileOutputStream(EXCELOUTPUTFILEPATH);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Comparing is done successfully!");
    }
}