package net.n2oapp.criteria.filters.rule.or;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class EqOrIsNull_NotEq implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        if (left.getValue().equals(right.getValue()))
            return new Filter(FilterType.isNull);
        if (left.getType().equals(FilterType.eqOrIsNull)) return left;
        else return right;
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.eqOrIsNull, FilterType.notEq);
    }
}
