package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;

public interface Button extends DatasourceIdAware, ModelAware, ActionsAware, IdAware {
    
    String getLabel();

    Boolean getValidate();

    void setValidate(Boolean validate);

    String[] getValidateDatasourceIds();

    void setValidateDatasourceIds(String[] validateDatasourceIds);

    String getConfirm();

    void setConfirm(String confirm);

    ConfirmTypeEnum getConfirmType();

    void setConfirmType(ConfirmTypeEnum confirmType);

    String getConfirmText();

    void setConfirmText(String confirmText);

    String getConfirmTitle();

    void setConfirmTitle(String confirmTitle);

    String getConfirmOkLabel();

    void setConfirmOkLabel(String confirmOkLabel);

    String getConfirmOkColor();

    void setConfirmOkColor(String confirmOkColor);

    String getConfirmCancelLabel();

    void setConfirmCancelLabel(String confirmCancelLabel);

    String getConfirmCancelColor();

    void setConfirmCancelColor(String confirmCancelColor);

    String getColor();

    void setColor(String color);

    String getTooltipPosition();

    void setTooltipPosition(String tooltipPosition);

    String getIcon();

    void setIcon(String icon);

    Boolean getRounded();

    void setRounded(Boolean rounded);

    @Deprecated
    default String getValidateString() {
        if (this.getValidate() == null)
            return null;
        return this.getValidate().toString();
    }

    @Deprecated
    default void setValidateString(String validate) {
        switch (validate) {
            case "widget":
            case "true":
            case "page":
                this.setValidate(true);
                break;
            case "none":
            case "false":
                this.setValidate(false);
                break;
            default:
                throw new UnsupportedOperationException(String.format("validate is [%s] unsupported", validate));
        }
    }

    @Deprecated
    default String getWidgetId() {
        return this.getDatasourceId();
    }

    @Deprecated
    default void setWidgetId(String widgetId) {
        this.setDatasourceId(widgetId);
    }
}
