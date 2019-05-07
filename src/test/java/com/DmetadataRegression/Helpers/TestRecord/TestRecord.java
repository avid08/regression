package com.DmetadataRegression.Helpers.TestRecord;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class TestRecord {
    private Boolean noData = null;
    private String date;
    private Long testID;
    private String testValue;
    private List<Pair<String, String>> options = null;

    public TestRecord setNoData() {
        this.noData = true;
        return this;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTestID() {
        return testID;
    }

    public void setTestID(Long testID) {
        this.testID = testID;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }

    public List<Pair<String, String>> getOptions() {
        return options;
    }

    public void setOptions(Pair<String, String> option) {
        if (this.options == null) this.options = new ArrayList<>();
        this.options.add(option);
    }

    public boolean hasData() {
        return this.noData == null;
    }
}
