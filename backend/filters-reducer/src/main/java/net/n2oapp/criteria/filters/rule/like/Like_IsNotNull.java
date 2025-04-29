package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Правило для объединения фильтров с типом like и isNotNull
 */
public class Like_IsNotNull extends AlwaysSuccessRule {

    public Like_IsNotNull() {
        super(FilterTypeEnum.like);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.like, FilterTypeEnum.isNotNull);
    }
}
