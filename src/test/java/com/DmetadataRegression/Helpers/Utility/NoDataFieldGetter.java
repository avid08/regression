package com.DmetadataRegression.Helpers.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

public class NoDataFieldGetter {

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 2) {
            String path = "src/test/resources/Metadata/" + args[0] + "/" + args[0] + args[1] + ".mngb";
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            TestDataList testDataList = new Gson().fromJson(br, TestDataList.class);
            testDataList.recordList.forEach(fieldMapTestRecordPair -> {
                if (!fieldMapTestRecordPair.getValue().hasData())
                    System.out.println(fieldMapTestRecordPair.getKey().fieldID);
            });
        } else {
            String path1 = "src/test/resources/Metadata/" + args[0] + "/" + args[0] + args[1] + ".mngb";
            String path2 = "src/test/resources/Metadata/" + args[0] + "/" + args[0] + args[2] + ".mngb";
            TestDataList testDataList1 = new Gson().fromJson(new BufferedReader(new FileReader(new File(path1))),
                    TestDataList.class);
            TestDataList testDataList2 = new Gson().fromJson(new BufferedReader(new FileReader(new File(path2))),
                    TestDataList.class);
            Set<String> missingInFirstEnv = new HashSet<>();
            Set<String> missingInSecondEnv = new HashSet<>();
            testDataList1.recordList.forEach(dataPoint -> {
                if (!dataPoint.getValue().hasData())
                    missingInFirstEnv.add(dataPoint.getKey().fieldID);
            });
            testDataList2.recordList.forEach(dataPoint -> {
                if (!dataPoint.getValue().hasData())
                    missingInSecondEnv.add(dataPoint.getKey().fieldID);
            });
            System.out.println("Missing in " + args[1] + ":");
            missingInFirstEnv.forEach(System.out::println);
            missingInFirstEnv.forEach(missingInSecondEnv::remove);
            System.out.println("\nMissing in " + args[2] + " but present in " + args[1] + ":");
            missingInSecondEnv.forEach(System.out::println);
        }
    }
}
