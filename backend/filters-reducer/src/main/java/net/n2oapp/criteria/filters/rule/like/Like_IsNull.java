package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysConflictRule;

/**
 * Правило для объединения фильтров с типом like и isNull
 */
public class Like_IsNull extends AlwaysConflictRule {

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.like, FilterType.isNull);
    }
}
