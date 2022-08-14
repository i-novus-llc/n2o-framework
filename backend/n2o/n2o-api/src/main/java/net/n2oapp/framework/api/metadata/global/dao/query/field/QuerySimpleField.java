package net.n2oapp.framework.api.metadata.global.dao.query.field;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;

/**
 * Модель простого поля запроса
 */
@Getter
@Setter
public class QuerySimpleField extends AbstractField implements NameAware {
    public static final String PK = "id";

    private String name;
    private String domain;
    @Deprecated
    private String expression;
    private String sortingExpression;
    private String sortingMapping;
    private String selectExpression;
    private String defaultValue;
    @Deprecated
    private N2oQuery.Filter[] filterList;

    private String joinBody;
    private Boolean noSorting;
    private Boolean noDisplay;

    private Boolean noJoin;

    public QuerySimpleField(QuerySimpleField field) {
        super(field);
        this.name = field.getName();
        this.domain = field.getDomain();
        this.sortingExpression = field.getSortingExpression();
        this.sortingMapping = field.getSortingMapping();
        this.selectExpression = field.getSelectExpression();
        this.defaultValue = field.getDefaultValue();
        this.joinBody = field.getJoinBody();
        this.noSorting = field.getNoSorting();
        this.noDisplay = field.getNoDisplay();
        this.noJoin = field.getNoJoin();
        this.expression= field.getExpression();
        this.filterList = field.getFilterList();
    }

    public QuerySimpleField(String id) {
        setId(id);
    }

    public QuerySimpleField() {
    }

    public Boolean getHasSorting() {
        return noSorting == null ? null : !noSorting;
    }

    public void setHasSorting(Boolean hasSorting) {
        this.noSorting = !hasSorting;
    }

    public Boolean getHasSelect() {
        return noDisplay == null ? null : !noDisplay;
    }

    public void setHasSelect(Boolean hasDisplay) {
        this.noDisplay = !hasDisplay;
    }

    public Boolean getHasJoin() {
        return noJoin == null ? null : !noJoin;
    }

    public void setHasJoin(Boolean hasJoin) {
        this.noJoin = !hasJoin;
    }
}
