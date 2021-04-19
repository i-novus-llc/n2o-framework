package net.n2oapp.criteria.dataset;


import java.util.Map;

/**
 * Модель маппинга поля операции
 */
public class FieldMapping {
    private String mapping;
    private Map<String, FieldMapping> childMapping;

    public FieldMapping() {
    }

    public FieldMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public Map<String, FieldMapping> getChildMapping() {
        return childMapping;
    }

    public void setChildMapping(Map<String, FieldMapping> childMapping) {
        this.childMapping = childMapping;
    }
}
