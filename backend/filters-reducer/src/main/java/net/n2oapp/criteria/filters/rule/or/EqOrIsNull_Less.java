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
public class EqOrIsNull_Less implements Rule {
    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterType.eqOrIsNull) && left.getType().equals(FilterType.less))
            return simplify(right, left);
        else if (left.getType().equals(FilterType.eqOrIsNull) && right.getType().equals(FilterType.less)) {
            Comparable value = (Comparable) left.getValue();
            Comparable top = (Comparable) right.getValue();
            if (value.compareTo(top) < 0) return new Filter(left.getValue());
            return null;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.eqOrIsNull, FilterType.less);
    }
}
