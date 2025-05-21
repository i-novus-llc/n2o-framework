package net.n2oapp.criteria.filters.rule.or;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

/**
 * Правило для объединения фильтров с типом eqOrIsNull и likeStart
 */
public class EqOrIsNullAndLikeStart implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.eqOrIsNull) && left.getType().equals(FilterTypeEnum.likeStart))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.eqOrIsNull) && right.getType().equals(FilterTypeEnum.likeStart)) {
            if (!(right.getValue() instanceof String) || !(left.getValue() instanceof String leftValue))
                return null;
            if (leftValue.matches(right.getValue() + ".*")) return new Filter(left.getValue());
            else return null;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.eqOrIsNull, FilterTypeEnum.likeStart);
    }
}
