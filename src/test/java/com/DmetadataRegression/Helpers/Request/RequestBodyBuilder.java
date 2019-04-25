package com.DmetadataRegression.Helpers.Request;

import java.util.Arrays;

public class RequestBodyBuilder implements RequestBuilder {

    private final RequestBody requestBody;
    private boolean hasEntities;
    private boolean hasIssues;

    public RequestBodyBuilder() {
        requestBody = new RequestBody();
    }

    @Override
    public RequestBody build() {
        return requestBody;
    }

    @Override
    public RequestBuilder setDate(String date) {
        requestBody.data.attributes.dateOptions.dates.add(date);
        return this;
    }

    @Override
    public RequestBuilder setFields(String... fields) {
        requestBody.data.attributes.fitchFieldIds.addAll(Arrays.asList(fields));
        return this;
    }

    @Override
    public RequestBuilder setEntities(Long id) {
        if (!hasEntities) {
            hasEntities=true;
            requestBody.data.attributes.setEntities();
        }
        requestBody.data.attributes.entities.add(new RequestBody.Entity(id));
        return this;
    }

    @Override
    public RequestBuilder setIssues(Long id) {
        if(!hasIssues){
            hasIssues=true;
            requestBody.data.attributes.setIssues();
        }
        requestBody.data.attributes.issues.add(new RequestBody.Issue(id));
        return this;
    }
    @Override
    public RequestBuilder setOptions(String option, String value){
        requestBody.data.attributes.options.add(requestBody.new Option(option,value));
        return this;
    }
}
