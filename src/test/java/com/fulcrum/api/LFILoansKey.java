package com.fulcrum.api;

public class LFILoansKey extends FulcrumKey {

    private Object agentId;
    private Object fitchFieldId;

    public LFILoansKey(Object agentId, Object fitchFieldId) {
        super(agentId, fitchFieldId);
    }
}
