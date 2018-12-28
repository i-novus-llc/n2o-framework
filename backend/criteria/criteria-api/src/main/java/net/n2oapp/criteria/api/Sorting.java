package net.n2oapp.criteria.api;

import java.io.Serializable;

/**
 * User: boris.fanyuk
 * Date: 02.11.12
 * Time: 18:20
 */
public class Sorting implements Serializable {

    private String field;

    private Direction direction;

    public Sorting() {
    }

    public Sorting(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
