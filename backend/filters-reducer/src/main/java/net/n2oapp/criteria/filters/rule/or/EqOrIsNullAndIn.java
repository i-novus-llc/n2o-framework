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
public class EqOrIsNullAndIn implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.eqOrIsNull) && left.getType().equals(FilterTypeEnum.in))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.eqOrIsNull) && right.getType().equals(FilterTypeEnum.in)) {
            if (((List) right.getValue()).contains(left.getValue())) return new Filter(left.getValue());
            else return null;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.eqOrIsNull, FilterTypeEnum.in);
    }
}
