package net.n2oapp.framework.access.api.model.filter;

import net.n2oapp.criteria.filters.FilterType;

import java.util.List;

/**
 * Фильтр доступа
 */
public class N2oAccessFilter {

    private String fieldId;
    private String value;
    private FilterType type;
    private List<String> values;

    public N2oAccessFilter() {
    }

    public N2oAccessFilter(String fieldId, FilterType type) {
        this.fieldId = fieldId;
        this.type = type;
    }


    public N2oAccessFilter(String fieldId, String value, FilterType type) {
        this.fieldId = fieldId;
        this.type = type;
        this.value = value;
    }

    public N2oAccessFilter(String fieldId, List<String> values, FilterType type) {
        this.fieldId = fieldId;
        this.type = type;
        this.values = values;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        N2oAccessFilter that = (N2oAccessFilter) o;

        if (fieldId != null ? !fieldId.equals(that.fieldId) : that.fieldId != null) return false;
        if (type != that.type) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (values != null ? !values.equals(that.values) : that.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fieldId != null ? fieldId.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }
}
