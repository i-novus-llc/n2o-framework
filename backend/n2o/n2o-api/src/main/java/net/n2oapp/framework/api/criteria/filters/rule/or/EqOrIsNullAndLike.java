package net.n2oapp.framework.api.criteria.filters.rule.or;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.Rule;

/**
 * Правило для объединения фильтров с типом eqOrIsNull и like
 */
public class EqOrIsNullAndLike implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.EQ_OR_IS_NULL) && left.getType().equals(FilterTypeEnum.LIKE))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.EQ_OR_IS_NULL) && right.getType().equals(FilterTypeEnum.LIKE)) {
            if (!(right.getValue() instanceof String) || !(left.getValue() instanceof String leftValue))
                return null;
            if (leftValue.matches(".*" + right.getValue() + ".*")) return new Filter(left.getValue());
            else return null;
        }
        throw new IllegalArgumentException(
                String.format("Некорректные типы фильтров: `%s` и `%s`",
                        left.getType().getId(), right.getType().getId()));
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.EQ_OR_IS_NULL, FilterTypeEnum.LIKE);
    }
}
