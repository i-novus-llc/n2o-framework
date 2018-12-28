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
public class InOrIsNull_IsNotNull implements Rule {


    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterType.inOrIsNull))
            return simplify(right, left);
        else {
            return new Filter(left.getValue(), FilterType.in);
        }
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.inOrIsNull, FilterType.isNotNull);
    }
}
