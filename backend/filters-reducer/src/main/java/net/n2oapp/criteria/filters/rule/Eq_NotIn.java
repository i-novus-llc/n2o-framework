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
public class Eq_NotIn implements Rule {
    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.eq) && left.getType().equals(FilterTypeEnum.notIn))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.eq) && right.getType().equals(FilterTypeEnum.notIn)) {
            if (((List) right.getValue()).contains(left.getValue())) {
                return null;
            } else return left;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.eq, FilterTypeEnum.notIn);
    }
}
