package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Исходная модель филдсета с динамическим числом полей
 */
@Getter
@Setter
@VisualComponent
public class N2oMultiFieldSet extends N2oFieldSet {
    @VisualAttribute
    private String childrenLabel;
    @VisualAttribute
    private String firstChildrenLabel;
    @VisualAttribute
    private String addButtonLabel;
    @VisualAttribute
    private String removeAllButtonLabel;
    @VisualAttribute
    private Boolean canRemoveFirst;
    @VisualAttribute
    private Boolean canAdd;
    @VisualAttribute
    private Boolean canRemove;
    @VisualAttribute
    private Boolean canRemoveAll;
    @VisualAttribute
    private Boolean canCopy;
    @VisualAttribute
    private String primaryKey;
    @VisualAttribute
    private Boolean generatePrimaryKey;
}
