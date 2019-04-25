package com.DmetadataRegression.Helpers.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.DmetadataRegression.Helpers.MetaType;
import com.DmetadataRegression.Helpers.Mongo.MongoFileBuffer;
import com.DmetadataRegression.Helpers.TestRecord.TestRecord;
import com.google.gson.Gson;

import javafx.util.Pair;

public class DeleteNoDataFields {
    public static void main(String[] args) throws IOException {
        String path = "src/test/resources/Metadata/" + args[0] + "/" + args[0] + System.getProperty("env") + ".mngb";
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        TestDataList testDataList = new Gson().fromJson(br, TestDataList.class);
        List<Pair<MetaType.FieldMap, TestRecord>> noDataList = new ArrayList<>();
        testDataList.recordList.forEach(fieldMapTestRecordPair -> {
            if (!fieldMapTestRecordPair.getValue().hasData())
                noDataList.add(fieldMapTestRecordPair);
        });
        MongoFileBuffer mongoFileBuffer = MongoFileBuffer.getInstance(args[0]);
        noDataList.forEach(dataPoint -> {
            mongoFileBuffer.deleteRecord(dataPoint.getKey());
        });
    }
}
