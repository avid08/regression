package com.fulcrum.api;

public class FulcrumKey {

    private Object agentId;
    private Object fitchFieldId;

    public FulcrumKey(Object agentId, Object fitchFieldId) {
        this.agentId = agentId;
        this.fitchFieldId = fitchFieldId;
    }

    public FulcrumKey(Object agentId) {
        this.agentId=agentId;
    }

    public Object getAgentId() {
        return agentId;
    }

    public Object getFitchFieldId() {
        return fitchFieldId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FulcrumKey)) return false;
        FulcrumKey fulcrumKey = (FulcrumKey) o;
        return agentId == fulcrumKey.agentId && fitchFieldId == fulcrumKey.fitchFieldId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((agentId == null) ? 0 : agentId.hashCode());
        return result;
    }
}
