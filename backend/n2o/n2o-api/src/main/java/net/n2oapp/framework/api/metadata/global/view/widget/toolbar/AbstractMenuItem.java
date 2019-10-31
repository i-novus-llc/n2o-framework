package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.view.ActionComponent;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.action.control.RefreshPolity;

import java.util.Map;

/**
 * Абстрактная модель пункта меню.
 */
@Getter
@Setter
public abstract class AbstractMenuItem extends ActionComponent implements ExtensionAttributesAware {
    private String description;
    private Boolean readonly;
    private String namespaceUri;
    private String color;
    private LabelType type;
    private String actionId;
    private String className;
    private Map<String, Object> properties;
    private Boolean defaultAction;
    private Boolean primary;
    private String key;
    private Boolean bulk;
    private Boolean validate;
    @Deprecated //use enabled as JS
    private N2oButtonCondition[] enablingConditions;
    @Deprecated //use visible as JS
    private N2oButtonCondition[] visibilityConditions;
    private RefreshPolity refreshPolity;
    private Map<N2oNamespace, Map<String, String>> extAttributes;
    private Boolean confirm;
    private ConfirmType confirmType;
    private String confirmText;
    private String confirmTitle;
    private String confirmOkLabel;
    private String confirmCancelLabel;
    private String tooltipPosition;

    @Deprecated
    public String getEnablingCondition() {
        if (this.getEnablingConditions() == null) return null;
        return this.getEnablingConditions()[0].getExpression();
    }

    @Deprecated
    public void setEnablingCondition(String expression) {
        if (this.getEnablingConditions() == null) {
            N2oButtonCondition condition = new N2oButtonCondition();
            condition.setExpression(expression);
            N2oButtonCondition[] conditions = new N2oButtonCondition[]{condition};
            this.setEnablingConditions(conditions);
        }
    }

    @Deprecated
    public String getVisibilityCondition() {
        if (this.getVisibilityConditions() == null) return null;
        return this.getVisibilityConditions()[0].getExpression();
    }

    @Deprecated
    public void setVisibilityCondition(String expression) {
        if (this.getVisibilityConditions() == null) {
            N2oButtonCondition condition = new N2oButtonCondition();
            condition.setExpression(expression);
            N2oButtonCondition[] conditions = new N2oButtonCondition[]{condition};
            this.setVisibilityConditions(conditions);
        }
    }
}
