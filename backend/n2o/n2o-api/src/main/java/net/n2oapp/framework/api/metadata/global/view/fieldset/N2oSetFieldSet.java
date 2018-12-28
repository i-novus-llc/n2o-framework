package net.n2oapp.framework.api.metadata.global.view.fieldset;

/**
 * Исходная модель прозрачного набора полей.
 */
public class N2oSetFieldSet extends N2oFieldSet {
    public static final String DEFAULT_SRC = "StandardFieldset";

    public N2oSetFieldSet() {
        setSrc(DEFAULT_SRC);
    }
}
