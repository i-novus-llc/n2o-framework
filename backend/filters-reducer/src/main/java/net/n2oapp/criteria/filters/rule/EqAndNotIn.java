package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class EqAndNotIn implements Rule {
    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.EQ) && left.getType().equals(FilterTypeEnum.NOT_IN))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.EQ) && right.getType().equals(FilterTypeEnum.NOT_IN)) {
            if (((List) right.getValue()).contains(left.getValue())) {
                return null;
            } else return left;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.EQ, FilterTypeEnum.NOT_IN);
    }
}
