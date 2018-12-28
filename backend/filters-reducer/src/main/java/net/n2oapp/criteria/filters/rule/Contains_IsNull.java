package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysConflictRule;

/**
 * Rule for merge isNull and overlap contains
 */
public class Contains_IsNull extends AlwaysConflictRule {

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.contains, FilterType.isNull);
    }
}
