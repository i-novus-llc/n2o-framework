package net.n2oapp.framework.api.criteria.filters.rule.like;

import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.AlwaysConflictRule;

/**
 * Правило для объединения фильтров с типом likeStart и isNull
 */
public class LikeStartAndIsNull extends AlwaysConflictRule {

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.LIKE_START, FilterTypeEnum.IS_NULL);
    }
}
