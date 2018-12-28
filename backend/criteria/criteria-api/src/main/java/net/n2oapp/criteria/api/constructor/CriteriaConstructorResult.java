package net.n2oapp.criteria.api.constructor;

import net.n2oapp.criteria.api.Direction;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 20.12.11
 * Time: 16:06
 */
public class CriteriaConstructorResult implements Serializable {
    private String selectClause;
    private String whereClause;
    private String orderByClause;
    private String joinClause;

    private Map<String, Object> parameters = new LinkedHashMap<String, Object>();

    public CriteriaConstructorResult() {
    }

    public CriteriaConstructorResult(String whereClause) {
        this(whereClause, new LinkedHashMap<String, Object>());
    }

    public CriteriaConstructorResult(String whereClause, Map<String, Object> parameters) {
        this.whereClause = whereClause;
        this.parameters = parameters;
    }

    public CriteriaConstructorResult addParameter(String name, Object value) {
        parameters.put(name, value);
        return this;
    }

    public CriteriaConstructorResult addParameter(Object value) {
        return addParameter("{0}", value);
    }


    public CriteriaConstructorResult addSearch(String search) {
        StringBuilder sb = new StringBuilder();
        if (notEmpty(whereClause)) {
            sb.append(whereClause)
                    .append(" and ");
        }
        whereClause = sb.append(search).toString();
        return this;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public CriteriaConstructorResult addSorting(String sortingQuery, Direction direction) {
        StringBuilder sb = new StringBuilder();
        if (notEmpty(orderByClause)) {
            sb.append(orderByClause)
                    .append(", ");
        }
        sb.append(sortingQuery);
        if (direction != null) {
            sb.append(' ').append(direction.getExpression());
        }
        orderByClause = sb.toString();
        return this;
    }

    public CriteriaConstructorResult addColumn(String columnQuery) {
        StringBuilder sb = new StringBuilder();
        if (notEmpty(selectClause)) {
            sb.append(selectClause)
                    .append(", ");
        }
        sb.append(columnQuery);
        selectClause = sb.toString();
        return this;
    }

    public CriteriaConstructorResult addJoin(String join) {
        StringBuilder sb = new StringBuilder();
        if (notEmpty(joinClause)) {
            sb.append(joinClause).append(" ");
        }
        sb.append(join);
        joinClause = sb.toString();
        return this;
    }

    private boolean notEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public String getWhereClause() {
        return whereClause;
    }

    public String getQueryStringFormat() {
        return whereClause;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public String getSelectClause() {
        return selectClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public String getJoinClause() {
        return joinClause;
    }
}
