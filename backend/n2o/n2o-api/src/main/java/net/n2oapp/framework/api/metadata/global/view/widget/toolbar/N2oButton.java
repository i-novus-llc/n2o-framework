package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAction;
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
public class N2oButton extends N2oAbstractButton implements Button, WidgetIdAware {
    private String actionId;
    private N2oAction[] actions;
    @N2oAttribute("Круглая форма")
    private Boolean rounded;
    private Boolean validate;
    private String[] validateDatasourceIds;

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

    @Override
    public ToolbarItem clone() {
        N2oButton newButton = (N2oButton) super.clone();
        newButton.setActionId(actionId);
        newButton.setActions(actions);
        newButton.setRounded(rounded);
        newButton.setValidate(validate);
        newButton.setValidateDatasourceIds(validateDatasourceIds);
        newButton.setConfirm(confirm);
        newButton.setConfirmType(confirmType);
        newButton.setConfirmText(confirmText);
        newButton.setConfirmTitle(confirmTitle);
        newButton.setConfirmOkLabel(confirmOkLabel);
        newButton.setConfirmOkColor(confirmOkColor);
        newButton.setConfirmCancelLabel(confirmCancelLabel);
        newButton.setConfirmCancelColor(confirmCancelColor);
        newButton.setDisableOnEmptyModel(disableOnEmptyModel);
        newButton.setDependencies(dependencies);
        return newButton;
    }
}

