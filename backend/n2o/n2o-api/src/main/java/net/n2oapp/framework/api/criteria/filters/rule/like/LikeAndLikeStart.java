package net.n2oapp.framework.api.criteria.filters.rule.like;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.Rule;

/**
 * Правило для объединения фильтров с типом like и likeStart
 */
public class LikeAndLikeStart implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.LIKE) && left.getType().equals(FilterTypeEnum.LIKE_START))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.LIKE) && right.getType().equals(FilterTypeEnum.LIKE_START)) {
            if (!(right.getValue() instanceof String rightValue) || !(left.getValue() instanceof String leftValue))
                return null;
            if (rightValue.startsWith(leftValue)) {
                return right;
            } else if(leftValue.startsWith(rightValue)){
                return new Filter(left.getValue(), FilterTypeEnum.LIKE_START);
            }
            return null;
        }
        throw new IllegalArgumentException(
                String.format("Некорректные типы фильтров: `%s` и `%s`",
                        left.getType().getId(), right.getType().getId()));
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.LIKE, FilterTypeEnum.LIKE_START);
    }
}
