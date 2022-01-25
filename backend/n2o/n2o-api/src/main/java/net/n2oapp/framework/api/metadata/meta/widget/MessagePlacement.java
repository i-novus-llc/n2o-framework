package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Положение сообщения при фиксации
 */
public enum MessagePlacement implements IdAware {
    top("top"),
    bottom("bottom"),
    topLeft("top-left"),
    topRight("top-right"),
    bottomLeft("bottom-left"),
    bottomRight("bottom-right");

    private String name;

    MessagePlacement(String name) {
        this.name = name;
    }

    @Override
    @JsonValue
    public String getId() {
        return name;
    }

    @Override
    public void setId(String id) {
        throw new UnsupportedOperationException();
    }
}
