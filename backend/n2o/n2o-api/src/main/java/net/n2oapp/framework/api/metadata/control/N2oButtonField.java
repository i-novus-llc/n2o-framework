package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

@Getter
@Setter
public class N2oButtonField extends N2oActionField implements Button, BadgeAware {

    private String icon;
    private LabelType type;
    private String badge;
    private String badgeColor;
    private Position badgePosition;
    private ShapeType badgeShape;
    private String badgeImage;
    private Position badgeImagePosition;
    private ShapeType badgeImageShape;
    private String datasourceId;
    private ReduxModel model;
    private String color;
    private Boolean validate;
    private String[] validateDatasourceIds;
    private String tooltipPosition;
    private Boolean rounded;
    private String confirm;
    private ConfirmType confirmType;
    private String confirmText;
    private String confirmTitle;
    private String confirmOkLabel;
    private String confirmOkColor;
    private String confirmCancelLabel;
    private String confirmCancelColor;
}
