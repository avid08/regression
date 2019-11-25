package com.fulcrum.api;

public class CSBondsKey extends FulcrumKey {

    private Object bondId;

    public CSBondsKey(Object agentId, Object fitchFieldId, Object bondId) {
        super(agentId, fitchFieldId);
        this.bondId=bondId;
    }
}
