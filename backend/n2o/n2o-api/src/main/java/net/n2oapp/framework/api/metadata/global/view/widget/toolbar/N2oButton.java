package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Исходная модель кнопки
 */
@Getter
@Setter
@N2oComponent
public class N2oButton extends N2oAbstractButton implements GroupItem, DatasourceIdAware, WidgetIdAware,
        ModelAware, ActionsAware {
    private String actionId;
    @N2oAttribute("Круглая форма")
    private Boolean rounded;
    private Boolean validate;
    private String[] validateDatasourceIds;
    private N2oAction[] actions;

    private String confirm;
    private ConfirmType confirmType;
    private String confirmText;
    private String confirmTitle;
    private String confirmOkLabel;
    private String confirmOkColor;
    private String confirmCancelLabel;
    private String confirmCancelColor;
    @N2oAttribute("Недоступность кнопки при пустой модели")
    private DisableOnEmptyModelType disableOnEmptyModel;

    private Dependency[] dependencies;


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
    public List<N2oAction> getListActions() {
        if (actions == null)
            return new ArrayList<>();
        return Arrays.asList(actions);
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

