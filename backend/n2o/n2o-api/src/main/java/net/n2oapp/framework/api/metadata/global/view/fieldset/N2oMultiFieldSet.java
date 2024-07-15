package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель филдсета с динамическим числом полей
 */
@Getter
@Setter
public class N2oMultiFieldSet extends N2oFieldSet {
    private String childrenLabel;
    private String firstChildrenLabel;
    private String canAdd;
    private String addButtonLabel;
    private String canRemove;
    private String canRemoveFirst;
    private String canRemoveAll;
    private String removeAllButtonLabel;
    private String canCopy;
    private String primaryKey;
    private Boolean generatePrimaryKey;
}
