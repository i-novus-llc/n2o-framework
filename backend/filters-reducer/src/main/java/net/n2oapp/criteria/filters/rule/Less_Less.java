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
public class Less_Less implements Rule {

    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        Comparable value1 = (Comparable) left.getValue();
        Comparable value2 = (Comparable) right.getValue();
        if (value1.compareTo(value2) < 0) return left;
        return right;
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.less, FilterType.less);
    }
}
