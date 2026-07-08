package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;

@Getter
@Setter
public class N2oButtonField extends N2oActionField implements Button, BadgeAware {

    private String icon;
    private String badge;
    private String badgeColor;
    private PositionEnum badgePosition;
    private ShapeTypeEnum badgeShape;
    private String badgeImage;
    private PositionEnum badgeImagePosition;
    private ShapeTypeEnum badgeImageShape;
    private String datasourceId;
    private ReduxModelEnum model;
    private String color;
    private Boolean validate;
    private String[] validateDatasourceIds;
    private String tooltipPosition;
    private Boolean rounded;
}
