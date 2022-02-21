package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

import java.util.Arrays;
import java.util.List;

/**
 * Исходная модель кнопки
 */
@Getter
@Setter
public class N2oButton extends N2oAbstractButton implements GroupItem, DatasourceIdAware, WidgetIdAware, ModelAware {
    private String actionId;
    private Boolean rounded;
    private String enabled;
    private Boolean validate;
    private String datasource;
    private ReduxModel model;
    private String[] validateDatasources;
    private N2oAction action;

    private String confirm;
    private ConfirmType confirmType;
    private String confirmText;
    private String confirmTitle;
    private String confirmOkLabel;
    private String confirmCancelLabel;
    private DisableOnEmptyModelType disableOnEmptyModel;

    private Dependency[] dependencies;

    @Deprecated
    private String enablingCondition;
    @Deprecated
    private String visibilityCondition;

    @Deprecated
    public String getWidgetId() {
        return datasource;
    }

    @Deprecated
    public void setWidgetId(String widgetId) {
        this.datasource = widgetId;
    }

    @Deprecated
    public String getValidateString() {
        if (validate == null)
            return null;
        return validate.toString();
    }

    @Deprecated
    public void setValidateString(String validate) {
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
            default:
                throw new UnsupportedOperationException(String.format("validate is [%s] unsupported", validate));
        }
    }

    @Override
    public List<N2oAction> getActions() {
        return Arrays.asList(getAction());
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @Getter
    @Setter
    public static class Dependency implements Source {
        private String value;
        private String datasource;
        private ReduxModel model;

        @Deprecated
        public String getRefWidgetId() {
            return datasource;
        }

        @Deprecated
        public void setRefWidgetId(String refWidgetId) {
            this.datasource = refWidgetId;
        }
    }

    @Getter
    @Setter
    public static class EnablingDependency extends Dependency {
        private String message;
    }

    public static class VisibilityDependency extends Dependency {
    }
}

