package com.fulcrum.api;

public class CSLoansKey extends FulcrumKey {

    private Object fc_tlb_euro;

    public CSLoansKey(Object agentId, Object fc_tlb_euro) {
        super(agentId);
        this.fc_tlb_euro = fc_tlb_euro;
    }

    @Override
    public Object getAgentId() {
        return super.getAgentId();
    }

    public Object getFcTlbEuro() {
        return fc_tlb_euro;
    }
}
