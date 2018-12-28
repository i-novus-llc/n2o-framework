package net.n2oapp.framework.api.criteria.expression;

/**
 * User: operehod
 * Date: 20.11.2014
 * Time: 15:21
 */
public class DisplayExpression extends FieldExpression{

    private String domain;

    public DisplayExpression(String fieldId, String expression, String domain) {
        super(fieldId, expression);
        this.domain = domain;
    }

    public String getFieldId() {
        return super.fieldId;
    }

    public void setFieldId(String fieldId) {
        super.fieldId = fieldId;
    }

    public void setExpression(String expression) {
        super.expression = expression;
    }

    public String getDomain() {
        return domain;
    }
}
