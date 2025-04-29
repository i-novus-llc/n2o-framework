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
public class Eq_Contains implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.eq) && left.getType().equals(FilterTypeEnum.contains))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.eq) && right.getType().equals(FilterTypeEnum.contains)) {
            List eqValues = left.getValue() instanceof List valueList ? valueList : Arrays.asList(left.getValue());
            if (((Collection)right.getValue()).stream().anyMatch(v -> !eqValues.contains(v)))
                return null;
            return left;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.eq, FilterTypeEnum.contains);
    }
}
