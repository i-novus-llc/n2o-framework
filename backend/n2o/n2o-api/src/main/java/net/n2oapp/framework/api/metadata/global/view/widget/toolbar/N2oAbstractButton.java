package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;

/**
 * Абстрактная модель пункта меню.
 */
@Getter
@Setter
public abstract class N2oAbstractButton extends N2oComponent implements IdAware {
    private String id;
    private String label;
    private String icon;
    private LabelType type;
    private String color;
    private String visible;
    private String enabled;
    private String description;
    private String tooltipPosition;

    @Deprecated
    private N2oButtonCondition[] enablingConditions;
    @Deprecated
    private N2oButtonCondition[] visibilityConditions;


    @Getter
    @Setter
    public static class Dependency implements Source {
        private String value;
        private String refWidgetId;
        private ReduxModel refModel;
    }

    @Getter
    @Setter
    public static class EnablingDependency extends Dependency {
        private String message;
    }

    public static class VisibilityDependency extends Dependency {
    }

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
