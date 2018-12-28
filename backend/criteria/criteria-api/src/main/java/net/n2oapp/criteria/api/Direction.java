package net.n2oapp.criteria.api;

import java.io.Serializable;

/**
 * User: boris.fanyuk
 * Date: 02.11.12
 * Time: 18:19
 */
public enum Direction implements Serializable {
    ASC, DESC,
    //
    ;

    private String expression = name().toLowerCase();

    public String getExpression() {
        return expression;
    }
}
