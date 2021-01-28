package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

import java.util.Arrays;
import java.util.List;

/**
 * Исходная модель кнопки
 */
@Getter
@Setter
public class N2oButton extends N2oAbstractButton implements GroupItem {
    private ReduxModel model;
    private String widgetId;
    private String actionId;
    private Boolean validate;
    private Boolean disableOnEmptyModel;
    private Boolean rounded;

    private Boolean confirm;
    private ConfirmType confirmType;
    private String confirmText;
    private String confirmTitle;
    private String confirmOkLabel;
    private String confirmCancelLabel;

    private N2oAction action;
    private Dependency[] dependencies;

    @Override
    public List<N2oAction> getActions() {
        return Arrays.asList(getAction());
    }
}

