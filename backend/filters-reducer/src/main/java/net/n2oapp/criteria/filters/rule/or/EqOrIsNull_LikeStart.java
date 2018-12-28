package net.n2oapp.criteria.filters.rule.or;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

/**
 * Правило для объединения фильтров с типом eqOrIsNull и likeStart
 */
public class EqOrIsNull_LikeStart implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterType.eqOrIsNull) && left.getType().equals(FilterType.likeStart))
            return simplify(right, left);
        else if (left.getType().equals(FilterType.eqOrIsNull) && right.getType().equals(FilterType.likeStart)) {
            if (!(right.getValue() instanceof String) || !(left.getValue() instanceof String))
                return null;
            if (((String) left.getValue()).matches(right.getValue() + ".*")) return new Filter(left.getValue());
            else return null;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.eqOrIsNull, FilterType.likeStart);
    }
}
