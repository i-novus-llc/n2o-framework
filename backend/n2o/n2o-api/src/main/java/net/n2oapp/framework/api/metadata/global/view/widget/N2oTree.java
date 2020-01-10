package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель виджета дерево
 */
@Getter
@Setter
public class N2oTree extends N2oWidget {
    private Boolean ajax;
    private Boolean checkboxes;
    private Boolean multiselect;

    private String parentFieldId;
    private String hasChildrenFieldId;
    private String valueFieldId;
    private String labelFieldId;
    private String iconFieldId;
    private String imageFieldId;
    private String badgeFieldId;
    private String badgeColorFieldId;
}
