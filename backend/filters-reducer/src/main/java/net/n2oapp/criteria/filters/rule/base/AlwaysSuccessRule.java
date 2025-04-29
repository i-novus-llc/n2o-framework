package net.n2oapp.criteria.filters.rule.base;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterTypeEnum;

/**
 * User: operehod
 * Date: 28.11.2014
 * Time: 13:48
 */
public abstract class AlwaysSuccessRule implements Rule {

    private FilterTypeEnum successType;

    protected AlwaysSuccessRule(FilterTypeEnum successType) {
        this.successType = successType;
    }

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (left.getType().equals(successType)) return left;
        else if (right.getType().equals(successType)) return right;
        throw new IllegalArgumentException();
    }

}
