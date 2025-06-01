package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class InAndIsNotNull extends AlwaysSuccessRule {

    public InAndIsNotNull() {
        super(FilterTypeEnum.IN);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.IN, FilterTypeEnum.IS_NOT_NULL);
    }
}
