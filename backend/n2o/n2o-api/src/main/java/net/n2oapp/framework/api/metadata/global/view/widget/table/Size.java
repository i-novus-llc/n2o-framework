package net.n2oapp.framework.api.metadata.global.view.widget.table;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Размер компонента
 */
public enum Size implements IdAware {
    SM("sm"),
    LG("lg");

    private final String value;

    Size(String value) {
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
