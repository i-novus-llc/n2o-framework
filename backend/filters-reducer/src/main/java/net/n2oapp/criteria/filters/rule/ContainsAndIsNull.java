package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysConflictRule;

/**
 * Rule for merge isNull and contains filters
 */
public class ContainsAndIsNull extends AlwaysConflictRule {

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.CONTAINS, FilterTypeEnum.IS_NULL);
    }
}
