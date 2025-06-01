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
public class EqAndIn implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.EQ) && left.getType().equals(FilterTypeEnum.IN))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.EQ) && right.getType().equals(FilterTypeEnum.IN)) {
            if (((List) right.getValue()).contains(left.getValue())) return left;
            else return null;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.EQ, FilterTypeEnum.IN);
    }
}
