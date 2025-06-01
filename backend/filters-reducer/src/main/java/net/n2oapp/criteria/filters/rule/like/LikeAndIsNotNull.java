package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * Правило для объединения фильтров с типом like и isNotNull
 */
public class LikeAndIsNotNull extends AlwaysSuccessRule {

    public LikeAndIsNotNull() {
        super(FilterTypeEnum.LIKE);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.LIKE, FilterTypeEnum.IS_NOT_NULL);
    }
}
