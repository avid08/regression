package com.fulcrum.api;

public class CSBondsKey extends FulcrumKey {

    private Object bondId;

    public CSBondsKey(Object agentId, Object fitchFieldId, Object bondId) {
        super(agentId, fitchFieldId);
        this.bondId=bondId;
    }

    public CSBondsKey(Object agentId, Object bondId){
        super(agentId);
        this.bondId=bondId;
    }

    @Override
    public Object getAgentId() {
        return super.getAgentId();
    }

    public Object getBondId() {
        return bondId;
    }
}
