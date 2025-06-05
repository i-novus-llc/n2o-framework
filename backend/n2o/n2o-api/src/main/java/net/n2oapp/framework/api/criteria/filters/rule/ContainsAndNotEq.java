package net.n2oapp.framework.api.criteria.filters.rule;

import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.AlwaysConflictRule;

/**
 * Rule for merge notEq and contains filters
 */
public class ContainsAndNotEq extends AlwaysConflictRule {

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.CONTAINS, FilterTypeEnum.NOT_EQ);
    }
}
