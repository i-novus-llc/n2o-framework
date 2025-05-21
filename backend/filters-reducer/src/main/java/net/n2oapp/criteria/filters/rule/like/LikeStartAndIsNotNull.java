package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Правило для объединения фильтров с типом likeStart и isNotNull
 */
public class LikeStartAndIsNotNull extends AlwaysSuccessRule {

    public LikeStartAndIsNotNull() {
        super(FilterTypeEnum.likeStart);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.likeStart, FilterTypeEnum.isNotNull);
    }
}
