package com.DmetadataRegression.Helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.io.Resources;

public class MetadataConfig {
    static class SheetData {
        int rowNumPermissionsRequired;
        XSSFSheet sheet;
        int rowNumFitchFieldId;
        int rowNumDataType;
        int rowNumLookupSource0Name;
        int rowNumLookupSource0Value;
        int rowNumLookupSource1Name;
        int rowNumLookupSource1Value;
    }

    private static SheetData getMetaSheet(String metaName) throws IOException, URISyntaxException {
        SheetData sheetData = new SheetData();
        File folder = new File("src/test/resources/Metadata/" + metaName + "/");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles.length < 1) {
            throw new IOException("No files in directory");
        }
        //String filename = listOfFiles[0].getName();
        String filename = "";
        for (File file :
                listOfFiles) {
            if (file.getName().contains("xlsx")) filename = file.getName();
        }

        URL fileUrl = Resources.getResource("Metadata/" + metaName + "/" + filename);
        System.out.println(fileUrl);
        File src = new File(fileUrl.toURI());

        FileInputStream file = new FileInputStream(src);

        XSSFWorkbook wb = new XSSFWorkbook(file);
        sheetData.sheet = wb.getSheet(wb.getSheetName(0));

        for (Cell cell :
                sheetData.sheet.getRow(0)) {
            switch (cell.getStringCellValue()) {
                case "fitchFieldId":
                    sheetData.rowNumFitchFieldId = cell.getColumnIndex();
                    break;
                case "dataType":
                    sheetData.rowNumDataType = cell.getColumnIndex();
                    break;
                case "lookupSources.0.name":
                    sheetData.rowNumLookupSource0Name = cell.getColumnIndex();
                    break;
                case "lookupSources.0.value":
                    sheetData.rowNumLookupSource0Value = cell.getColumnIndex();
                    break;
                case "lookupSources.1.name":
                    sheetData.rowNumLookupSource1Name = cell.getColumnIndex();
                    break;
                case "lookupSources.1.value":
                    sheetData.rowNumLookupSource1Value = cell.getColumnIndex();
                    break;
                case "permissionsRequired.0":
                    sheetData.rowNumPermissionsRequired = cell.getColumnIndex();
                    break;

            }
        }

        return sheetData;
    }

    public static List<MetaType.FieldMap> getListOfLookups(String metaName) throws IOException, URISyntaxException {
        SheetData sheetData = getMetaSheet(metaName);
        List<MetaType.FieldMap> fieldMapList = new ArrayList<>();
        XSSFSheet sheet = sheetData.sheet;
        DataFormatter df = new DataFormatter();
        int rowCount = sheet.getPhysicalNumberOfRows();
        System.out.println(rowCount - 1 + " fields are available");
        for (int i = 1; i < rowCount; i++) {
            MetaType.FieldMap fieldMap = new MetaType.FieldMap();
            fieldMap.fieldID = sheet.getRow(i).getCell(sheetData.rowNumFitchFieldId).getStringCellValue();
            fieldMap.dataType = sheet.getRow(i).getCell(sheetData.rowNumDataType).getStringCellValue();
            fieldMap.permissionsRequired = sheet.getRow(i).getCell(sheetData.rowNumPermissionsRequired).getStringCellValue();
            fieldMap.lookUpSourceList.add(new MetaType.LookUpSource(
                    sheet.getRow(i).getCell(sheetData.rowNumLookupSource0Name) == null ?
                            "null" : df.formatCellValue(sheet.getRow(i).getCell(sheetData.rowNumLookupSource0Name)),
                    sheet.getRow(i).getCell(sheetData.rowNumLookupSource0Value) == null ?
                            "null" : df.formatCellValue(sheet.getRow(i).getCell(sheetData.rowNumLookupSource0Value))));
            fieldMap.lookUpSourceList.add(new MetaType.LookUpSource(
                    sheet.getRow(i).getCell(sheetData.rowNumLookupSource1Name) == null ?
                            "null" : df.formatCellValue(sheet.getRow(i).getCell(sheetData.rowNumLookupSource1Name)),
                    sheet.getRow(i).getCell(sheetData.rowNumLookupSource1Value) == null ?
                            "null" : df.formatCellValue(sheet.getRow(i).getCell(sheetData.rowNumLookupSource1Value))));
            fieldMapList.add(fieldMap);
        }
        return fieldMapList;
    }
}
