package net.n2oapp.framework.api.metadata.global.dao.query;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.NameAware;

/**
 * Модель простого поля запроса
 */
@Getter
@Setter
public class SimpleField extends AbstractField implements NameAware {
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

    public SimpleField(String id) {
        setId(id);
    }

    public SimpleField() {
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
