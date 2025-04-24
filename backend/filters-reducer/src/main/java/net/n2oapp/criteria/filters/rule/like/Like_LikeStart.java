package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

/**
 * Правило для объединения фильтров с типом like и likeStart
 */
public class Like_LikeStart implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterType.like) && left.getType().equals(FilterType.likeStart))
            return simplify(right, left);
        else if (left.getType().equals(FilterType.like) && right.getType().equals(FilterType.likeStart)) {
            if (!(right.getValue() instanceof String rightValue) || !(left.getValue() instanceof String leftValue))
                return null;
            if (rightValue.startsWith(leftValue)) {
                return right;
            } else if(leftValue.startsWith(rightValue)){
                return new Filter(left.getValue(), FilterType.likeStart);
            }
            return null;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.like, FilterType.likeStart);
    }
}
