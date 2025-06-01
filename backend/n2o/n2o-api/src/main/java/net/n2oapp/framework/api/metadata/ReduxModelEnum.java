package net.n2oapp.framework.api.metadata;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Модель на клиенте
 */
public enum ReduxModelEnum implements IdAware {
    RESOLVE("resolve"),
    FILTER("filter"),
    SELECTED("selected"),
    EDIT("edit"),
    MULTI("multi"),
    DATASOURCE("datasource");

    private final String id;

    ReduxModelEnum(String id) {
        this.id = id;
    }

    @Override
    public void setId(String id) {
        // no implementation
    }

    @Override
    @JsonValue
    public String getId() {
        return id;
    }
}
