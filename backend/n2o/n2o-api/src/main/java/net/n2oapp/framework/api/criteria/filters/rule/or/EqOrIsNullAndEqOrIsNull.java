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
public class EqOrIsNullAndEqOrIsNull implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        if (left.getValue().equals(right.getValue())) return right;
        return new Filter(FilterTypeEnum.IS_NULL);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.EQ_OR_IS_NULL, FilterTypeEnum.EQ_OR_IS_NULL);
    }
}
