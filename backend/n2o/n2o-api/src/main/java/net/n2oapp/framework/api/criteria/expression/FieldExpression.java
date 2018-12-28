package net.n2oapp.framework.api.criteria.expression;

/**
 * User: operehod
 * Date: 20.11.2014
 * Time: 19:04
 */
public abstract class FieldExpression {

    protected String fieldId;
    protected String expression;

    public FieldExpression(String fieldId, String expression) {
        this.fieldId = fieldId;
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldExpression that = (FieldExpression) o;

        if (fieldId != null ? !fieldId.equals(that.fieldId) : that.fieldId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return fieldId != null ? fieldId.hashCode() : 0;
    }
}
