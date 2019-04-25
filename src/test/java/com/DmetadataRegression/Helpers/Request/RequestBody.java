package com.DmetadataRegression.Helpers.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestBody {


    public RequestBody() {

        data = new Data();
    }

    Data data;

    public class Data {
        Data() {
            attributes = new Attributes();
        }

        final String type = "valueRequest";
        Attributes attributes;

    }

    public class Attributes {
        Attributes() {
            fitchFieldIds = new ArrayList<>();
            dateOptions = new DateOptions();
            options = new ArrayList<>();
        }

        public void setEntities() {
            entities = new ArrayList<>();
        }

        public void setIssues() {
            issues = new ArrayList<>();
        }

        DateOptions dateOptions;
        List<Entity> entities;
        List<Issue> issues;
        List<String> fitchFieldIds;
        List<Option> options;
    }

    public class DateOptions {
        DateOptions() {
            dates = new ArrayList<>();
            periods = new ArrayList<>();
        }

        List<String> dates;
        List<String> periods;
    }

    public static class Entity {
        Entity(Long id) {
            this.id = id;
        }

        Long id;
        final String type = "fitchId";
    }

    public static class Issue {
        Issue(Long id) {
            this.id = id;
        }

        Long id;
        final String type = "fitchIssueId";
    }
    class Option {
        String name;
        String value;

        public Option(String option, String value) {
            this.name=option;
            this.value=value;
        }
    }
}
