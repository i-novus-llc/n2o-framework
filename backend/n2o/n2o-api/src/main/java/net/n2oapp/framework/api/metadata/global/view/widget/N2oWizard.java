package net.n2oapp.framework.api.metadata.global.view.widget;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;

import java.io.Serializable;

/**
 * @author V. Alexeev.
 */
public class N2oWizard extends N2oWidget {

    private Step[] steps;
    private Finish finish;

    public Step[] getSteps() {
        return steps;
    }

    public void setSteps(Step[] steps) {
        this.steps = steps;
    }

    public Finish getFinish() {
        return finish;
    }

    public void setFinish(Finish finish) {
        this.finish = finish;
    }

    public static class Step implements Serializable {

        private String name;
        private String description;
        private String icon;
        private String condition;
        private String nextActionId;
        private String nextLabel;
        private N2oFieldSet[] fieldSets;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public N2oFieldSet[] getFieldSets() {
            return fieldSets;
        }

        public void setFieldSets(N2oFieldSet[] fieldSets) {
            this.fieldSets = fieldSets;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getNextActionId() {
            return nextActionId;
        }

        public void setNextActionId(String nextActionId) {
            this.nextActionId = nextActionId;
        }

        public String getNextLabel() {
            return nextLabel;
        }

        public void setNextLabel(String nextLabel) {
            this.nextLabel = nextLabel;
        }
    }

    public static class Finish implements Serializable {

        private String name;
        private String finishLabel;
        private String actionId;
        private N2oFieldSet[] fieldSets;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFinishLabel() {
            return finishLabel;
        }

        public void setFinishLabel(String finishLabel) {
            this.finishLabel = finishLabel;
        }

        public String getActionId() {
            return actionId;
        }

        public void setActionId(String actionId) {
            this.actionId = actionId;
        }

        public N2oFieldSet[] getFieldSets() {
            return fieldSets;
        }

        public void setFieldSets(N2oFieldSet[] fieldSets) {
            this.fieldSets = fieldSets;
        }
    }
}
