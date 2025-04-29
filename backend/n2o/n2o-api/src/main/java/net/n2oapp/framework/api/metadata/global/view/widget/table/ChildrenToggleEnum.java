package net.n2oapp.framework.api.metadata.global.view.widget.table;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Виды отображения дочерних записей таблицы
 */
public enum ChildrenToggleEnum implements IdAware {
    COLLAPSE("collapse"),   // свернутый
    EXPAND("expand");       // раскрытый

    private final String value;

    ChildrenToggleEnum(String value) {
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
