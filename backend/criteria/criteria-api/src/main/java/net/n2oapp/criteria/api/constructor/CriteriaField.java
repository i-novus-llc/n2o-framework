package net.n2oapp.criteria.api.constructor;

import java.lang.reflect.Field;

/**
 * User: marat
 * Date: 27.11.12
 * Time: 16:04
 */
public class CriteriaField {
    private Field field;

    private Object value;

    private CriteriaConstructorResult result;

    public CriteriaField(Field field, Object value) {
        this.field = field;
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field expression) {
        this.field = expression;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public CriteriaConstructorResult getResult() {
        return result;
    }

    public void setResult(CriteriaConstructorResult result) {
        this.result = result;
    }
}
