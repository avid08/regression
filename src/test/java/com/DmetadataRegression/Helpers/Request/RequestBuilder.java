package com.DmetadataRegression.Helpers.Request;

public interface RequestBuilder {
    RequestBody build();

    RequestBuilder setDate(String date);

    RequestBuilder setFields(String... field);

    RequestBuilder setEntities(Long id);

    RequestBuilder setIssues(Long id);

    RequestBuilder setOptions(String option, String value);
}
