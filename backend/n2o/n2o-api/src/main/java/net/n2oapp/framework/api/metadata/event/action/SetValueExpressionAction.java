package net.n2oapp.framework.api.metadata.event.action;

import net.n2oapp.framework.api.metadata.event.action.N2oAbstractAction;

/**
 * Событие set-value-expression
 */
public class SetValueExpressionAction extends N2oAbstractAction {
    private static final String DEFAULT_SRC = "";
    //todo:добавить src
    private String expression;
    private String targetFieldId;

    public SetValueExpressionAction() {
        setSrc(DEFAULT_SRC);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getTargetFieldId() {
        return targetFieldId;
    }

    public void setTargetFieldId(String targetFieldId) {
        this.targetFieldId = targetFieldId;
    }

}
