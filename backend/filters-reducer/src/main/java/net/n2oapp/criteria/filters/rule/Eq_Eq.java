package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.rule.base.Rule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class Eq_Eq implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        if (left.getValue().equals(right.getValue())) return left;
        return null;
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.eq, FilterType.eq);
    }
}
