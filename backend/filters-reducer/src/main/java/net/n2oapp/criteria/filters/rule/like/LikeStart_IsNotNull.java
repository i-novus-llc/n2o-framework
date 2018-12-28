package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Правило для объединения фильтров с типом likeStart и isNotNull
 */
public class LikeStart_IsNotNull extends AlwaysSuccessRule {

    public LikeStart_IsNotNull() {
        super(FilterType.likeStart);
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.likeStart, FilterType.isNotNull);
    }
}
