package net.n2oapp.framework.api.metadata.global.view.fieldset;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

public enum FieldLabelLocationEnum implements IdAware {
    TOP("top"),
    LEFT("left"),
    RIGHT("right");

    private final String value;

    FieldLabelLocationEnum(String value) {
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
