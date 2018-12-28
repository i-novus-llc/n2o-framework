package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysConflictRule;

/**
 * Rule for reduse overlap with notEq filters by AND
 */
public class Overlap_NotEq extends AlwaysConflictRule {

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.overlaps, FilterType.notEq);
    }
}
