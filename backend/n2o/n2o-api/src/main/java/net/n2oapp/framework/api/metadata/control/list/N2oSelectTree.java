package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.multi.N2oMultiListField;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.InheritanceNodes;

/**
 * Компонент выбора в выпадающем списке в виде дерева
 */
@Getter
@Setter
public class N2oSelectTree extends N2oListField implements N2oSingleListField, N2oMultiListField {
    private Boolean ajax;
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

    @Deprecated
    public void setInheritanceNodes(InheritanceNodes nodes) {
        setParentFieldId(nodes.getParentFieldId());
        setLabelFieldId(nodes.getLabelFieldId());
        setHasChildrenFieldId(nodes.getHasChildrenFieldId());
        setQueryId(nodes.getQueryId());
        setIconFieldId(nodes.getIconFieldId());
        setValueFieldId(nodes.getValueFieldId());
        setSearchFilterId(nodes.getSearchFilterId());
        setEnabledFieldId(nodes.getEnabledFieldId());
        setPreFilters(nodes.getPreFilters());
    }

    @Deprecated
    public InheritanceNodes getInheritanceNodes() {
        InheritanceNodes nodes = new InheritanceNodes();
        nodes.setParentFieldId(getParentFieldId());
        nodes.setLabelFieldId(getLabelFieldId());
        nodes.setHasChildrenFieldId(getHasChildrenFieldId());
        nodes.setQueryId(getQueryId());
        nodes.setIconFieldId(getIconFieldId());
        nodes.setValueFieldId(getValueFieldId());
        nodes.setSearchFilterId(getSearchFilterId());
        nodes.setEnabledFieldId(getEnabledFieldId());
        nodes.setPreFilters(getPreFilters());
        return nodes.isEmpty() ? null : nodes;
    }

    @Override
    public boolean isSingle() {
        return checkboxes == null || !checkboxes;
    }
}
