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
    private String addButtonLabel;
    private String removeAllButtonLabel;
    private Boolean canRemoveFirst;
    private Boolean canAdd;
    private Boolean canRemove;
    private Boolean canRemoveAll;
    private Boolean canCopy;
}
