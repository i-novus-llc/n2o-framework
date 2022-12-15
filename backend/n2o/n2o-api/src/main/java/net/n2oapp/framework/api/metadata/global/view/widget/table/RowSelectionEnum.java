package net.n2oapp.framework.api.metadata.global.view.widget.table;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Вариант выбора строки таблицы
 */
public enum RowSelectionEnum implements IdAware {
    NONE("none"),
    ACTIVE("active"),
    RADIO("radio"),
    CHECKBOX("checkbox");

    private final String value;

    RowSelectionEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getId() {
        return this.value;
    }

    @Override
    public void setId(String id) {
        throw new UnsupportedOperationException();
    }
}
