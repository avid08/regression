package com.fulcrum.api;

public class LFIBondsKey extends FulcrumKey {

    private Object lfyHyId;

    public LFIBondsKey(Object agentId, Object fitchFieldId, Object lfyHyId) {
        super(agentId, fitchFieldId);
        this.lfyHyId = lfyHyId;
    }
}
