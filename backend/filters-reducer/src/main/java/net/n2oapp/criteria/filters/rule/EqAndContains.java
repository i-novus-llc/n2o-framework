package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Rule for merge eq and contains filters
 */
public class EqAndContains implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.EQ) && left.getType().equals(FilterTypeEnum.CONTAINS))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.EQ) && right.getType().equals(FilterTypeEnum.CONTAINS)) {
            List eqValues = left.getValue() instanceof List valueList ? valueList : Arrays.asList(left.getValue());
            if (((Collection)right.getValue()).stream().anyMatch(v -> !eqValues.contains(v)))
                return null;
            return left;
        }
        throw new IllegalArgumentException(
                String.format("Некорректные типы фильтров: `%s` и `%s`",
                        left.getType().getId(), right.getType().getId()));
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.EQ, FilterTypeEnum.CONTAINS);
    }
}
