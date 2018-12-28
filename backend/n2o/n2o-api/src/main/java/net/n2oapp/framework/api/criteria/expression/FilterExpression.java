package net.n2oapp.framework.api.criteria.expression;

import net.n2oapp.criteria.filters.FilterType;

/**
 * User: operehod
 * Date: 07.11.2014
 * Time: 12:08
 */
public class FilterExpression extends FieldExpression {

    private FilterType filterType;
    private String domain;

    public FilterExpression(String filterFieldId, String expression, FilterType filterType, String domain) {
        super(filterFieldId, expression);
        this.filterType = filterType;
        this.domain = domain;
    }

    public FilterExpression(String filterFieldId, String expression) {
        super(filterFieldId, expression);
        this.filterType = FilterType.eq;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public String getFilterFieldId() {
        return super.fieldId;
    }

    public String getDomain() {
        return domain;
    }
}
