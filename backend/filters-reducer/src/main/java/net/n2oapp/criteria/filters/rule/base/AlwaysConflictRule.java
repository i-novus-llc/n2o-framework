package net.n2oapp.criteria.filters.rule.base;

import net.n2oapp.criteria.filters.Filter;

/**
 * User: operehod
 * Date: 28.11.2014
 * Time: 13:48
 */
public abstract class AlwaysConflictRule implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        return null;
    }

}
