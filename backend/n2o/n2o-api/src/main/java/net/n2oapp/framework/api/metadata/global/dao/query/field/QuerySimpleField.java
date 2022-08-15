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
    private String defaultValue;
    private String sortingExpression;
    private String sortingMapping;
    private Boolean isSorted;
    @Deprecated
    private N2oQuery.Filter[] filterList;

    public QuerySimpleField(QuerySimpleField field) {
        super(field);
        this.name = field.getName();
        this.domain = field.getDomain();
        this.defaultValue = field.getDefaultValue();
        this.filterList = field.getFilterList();
        this.sortingExpression = field.getSortingExpression();
        this.sortingMapping = field.getSortingMapping();
        this.isSorted = field.getIsSorted();
    }

    public QuerySimpleField(String id) {
        setId(id);
    }

    public QuerySimpleField() {
    }
}
