package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
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
        if (right.getType().equals(FilterType.eq) && left.getType().equals(FilterType.contains))
            return simplify(right, left);
        else if (left.getType().equals(FilterType.eq) && right.getType().equals(FilterType.contains)) {
            List eqValues = left.getValue() instanceof List ? (List) left.getValue() : Arrays.asList(left.getValue());
            if (((Collection)right.getValue()).stream().anyMatch(v -> !eqValues.contains(v)))
                return null;
            return left;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.eq, FilterType.contains);
    }
}
