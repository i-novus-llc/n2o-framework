package net.n2oapp.criteria.filters.rule.or;

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
public class EqOrIsNullAndNotIn implements Rule {
    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.eqOrIsNull) && left.getType().equals(FilterTypeEnum.notIn))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.eqOrIsNull) && right.getType().equals(FilterTypeEnum.notIn)) {
            if (((List) right.getValue()).contains(left.getValue())) {
                return new Filter(FilterTypeEnum.isNull);
            } else return left;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.eqOrIsNull, FilterTypeEnum.notIn);
    }
}
