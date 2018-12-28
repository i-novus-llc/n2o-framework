package net.n2oapp.framework.api.metadata.global.view.widget;

import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Режим формы
 */
public enum FormMode implements IdAware {
    ONE_MODEL("one-model"), TWO_MODELS("two-models");

    private String id;

    FormMode(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
    }
}
