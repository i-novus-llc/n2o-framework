package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Правило для объединения фильтров с типом likeStart и isNotNull
 */
public class LikeStart_IsNotNull extends AlwaysSuccessRule {

    public LikeStart_IsNotNull() {
        super(FilterTypeEnum.likeStart);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.likeStart, FilterTypeEnum.isNotNull);
    }
}
