package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Rule for merge isNotNull and contains filters
 * */
public class ContainsAndIsNotNull extends AlwaysSuccessRule {

    public ContainsAndIsNotNull() {
        super(FilterTypeEnum.contains);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.contains, FilterTypeEnum.isNotNull);
    }
}
