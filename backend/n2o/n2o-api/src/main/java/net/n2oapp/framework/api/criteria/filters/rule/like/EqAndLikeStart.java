package net.n2oapp.framework.api.criteria.filters.rule.like;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.Rule;

/**
 * Правило для объединения фильтров с типом eq и likeStart
 */
public class EqAndLikeStart implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.EQ) && left.getType().equals(FilterTypeEnum.LIKE_START))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.EQ) && right.getType().equals(FilterTypeEnum.LIKE_START)) {
            if (!(right.getValue() instanceof String) || !(left.getValue() instanceof String leftValue))
                return null;
            if (leftValue.matches(right.getValue() + ".*")) return left;
            else return null;
        }
        throw new IllegalArgumentException(
                String.format("Некорректные типы фильтров: `%s` и `%s`",
                        left.getType().getId(), right.getType().getId()));
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.EQ, FilterTypeEnum.LIKE_START);
    }
}
