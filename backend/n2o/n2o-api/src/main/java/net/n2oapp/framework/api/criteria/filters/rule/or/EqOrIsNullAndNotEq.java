package net.n2oapp.framework.api.criteria.filters.rule.or;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.Rule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class EqOrIsNullAndNotEq implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        if (left.getValue().equals(right.getValue()))
            return new Filter(FilterTypeEnum.IS_NULL);
        if (left.getType().equals(FilterTypeEnum.EQ_OR_IS_NULL)) return left;
        else return right;
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.EQ_OR_IS_NULL, FilterTypeEnum.NOT_EQ);
    }
}
