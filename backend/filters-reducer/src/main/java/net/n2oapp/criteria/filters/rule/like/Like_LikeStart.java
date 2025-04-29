package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

/**
 * Правило для объединения фильтров с типом like и likeStart
 */
public class Like_LikeStart implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.like) && left.getType().equals(FilterTypeEnum.likeStart))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.like) && right.getType().equals(FilterTypeEnum.likeStart)) {
            if (!(right.getValue() instanceof String rightValue) || !(left.getValue() instanceof String leftValue))
                return null;
            if (rightValue.startsWith(leftValue)) {
                return right;
            } else if(leftValue.startsWith(rightValue)){
                return new Filter(left.getValue(), FilterTypeEnum.likeStart);
            }
            return null;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.like, FilterTypeEnum.likeStart);
    }
}
