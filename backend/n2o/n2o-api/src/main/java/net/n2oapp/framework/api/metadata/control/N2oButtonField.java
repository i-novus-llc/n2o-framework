package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;

@Getter
@Setter
public class N2oButtonField extends N2oActionField implements ModelAware, DatasourceIdAware {
    private String title;
    private String titleFieldId;
    private String icon;
    private String iconFieldId;
    private LabelType type;
    private String datasourceId;
    private ReduxModel model;
    private String color;
    private Boolean validate;
    private String[] validateDatasources;
    private String tooltipPosition;
    private Boolean rounded;

    private Boolean confirm;
    private ConfirmType confirmType;
    private String confirmText;
    private String confirmTitle;
    private String confirmOkLabel;
    private String confirmCancelLabel;

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
