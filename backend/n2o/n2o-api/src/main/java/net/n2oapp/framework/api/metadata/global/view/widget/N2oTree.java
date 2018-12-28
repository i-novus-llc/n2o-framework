package net.n2oapp.framework.api.metadata.global.view.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.tree.GroupingNodes;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.InheritanceNodes;


/**
 * User: iryabov
 * Date: 05.02.13
 * Time: 19:10
 */
public class N2oTree extends N2oWidget {
    private Boolean ajax;
    private Boolean search;
    private Boolean expand;
    private Boolean checkboxes;
    private InheritanceNodes inheritanceNodes;
    private GroupingNodes groupingNodes;
    private Boolean autoSelect;

    public GroupingNodes getGroupingNodes() {
        return groupingNodes;
    }

    public void setGroupingNodes(GroupingNodes groupingNodes) {
        this.groupingNodes = groupingNodes;
    }

    public Boolean getAjax() {
        return ajax;
    }

    public void setAjax(Boolean ajax) {
        this.ajax = ajax;
    }

    public InheritanceNodes getInheritanceNodes() {
        return inheritanceNodes;
    }

    public void setInheritanceNodes(InheritanceNodes inheritanceNodes) {
        this.inheritanceNodes = inheritanceNodes;
    }

    public Boolean getSearch() {
        return search;
    }

    public Boolean getExpand() {
        return expand;
    }

    public void setSearch(Boolean search) {
        this.search = search;
    }

    public void setExpand(Boolean expand) {
        this.expand = expand;
    }

    public Boolean getCheckboxes() {
        return checkboxes;
    }

    public void setCheckboxes(Boolean checkboxes) {
        this.checkboxes = checkboxes;
    }

    public Boolean getAutoSelect() {
        return autoSelect;
    }

    public void setAutoSelect(Boolean autoSelect) {
        this.autoSelect = autoSelect;
    }
}
