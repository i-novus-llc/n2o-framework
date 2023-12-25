package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.action.N2oConfirmAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oIfBranchAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;

@Getter
@Setter
public class N2oButtonField extends N2oActionField implements Button, BadgeAware {

    private String icon;
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
    @Deprecated
    private String confirm;
    @Deprecated
    private ConfirmType confirmType;
    @Deprecated
    private String confirmText;
    @Deprecated
    private String confirmTitle;
    @Deprecated
    private String confirmOkLabel;
    @Deprecated
    private String confirmOkColor;
    @Deprecated
    private String confirmCancelLabel;
    @Deprecated
    private String confirmCancelColor;

    @Deprecated
    public void adapterV3() {
        if (confirm != null && !confirm.equals("false")) {
            N2oConfirmAction confirmAction = new N2oConfirmAction();
            confirmAction.setType(confirmType);
            confirmAction.setText(confirmText);
            confirmAction.setTitle(confirmTitle);
            N2oConfirmAction.ConfirmButton[] confirmButtons = new N2oConfirmAction.ConfirmButton[2];
            confirmButtons[0] = new N2oConfirmAction.OkButton();
            confirmButtons[1] = new N2oConfirmAction.CancelButton();
            if (confirmOkLabel != null || confirmOkColor != null) {
                confirmButtons[0].setLabel(confirmOkLabel);
                confirmButtons[0].setColor(confirmOkColor);
            }
            if (confirmCancelLabel != null || confirmCancelColor != null) {
                confirmButtons[1].setLabel(confirmCancelLabel);
                confirmButtons[1].setColor(confirmCancelColor);
            }
            confirmAction.setConfirmButtons(confirmButtons);

            N2oAction outAction;
            if (isLink(confirm)) {
                outAction = new N2oIfBranchAction();
                ((N2oIfBranchAction) outAction).setTest(unwrapLink(confirm));
                ((N2oIfBranchAction) outAction).setDatasourceId(getDatasourceId());
                ((N2oIfBranchAction) outAction).setActions(new N2oAction[]{confirmAction});
            } else {
                outAction = confirmAction;
            }
            N2oAction[] actions = getActions();
            if (actions == null) {
                actions = new N2oAction[1];
                actions[0] = outAction;
                setActions(actions);
            } else {
                N2oAction[] out = new N2oAction[actions.length + 1];
                System.arraycopy(actions, 0, out, 1, actions.length);
                out[0] = outAction;
                setActions(out);
            }
        }
    }
}
