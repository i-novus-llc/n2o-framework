package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.control.N2oListField;

/**
 * Компонент выбора в выпадающем списке в виде дерева
 */
@Getter
@Setter
public class N2oSelectTree extends N2oListField {
    @VisualAttribute
    private Boolean ajax;
    @VisualAttribute
    private Integer size;
    @VisualAttribute
    private String parentFieldId;
    @VisualAttribute
    private String parentIsNullFieldId;
    @VisualAttribute
    private String hasChildrenFieldId;
    @VisualAttribute
    private Boolean search;
    @VisualAttribute
    private Boolean checkboxes;
    @VisualAttribute
    private CheckingStrategy checkingStrategy;
    @VisualAttribute
    private Integer maxTagCount;

    @Override
    public boolean isSingle() {
        return checkboxes == null || !checkboxes;
    }
}
