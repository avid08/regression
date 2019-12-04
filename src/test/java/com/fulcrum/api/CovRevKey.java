package com.fulcrum.api;

public class CovRevKey {

    private Object id;
    private Object entityId;

    public Object getId() {
        return id;
    }

    public Object getEntityId() {
        return entityId;
    }

    public CovRevKey(Object id, Object entityId) {
        this.id = id;
        this.entityId = entityId;
    }
}
