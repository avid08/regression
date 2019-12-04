package com.fulcrum.api;

public class LFIBondsKey extends FulcrumKey {

    private Object lfyHyId;

    public LFIBondsKey(Object agentId, Object fitchFieldId, Object lfyHyId) {
        super(agentId, fitchFieldId);
        this.lfyHyId = lfyHyId;
    }

    public LFIBondsKey(Object agentId, Object lfyHyId){
        super(agentId);
        this.lfyHyId = lfyHyId;
    }

    @Override
    public Object getAgentId() {
        return super.getAgentId();
    }

    public Object getLfyHyId() {
        return lfyHyId;
    }
}
