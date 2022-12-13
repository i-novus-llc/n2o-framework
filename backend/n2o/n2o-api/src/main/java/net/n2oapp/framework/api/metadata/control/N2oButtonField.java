package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

@Getter
@Setter
@VisualComponent
public class N2oButtonField extends N2oActionField implements ModelAware, DatasourceIdAware, BadgeAware {
    @VisualAttribute
    private String title;
    @VisualAttribute
    private String titleFieldId;
    @VisualAttribute
    private String icon;
    @VisualAttribute
    private String iconFieldId;
    @VisualAttribute
    private String badge;
    @VisualAttribute
    private String badgeColor;
    @VisualAttribute
    private Position badgePosition;
    @VisualAttribute
    private ShapeType badgeShape;
    @VisualAttribute
    private String badgeImage;
    @VisualAttribute
    private Position badgeImagePosition;
    @VisualAttribute
    private ShapeType badgeImageShape;
    @VisualAttribute
    private LabelType type;
    private String datasourceId;
    private ReduxModel model;
    @VisualAttribute
    private String color;
    private Boolean validate;
    private String[] validateDatasourceIds;
    private String tooltipPosition;
    @VisualAttribute
    private Boolean rounded;
    @VisualAttribute
    private Boolean confirm;
    @VisualAttribute
    private ConfirmType confirmType;
    @VisualAttribute
    private String confirmText;
    @VisualAttribute
    private String confirmTitle;
    @VisualAttribute
    private String confirmOkLabel;
    @VisualAttribute
    private String confirmOkColor;
    @VisualAttribute
    private String confirmCancelLabel;
    @VisualAttribute
    private String confirmCancelColor;

    @Deprecated
    public String getWidgetId() {
        return datasourceId;
    }

    @Deprecated
    public void setWidgetId(String widgetId) {
        this.datasourceId = widgetId;
    }

    @Deprecated
    public String getValidateString() {
        if (validate == null)
            return null;
        return validate.toString();
    }

    @Deprecated
    public void setValidateString(String validate) {
        if (validate == null) return;
        switch (validate) {
            case "widget":
            case "true":
            case "page":
                this.validate = true;
                break;
            case "none":
            case "false":
                this.validate = false;
                break;
            default: throw new UnsupportedOperationException(String.format("validate is [%s] unsupported", validate));
        }
    }
}
