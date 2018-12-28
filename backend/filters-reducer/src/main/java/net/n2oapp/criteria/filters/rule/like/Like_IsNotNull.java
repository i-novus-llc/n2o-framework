package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Правило для объединения фильтров с типом like и isNotNull
 */
public class Like_IsNotNull extends AlwaysSuccessRule {

    public Like_IsNotNull() {
        super(FilterType.like);
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.like, FilterType.isNotNull);
    }
}
