package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oListField;

/**
 * Компонент выбора в выпадающем списке в виде дерева
 */
@Getter
@Setter
public class N2oSelectTree extends N2oListField {
    private Boolean ajax;
    private Integer size;
    private String parentFieldId;
    private String parentIsNullFieldId;
    private String hasChildrenFieldId;
    private Boolean search;
    private Boolean checkboxes;
    private CheckingStrategy checkingStrategy;
    private Integer maxTagCount;

    @Override
    public boolean isSingle() {
        return checkboxes == null || !checkboxes;
    }
}
