package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Rule for merge contains and contains filters
 */
public class Contains_Contains implements Rule {
    @Override
    public Filter simplify(Filter left, Filter right) {
        Filter result = new Filter(FilterTypeEnum.contains);
        Set res = new HashSet<>();
        List list1 = (List) left.getValue();
        List list2 = (List) right.getValue();
        list1.stream().forEach(v -> res.add(v));
        list2.stream().forEach(v -> res.add(v));
        if (res.isEmpty()) {
            return null;
        }
        result.setValue(new ArrayList<>(res));
        return result;
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.contains, FilterTypeEnum.contains);
    }
}
