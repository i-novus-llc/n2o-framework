package net.n2oapp.framework.api.criteria.filters.rule.or;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.Rule;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Rule for merge eqOrIsNull and contains filters
 */
public class EqOrIsNullAndContains implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.EQ_OR_IS_NULL) && left.getType().equals(FilterTypeEnum.CONTAINS))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.EQ_OR_IS_NULL) && right.getType().equals(FilterTypeEnum.CONTAINS)) {
            List eqValues = left.getValue() instanceof List list ? list : Arrays.asList(left.getValue());
            if (((Collection) right.getValue()).stream().anyMatch(v -> !eqValues.contains(v)))
                return null;
            return new Filter(left.getValue());
        }
        throw new RuntimeException("Incorrect restriction's type");
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.EQ_OR_IS_NULL, FilterTypeEnum.CONTAINS);
    }
}
