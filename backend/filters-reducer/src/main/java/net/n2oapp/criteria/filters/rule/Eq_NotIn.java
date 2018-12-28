package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class Eq_NotIn implements Rule {
    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterType.eq) && left.getType().equals(FilterType.notIn))
            return simplify(right, left);
        else if (left.getType().equals(FilterType.eq) && right.getType().equals(FilterType.notIn)) {
            if (((List) right.getValue()).contains(left.getValue())) {
                return null;
            } else return left;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.eq, FilterType.notIn);
    }
}
