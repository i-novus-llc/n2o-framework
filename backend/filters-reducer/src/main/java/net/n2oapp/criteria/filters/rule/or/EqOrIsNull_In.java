package net.n2oapp.criteria.filters.rule.or;

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
public class EqOrIsNull_In implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterType.eqOrIsNull) && left.getType().equals(FilterType.in))
            return simplify(right, left);
        else if (left.getType().equals(FilterType.eqOrIsNull) && right.getType().equals(FilterType.in)) {
            if (((List) right.getValue()).contains(left.getValue())) return new Filter(left.getValue());
            else return null;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.eqOrIsNull, FilterType.in);
    }
}
