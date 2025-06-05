package net.n2oapp.framework.api.criteria.filters.rule.like;

import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Правило для объединения фильтров с типом likeStart и isNotNull
 */
public class LikeStartAndIsNotNull extends AlwaysSuccessRule {

    public LikeStartAndIsNotNull() {
        super(FilterTypeEnum.LIKE_START);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.LIKE_START, FilterTypeEnum.IS_NOT_NULL);
    }
}
