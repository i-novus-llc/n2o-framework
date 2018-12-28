package net.n2oapp.framework.api.criteria;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;

/**
 * User: operehod
 * Date: 12.11.2014
 * Time: 11:32
 */
public class Restriction extends Filter {

    private String fieldId;

    public Restriction(String fieldId, Filter filter) {
        super(filter);
        this.fieldId = fieldId;
    }

    public Restriction(String fieldId, Object value, FilterType type) {
        super(value, type);
        this.fieldId = fieldId;
    }

    public Restriction(String fieldId, Object value) {
        super(value);
        this.fieldId = fieldId;
    }

    public String getFieldId() {
        return fieldId;
    }
}
