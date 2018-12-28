package net.n2oapp.framework.api.metadata.control;

import net.n2oapp.framework.api.metadata.Source;

/**
 * Исходная модель условий на поле
 */
public class N2oFieldCondition implements Source {

    private String condition;
    private String on;

    public N2oFieldCondition() {
    }

    public N2oFieldCondition(String condition) {
        this.condition = condition;
    }

    public N2oFieldCondition(String condition, String on) {
        this.condition = condition;
        this.on = on;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }
}
