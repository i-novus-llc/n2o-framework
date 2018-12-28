package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

/**
 * Правило для объединения фильтров с типом eq и likeStart
 */
public class Eq_LikeStart implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterType.eq) && left.getType().equals(FilterType.likeStart))
            return simplify(right, left);
        else if (left.getType().equals(FilterType.eq) && right.getType().equals(FilterType.likeStart)) {
            if (!(right.getValue() instanceof String) || !(left.getValue() instanceof String))
                return null;
            if (((String) left.getValue()).matches(right.getValue() + ".*")) return left;
            else return null;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.eq, FilterType.likeStart);
    }
}
