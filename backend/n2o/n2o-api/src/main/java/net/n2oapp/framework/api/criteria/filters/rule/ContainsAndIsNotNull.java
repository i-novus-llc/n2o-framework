package net.n2oapp.framework.api.criteria.filters.rule;

import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Rule for merge isNotNull and contains filters
 * */
public class ContainsAndIsNotNull extends AlwaysSuccessRule {

    public ContainsAndIsNotNull() {
        super(FilterTypeEnum.CONTAINS);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.CONTAINS, FilterTypeEnum.IS_NOT_NULL);
    }
}
