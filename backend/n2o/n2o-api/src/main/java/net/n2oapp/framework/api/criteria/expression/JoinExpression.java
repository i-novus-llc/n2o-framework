package net.n2oapp.framework.api.criteria.expression;

/**
 * @author V. Alexeev.
 */
public class JoinExpression extends FieldExpression {

    public JoinExpression(String fieldId, String expression) {
        super(fieldId, expression);
    }

    public String getFieldId() {
        return super.fieldId;
    }
}
