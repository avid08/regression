package com.fulcrum.api;

import java.util.Objects;

public class ConfigSchemaKey {

    private Object attributeName;
    private Object sourceCategory;
    private Object informationProvenance;

    public ConfigSchemaKey(Object attributeName, Object sourceCategory, Object informationProvenance) {
        this.attributeName = attributeName;
        this.sourceCategory = sourceCategory;
        this.informationProvenance = informationProvenance;
    }

    public Object getAttributeName() {
        return attributeName;
    }

    public Object getSourceCategory() {
        return sourceCategory;
    }

    public Object getInformationProvenance() {
        return informationProvenance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigSchemaKey that = (ConfigSchemaKey) o;
        return Objects.equals(attributeName, that.attributeName) &&
                Objects.equals(sourceCategory, that.sourceCategory) &&
                Objects.equals(informationProvenance, that.informationProvenance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeName, sourceCategory, informationProvenance);
    }
}
