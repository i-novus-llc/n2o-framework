package net.n2oapp.framework.api.criteria.filters.rule.or;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.Rule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class EqOrIsNullAndLess implements Rule {
    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.EQ_OR_IS_NULL) && left.getType().equals(FilterTypeEnum.LESS))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.EQ_OR_IS_NULL) && right.getType().equals(FilterTypeEnum.LESS)) {
            Comparable value = (Comparable) left.getValue();
            Comparable top = (Comparable) right.getValue();
            if (value.compareTo(top) < 0) return new Filter(left.getValue());
            return null;
        }
        throw new IllegalArgumentException(
                String.format("Некорректные типы фильтров: `%s` и `%s`",
                        left.getType().getId(), right.getType().getId()));
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.EQ_OR_IS_NULL, FilterTypeEnum.LESS);
    }
}
