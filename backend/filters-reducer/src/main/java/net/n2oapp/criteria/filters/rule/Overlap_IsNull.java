package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysConflictRule;

/**
 * Rule for merge isNull and overlap filters
 */
public class Overlap_IsNull extends AlwaysConflictRule {

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.overlaps, FilterType.isNull);
    }
}
