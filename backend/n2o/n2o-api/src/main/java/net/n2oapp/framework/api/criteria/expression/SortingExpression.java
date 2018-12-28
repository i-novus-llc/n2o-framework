package net.n2oapp.framework.api.criteria.expression;

import net.n2oapp.criteria.api.Direction;

/**
 * User: operehod
 * Date: 20.11.2014
 * Time: 15:41
 */
public class SortingExpression extends FieldExpression {

    private Direction direction;

    public SortingExpression(String fieldId, Direction direction, String expression) {
        super(fieldId,expression);
        this.direction = direction;
    }

    public String getFieldId() {
        return super.fieldId;
    }

    public Direction getDirection() {
        return direction;
    }

}
