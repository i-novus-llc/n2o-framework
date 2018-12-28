package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Rule for merge isNotNull and overlap filters
 * */
public class Overlap_IsNotNull extends AlwaysSuccessRule {

    public Overlap_IsNotNull() {
        super(FilterType.overlaps);
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.overlaps, FilterType.isNotNull);
    }
}
