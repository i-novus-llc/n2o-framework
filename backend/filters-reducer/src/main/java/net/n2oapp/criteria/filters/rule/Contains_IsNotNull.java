package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Rule for merge isNotNull and contains filters
 * */
public class Contains_IsNotNull extends AlwaysSuccessRule {

    public Contains_IsNotNull() {
        super(FilterType.contains);
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.contains, FilterType.isNotNull);
    }
}
