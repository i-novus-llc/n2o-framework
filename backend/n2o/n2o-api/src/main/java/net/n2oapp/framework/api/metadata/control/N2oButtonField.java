package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;

@Getter
@Setter
public class N2oButtonField extends N2oField implements ModelAware {

    private String title;
    private String titleFieldId;
    private String icon;
    private String iconFieldId;
    private LabelType type;

    private N2oAction action;
    private String eventId;

    private String widgetId;
    private ReduxModel model;

    private String actionId;
    private String color;
    private Boolean validate;
    private Boolean confirm;
    private ConfirmType confirmType;
    private String confirmText;
    private String confirmTitle;
    private String confirmOkLabel;
    private String confirmCancelLabel;
    private String tooltipPosition;

}
