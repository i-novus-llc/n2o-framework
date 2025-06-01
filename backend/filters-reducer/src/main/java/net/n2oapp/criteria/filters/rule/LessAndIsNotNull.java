package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class LessAndIsNotNull extends AlwaysSuccessRule {

    public LessAndIsNotNull() {
        super(FilterTypeEnum.LESS);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.LESS, FilterTypeEnum.IS_NOT_NULL);
    }
}
