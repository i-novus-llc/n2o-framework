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
    private String sortingExpression;
    private String sortingMapping;
    private String selectExpression;
    private String defaultValue;
    @Deprecated
    private N2oQuery.Filter[] filterList;

    private Boolean isSorted;
    private Boolean isSelected;

    public QuerySimpleField(QuerySimpleField field) {
        super(field);
        this.name = field.getName();
        this.domain = field.getDomain();
        this.sortingExpression = field.getSortingExpression();
        this.sortingMapping = field.getSortingMapping();
        this.selectExpression = field.getSelectExpression();
        this.defaultValue = field.getDefaultValue();
        this.isSorted = field.getIsSorted();
        this.isSelected = field.getIsSelected();
        this.filterList = field.getFilterList();
    }

    public QuerySimpleField(String id) {
        setId(id);
    }

    public QuerySimpleField() {
    }
}
