package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysConflictRule;

/**
 * Правило для объединения фильтров с типом like и isNull
 */
public class Like_IsNull extends AlwaysConflictRule {

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.like, FilterTypeEnum.isNull);
    }
}
