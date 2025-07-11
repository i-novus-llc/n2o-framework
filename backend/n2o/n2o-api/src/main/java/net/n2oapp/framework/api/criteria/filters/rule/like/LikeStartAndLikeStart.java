package net.n2oapp.framework.api.criteria.filters.rule.like;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.Rule;

/**
 * Правило для объединения 2 фильтров с типом likeStart
 */
public class LikeStartAndLikeStart implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (!(right.getValue() instanceof String rightValue) || !(left.getValue() instanceof String leftValue))
            return null;
        if (rightValue.matches(left.getValue() + ".*")) {
            return right;
        } else if (leftValue.matches(right.getValue() + ".*")) {
            return left;
        }
        return null;
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.LIKE_START, FilterTypeEnum.LIKE_START);
    }
}
