package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.GroupingNodes;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.InheritanceNodes;

@Getter
@Setter
public class N2oTree extends N2oWidget {
    private Boolean ajax;
    private Boolean search;
    private Boolean expand;
    private Boolean checkboxes;
    private InheritanceNodes inheritanceNodes;
    private GroupingNodes groupingNodes;
    private Boolean autoSelect;

    private String parentFieldId;
    private String labelFieldId;
    private String hasChildrenFieldId;
    private String queryId;
    private String iconFieldId;
    private String valueFieldId;
    private String masterFieldId;
    private String detailFieldId;
    private String searchFieldId;
    private String enabledFieldId;
}
