package com.fulcrum.api;

public class FulcrumPostgresKey {

    private Object securityId;
    private Object fieldId;

    public FulcrumPostgresKey(Object securityId, Object fieldId) {
        this.securityId = securityId;
        this.fieldId = fieldId;
    }

    public Object getSecurityId() {
        return securityId;
    }

    public Object getFieldId() {
        return fieldId;
    }
}
