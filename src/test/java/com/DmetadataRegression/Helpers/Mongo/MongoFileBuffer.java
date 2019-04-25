package com.DmetadataRegression.Helpers.Mongo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.DmetadataRegression.Helpers.MetaType.FieldMap;
import com.DmetadataRegression.Helpers.TestRecord.TestRecord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.util.Pair;

public class MongoFileBuffer {
    private static final Map<String, MongoFileBuffer> fileBufferList = new HashMap<>();
    private TestDataList testDataList;
    private final File bufferFile;

    public static MongoFileBuffer getInstance(String metaName) throws IOException {
        if (fileBufferList.get(metaName) == null)
            fileBufferList.put(metaName, new MongoFileBuffer(metaName));
        return fileBufferList.get(metaName);

    }

    class TestDataList {
        final List<Pair<FieldMap, TestRecord>> recordList = new ArrayList<>();
    }

    private MongoFileBuffer(String metaName) throws IOException {
        String path = "src/test/resources/Metadata/" + metaName + "/" + metaName + System.getProperty("env") + ".mngb";
        bufferFile = new File(path);
        bufferFile.createNewFile();
        testDataList = getList();
    }

    public boolean hasField(FieldMap fieldMap) {
        TestRecord testRecord = getRecord(fieldMap);
        if (fieldMap.state == FieldMap.State.RecordedNotFoundFirstTime)
            return false;
        return testRecord != null;
    }

    public TestRecord getRecord(FieldMap fieldMap) {
        for (Pair<FieldMap, TestRecord> testPair :
                testDataList.recordList) {
            if (testPair.getKey().fieldID.equals(fieldMap.fieldID)) {
                fieldMap.state = fieldMap.state == FieldMap.State.NoRecord ? testPair.getKey().state : fieldMap.state;
                return testPair.getValue();
            }
        }
        return null;
    }

    public TestRecord insertRecord(FieldMap fieldMap, TestRecord testRecord) {
        if (fieldMap.state == FieldMap.State.RecordedNotFoundFirstTime)
            deleteRecord(fieldMap);
        fieldMap.state = FieldMap.State.RecordedThenForceUpdated;
        testDataList.recordList.add(new Pair<>(fieldMap, testRecord));
        writeList(testDataList);
        return testRecord;
    }

    public void deleteRecord(FieldMap fieldMap) {
        for (Pair<FieldMap, TestRecord> record :
                testDataList.recordList) {
            if (record.getKey().fieldID.equals(fieldMap.fieldID)) {
                testDataList.recordList.remove(record);
                break;
            }
        }
        writeList(testDataList);
    }

    public void updateCachedRecords() {
        testDataList.recordList.forEach(fieldMapTestRecordPair -> {
            if (fieldMapTestRecordPair.getKey().state == FieldMap.State.RecordedThenForceUpdated)
                fieldMapTestRecordPair.getKey().state = FieldMap.State.Recorded;
        });
        writeList(testDataList);
    }

    private TestDataList getList() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(bufferFile));
        TestDataList testDataListFromFile = new Gson().fromJson(br, TestDataList.class);
        return testDataListFromFile != null ? testDataListFromFile : new TestDataList();
    }

    private void writeList(TestDataList testDataList) {
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(bufferFile));
            fileWriter.write(new GsonBuilder().setPrettyPrinting().create().toJson(testDataList));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
