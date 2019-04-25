package com.DmetadataRegression.Helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.bson.Document;

import com.DmetadataRegression.Helpers.TestRecord.TestRecord;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;

import javafx.util.Pair;

public interface MetaType {


    TestRecord convertDocToRecord(Document document, String testValue);

    class FieldMap {
        public final List<LookUpSource> lookUpSourceList = new ArrayList<>();
        public String fieldID;
        public String dataType;
        public State state = State.NoRecord;
        public String permissionsRequired;

        public enum State {
            NoRecord, Recorded, RecordedNotFoundFirstTime, RecordedThenForceUpdated
        }
    }


    TestRecord getDataFromMongo(FieldMap fieldMap, String metaName) throws IOException;

    Pair<String, Response> getDataFromAPI(TestRecord testRecord, FieldMap fieldMap, List<Header> headerList, String url);

    static String compareData(Response response, TestRecord testRecord) {
        int[] statusCodes = {200, 206};
        if (ArrayUtils.contains(statusCodes, response.getStatusCode())) {
            Pattern pattern = Pattern.compile("\"value\":\\[(.*?)]");
            Matcher matcher = pattern.matcher(response.asString());
            if (matcher.find()) {
                String valueString = matcher.group(1);
                return valueString.contains(testRecord.getTestValue()) ? null : valueString;
            }
            return response.asString();
        } else {
            return response.asString();
        }
    }


    class LookUpSource {
        public final String name;
        public final String value;

        LookUpSource(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
