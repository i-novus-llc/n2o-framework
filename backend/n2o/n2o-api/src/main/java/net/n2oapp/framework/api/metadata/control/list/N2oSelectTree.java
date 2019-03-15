package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.multi.N2oMultiListField;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.GroupingNodes;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.InheritanceNodes;

/**
 * Компонент ввода select-tree
 */
@Getter
@Setter
public class N2oSelectTree extends N2oListField implements N2oSingleListField, N2oMultiListField {
    private Boolean ajax;
    private InheritanceNodes inheritanceNodes;
    private GroupingNodes groupingNodes;
    private Integer size;
    private String parentFieldId;
    private String parentIsNullFieldId;
    private String hasChildrenFieldId;
    private Boolean search;
    private Boolean checkboxes;
    private CheckingStrategy checkingStrategy;
    private Integer maxTagCount;


    private boolean hasCheckboxes() {
        return checkboxes != null && checkboxes;
    }

    @Override
    public boolean isSingle() {
        return checkboxes == null || !checkboxes;
    }
}
