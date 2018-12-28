package net.n2oapp.criteria.filters.rule.like;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

/**
 * Правило для объединения 2 фильтров с типом likeStart
 */
public class LikeStart_LikeStart implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (!(right.getValue() instanceof String) || !(left.getValue() instanceof String))
            return null;
        if (((String)right.getValue()).matches(left.getValue() + ".*")) {
            return right;
        } else if (((String)left.getValue()).matches(right.getValue() + ".*")) {
            return left;
        }
        return null;
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.likeStart, FilterType.likeStart);
    }
}
