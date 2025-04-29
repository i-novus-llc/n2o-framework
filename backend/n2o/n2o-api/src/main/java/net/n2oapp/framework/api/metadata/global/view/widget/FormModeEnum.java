package net.n2oapp.framework.api.metadata.global.view.widget;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Режим формы
 */
public enum FormModeEnum implements IdAware {
    ONE_MODEL("one-model"), TWO_MODELS("two-models");

    private String id;

    FormModeEnum(String id) {
        this.id = id;
    }

    @Override
    @JsonValue
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
    }
}
