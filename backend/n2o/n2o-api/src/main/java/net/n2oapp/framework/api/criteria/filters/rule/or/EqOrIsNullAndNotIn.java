package net.n2oapp.framework.api.criteria.filters.rule.or;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.Rule;

import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class EqOrIsNullAndNotIn implements Rule {
    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.EQ_OR_IS_NULL) && left.getType().equals(FilterTypeEnum.NOT_IN))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.EQ_OR_IS_NULL) && right.getType().equals(FilterTypeEnum.NOT_IN)) {
            if (((List) right.getValue()).contains(left.getValue())) {
                return new Filter(FilterTypeEnum.IS_NULL);
            } else return left;
        }
        throw new IllegalArgumentException(
                String.format("Некорректные типы фильтров: `%s` и `%s`",
                        left.getType().getId(), right.getType().getId()));
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.EQ_OR_IS_NULL, FilterTypeEnum.NOT_IN);
    }
}
