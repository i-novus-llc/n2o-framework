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
    private String defaultValue;
    @Deprecated
    private N2oQuery.Filter[] filterList;

    public SimpleField(SimpleField field) {
        super(field);
        this.name = field.getName();
        this.domain = field.getDomain();
        this.defaultValue = field.getDefaultValue();
        this.filterList = field.getFilterList();
    }

    public SimpleField(String id) {
        setId(id);
    }

    public SimpleField() {
    }
}
